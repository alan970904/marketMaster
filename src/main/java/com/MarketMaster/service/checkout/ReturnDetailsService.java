package com.MarketMaster.service.checkout;

import com.MarketMaster.dao.checkout.ReturnDetailsDao;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import java.util.List;
import java.util.Map;

public class ReturnDetailsService {
    private ReturnDetailsDao returnDetailsDao = new ReturnDetailsDao();

    public ReturnDetailsBean getReturnDetails(String returnId) {
        return ReturnDetailsDao.getOne(returnId);
    }

    public List<ReturnDetailsBean> getAllReturnDetails() {
        return ReturnDetailsDao.getAll();
    }

    public void addReturnDetails(ReturnDetailsBean returnDetails) {
        ReturnDetailsDao.insert(returnDetails);
    }

    public void updateReturnDetails(ReturnDetailsBean returnDetails) {
        ReturnDetailsDao.update(returnDetails);
    }

    public void deleteReturnDetails(String returnId) {
        ReturnDetailsDao.delete(returnId);
    }

    public List<ReturnDetailsBean> searchReturnDetailsByProductId(String productId) {
        return ReturnDetailsDao.searchByProductId(productId);
    }

    public List<Map<String, Object>> getReturnComparisonReport() {
        return ReturnDetailsDao.getReturnComparisonReport();
    }
}