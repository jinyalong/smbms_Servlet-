package com.codefriday.service.user;

import com.codefriday.ropo.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {
    //用户登录业务
    public User login(String userCode,String password);
    //修改密码业务
    public boolean updatePwd(int id,String newPwd);
    //查询用户数量
    public int getUserCount(String userName,int userRole);
    //查询用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);
    //添加用户
    public boolean addUser(User user);
    //查询用户编码是否可用
    public boolean userExist(String userCode);
    //通过id删除用户
    public boolean delUserById(int userID);
    //通过id获得用户
    public User getUserById(int userID);
    //修改用户
    public boolean modifyUser(User user);
}
