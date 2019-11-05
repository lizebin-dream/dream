package cn.itcast;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.dao.stat.StatDao;
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
import java.util.Map;
import java.util.UUID;

/**
 * 演示逆向工程产生的方法的使用
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class StatDaoTest {

    @Autowired
    private StatDao statDao;


    @Test
    public void testGetFactoryData(){
        List<Map<String, Object>> list = statDao.getFactoryData("1");
        System.out.println(list);
    }


}
