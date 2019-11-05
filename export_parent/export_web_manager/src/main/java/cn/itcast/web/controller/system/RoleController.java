package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.sun.media.sound.ModelSource;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController{

    //注入业务
    @Autowired
    private RoleService roleService;
    //注入部门service
    @Autowired
    private DeptService deptService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 分页展示角色
     *   方法请求地址：http://localhost:8080/system/role/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/role/role-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){
        //（租用）登录企业的ID
        String companyId = getLoginCompanyId();


        //1.调用业务，获取PageInfo
        PageInfo<Role> pageInfo = roleService.findByPage(companyId, pageNum, pageSize);

        //2.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "system/role/role-list";
    }

    /**
     * 进入角色添加页面
     *   方法请求地址：http://localhost:8080/system/role/toAdd.do
     *   方法的参数：无
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/role/role-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        //3.返回页面
        return "system/role/role-add";
    }

    /**
     * 保存角色信息（添加、更新）
     *   方法请求地址：http://localhost:8080/system/role/edit.do
     *   方法的参数：Role对象
     *   方法的返回值/跳转的页面：重定向到角色列表（redirect:/system/role/list.do）
     */
    @RequestMapping("/edit")
    public String edit(Role role){
        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到Role对象中
        role.setCompanyId(companyId);
        role.setCompanyName(compayName);

        //1.判断是否存在ID
        if(StringUtil.isEmpty(role.getId())){
            //1.1 不存在，添加
            roleService.save(role);
        }else{
            //1.2 存在，更新
            roleService.update(role);
        }

        return "redirect:/system/role/list.do";
    }

    /**
     * 进入修改角色页面
     *    方法请求地址：http://localhost:8080/system/role/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/role/role-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.根据id查询角色
        Role role = roleService.findById(id);

        //2.把角色对象存入域对象
        request.setAttribute("role",role);

        return "system/role/role-update";
    }

    /**
     * 角色删除
     *   方法请求地址：http://localhost:8080/system/role/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：重定向会列表页面
     */
    @RequestMapping("/delete")
    public String delete(String id){
        //1.调用业务，删除角色
        roleService.delete(id);
        //3.返回result的Map对象
        return "redirect:/system/role/list.do";
    }

    /**
     * 进入角色分配权限页面
     *   方法请求地址：http://localhost:8080/system/role/roleModule.do
     *   方法的参数：roleid=1  (角色ID)
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/role/role-module.jsp
     *
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid){

        //1.根据id查询角色
        Role role = roleService.findById(roleid);
        //2.存入request
        request.setAttribute("role",role);

        //先进入角色分配权限页面，再发送一个ajax请求去后台抓取模块数据
        return "system/role/role-module";
    }

    /**
     * 加载ztree数据
     *   方法请求地址：http://localhost:8080/system/role/getZtreeNodes.do
     *   方法的参数：roleid=1  (角色ID)
     *   方法的返回值/跳转的页面：JSON格式
     *       [
                 { id:12, pId:1, name:"出口报运", open:true, checked: true }
             ]

         使用List<Map<String,Object>>对象格式封装ztree需要的数据
     *
     */
    @RequestMapping("/getZtreeNodes")
    @ResponseBody
    public List<Map<String,Object>> getZtreeNodes(String roleid){
        //1.设计一个List集合，用于存储所有ztree的数据
        List<Map<String,Object>> list = new ArrayList<>();

        //2.查询所有模块数据
        List<Module> moduleList = moduleService.findAll();

        //3.根据角色ID查询分配过的模块
        List<Module> roleModules = moduleService.findRoleModuleByRoleId(roleid);

        //4.封装List集合
        if(moduleList!=null && moduleList.size()>0){

            //4.1 遍历所有模块
            for(Module module:moduleList){
                //{ id:12, pId:1, name:"出口报运", open:true, checked: true }
                Map<String,Object> map = new HashMap<>();
                map.put("id",module.getId());
                map.put("pId",module.getParentId());
                map.put("name",module.getName());
                map.put("open",true);//全部展示

                //让哪些该角色绑定过的模块  勾上（加上checked=true属性）
                //contains: 判断某个List集合中是否包含某个对象（判断对象的所有属性值内容是否一致）
                if(roleModules.contains(module)){
                    map.put("checked",true);
                }

              /* for(Module m2:roleModules){
                   if(m2.getId()==module.getId()){
                       map.put("checked",true);
                   }
               }*/

              //把Map放入List集合中
                list.add(map);
            }

        }

        //返回List集合数据
        return list;
    }

    /**
     * 保存角色分配的模块
     *   方法请求地址：http://localhost:8080/system/role/updateRoleModule.do
     *   方法的参数：roleid=1&moduleIds=1,2,3  (角色ID和所有模块ID)
     *   方法的返回值/跳转的页面：重定向会列表页面
     *
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String moduleIds){

        //调用业务，保存数据
        roleService.updateRoleModule(roleid,moduleIds);

        return "redirect:/system/role/list.do";
    }
}
