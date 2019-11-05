package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Module;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模块控制器
 */
@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController{

    //注入业务
    @Autowired
    private ModuleService moduleService;
    //注入部门service
    @Autowired
    private DeptService deptService;

    /**
     * 分页展示模块
     *   方法请求地址：http://localhost:8080/system/module/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/module/module-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){

        //1.调用业务，获取PageInfo
        PageInfo<Module> pageInfo = moduleService.findByPage(pageNum, pageSize);

        //2.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "system/module/module-list";
    }

    /**
     * 进入模块添加页面
     *   方法请求地址：http://localhost:8080/system/module/toAdd.do
     *   方法的参数：无
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/module/module-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){

        //1.查询所有模块
        List<Module> moduleList = moduleService.findAll();

        //2.存入request域对象
        request.setAttribute("menus",moduleList);

        //3.返回页面
        return "system/module/module-add";
    }

    /**
     * 保存模块信息（添加、更新）
     *   方法请求地址：http://localhost:8080/system/module/edit.do
     *   方法的参数：Module对象
     *   方法的返回值/跳转的页面：重定向到模块列表（redirect:/system/module/list.do）
     */
    @RequestMapping("/edit")
    public String edit(Module module){
        //1.判断是否存在ID
        if(StringUtil.isEmpty(module.getId())){
            //1.1 不存在，添加
            moduleService.save(module);
        }else{
            //1.2 存在，更新
            moduleService.update(module);
        }

        return "redirect:/system/module/list.do";
    }

    /**
     * 进入修改模块页面
     *    方法请求地址：http://localhost:8080/system/module/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/module/module-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.根据id查询模块
        Module module = moduleService.findById(id);

        //2.把模块对象存入域对象
        request.setAttribute("module",module);

        //3.查询所有模块
        List<Module> moduleList = moduleService.findAll();

        //4.存入request域对象
        request.setAttribute("menus",moduleList);

        return "system/module/module-update";
    }

    /**
     * 模块删除
     *   方法请求地址：http://localhost:8080/system/module/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：{"message":"删除成功"} 或 {"message":"删除失败：该模块有子模块"}
     */
    @RequestMapping("/delete")
    public String delete(String id){
        //1.调用业务，删除模块，返回boolean
        moduleService.delete(id);
        return "redirect:/system/module/list.do";
    }
}
