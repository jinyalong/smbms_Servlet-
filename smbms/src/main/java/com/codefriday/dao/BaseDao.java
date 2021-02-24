package com.codefriday.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String Driver;
    private static String Url;
    private static String User;
    private static String Password;
    //类加载时初始化
    static {
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Driver = properties.getProperty("Driver");
        Url = properties.getProperty("Url");
        User = properties.getProperty("User");
        Password = properties.getProperty("Password");
    }
    //获得连接的静态方法
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(Driver);
            conn =  DriverManager.getConnection(Url,User,Password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    //查询公共方法
    public static ResultSet execute(Connection conn,PreparedStatement pst,ResultSet rs,String sql,Object params[]) throws SQLException {
        pst = conn.prepareStatement(sql);
        for(int i = 0;i < params.length;i++){
            pst.setObject(i+1,params[i]);
        }
        rs = pst.executeQuery();
        return rs;
    }
    //增删改公共方法，重载
    public static int execute(Connection conn,PreparedStatement pst,String sql,Object params[]) throws SQLException {
        pst = conn.prepareStatement(sql);
        for(int i = 0;i < params.length;i++){
            pst.setObject(i+1,params[i]);
        }
        int UpdateRows = pst.executeUpdate();
        return UpdateRows;
    }
    //关闭资源
    public static boolean CloseResource(Connection conn,PreparedStatement pst,ResultSet rs){
        boolean flag = true;
        if(rs!=null){
            try {
                rs.close();
                rs = null;
            } catch (SQLException throwables) {
                flag = false;
                throwables.printStackTrace();
            }
        }
        if(pst!=null){
            try {
                pst.close();
                pst = null;
            } catch (SQLException throwables) {
                flag = false;
                throwables.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
                conn = null;
            } catch (SQLException throwables) {
                flag = false;
                throwables.printStackTrace();
            }
        }
        return flag;
    }
}
