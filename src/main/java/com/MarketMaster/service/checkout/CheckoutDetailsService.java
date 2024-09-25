package com.MarketMaster.service.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.dao.checkout.CheckoutDetailsDao;
import com.MarketMaster.exception.DataAccessException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CheckoutDetailsService {
    @Autowired
    private CheckoutDetailsDao checkoutDetailsDao;

    // 獲取單一結帳明細
    public CheckoutDetailsBean getCheckoutDetails(String checkoutId, String productId) throws DataAccessException {
        return checkoutDetailsDao.getOne(checkoutId, productId);
    }

    // 獲取特定結帳ID的所有明細
    public List<CheckoutDetailsBean> getPartCheckoutDetails(String checkoutId) throws DataAccessException {
        return checkoutDetailsDao.getPart(checkoutId);
    }

    // 獲取所有結帳明細
    public List<CheckoutDetailsBean> getAllCheckoutDetails() throws DataAccessException {
        return checkoutDetailsDao.getAll();
    }

    // 新增結帳明細
    public void addCheckoutDetails(CheckoutDetailsBean checkoutDetails) throws DataAccessException {
        checkoutDetailsDao.insert(checkoutDetails);
    }

    // 更新結帳明細
    public void updateCheckoutDetails(CheckoutDetailsBean checkoutDetails) throws DataAccessException {
        checkoutDetailsDao.update(checkoutDetails);
    }

    // 刪除結帳明細
    public void deleteCheckoutDetails(String checkoutId, String productId) throws DataAccessException {
        checkoutDetailsDao.delete(checkoutId, productId);
    }

    // 根據產品ID搜索結帳明細
    public List<CheckoutDetailsBean> searchCheckoutDetailsByProductId(String productId) throws DataAccessException {
        return checkoutDetailsDao.searchByProductId(productId);
    }

    // 獲取產品退貨率
    public List<Map<String, Object>> getProductReturnRates() throws DataAccessException {
        return checkoutDetailsDao.getProductReturnRates();
    }
}