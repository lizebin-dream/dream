package cn.itcast.web.shiro;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户的认证和授权的Realm
 */
public class AuthRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;

    /**
     * 认证方法，该方法编写认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //System.out.println("认证方法...");


        //1.判断用户名是否存在
        //1.1 获取当前用户输入的登录数据
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        //1.2 获取登录时的用户名
        String email = token.getUsername();

        //1.3 查询数据库判断邮箱是否存在
        User loginUser = userService.findByEmail(email);

        //1.4 判断用户是否存在
        if(loginUser==null){
            //用户不存在
            return null; // 只需要返回null，Shiro自动抛出UnknownAccountException异常
        }

        //2.返回数据库的密码，让Shiro判断密码是否正确
        /**
         * 参数一：用户登录成功后，需要获取的登录数据（使用Subject.getPrincipal获取的）
         * 参数二：（最重要）数据库的密码
         * 参数三：只有在多个Realm（多张用户表）的情况下才有用的（如果只有一个Realm可以随便写）
         */
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(loginUser,loginUser.getPassword(),loginUser.getUserName());

        return info;
    }


    /**
     * 授权方法，该方法编写授权逻辑
     * @param principalCollection
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //System.out.println("授权方法...");


        //1.创建SimpleAuthorizationInfo授权对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //2.根据当前登录用户查询分配的权限（模块）
        //2.1 获取登录用户信息
        Subject subject = SecurityUtils.getSubject();
        User loginUser = (User)subject.getPrincipal();

        //2.2 查询用户查询分配的权限
        List<Module> moduleList = userService.findModulesByUserId(loginUser.getId());

        //3. 把模块的权限标记（cpermission字段值）存入授权对象
         Set<String> permissions = new HashSet<>();

         //3.1 遍历所有模块数据
         if(moduleList!=null && moduleList.size()>0){
             for(Module module:moduleList){
                 //3.2 存入模块的权限标记（cpermission字段值）
                 if(module.getCpermission()!=null){
                     permissions.add(module.getCpermission());
                 }
             }
         }
         //3.3 把所有权限标记的Set集合存入info授权对象中
        info.addStringPermissions(permissions);

        //4.返回授权对象
        return info;
    }


}
