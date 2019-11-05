package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

/**
 * 部门dao接口
 */
public interface DeptDao {

    /**
     * 查询所有部门 （查询某个企业下的所有部门）
     *  companyId: 每个租户（企业）的标记
     */
    public List<Dept> findAll(String companyId);

    /**
     * 根据ID查询部门（是为了封装Dept对象中parent父部门属性）
     */
    public Dept findById(String id);

    /**
     * 添加
     * @param dept
     */
    void save(Dept dept);

    /**
     * 更新
     * @param dept
     */
    void update(Dept dept);

    /**
     * 查询某个部门的子部门数量
     * @param id
     * @return
     */
    long findDeptByParentId(String id);

    /**
     * 删除部门
     * @param id
     */
    void delete(String id);
}
