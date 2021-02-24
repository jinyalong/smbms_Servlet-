package com.codefriday.service.user;

import com.codefriday.dao.BaseDao;
import com.codefriday.dao.user.UserDao;
import com.codefriday.dao.user.UserDaoImpl;
import com.codefriday.ropo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserServiceImpl implements UserService{
    //业务层会调用Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;
        //业务层调用具体数据库操作
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(userCode,connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        if(user!=null&&user.getUserPassword().equals(password))
            return user;
        else return null;
    }
    //修改密码业务
    @Override
    public boolean updatePwd(int id, String newPwd) {
        Connection connection = null;
        int rows = 0;
        try{
            connection = BaseDao.getConnection();
            rows = userDao.updatePwd(id, newPwd, connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return rows>0;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = new ArrayList<User>();
        try{
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection,userName,userRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return userList;
    }

    @Override
    public int getUserCount(String userName, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection,userName,userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return count;
    }

    @Override
    public boolean addUser(User user) {
        Connection connection = null;
        boolean flag = false;
        try{
            connection = BaseDao.getConnection();
            //关闭自动提交开启事务
            connection.setAutoCommit(false);
            flag = (userDao.addUser(connection,user)>0);
            connection.commit();

        }catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return flag;
    }

    //已经存在返回真
    @Override
    public boolean userExist(String userCode) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            flag = (userDao.getLoginUser(userCode,connection) == null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return !flag;
    }

    @Override
    public boolean delUserById(int userID) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            flag = (userDao.delUser(connection,userID)>0);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public User getUserById(int userID) {
        Connection connection = null;
        User user = null;
        try{
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection,userID);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return user;
    }

    //修改用户
    @Override
    public boolean modifyUser(User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.modify(connection,user) > 0)
                flag = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.CloseResource(connection, null, null);
        }
        return flag;
    }
}
