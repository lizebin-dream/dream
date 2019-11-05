package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 用户service实现
 */
@Service
public class UserServiceImpl implements UserService{

    //注入dao对象
    @Autowired
    private UserDao userDao;
    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.查询全部数据
        List<User> list = userDao.findAll(companyId);

        //3.封装PageInfo
        PageInfo<User> pageInfo = new PageInfo<>(list);

        //4.返回
        return pageInfo;
    }

    /**
     * 查询指定企业的所有用户
     * @param companyId 企业ID
     * @return
     */
    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    /**
     * 添加
     * @param user
     */
    @Override
    public void save(User user) {
        //1.生成ID
        user.setId(UUID.randomUUID().toString());

        //对密码加盐加密
        if(user.getPassword()!=null) {
            user.setPassword(new Md5Hash(user.getPassword(),user.getEmail()).toString());
        }

        //2.保存数据
        userDao.save(user);
    }

    /**
     * 更新
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {

        //1.先查询用户角色表是否关联该用户
        long count = userDao.findUserRoleByUserId(id);

        if(count>0){
            //1.2 如果有，返回false
            return false;
        }
        //1.3 如果没有，直接删除用户，返回true
        userDao.delete(id);
        return true;
    }

    /**
     *  查询指定用户分配过的角色列表
     * @param id
     * @return
     */
    @Override
    public List<Role> findUserRoleByUserId(String id) {
        return userDao.findUserRoles(id);
    }

    /**
     * 用户分配角色
     * @param userid
     * @param roleIds
     */
    @Override
    public void changeRole(String userid, String[] roleIds) {
        //1.先删除当前用户分配过的角色
        userDao.deleteUserRoleByUserId(userid);

        //2.给用户分配新的角色
        if(roleIds!=null && roleIds.length>0){
            for(String roleId:roleIds){
                userDao.saveUserRole(userid,roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * 根据用户查询所拥有的模块
     * @param id
     * @return
     */
    @Override
    public List<Module> findModulesByUserId(String id) {
        //1.根据id查询用户对象（为了获取用户等级）
        User user = userDao.findById(id);

        //2.根据用户等级判断不同的情况
        if(user!=null){
            if(user.getDegree()==0){
                //2.1 如果等级为0，查询belong为0的模块
                return moduleDao.findByBelong("0");
            }else if(user.getDegree()==1){
                //2.2 如果等级为1，查询belong为1的模块
                return moduleDao.findByBelong("1");
            }else{
                //2.3 如果等级为2,3,4，根据RBAC表来查询当前用户分配的模块
                return moduleDao.findModuleByUserId(id);
            }
        }
        return null;
    }
}
