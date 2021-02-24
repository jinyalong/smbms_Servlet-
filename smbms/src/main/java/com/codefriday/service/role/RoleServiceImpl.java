package com.codefriday.service.role;

import com.codefriday.dao.BaseDao;
import com.codefriday.dao.role.RoleDao;
import com.codefriday.dao.role.RoleDaoImpl;
import com.codefriday.ropo.Role;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao= null;
    public RoleServiceImpl(){
        roleDao = new RoleDaoImpl();
    }
    //获得角色列表
    @Override
    public List<Role> getRoleList(){
        Connection connection = null;
        List<Role> roleList = new ArrayList<Role>();
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleList;
    }
}
