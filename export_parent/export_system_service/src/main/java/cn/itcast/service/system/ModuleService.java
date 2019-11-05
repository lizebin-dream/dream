package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *模块service接口
 */
public interface ModuleService {

    /**
     * 分页查询模块
     */
    public PageInfo<Module> findByPage(int pageNum, int pageSize);

    /**
     * 查询指定企业的所有模块
     * @return
     */
    List<Module> findAll();

    /**
     * 添加
     * @param module
     */
    void save(Module module);

    /**
     * 更新
     * @param module
     */
    void update(Module module);

    /**
     * 根据id查询模块
     * @param id
     * @return
     */
    Module findById(String id);

    /**
     * 删除模块
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 查询指定角色分配过的模块
     * @param roleid
     * @return
     */
    List<Module> findRoleModuleByRoleId(String roleid);
}
