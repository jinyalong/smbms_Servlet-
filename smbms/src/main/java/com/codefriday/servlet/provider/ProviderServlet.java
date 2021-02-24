package com.codefriday.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.codefriday.ropo.Provider;
import com.codefriday.ropo.User;
import com.codefriday.service.provider.ProviderService;
import com.codefriday.service.provider.ProviderServiceImpl;
import com.codefriday.util.Constants;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method!=null&&method.equals("query")){//显示主界面查询
            this.query(req,resp);
        }else if(method!=null&&method.equals("delprovider")){//删除按钮
            this.delProvider(req,resp);
        }else if(method!=null&&method.equals("view")){//查看按钮
            this.getProviderById(req,resp,"providerview.jsp");
        }else if(method!=null&&method.equals("modify")){//修改按钮
            this.getProviderById(req,resp,"providermodify.jsp");
        }else if(method!=null&&method.equals("add")){//添加订单保存按钮
            this.add(req,resp);
        }else if(method!=null&&method.equals("modifysave")){//修改界面的保存按钮
            this.modify(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryProName = req.getParameter("queryProName");
        String queryProCode = req.getParameter("queryProCode");
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList(queryProName,queryProCode);
        req.setAttribute("providerList", providerList);
        req.setAttribute("queryProName", queryProName);
        req.setAttribute("queryProCode", queryProCode);
        req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
    }
    private void delProvider(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            ProviderService providerService = new ProviderServiceImpl();
            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }
    private void getProviderById(HttpServletRequest req, HttpServletResponse resp,String url) throws ServletException, IOException {
        String id = req.getParameter("proid");
        if(!StringUtils.isNullOrEmpty(id)){
            ProviderService providerService = new ProviderServiceImpl();
            Provider provider = null;
            provider = providerService.getProviderById(id);
            req.setAttribute("provider", provider);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }
    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        boolean flag = false;
        ProviderService providerService = new ProviderServiceImpl();
        flag = providerService.add(provider);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("provideradd.jsp").forward(req, resp);
        }
    }
    private void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        String id = req.getParameter("id");
        Provider provider = new Provider();
        provider.setId(Integer.valueOf(id));
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        boolean flag = false;
        ProviderService providerService = new ProviderServiceImpl();
        flag = providerService.modify(provider);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }
    }
}
