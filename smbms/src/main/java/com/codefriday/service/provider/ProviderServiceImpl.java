package com.codefriday.service.provider;

import com.codefriday.dao.BaseDao;
import com.codefriday.dao.provider.ProviderDao;
import com.codefriday.dao.provider.ProviderDaoImpl;
import com.codefriday.ropo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private ProviderDao providerDao = null;

    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
    }

    @Override
    public boolean add(Provider provider) {
        Connection connection = null;
        boolean flag = false;
        try{
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = providerDao.add(connection,provider)>0;
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
    public List<Provider> getProviderList(String proName, String proCode) {
        Connection connection = null;
        List<Provider> providerList = new ArrayList<>();
        try{
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection,proName,proCode);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return providerList;
    }

    @Override
    public int deleteProviderById(String delId) {
        Connection connection = null;
        int rows = 0;
        try{
            connection = BaseDao.getConnection();
            rows  = providerDao.deleteProviderById(connection, delId);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return rows;
    }

    @Override
    public Provider getProviderById(String id) {
        Connection connection = null;
        Provider provider = null;
        try{
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection,id);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return provider;
    }

    @Override
    public boolean modify(Provider provider) {
        Connection connection = null;
        boolean flag = false;
        try{
            connection = BaseDao.getConnection();
            flag = providerDao.modify(connection,provider)>0;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.CloseResource(connection,null,null);
        }
        return flag;
    }
}
