package com.codefriday.dao.user;

import com.codefriday.ropo.Role;
import com.codefriday.ropo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到登录用户
    public User getLoginUser(String userCode,Connection connection) throws SQLException;

    //修改用户密码
    public int updatePwd(int id,String newPwd,Connection connection) throws SQLException;

    //查询用户总数,带特定的参数
    public int getUserCount(Connection connection,String userName,int userRole) throws SQLException;

    //获得用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;

    //添加用户
    public int addUser(Connection connection,User user) throws Exception;

    //删除用户
    public int delUser(Connection connection,int userID) throws Exception;

    //通过id获得用户
    public User getUserById(Connection connection,int userId) throws Exception;

    //修改用户
    public int modify(Connection connection,User user)throws Exception;
}
