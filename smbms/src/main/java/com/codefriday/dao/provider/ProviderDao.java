package com.codefriday.dao.provider;

import com.codefriday.ropo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {
    //增加供应商
    public int add(Connection connection, Provider provider)throws Exception;
    //获得供应商列表
    public List<Provider> getProviderList(Connection connection, String proName, String proCode)throws Exception;
    //删除订单通过id
    public int deleteProviderById(Connection connection, String delId)throws Exception;
    //通过id获得单个订单
    public Provider getProviderById(Connection connection, String id)throws Exception;
    //修改订单
    public int modify(Connection connection, Provider provider)throws Exception;
}
