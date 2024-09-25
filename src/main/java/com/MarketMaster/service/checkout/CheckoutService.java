package com.MarketMaster.service.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.dao.checkout.CheckoutDao;
import com.MarketMaster.dao.checkout.CheckoutDetailsDao;
import com.MarketMaster.dao.employee.EmpDao;
import com.MarketMaster.dao.product.ProductDao;
import com.MarketMaster.exception.DataAccessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CheckoutService {
    @Autowired
    private CheckoutDao checkoutDao;
    
    @Autowired
    private CheckoutDetailsDao checkoutDetailsDao;

    @Autowired
    private EmpDao empDao;
    
    @Autowired
    private ProductDao productDao;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    // 獲取單個結帳記錄
    public CheckoutBean getCheckout(String checkoutId) throws DataAccessException {
        return checkoutDao.getOne(checkoutId);
    }

    // 獲取所有結帳記錄
    public List<CheckoutBean> getAllCheckouts() throws DataAccessException {
        return checkoutDao.getAll();
    }

    // 新增結帳記錄
    public void addCheckout(CheckoutBean checkout) throws DataAccessException {
        checkoutDao.insert(checkout);
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

    // 獲取所有員工
    public List<EmpBean> getAllEmployees() throws DataAccessException {
        return checkoutDao.getAllEmployees();
    }

    // 根據類別獲取產品
    public List<ProductBean> getProductNamesByCategory(String category) throws DataAccessException {
        return checkoutDao.getProductNamesByCategory(category);
    }
    
    public List<CheckoutDetailsBean> parseCheckoutDetails(String detailsJson) throws DataAccessException {
        try {
            return objectMapper.readValue(detailsJson, new TypeReference<List<CheckoutDetailsBean>>() {});
        } catch (IOException e) {
            throw new DataAccessException("解析結帳明細失敗", e);
        }
    }

    // 插入結帳記錄和明細
    @Transactional
    public boolean insertCheckoutWithDetails(CheckoutBean checkout, List<CheckoutDetailsBean> details) throws DataAccessException {
        try {
            // 插入結帳記錄
            checkoutDao.insert(checkout);

            // 插入結帳明細
            for (CheckoutDetailsBean detail : details) {
                detail.setCheckoutId(checkout.getCheckoutId());
                checkoutDetailsDao.insert(detail);
            }

            // 計算並更新總金額和紅利點數
            BigDecimal totalAmount = calculateTotalAmount(details);
            int bonusPoints = calculateBonusPoints(totalAmount);
            checkout.setCheckoutTotalPrice(totalAmount.intValue());
            checkout.setBonusPoints(bonusPoints);
            checkoutDao.update(checkout);

            return true;
        } catch (Exception e) {
            throw new DataAccessException("新增結帳記錄和明細失敗", e);
        }
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
    private BigDecimal calculateTotalAmount(List<CheckoutDetailsBean> details) {
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