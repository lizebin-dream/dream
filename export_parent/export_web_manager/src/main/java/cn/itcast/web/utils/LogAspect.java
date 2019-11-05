package cn.itcast.web.utils;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 日志切面类
 *   切面 = 通知+切入点
 */
//@Aspect // 标记为切面类
@Component // 把切面对象放入IOC容器
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    /**
     * 环绕通知方法:记录日志的方法
     *   ProceedingJoinPoint: 控制目标对象的方法执行 或 获取目标对象的信息
     */
    @Around(value = "execution(* cn.itcast.web.controller.*.*.*(..))")
    public Object log(ProceedingJoinPoint jp){
        //result：目标对象的方法返回值
        Object result = null;
        //1.获取当前登录用户
        User loginUser = (User)session.getAttribute("loginUser");

        //获取当前目标执行的方法
        //2.getSignature: 获取目标方法信息
        String methodName = jp.getSignature().getName();
        //获取当前方法所在的类
        //3. getTarget(): 获取目标对象
        String fullClassName = jp.getTarget().getClass().getName();
        try {
            SysLog sysLog = new SysLog();
            //4.封装日志信息
            //4.1封装操作用户的信息
            if(loginUser!=null){
                sysLog.setUserName(loginUser.getUserName());
                sysLog.setCompanyId(loginUser.getCompanyId());
                sysLog.setCompanyName(loginUser.getCompanyName());
            }
            //4.2 获取当前用户的IP地址
            String ip = request.getLocalAddr();
            sysLog.setIp(ip);
            //4.3 设置操作时间
            sysLog.setTime(new Date());
            //4.4 设置方法名称
            sysLog.setMethod(methodName);
            //4.5 设置操作的哪个类
            sysLog.setAction(fullClassName);

            //编写前置通知代码
            //5. 保存日志信息
            sysLogService.save(sysLog);

            //6. 执行目标对象的方法
            result = jp.proceed();

            //编写后置通知代码
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //编写异常通知代码
        }finally {
            //编写最终通知代码
        }
        return result;
    }


}
