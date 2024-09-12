package com.MarketMaster.service.checkout;

import java.util.List;
import java.util.Map;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.dao.checkout.ReturnDetailsDao;

public class ReturnDetailsService {
    private ReturnDetailsDao returnDetailsDao = new ReturnDetailsDao();

//    public ReturnDetailsBean getReturnDetails(String returnId, String checkoutId, String productId) {
//        return returnDetailsDao.getOne(returnId, checkoutId, productId);
//    }

//    public List<ReturnDetailsBean> getAllReturnDetails() {
//        return returnDetailsDao.getAll();
//    }
//
//    public void addReturnDetails(ReturnDetailsBean returnDetails) {
//        returnDetailsDao.insert(returnDetails);
//    }
//
//    public void updateReturnDetails(ReturnDetailsBean returnDetails) {
//        returnDetailsDao.update(returnDetails);
//    }
//
//    public void deleteReturnDetails(String returnId, String checkoutId, String productId) {
//        returnDetailsDao.delete(returnId, checkoutId, productId);
//    }
//
//    public List<ReturnDetailsBean> searchReturnDetailsByProductId(String productId) {
//        return returnDetailsDao.searchByProductId(productId);
//    }
//
//    public List<Map<String, Object>> getReturnComparisonReport() {
//        return returnDetailsDao.getReturnComparisonReport();
//    }
}