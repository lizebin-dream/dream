package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部
    List<Role> findAll(String companyId);

	//根据id删除
    void delete(String id);

	//添加
    void save(Role role);

	//更新
    void update(Role role);

    /**
     * 删除角色分配过的权限
     * @param roleid
     */
    void deleteRoleModuleByRoleId(String roleid);

    /**
     * 角色分配模块
     * @param roleid
     * @param moduleId
     */
    void saveRoleMoudle(String roleid, String moduleId);
}