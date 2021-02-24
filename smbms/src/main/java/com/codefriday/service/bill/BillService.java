package com.codefriday.service.bill;

import com.codefriday.ropo.Bill;

import java.util.List;

public interface BillService {
    //增加订单
    public boolean add(Bill bill);
    //获得订单列表，带条件
    public List<Bill> getBillList(Bill bill);
    //删除订单
    public boolean deleteBillById(String delId);
    //通过ID获得单个订单
    public Bill getBillById(String id);
    //修改订单
    public boolean modify(Bill bill);
}
