package com.MarketMaster.service.checkout;

import com.MarketMaster.dao.checkout.ReturnProductDao;
import com.MarketMaster.bean.checkout.CheckoutBean;
import java.util.List;
import java.util.Map;

public class ReturnProductService {
    private ReturnProductDao returnProductDao = new ReturnProductDao();

    public CheckoutBean getReturnProduct(String checkoutId) {
        return ReturnProductDao.getOne(checkoutId);
    }

    public List<CheckoutBean> getAllReturnProducts() {
        return ReturnProductDao.getAll();
    }

    public void addReturnProduct(CheckoutBean returnProduct) {
        ReturnProductDao.insert(returnProduct);
    }

    public void updateReturnProduct(CheckoutBean returnProduct) {
        ReturnProductDao.update(returnProduct);
    }

    public void deleteReturnProduct(String checkoutId) {
        ReturnProductDao.delete(checkoutId);
    }

    public List<CheckoutBean> searchReturnProductsByTel(String customerTel) {
        return ReturnProductDao.searchByTel(customerTel);
    }

    public void updateReturnTotalPrice(String returnId) {
        ReturnProductDao.updateTotalPrice(returnId);
    }

    public List<Map<String, Object>> getDailyReturnsReport() {
        return ReturnProductDao.getDailyReturnsReport();
    }

    public List<Map<String, Object>> getReturnSummary() {
        return ReturnProductDao.getReturnSummary();
    }
}