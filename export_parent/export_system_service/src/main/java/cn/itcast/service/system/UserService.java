package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *用户service接口
 */
public interface UserService {

    /**
     * 分页查询用户
     */
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize);

    /**
     * 查询指定企业的所有用户
     * @param companyId
     * @return
     */
    List<User> findAll(String companyId);

    /**
     * 添加
     * @param user
     */
    void save(User user);

    /**
     * 更新
     * @param user
     */
    void update(User user);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findById(String id);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 查询指定用户分配过的角色
     * @param id
     * @return
     */
    List<Role> findUserRoleByUserId(String id);

    /**
     * 用户分配角色
     * @param userid
     * @param roleIds
     */
    void changeRole(String userid, String[] roleIds);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 根据用户查询所拥有的模块
     * @param id
     * @return
     */
    List<Module> findModulesByUserId(String id);
}
