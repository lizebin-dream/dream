package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 部门service实现
 */
@Service
public class DeptServiceImpl implements DeptService{

    //注入dao对象
    @Autowired
    private DeptDao deptDao;

    @Override
    public PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.查询全部数据
        List<Dept> list = deptDao.findAll(companyId);

        //3.封装PageInfo
        PageInfo<Dept> pageInfo = new PageInfo<>(list);

        //4.返回
        return pageInfo;
    }

    /**
     * 查询指定企业的所有部门
     * @param companyId 企业ID
     * @return
     */
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    /**
     * 添加
     * @param dept
     */
    @Override
    public void save(Dept dept) {
        //1.生成ID
        dept.setId(UUID.randomUUID().toString());
        //2.保存数据
        deptDao.save(dept);
    }

    /**
     * 更新
     * @param dept
     */
    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {
        //先判断当前部门是否有子部门，如果有，则提示用户"当前部门被引用，不能删除"

        //1.先判断当前部门是否有子部门（SELECT COUNT(*) pe_dept WHERE parent_id = '100';）
        long count = deptDao.findDeptByParentId(id);

        //2.如果有子部门，则返回false
        if(count>0){
            return false;
        }
        //3.如果没有子部门，删除当前部门，返回true
        deptDao.delete(id);
        return true;
    }
}
