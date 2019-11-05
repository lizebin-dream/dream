package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController{

    //注入业务
    @Autowired
    private DeptService deptService;
    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页展示部门
     *   方法请求地址：http://localhost:8080/system/dept/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/dept/dept-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){
        //（租用）登录企业的ID
        String companyId = getLoginCompanyId();


        //1.调用业务，获取PageInfo
        PageInfo<Dept> pageInfo = deptService.findByPage(companyId, pageNum, pageSize);

        //2.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "system/dept/dept-list";
    }

    /**
     * 进入部门添加页面
     *   方法请求地址：http://localhost:8080/system/dept/toAdd.do
     *   方法的参数：无
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/dept/dept-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        //（租用）登录企业的ID
        String companyId = getLoginCompanyId();

        //1.查询当前登录企业的所有部门
        List<Dept> deptList = deptService.findAll(companyId);

        //2.把所有部门存入域对象
        request.setAttribute("deptList",deptList);

        //3.返回页面
        return "system/dept/dept-add";
    }

    /**
     * 保存部门信息（添加、更新）
     *   方法请求地址：http://localhost:8080/system/dept/edit.do
     *   方法的参数：Dept对象
     *   方法的返回值/跳转的页面：重定向到部门列表（redirect:/system/dept/list.do）
     */
    @RequestMapping("/edit")
    public String edit(Dept dept){
        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到Dept对象中
        dept.setCompanyId(companyId);
        dept.setCompanyName(compayName);

        //1.判断是否存在ID
        if(StringUtil.isEmpty(dept.getId())){
            //1.1 不存在，添加
            deptService.save(dept);
        }else{
            //1.2 存在，更新
            deptService.update(dept);
        }

        return "redirect:/system/dept/list.do";
    }

    /**
     * 进入修改部门页面
     *    方法请求地址：http://localhost:8080/system/dept/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/dept/dept-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //获取当前登录企业的ID
        String companyId = getLoginCompanyId();

        //1.根据id查询部门
        Dept dept = deptService.findById(id);

        //2.把部门对象存入域对象
        request.setAttribute("dept",dept);

        //3.查询当前企业的所有部门
        List<Dept> deptList = deptService.findAll(companyId);

        //4.把所有部门存入域对象
        request.setAttribute("deptList",deptList);

        return "system/dept/dept-update";
    }

    /**
     * 部门删除
     *   方法请求地址：http://localhost:8080/system/dept/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：{"message":"删除成功"} 或 {"message":"删除失败：该部门有子部门"}
     */
    @RequestMapping("/delete")
    @ResponseBody// 把方法的返回值转换为Json字符串
    public Map<String,Object> delete(String id){
        Map<String,Object> result = new HashMap<>();

        //1.调用业务，删除部门，返回boolean
        boolean flag = deptService.delete(id);

        //2.判断是否删除成功，封装Map对象
        if(flag){
            //成功
            result.put("message","删除成功");
        }else{
            result.put("message","删除失败：该部门有子部门");
        }
        //3.返回result的Map对象
        return result;
    }
}
