package com.MarketMaster.service.checkout;

import com.MarketMaster.dao.checkout.CheckoutDetailsDao;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

public class CheckoutDetailsService {
	private static final Logger logger = Logger.getLogger(CheckoutDetailsService.class.getName());
    private CheckoutDetailsDao checkoutDetailsDao;

    public CheckoutDetailsService() {
        checkoutDetailsDao = new CheckoutDetailsDao();
    }

    public CheckoutDetailsBean getCheckoutDetails(String checkoutId, String productId) {
        return CheckoutDetailsDao.getOne(checkoutId, productId);
    }
    
    public List<CheckoutDetailsBean> getPartCheckoutDetails(String checkoutId) {
        return CheckoutDetailsDao.getPart(checkoutId);
    }

    public List<CheckoutDetailsBean> getAllCheckoutDetails() {
        return CheckoutDetailsDao.getAll();
    }

    public void addCheckoutDetails(CheckoutDetailsBean checkoutDetails) {
        CheckoutDetailsDao.insert(checkoutDetails);
    }

    public void updateCheckoutDetails(CheckoutDetailsBean checkoutDetails) {
        logger.info("開始更新結帳明細: " + checkoutDetails);
        try {
            // 計算新的 checkout_price
            int newCheckoutPrice = checkoutDetails.getNumberOfCheckout() * checkoutDetails.getProductPrice();
            checkoutDetails.setCheckoutPrice(newCheckoutPrice);
            
            checkoutDetailsDao.update(checkoutDetails);
            logger.info("結帳明細更新成功: " + checkoutDetails);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "更新結帳明細失敗", e);
            throw e;
        }
    }

    public void deleteCheckoutDetails(String checkoutId, String productId) {
        logger.info("刪除結帳ID為 " + checkoutId + " 和產品ID為 " + productId + " 的結帳明細");
        try {
            checkoutDetailsDao.delete(checkoutId, productId);
            logger.info("結帳明細刪除成功");
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "刪除結帳明細失敗", e);
            throw e;
        }
    }

    public List<CheckoutDetailsBean> searchCheckoutDetailsByProductId(String productId) {
        return CheckoutDetailsDao.searchByProductId(productId);
    }

    public void updateAfterReturn(String checkoutId, String productId, int returnQuantity, BigDecimal returnPrice) {
        CheckoutDetailsDao.updateAfterReturn(checkoutId, productId, returnQuantity, returnPrice);
    }

    public void cancelReturn(String checkoutId, String productId, int returnQuantity, BigDecimal returnPrice) {
        CheckoutDetailsDao.cancelReturn(checkoutId, productId, returnQuantity, returnPrice);
    }

    public List<Map<String, Object>> getProductReturnRates() {
        return CheckoutDetailsDao.getProductReturnRates();
    }
    
   
}