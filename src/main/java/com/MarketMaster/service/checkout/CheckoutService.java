package com.MarketMaster.service.checkout;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.dao.checkout.CheckoutDao;

public class CheckoutService {
    private static final Logger logger = Logger.getLogger(CheckoutService.class.getName());
    private CheckoutDao checkoutDao;

    public CheckoutService() {
        checkoutDao = new CheckoutDao();
    }

    // 獲取單個結帳記錄
    public CheckoutBean getCheckout(String checkoutId) {
        logger.info("獲取結帳ID為 " + checkoutId + " 的記錄");
        return checkoutDao.getOne(checkoutId);
    }

    // 獲取所有結帳記錄
    public List<CheckoutBean> getAllCheckouts() {
        logger.info("獲取所有結帳記錄");
        return checkoutDao.getAll();
    }

    // 新增結帳記錄
    public boolean addCheckout(CheckoutBean checkout) {
        logger.info("開始新增結帳記錄");
        try {
            return checkoutDao.insert(checkout);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "新增結帳記錄時發生錯誤", e);
            return false;
        }
    }

    // 刪除結帳記錄
    public void deleteCheckout(String checkoutId) {
        logger.info("刪除結帳ID為 " + checkoutId + " 的記錄");
        checkoutDao.delete(checkoutId);
    }

    // 獲取要更新的結帳記錄
    public CheckoutBean getUpdateCheckout(String checkoutId) {
        logger.info("獲取要更新的結帳ID為 " + checkoutId + " 的記錄");
        return checkoutDao.getUpdate(checkoutId);
    }

    // 更新結帳記錄
    public boolean updateCheckout(CheckoutBean checkout) {
        logger.info("更新結帳ID為 " + checkout.getCheckoutId() + " 的記錄");
        return checkoutDao.update(checkout);
    }

    // 根據電話號碼搜索結帳記錄
    public List<CheckoutBean> searchCheckoutsByTel(String customerTel) {
        logger.info("搜索電話號碼為 " + customerTel + " 的結帳記錄");
        return checkoutDao.searchByTel(customerTel);
    }

    // 更新結帳總價
    public void updateTotalPrice(String checkoutId) {
        logger.info("更新結帳ID為 " + checkoutId + " 的總價");
        checkoutDao.updateTotalPrice(checkoutId);
    }

    // 獲取每日銷售報告
    public List<Map<String, Object>> getDailySalesReport() {
        logger.info("獲取每日銷售報告");
        return checkoutDao.getDailySalesReport();
    }

    // 獲取結帳總表
    public List<Map<String, Object>> getCheckoutSummary() {
        logger.info("獲取結帳總表");
        return checkoutDao.getCheckoutSummary();
    }

    // 生成下一個結帳ID
    public String generateNextCheckoutId() {
        logger.info("開始生成下一個結帳ID");
        String lastId = checkoutDao.getLastCheckoutId();

        if (lastId == null || !lastId.matches("C\\d{8}")) {
            logger.info("無效的上一個ID或沒有現有ID，返回初始ID: C00000001");
            return "C00000001";
        }

        try {
            int nextNumber = Integer.parseInt(lastId.substring(1)) + 1;
            String nextId = String.format("C%08d", nextNumber);
            logger.info("成功生成下一個結帳ID: " + nextId);
            return nextId;
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "解析上一個ID時發生錯誤", e);
            return "C00000001";
        }
    }

    // 獲取所有員工ID
    public List<EmpBean> getAllEmployees() {
        logger.info("獲取所有員工ID");
        return checkoutDao.getAllEmployees();
    }

 // 根據類別獲取所有產品名稱 & ID & 價錢
    public List<ProductBean> getProductNamesByCategory(String category) throws ClassNotFoundException, SQLException, NamingException {
        logger.info("獲取類別為 '" + category + "' 的產品");
        return checkoutDao.getProductNamesByCategory(category);
    }



    // 插入結帳記錄和明細
    public boolean insertCheckoutWithDetails(CheckoutBean checkout, List<CheckoutDetailsBean> details) {
        logger.info("插入結帳記錄和明細，結帳ID: " + checkout.getCheckoutId());
        try {
            checkoutDao.insertCheckoutWithDetails(checkout, details);
            // 更新產品庫存
            for (CheckoutDetailsBean detail : details) {
                checkoutDao.updateProductStock(detail.getProductId(), detail.getNumberOfCheckout());
            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "插入結帳記錄和明細時發生錯誤", e);
            return false;
        }
    }


 // 刪除結帳記錄和相關的結帳明細
    public void deleteCheckoutAndDetails(String checkoutId) {
        logger.info("開始刪除結帳記錄及其相關明細，結帳ID: " + checkoutId);
        try {
            checkoutDao.deleteCheckoutAndDetails(checkoutId);
            logger.info("成功刪除結帳記錄及其相關明細，結帳ID: " + checkoutId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除結帳記錄及其相關明細時發生錯誤，結帳ID: " + checkoutId, e);
            throw new RuntimeException("刪除結帳記錄及其相關明細失敗", e);
        }
    }

    // 更新總金額和紅利點數
    public void updateTotalAndBonus(String checkoutId, BigDecimal totalAmount, int bonusPoints) {
        logger.info("更新結帳ID為 " + checkoutId + " 的總金額和紅利點數");
        checkoutDao.updateTotalAndBonus(checkoutId, totalAmount, bonusPoints);
    }

    // 計算紅利點數
    public int calculateBonusPoints(BigDecimal totalAmount) {
        // 假設每100元可得1點紅利
        return totalAmount.divide(new BigDecimal(100), 0, BigDecimal.ROUND_DOWN).intValue();
    }



}