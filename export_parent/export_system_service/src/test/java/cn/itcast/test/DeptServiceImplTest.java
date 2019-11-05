package cn.itcast.test;

import cn.itcast.domain.company.Company;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试部门业务方法
 *  spring+junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml") // 读取spring配置文件
public class DeptServiceImplTest {

    @Autowired
    private DeptService deptService;


    /**
     * 分页方法
     */
    @Test
    public void testFindByPage(){
        PageInfo<Dept> pageInfo = deptService.findByPage("1",1,3);

        System.out.println("总记录数："+pageInfo.getTotal());
        System.out.println("总页数："+pageInfo.getPages());
        System.out.println("上一页:"+pageInfo.getPrePage());
        System.out.println("下一页:"+pageInfo.getNextPage());
        System.out.println("当前页："+pageInfo.getPageNum());
        System.out.println("当前数据:");
        List<Dept> list = pageInfo.getList();
        System.out.println(list);
    }

}
