package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *角色service接口
 */
public interface RoleService {

    /**
     * 分页查询角色
     */
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize);

    /**
     * 查询指定企业的所有角色
     * @param companyId
     * @return
     */
    List<Role> findAll(String companyId);

    /**
     * 添加
     * @param role
     */
    void save(Role role);

    /**
     * 更新
     * @param role
     */
    void update(Role role);

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    Role findById(String id);

    /**
     * 删除角色
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 角色分配模块
     * @param roleid
     * @param moduleIds
     */
    void updateRoleModule(String roleid, String moduleIds);
}
