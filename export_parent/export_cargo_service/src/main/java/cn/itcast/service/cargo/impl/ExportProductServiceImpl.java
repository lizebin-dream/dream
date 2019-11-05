package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 报运单业务实现
 */
@Service // 必须导入dubbo的注解
public class ExportProductServiceImpl implements ExportProductService{

    //注入dao对象
    @Autowired
    private ExportProductDao exportProductDao; //报运单dao

    /**
     * 分页查询
     * @param exportProductExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample, int pageNum, int pageSize) {
        //1.设置当前页码和页面大小
        PageHelper.startPage(pageNum,pageSize);
        //2.查询所有数据，封装PageInfo对象
        return new PageInfo<ExportProduct>(exportProductDao.selectByExample(exportProductExample));
    }

    /**
     * 查询所有
     * @param exportProductExample
     * @return
     */
    @Override
    public List<ExportProduct> findAll(ExportProductExample exportProductExample) {
        return exportProductDao.selectByExample(exportProductExample);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public ExportProduct findById(String id) {
        return exportProductDao.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param exportProduct
     */
    @Override
    public void save(ExportProduct exportProduct) {
        //1.保存报运单表数据
        //1.1 设置ID
        exportProduct.setId(UUID.randomUUID().toString());
        exportProductDao.insertSelective(exportProduct);
    }

    /**
     * 修改
     * @param exportProduct
     */
    @Override
    public void update(ExportProduct exportProduct) {
        exportProductDao.updateByPrimaryKeySelective(exportProduct);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {
        exportProductDao.deleteByPrimaryKey(id);
    }

}
