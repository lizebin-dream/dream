package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;

import java.util.List;


public interface UserDao {

	//根据企业id查询全部
	List<User> findAll(String companyId);

	//根据id查询
    User findById(String userId);

	//根据id删除
	void delete(String userId);

	//保存
	void save(User user);

	//更新
	void update(User user);

	/**
	 * 判断该用户是否存在角色
	 * @param id
	 * @return
	 */
	long findUserRoleByUserId(String id);

	/**
	 * 查询指定用户分配过的角色列表
	 * @param id
	 * @return
	 */
	List<Role> findUserRoles(String id);

	/**
	 * 删除用户绑定的角色
	 * @param userid
	 */
    void deleteUserRoleByUserId(String userid);

	/**
	 * 保存用户和角色
	 * @param userid
	 * @param roleId
	 */
	void saveUserRole(String userid, String roleId);

	/**
	 * 根据邮箱查询用户
	 * @param email
	 * @return
	 */
    User findByEmail(String email);
}