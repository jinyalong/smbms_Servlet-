package com.codefriday.dao.user;

import com.codefriday.dao.BaseDao;
import com.codefriday.ropo.User;
import com.mysql.jdbc.StringUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    //得到登录用户
    @Override
    public User getLoginUser(String userCode,Connection connection) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user =  null;
        if(connection!=null){
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection,pstm,rs,sql,params);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));

            }
            BaseDao.CloseResource(null,pstm,rs);

        }
        return user;
    }
    //修改用户密码
    @Override
    public int updatePwd(int id, String newPwd,Connection connection) throws SQLException {
        PreparedStatement pst = null;
        int rows = 0;
        if(connection!=null){
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params={newPwd,id};
            rows = BaseDao.execute(connection, pst, sql, params);
        }
        BaseDao.CloseResource(null,pst,null);
        return rows;
    }
    //获得指定参数的用户名数量
    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        List list = new ArrayList();
        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id ");
            if(userName!=null){
                list.add("%"+userName+"%");
                sql.append("and userName like ?");
            }if(userRole>0){
                list.add(userRole);
                sql.append("and userRole = ?");
            }
            System.out.println(sql.toString());
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            if(rs.next()){
                count = rs.getInt("count");
            }
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate ASC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            while(rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.CloseResource(null, pstm, rs);
        }
        return userList;
    }

    @Override
    public int addUser(Connection connection, User user) throws Exception {
        PreparedStatement pstm = null;
        int rows = 0;
        if(connection!=null){
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getUserRole(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            rows = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.CloseResource(null,pstm,null);
        }
        return rows;
    }

    //删除用户
    @Override
    public int delUser(Connection connection, int userID) throws Exception {
        PreparedStatement pstm = null;
        int rows = 0;
        if(connection!=null){
            String sql = "delete from smbms_user where id = ?";
            Object[] params = {userID};
            rows = BaseDao.execute(connection,pstm,sql,params);
            BaseDao.CloseResource(null,pstm,null);
        }
        return rows;
    }

    @Override
    public User getUserById(Connection connection, int userId) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user =  null;
        if(connection!=null){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params = {userId};
            rs = BaseDao.execute(connection,pstm,rs,sql,params);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));

            }
            BaseDao.CloseResource(null,pstm,rs);
        }
        return user;
    }

    //修改用户
    @Override
    public int modify(Connection connection, User user) throws Exception {
        int rows = 0;
        PreparedStatement pstm = null;
        if(null != connection){
            String sql = "update smbms_user set userName=?,"+
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                    user.getModifyDate(),user.getId()};
            rows = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.CloseResource(null, pstm, null);
        }
        return rows;
    }
}
