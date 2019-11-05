package cn.itcast.web.exceptions;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定制的全局异常处理类
 */
public class CustomExceptionRosolver implements HandlerExceptionResolver{

    /**
     *该方法在控制器的任何方法出现异常就会执行（注意：在控制器不能自行处理异常，不要自己try catch）
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mv = new ModelAndView();

        //1.存入异常信息
        mv.addObject("errorMsg","对不起，我错误了："+e.getMessage());

        //2.跳转到错误提示页面
        mv.setViewName("error");

        return mv;
    }
}
