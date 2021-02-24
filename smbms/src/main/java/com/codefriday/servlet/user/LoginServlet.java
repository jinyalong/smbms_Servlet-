package com.codefriday.servlet.user;

import com.codefriday.ropo.User;
import com.codefriday.service.user.UserServiceImpl;
import com.codefriday.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("登录Servlet---");
        String userCode = req.getParameter("userCode");
        String password = req.getParameter("userPassword");
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(userCode, password);
        if(user!=null){//存在这个用户，可以登录
            //将用户的信息放到Session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //重定向到内部
            resp.sendRedirect("/jsp/frame.jsp");
        }else{//查不到这个人
            //转发回去，并提示错误信息
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
