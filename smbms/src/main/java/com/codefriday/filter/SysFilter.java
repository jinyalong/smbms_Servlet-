package com.codefriday.filter;

import com.codefriday.ropo.User;
import com.codefriday.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        //获得Session
        User user = (User) request1.getSession().getAttribute(Constants.USER_SESSION);
        //说明已经注销或者未登录
        if(user == null){
            response1.sendRedirect(request1.getContextPath()+"/error.jsp");
        }else{
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
