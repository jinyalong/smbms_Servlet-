package com.codefriday.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.codefriday.ropo.Role;
import com.codefriday.ropo.User;
import com.codefriday.service.role.RoleService;
import com.codefriday.service.role.RoleServiceImpl;
import com.codefriday.service.user.UserService;
import com.codefriday.service.user.UserServiceImpl;
import com.codefriday.util.Constants;
import com.codefriday.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        //Servlet复用
        if(method!=null&&method.equals("savepwd")){//修改密码
            this.updatePwd(req,resp);
        }else if(method!=null&&method.equals("pwdmodify")){//验证旧密码
            this.checkOldpwd(req,resp);
        }else if(method!=null&&method.equals("query")){//查询用户列表（联合角色列表）
            this.query(req,resp);
        }else if(method!=null&&method.equals("add")){//添加用户
            this.add(req,resp);
        }else if(method!=null&&method.equals("getrolelist")){//添加用户界面的获得角色列表
            this.getRoleList(req,resp);
        }else if(method!=null&&method.equals("ucexist")){//查询用户名是否存在
            this.ucExist(req,resp);
        }else if(method!=null&&method.equals("deluser")){//删除用户
            this.delUser(req,resp);
        }else if(method!=null&&method.equals("modify")) {//跳转到修改页面
            this.getUserById(req, resp, "usermodify.jsp");
        }else if(method!=null&&method.equals("modifyexe")){//执行修改
            this.modify(req,resp);
        }else if(method!=null&&method.equals("view")){
            this.getUserById(req, resp,"userview.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码的控制层代码
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object o = session.getAttribute(Constants.USER_SESSION);
        String newPwd = req.getParameter("newpassword");
        if(o!=null && newPwd!=null && newPwd.length()!=0){
            UserService userService = new UserServiceImpl();
            boolean flag = userService.updatePwd(((User) o).getId(), newPwd);
            if(flag){//密码修改成功，移除现在登录的user_session
                req.setAttribute("message","修改成功，请退出重新登录!");
                session.removeAttribute(Constants.USER_SESSION);
            }else{//密码修改失败
                req.setAttribute("message","修改失败!");
            }
        }else{
            req.setAttribute("message","新密码错误!");
        }
        req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);
    }

    //ajax验证旧密码
    private void checkOldpwd(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();
        Object o = session.getAttribute(Constants.USER_SESSION);

        String oldPwd = req.getParameter("oldpassword");
        //结果集
        HashMap<String,String> resultMap = new HashMap<String,String>();
        if(o!=null){
            if(oldPwd!=null || oldPwd.length()!=0){//输入旧密码不为空
                String userPassword = ((User)o).getUserPassword();
                if(userPassword.equals(oldPwd)){//输入旧密码正确
                    resultMap.put("result","true");
                }else{//输入旧密码错误
                    resultMap.put("result","false");
                }
            }
            else{//输入旧密码为空
                resultMap.put("result","error");
            }
        }else{//Session失效了
            resultMap.put("result","sessionerror");
        }
        //设置返回json格式数据
        resp.setContentType("application/json");
        try {
            PrintWriter out = resp.getWriter();
            //阿里巴巴fastjson工具类
            out.println(JSONArray.toJSONString(resultMap));
            //刷新并关闭流
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获得用户，角色列表
    private void query(HttpServletRequest req, HttpServletResponse resp){
        //查询用户列表

        //从前端获取数据
        String queryName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;
        int pageSize = 5;//一页显示的个数，配置文件中
        int currentPageNo = 1;
        if(queryName == null){
            queryName = "";
        }
        if(temp!=null&&!temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }
        if(pageIndex!=null&&!pageIndex.equals("")){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获得用户列表
        UserService userService = new UserServiceImpl();

        //获取用户总数(分页：上一页、下一页)
        int totalCount = userService.getUserCount(queryName, queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //控制首页，尾页
        int totalPageCount = pageSupport.getTotalPageCount();
        if(totalPageCount<1){
            //最小为第一页
            currentPageNo = 1;
        }else if(currentPageNo>totalCount){
            currentPageNo = totalCount;
        }
        //获取用户列表展示
        List<User> userList = userService.getUserList(queryName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);

        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //增加用户
    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("add()================");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        UserService userService = new UserServiceImpl();
        if(userService.addUser(user)){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }
    //角色列表
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(roleList));
        out.flush();
        out.close();
    }
    private void ucExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //判断用户账号是否可用
        String userCode = req.getParameter("userCode");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            UserService userService = new UserServiceImpl();
            boolean flag = userService.userExist(userCode);
            if(flag){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(resultMap));
        out.flush();
        out.close();
    }

    //删除用户
    void delUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uid = req.getParameter("uid");
        int userId = 0;
        if(!StringUtils.isNullOrEmpty(uid)){
            try{
                userId = Integer.parseInt(uid);
            }catch (Exception e){
                userId = 0;
            }
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(userId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            UserService userService = new UserServiceImpl();
            if(userService.delUserById(userId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //跳转到修改用户界面
    private void getUserById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(Integer.parseInt(id));
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    private void modify(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        UserService userService = new UserServiceImpl();
        if(userService.modifyUser(user)){//修改成功
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("usermodify.jsp").forward(request, response);
        }
    }
}
