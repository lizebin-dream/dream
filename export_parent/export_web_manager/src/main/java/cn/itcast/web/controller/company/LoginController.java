package cn.itcast.web.controller.company;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 登录控制器
 */
@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;

    /**
     * 登录方法
     *   请求路径： http://localhost:8080/login.do
     *   请求参数：email=124@qq.com&password=123
     *   返回数据： /WEB-INF/pages/home/main.jsp
     */
    /*@RequestMapping("/login")
    public String login(String email,String password){
        //1.基本数据判断
        // forward:完整的页面地址（不经过视图解析器）
        if(StringUtil.isEmpty(email) || StringUtil.isEmpty(password)){
            request.setAttribute("error","邮箱或密码不能为空!");
            return "forward:/login.jsp";
        }

        //2.根据Email查询用户是否存在
        User loginUser = userService.findByEmail(email);

        //3.如果存在Email，判断密码是否正确
        if(loginUser!=null){
            if(loginUser.getPassword().equals(password)){
                //登录成功
                //把登录用户信息存入session域
                session.setAttribute("loginUser",loginUser);

                //根据当前登录用户ID查询所拥有的模块（菜单）
                List<Module> moduleList = userService.findModulesByUserId(loginUser.getId());

                //存入session域
                session.setAttribute("modules",moduleList);

                //4.1.Email和密码都正确，跳转到main.jsp
                return "home/main";
            }
        }

        //4.2 如果不正确，跳转到login.jsp，提示用户
        request.setAttribute("error","邮箱或密码不正确");
        return "forward:/login.jsp";

    }*/


    /**
     * 使用Shiro的认证功能
     * @param email
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(String email,String password){
        //1.基本数据判断
        // forward:完整的页面地址（不经过视图解析器）
        if(StringUtil.isEmpty(email) || StringUtil.isEmpty(password)){
            request.setAttribute("error","邮箱或密码不能为空!");
            return "forward:/login.jsp";
        }

        //使用Shiro的认证过程


        //1.通过SecurityUtils获取Subject
        Subject subject = SecurityUtils.getSubject();

        //2.通过AuthenticationToken封装用户登录信息
        AuthenticationToken token = new UsernamePasswordToken(email,password);

        //3.调用login方法进行认证，执行AuthRealm的认证方法
        try {
            subject.login(token);

            //4.处理登录成功细节
            //4.1 获取用户登录信息（在AuthRealm中存储的）
            User loginUser = (User)subject.getPrincipal();

            //4.2 存入session域
            session.setAttribute("loginUser",loginUser);

            //4.3 获取登录用户的模块数据
            //根据当前登录用户ID查询所拥有的模块（菜单）
            List<Module> moduleList = userService.findModulesByUserId(loginUser.getId());
            //存入session域
            session.setAttribute("modules",moduleList);

            //4.4 跳转到主页
            return "home/main";
        }catch (UnknownAccountException e){ // UnknownAccountException: Shiro提供的用户不存在异常
            request.setAttribute("error","用户名不存在");
            return "forward:/login.jsp";
        }catch (IncorrectCredentialsException e){ // IncorrectCredentialsException: Shiro提供的密码错误异常
            request.setAttribute("error","密码输入有误");
            return "forward:/login.jsp";
        }

    }

    /**
     * 进入主页内容页面
     *   请求路径： http://localhost:8080/home.do
     *   请求参数：无
     *   返回数据： /WEB-INF/pages/home/home.jsp
     */
    @RequestMapping("/home")
    public String home(){
        return "home/home";
    }

   /* *//**
     * 注销
     *    请求路径： http://localhost:8080/logout.do
     *    请求参数：无
     *   返回数据： /login.jsp
     *
     *//*
    @RequestMapping("/logout")
    public String logout(){
        //1.从session删除登录用户数据
        session.removeAttribute("loginUser");
        //2.把session销毁
        session.invalidate();
        //3.回到登录页面
        return "redirect:/login.jsp";
    }
*/

    /**
     * Shiro注销
     *    请求路径： http://localhost:8080/logout.do
     *    请求参数：无
     *   返回数据： /login.jsp
     *
     */
    @RequestMapping("/logout")
    public String logout(){
        session.removeAttribute("loginUser");
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2.Shiro注销（底层删除Shiro当初存入session的数据）
        subject.logout();

        //3.回到登录页面
        return "redirect:/login.jsp";
    }

}
