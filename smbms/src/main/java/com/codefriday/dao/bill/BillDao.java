package com.codefriday.dao.bill;

import com.codefriday.ropo.Bill;

import java.sql.Connection;
import java.util.List;


public interface BillDao {
    //增加订单
    public int add(Connection connection, Bill bill)throws Exception;
    //获得订单列表
    public List<Bill> getBillList(Connection connection, Bill bill)throws Exception;
    //删除订单
    public int deleteBillById(Connection connection, String delId)throws Exception;
    //获得单个订单
    public Bill getBillById(Connection connection, String id)throws Exception;
    //修改订单
    public int modify(Connection connection, Bill bill)throws Exception;
    //供应商订单数
    public int getBillCountByProviderId(Connection connection, String providerId)throws Exception;
}
