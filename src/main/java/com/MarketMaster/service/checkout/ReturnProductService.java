package com.MarketMaster.service.checkout;

import com.MarketMaster.bean.checkout.ReturnProductBean;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.dao.checkout.ReturnProductDao;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnProductService {
    private static final Logger logger = Logger.getLogger(ReturnProductService.class.getName());
    private ReturnProductDao returnProductDao = new ReturnProductDao();

    public ReturnProductBean getReturnProduct(String returnId) {
        return returnProductDao.getOne(returnId);
    }

    public List<ReturnProductBean> getAllReturnProducts() {
        return returnProductDao.getAll();
    }

    public void addReturnProduct(ReturnProductBean returnProduct) {
        returnProductDao.insert(returnProduct);
    }

    public boolean updateReturnProduct(ReturnProductBean returnProduct) {
        return returnProductDao.update(returnProduct);
    }

    public void deleteReturnProduct(String returnId) {
        returnProductDao.delete(returnId);
    }

    public List<ReturnProductBean> searchReturnProductsByEmployeeId(String employeeId) {
        return returnProductDao.searchByEmployeeId(employeeId);
    }

    public void updateReturnTotalPrice(String returnId) {
        returnProductDao.updateTotalPrice(returnId);
    }

    public List<Map<String, Object>> getDailyReturnsReport() {
        return returnProductDao.getDailyReturnsReport();
    }

    public List<Map<String, Object>> getReturnSummary() {
        return returnProductDao.getReturnSummary();
    }

    public String getLastReturnId() {
        return returnProductDao.getLastReturnId();
    }

    public boolean insertReturnWithDetails(ReturnProductBean returnProduct, List<ReturnDetailsBean> details) {
        return returnProductDao.insertReturnWithDetails(returnProduct, details);
    }

    public void deleteReturnAndDetails(String returnId) {
        returnProductDao.deleteReturnAndDetails(returnId);
    }
}