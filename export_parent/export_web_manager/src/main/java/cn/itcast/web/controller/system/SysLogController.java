package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.SysLogService;
import cn.itcast.service.system.UserService;
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
 * 系统日志控制器
 */
@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController{

    //注入业务
    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页展示用户
     *   方法请求地址：http://localhost:8080/system/user/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/system/user/user-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){
        //（租用）登录企业的ID
        String companyId = getLoginCompanyId();


        //1.调用业务，获取PageInfo
        PageInfo<SysLog> pageInfo = sysLogService.findByPage(companyId, pageNum, pageSize);

        //2.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "system/log/log-list";
    }

}
