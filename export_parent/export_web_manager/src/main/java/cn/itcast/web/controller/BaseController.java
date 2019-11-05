package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 通用控制器类，抽取所有控制器的通用代码
 */
public class BaseController {

    //注入request
    @Autowired
    protected HttpServletRequest request;
    //注入response
    @Autowired
    protected HttpServletResponse response;
    //注入session
    @Autowired
    protected HttpSession session;

    /**
     * 获取当前登录企业的ID
     */
    protected String getLoginCompanyId(){
        return getLoginUser().getCompanyId();
    }

    /**
     * 获取当前登录企业的名称
     */
    protected String getLoginCompanyName(){
        return getLoginUser().getCompanyName();
    }

    /**
     * 获取登录用户数据
     */
    protected User getLoginUser(){
        return (User)session.getAttribute("loginUser");
    }
}
