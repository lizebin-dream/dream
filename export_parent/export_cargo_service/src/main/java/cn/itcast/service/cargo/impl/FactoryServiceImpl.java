package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.FactoryService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 生产工厂业务实现
 */
@Service // 必须导入dubbo的注解
public class FactoryServiceImpl implements FactoryService{

    //注入dao对象
    @Autowired
    private FactoryDao factoryDao;

    /**
     * 分页查询
     * @param factoryExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<Factory> findByPage(FactoryExample factoryExample, int pageNum, int pageSize) {
        //1.设置当前页码和页面大小
        PageHelper.startPage(pageNum,pageSize);
        //2.查询所有数据，封装PageInfo对象
        return new PageInfo<Factory>(factoryDao.selectByExample(factoryExample));
    }

    /**
            * 查询所有
     * @param factoryExample
     * @return
             */
    @Override
    public List<Factory> findAll(FactoryExample factoryExample) {
        return factoryDao.selectByExample(factoryExample);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param factory
     */
    @Override
    public void save(Factory factory) {
        //1.设置ID
        factory.setId(UUID.randomUUID().toString());
        //2.设置创建时间和更新时间
        factory.setCreateTime(new Date());//创建时间
        factory.setUpdateTime(new Date());//更新时间
        factoryDao.insertSelective(factory);
    }

    /**
     * 修改
     * @param factory
     */
    @Override
    public void update(Factory factory) {
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);
    }

    /**
     * 根据工厂名称查询工厂
     * @param factoryName
     * @return
     */
   /* @Override
    public Factory findByFactoryName(String factoryName) {
        return factoryDao.findByFactoryName(factoryName);
    }*/

    /**
     * 根据工厂名称查询工厂
     * @param factoryName
     * @return
     */
    @Override
    public Factory findByFactoryName(String factoryName) {
        //1.创建FactoryExample
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andFactoryNameEqualTo(factoryName);

        //2.调用dao方法
        List<Factory> list = factoryDao.selectByExample(factoryExample);
        if(list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }

    }
}
