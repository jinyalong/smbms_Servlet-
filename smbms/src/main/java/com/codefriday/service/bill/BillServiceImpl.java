package com.codefriday.service.bill;

import com.codefriday.dao.BaseDao;
import com.codefriday.dao.bill.BillDao;
import com.codefriday.dao.bill.BillDaoImpl;
import com.codefriday.ropo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BillServiceImpl implements BillService{
    private BillDao billDao = null;

    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    @Override
    public boolean add(Bill bill) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            flag = (billDao.add(connection,bill) > 0);
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

    @Override
    public List<Bill> getBillList(Bill bill) {
        Connection connection = null;
        List<Bill> billList = new ArrayList<>();
        connection = BaseDao.getConnection();
        try {
            billList = billDao.getBillList(connection, bill);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return billList;
    }

    @Override
    public boolean deleteBillById(String delId) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            flag = (billDao.deleteBillById(connection, delId) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.CloseResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public Bill getBillById(String id) {
        Bill bill = null;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        }catch (Exception e) {
            e.printStackTrace();
            bill = null;
        }finally{
            BaseDao.CloseResource(connection, null, null);
        }
        return bill;
    }

    @Override
    public boolean modify(Bill bill) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            flag = (billDao.modify(connection,bill) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.CloseResource(connection, null, null);
        }
        return flag;
    }
}
