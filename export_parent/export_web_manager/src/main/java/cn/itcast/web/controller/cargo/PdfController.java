package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.BeanMapUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

/**
 * Pdf导出控制器
 */
@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController{

    /**
     * 1）基本导出（静态数据）
     *   请求路径：http://localhost:8080/cargo/export/exportPdf.do
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test01.jasper");

        //2.构建Print对象，用于让模块结合数据
        *//**
         * 参数一：文件输入流
         * 参数二：参数列表（模板可以添加参数：paramters）
         * 参数三：数据源（模板如果引用了数据源可以添加进来）  如果没有数据源，则设置空的数据源：JREmptyDataSource
         *//*
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),new JREmptyDataSource());

        //3.使用Exporter导出PDF
        *//**
         * 参数一：print对象，需要输出的内容
         * 参数二：输出流，输出到什么地方？
         *//*
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());

    }*/


    /**
     * 2）带参数的导出
     * @throws Exception
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test02_param.jasper");

        //2.构建Print对象，用于让模块结合数据
        //第二个参数就是用来填充模板中的parameters
        Map<String, Object> map = new HashMap<>();
        map.put("userName","小泽");
        map.put("email","ze@qq.com");
        map.put("companyName","小泽科技");
        map.put("deptName","视频组");

        JasperPrint print = JasperFillManager.fillReport(in,map,new JREmptyDataSource());

        //3.使用Exporter导出PDF
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());

    }*/

/*
    //注入数据源
    @Autowired
    private DataSource dataSource;

*/

    /**
     * 3)使用JDBC数据源导出
     * @throws Exception
     */
   /* @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test03_jdbc.jasper");

        //2.构建Print对象，用于让模块结合数据
        //第三个参数：如果是JDBC数据源，应该设置Connection对象
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),dataSource.getConnection());

        //3.使用Exporter导出PDF
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());

    }*/

    /**
     * 4)使用JavaBean数据源导出
     * @throws Exception
     */
   /* @RequestMapping("/exportPdf")
    public void exportPdf() throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test04_javabean.jasper");

        //2.构建Print对象，用于让模块结合数据
        //注意：JavaBean的属性名称和模版的Fileds的名称一致的
        List<User> list = new ArrayList<>();
        for(int i=1;i<=10;i++){
            User user = new User();
            user.setUserName("张三-"+i);
            user.setEmail("zhangsan-"+i+"@qq.com");
            user.setCompanyName("熊掌科技");
            user.setDeptName("开发部");
            list.add(user);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        //第三个参数：JavaBean作为数据源，使用JRBeanCollectionDataSource对象来填充
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        //3.使用Exporter导出PDF
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());

    }*/



    /**
     * 5)分组导出
     * @throws Exception
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test05_group.jasper");

        //2.构建Print对象，用于让模块结合数据
        //注意：JavaBean的属性名称和模版的Fileds的名称一致的
        List<User> list = new ArrayList<>();
        for(int j=1;j<5;j++) {
            for (int i = 1; i <= 10; i++) {
                User user = new User();
                user.setUserName("张三-" + i);
                user.setEmail("zhangsan-" + i + "@qq.com");
                user.setCompanyName("熊掌科技-" + j);
                user.setDeptName("开发部");
                list.add(user);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        //第三个参数：JavaBean作为数据源，使用JRBeanCollectionDataSource对象来填充
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        //3.使用Exporter导出PDF
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());

    }*/




    /**
     * 6)图标导出
     * @throws Exception
     */
    /*@RequestMapping("/exportPdf")
    public void exportPdf() throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test06_pie.jasper");

        //2.构建Print对象，用于让模块结合数据
        //注意：JavaBean的属性名称和模版的Fileds的名称一致的
        List<Map> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Map map = new HashMap();
            map.put("title","标题:"+i);
            map.put("value",new Random().nextInt(100));
            list.add(map);
        }


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        //第三个参数：JavaBean作为数据源，使用JRBeanCollectionDataSource对象来填充
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        //3.使用Exporter导出PDF
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());

    }*/

    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;


    @RequestMapping("/exportPdf")
    public void exportPdf(String id) throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/export.jasper");

        //2.构建Print对象，用于让模块结合数据
        //2.1 查询报运单数据，填充map集合中（Parameters里面）
        Export export = exportService.findById(id);
        //2.2 使用工具类把JavaBean数据转为Map集合
        Map map = BeanMapUtils.beanToMap(export);

        //2.2 根据报运单ID查询报运商品列表数据，填充list集合中（Fields里面）
        ExportProductExample epExample = new ExportProductExample();
        epExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(epExample);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        //第三个参数：JavaBean作为数据源，使用JRBeanCollectionDataSource对象来填充
        JasperPrint print = JasperFillManager.fillReport(in,map,dataSource);

        //3.使用Exporter导出PDF
        //3.1 设置输出格式
        response.setContentType("application/pdf;charset=utf-8");
        //3.2 弹出下载提示框
        response.setHeader("Content-Disposition","attachment;filename=export.pdf");
        //3.3 获取输出流
        ServletOutputStream out = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(print,out);
        out.close();
    }
}
