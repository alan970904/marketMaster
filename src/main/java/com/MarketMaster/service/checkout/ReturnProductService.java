package com.MarketMaster.service.checkout;

import java.util.List;
import java.util.Map;
import com.MarketMaster.bean.checkout.ReturnProductBean;
import com.MarketMaster.dao.checkout.ReturnProductDao;

public class ReturnProductService {
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

    public void updateReturnProduct(ReturnProductBean returnProduct) {
        returnProductDao.update(returnProduct);
    }

    public void deleteReturnProduct(String returnId) {
        returnProductDao.delete(returnId);
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
}