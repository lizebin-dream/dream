package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *部门service接口
 */
public interface DeptService {

    /**
     * 分页查询部门
     */
    public PageInfo<Dept> findByPage(String companyId,int pageNum,int pageSize);

    /**
     * 查询指定企业的所有部门
     * @param companyId
     * @return
     */
    List<Dept> findAll(String companyId);

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
     * 根据id查询部门
     * @param id
     * @return
     */
    Dept findById(String id);

    /**
     * 删除部门
     * @param id
     * @return
     */
    boolean delete(String id);
}
