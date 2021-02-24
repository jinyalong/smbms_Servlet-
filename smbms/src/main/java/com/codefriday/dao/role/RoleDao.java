package com.codefriday.dao.role;

import com.codefriday.ropo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {

    //获得角色列表
    public List<Role> getRoleList(Connection connection) throws Exception;
}
