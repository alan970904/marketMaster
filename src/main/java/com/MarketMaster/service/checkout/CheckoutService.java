package com.MarketMaster.service.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.dao.checkout.CheckoutDao;
import com.MarketMaster.exception.DataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CheckoutService {

    @Autowired
    private CheckoutDao checkoutDao;

    // 獲取單個結帳記錄
    public CheckoutBean getCheckout(String checkoutId) throws DataAccessException {
        return checkoutDao.getOne(checkoutId);
    }

    // 獲取所有結帳記錄
    public List<CheckoutBean> getAllCheckouts() throws DataAccessException {
        return checkoutDao.getAll();
    }

    // 新增結帳記錄
    public boolean addCheckout(CheckoutBean checkout) throws DataAccessException {
        checkoutDao.insert(checkout);
        return true;
    }

    // 刪除結帳記錄
    public void deleteCheckout(String checkoutId) throws DataAccessException {
        checkoutDao.delete(checkoutId);
    }

    // 更新結帳記錄
    public boolean updateCheckout(CheckoutBean checkout) throws DataAccessException {
        return checkoutDao.update(checkout);
    }

    // 根據電話號碼搜索結帳記錄
    public List<CheckoutBean> searchCheckoutsByTel(String customerTel) throws DataAccessException {
        return checkoutDao.searchByTel(customerTel);
    }

    // 獲取每日銷售報告
    public List<Map<String, Object>> getDailySalesReport() throws DataAccessException {
        return checkoutDao.getDailySalesReport();
    }

    // 獲取結帳總表
    public List<Map<String, Object>> getCheckoutSummary() throws DataAccessException {
        return checkoutDao.getCheckoutSummary();
    }

    // 生成下一個結帳ID
    public String generateNextCheckoutId() throws DataAccessException {
        String lastId = checkoutDao.getLastCheckoutId();
        if (lastId == null || !lastId.matches("C\\d{8}")) {
            return "C00000001";
        }
        int nextNumber = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("C%08d", nextNumber);
    }

    // 獲取所有員工ID
    public List<EmpBean> getAllEmployees() throws DataAccessException {
        return checkoutDao.getAllEmployees();
    }

    // 根據類別獲取所有產品名稱 & ID & 價錢
    public List<ProductBean> getProductNamesByCategory(String category) throws DataAccessException {
        return checkoutDao.getProductNamesByCategory(category);
    }

    // 插入結帳記錄和明細
    public boolean insertCheckoutWithDetails(CheckoutBean checkout, List<CheckoutDetailsBean> details) throws DataAccessException {
        return checkoutDao.insertCheckoutWithDetails(checkout, details);
    }

    // 刪除結帳記錄和相關的結帳明細
    public void deleteCheckoutAndDetails(String checkoutId) throws DataAccessException {
        checkoutDao.deleteCheckoutAndDetails(checkoutId);
    }

    // 更新總金額和紅利點數
    public void updateTotalAndBonus(String checkoutId, BigDecimal totalAmount, int bonusPoints) throws DataAccessException {
        checkoutDao.updateTotalAndBonus(checkoutId, totalAmount, bonusPoints);
    }

    // 計算總金額
    public BigDecimal calculateTotalAmount(List<CheckoutDetailsBean> details) {
        return details.stream()
                .map(detail -> new BigDecimal(detail.getCheckoutPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 計算紅利點數
    public int calculateBonusPoints(BigDecimal totalAmount) {
        // 假設每100元可得1點紅利
        return totalAmount.divide(new BigDecimal(100), 0, BigDecimal.ROUND_DOWN).intValue();
    }
}