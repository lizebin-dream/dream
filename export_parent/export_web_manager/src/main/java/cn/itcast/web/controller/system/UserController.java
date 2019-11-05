package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController{

    //注入业务
    @Autowired
    private UserService userService;
    //注入部门service
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;

    /**
     * 分页展示用户
     *   方法请求地址：http://localhost:8080/system/user/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/user/user-list.jsp
     */
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){

        //判断当前用户是否有用户管理权限
       /* Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("用户管理");
*/
        //（租用）登录企业的ID
        String companyId = getLoginCompanyId();


        //1.调用业务，获取PageInfo
        PageInfo<User> pageInfo = userService.findByPage(companyId, pageNum, pageSize);

        //2.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "system/user/user-list";
    }

    /**
     * 进入用户添加页面
     *   方法请求地址：http://localhost:8080/system/user/toAdd.do
     *   方法的参数：无
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/user/user-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        //（租用）登录企业的ID
        String companyId = getLoginCompanyId();

        //1.查询当前登录企业的所有部门
        List<Dept> userList = deptService.findAll(companyId);

        //2.把所有部门存入域对象
        request.setAttribute("deptList",userList);

        //3.返回页面
        return "system/user/user-add";
    }

    /**
     * 保存用户信息（添加、更新）
     *   方法请求地址：http://localhost:8080/system/user/edit.do
     *   方法的参数：User对象
     *   方法的返回值/跳转的页面：重定向到用户列表（redirect:/system/user/list.do）
     */
    @RequestMapping("/edit")
    public String edit(User user){
        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到User对象中
        user.setCompanyId(companyId);
        user.setCompanyName(compayName);

        //1.判断是否存在ID
        if(StringUtil.isEmpty(user.getId())){
            //1.1 不存在，添加
            userService.save(user);
        }else{
            //1.2 存在，更新
            userService.update(user);
        }

        return "redirect:/system/user/list.do";
    }

    /**
     * 进入修改用户页面
     *    方法请求地址：http://localhost:8080/system/user/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/user/user-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //获取当前登录企业的ID
        String companyId = getLoginCompanyId();

        //1.根据id查询用户
        User user = userService.findById(id);

        //2.把用户对象存入域对象
        request.setAttribute("user",user);

        //3.查询当前企业的所有部门
        List<Dept> deptList = deptService.findAll(companyId);

        //4.把所有部门存入域对象
        request.setAttribute("deptList",deptList);

        return "system/user/user-update";
    }

    /**
     * 用户删除
     *   方法请求地址：http://localhost:8080/system/user/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：{"message":"删除成功"} 或 {"message":"删除失败：该用户有子用户"}
     */
    @RequestMapping("/delete")
    @ResponseBody// 把方法的返回值转换为Json字符串
    public Map<String,Object> delete(String id){
        Map<String,Object> result = new HashMap<>();

        //1.调用业务，删除用户，返回boolean
        boolean flag = userService.delete(id);

        //2.判断是否删除成功，封装Map对象
        if(flag){
            //成功
            result.put("message","删除成功");
        }else{
            result.put("message","删除失败：该用户被引用");
        }
        //3.返回result的Map对象
        return result;
    }

    /**
     * 进入用户分配角色页面
     *   方法请求地址：http://localhost:8080/system/user/roleList.do
     *   方法的参数：id=1  （用户ID）
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/user/user-role.jsp
     *
     */
    @RequestMapping("/roleList")
    public String roleList(String id){
        //1.根据ID查询用户
        User user = userService.findById(id);
        //2.存入request
        request.setAttribute("user",user);

        //3.查询所有角色列表
        List<Role> roleList = roleService.findAll(getLoginCompanyId());
        request.setAttribute("roleList",roleList);

        //4.查询该用户分配过的角色列表
        List<Role> userRoles = userService.findUserRoleByUserId(id);
        request.setAttribute("userRoles",userRoles);
        //5.返回页面
        return "system/user/user-role";
    }

    /**
     * 保存用户和角色数据
     *    方法请求地址：http://localhost:8080/system/user/changeRole.do
     *    方法的参数：userid=1&roleIds=1&roleIds=2&roleIds=3  （用户ID）
     *   方法的返回值/跳转的页面：重定向到用户列表（redirect:/system/user/list.do）
     *
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userid,String[] roleIds){

        userService.changeRole(userid,roleIds);

        return "redirect:/system/user/list.do";
    }

}
