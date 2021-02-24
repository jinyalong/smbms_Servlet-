package com.codefriday.service.provider;

import com.codefriday.ropo.Provider;

import java.util.List;

public interface ProviderService {
    //增加供应商
    public boolean add(Provider provider);
    //通过条件查询供应商列表
    public List<Provider> getProviderList(String proName, String proCode);
    //删除供应商
    public int deleteProviderById(String delId);
    //通过id获得单个供应商
    public Provider getProviderById(String id);
    //修改供应商
    public boolean modify(Provider provider);
}
