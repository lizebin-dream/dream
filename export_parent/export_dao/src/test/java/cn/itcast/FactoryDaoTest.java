package cn.itcast;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 演示逆向工程产生的方法的使用
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class FactoryDaoTest {

    @Autowired
    private FactoryDao factoryDao;

  /*  *//**
     * 简单添加-全表插入
     *//*
    @Test
    public void testInsert(){
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setFactoryName("1号测试工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        factoryDao.insert(factory);
    }*/

    /**
     * 选择性添加-动态插入
     */
    @Test
    public void testInsertSelective(){
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setFactoryName("2号测试工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        factoryDao.insertSelective(factory);
    }

    /**
     * 删除
     */
    @Test
    public void testDeleteByPrimaryKey(){
      factoryDao.deleteByPrimaryKey("d35e965e-d947-4b78-aeeb-78f515a9a7a8");
    }

   /* *//**
     * 修改 -- 全表更新
     *//*
    @Test
    public void testUpdateByPrimaryKey(){
        Factory factory = new Factory();
        factory.setId("f50124af-3faf-4040-82e2-5fb7d49c0600");
        factory.setFactoryName("3号测试工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        factoryDao.updateByPrimaryKey(factory);
    }*/

    /**
     * 修改 -- 动态更新
     */
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Factory factory = new Factory();
        factory.setId("f50124af-3faf-4040-82e2-5fb7d49c0600");
        factory.setFactoryName("4号测试工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        factoryDao.updateByPrimaryKeySelective(factory);
    }

    /**
     * 条件查询
     */
    @Test
    public void testSelectByExample() throws ParseException {
        //1.创建Example对象: 封装所有查询条件
        FactoryExample factoryExample = new FactoryExample();

        //2.创建Criteria：存入条件
        FactoryExample.Criteria criteria = factoryExample.createCriteria();

        //3.往Criteria设置查询条件
        //需求：工厂名称为“3号测试工厂”
        //criteria.andFactoryNameEqualTo("4号测试工厂"); // factory_name = '3号测试工厂'
        criteria.andFullNameLike("%厂%");

        criteria.andCreateTimeGreaterThan(new SimpleDateFormat("yyyy-MM-dd").parse("2019-08-15"));


        List<Factory> list = factoryDao.selectByExample(factoryExample);

        System.out.println(list.size());
        System.out.println(list);
    }


}
