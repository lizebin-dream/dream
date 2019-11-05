package cn.itcast.web.controller.company;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 企业控制器
 */
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController{

    //注入service对象
    @Reference
    private CompanyService companyService;

    /**
     * 展示所有企业数据
     * 方法Api：
     *    方法请求地址：http://localhost:8080/company/list.do
     *    方法的参数：无
     *    方法的返回值/跳转的页面：/WEB-INF/pages/company/company-list.jsp
     */
    /*@RequestMapping("/list")
    public String list(HttpServletRequest request){
        //1.调用service，获取数据
        List<Company> list = companyService.findAll();

        //2.存入数据到request域对象
        request.setAttribute("list",list);

        //3.指定跳转页面
        return "company/company-list";
    }*/

    /**
     * 添加企业
     */
    @RequestMapping("/save")
    public String save(Date date){
        int i = 10/0;

        System.out.println("日期："+date);
        return "success";
    }

    /**
     * 进入企业添加页面
     *    方法请求地址：http://localhost:8080/company/toAdd.do
     *    方法的参数：无
     *    方法的返回值/跳转的页面：/WEB-INF/pages/company/company-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "company/company-add";
    }

    /**
     * 保存企业信息（添加/更新）
     *   方法请求地址：http://localhost:8080/company/edit.do
     *   方法的参数：Company表单数据（对应的Company实体类数据）
     *   方法的返回值/跳转的页面：重定向到企业列表页面（redirect:/company/list.do）
     *
     */
    @RequestMapping("/edit")
    public String edit(Company company){

        //判断是否存在企业ID，决定执行添加还是更新
        if(StringUtils.isEmpty(company.getId()) ){
            //添加（alt + enter）
            companyService.save(company);
        }else{
            //更新
            companyService.update(company);
        }

        return "redirect:/company/list.do";
    }

    /**
     * 进入企业修改页面，回显数据
     *   方法请求地址：http://localhost:8080/company/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/company/company-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        //1.调用业务，根据id查询企业
        Company company = companyService.findById(id);
        //2.存入数据到request域对象
        request.setAttribute("company",company);

        return "company/company-update";
    }

    /**
     * 企业删除
     *   方法请求地址： http://localhost:8080/company/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：重定向到企业列表页面（redirect:/company/list.do）
     */
    @RequestMapping("/delete")
    public String delete(String id){

        //执行删除业务
        companyService.delete(id);

        return "redirect:/company/list.do";
    }

    /**
     * 分页显示企业
     *   方法请求地址： http://localhost:8080/company/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/company/company-list.jsp
     */
    /**
     *
     * @param pageNum 当前页码   @RequestParam(defaultValue = "1"): 当前页面没有传递pageNum参数，则使用1为默认值
     * @param pageSize 页面大小
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("企业管理")
    public  String list(@RequestParam(defaultValue = "1") int pageNum,
                @RequestParam(defaultValue = "5") int pageSize
                       ){

        //判断当前用户是否有企业管理权限
       /* Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("企业管理");
*/
        //1.调用业务，获取分页数据（PageInfo）
        PageInfo<Company> pageInfo = companyService.findByPage(pageNum,pageSize);

        //2.把PageInfo存入request域对象
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "company/company-list";
    }
}
