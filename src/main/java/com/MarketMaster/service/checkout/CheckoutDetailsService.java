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

    public CheckoutDetailsBean getCheckoutDetails(String checkoutId, String productId) throws DataAccessException {
        return checkoutDetailsDao.getOne(checkoutId, productId);
    }

    public List<CheckoutDetailsBean> getPartCheckoutDetails(String checkoutId) throws DataAccessException {
        return checkoutDetailsDao.getPart(checkoutId);
    }

    public List<CheckoutDetailsBean> getAllCheckoutDetails() throws DataAccessException {
        return checkoutDetailsDao.getAll();
    }

    public void addCheckoutDetails(CheckoutDetailsBean checkoutDetails) throws DataAccessException {
        checkoutDetailsDao.insert(checkoutDetails);
    }

    public void updateCheckoutDetails(CheckoutDetailsBean checkoutDetails) throws DataAccessException {
        int newCheckoutPrice = checkoutDetails.getNumberOfCheckout() * checkoutDetails.getProductPrice();
        checkoutDetails.setCheckoutPrice(newCheckoutPrice);
        checkoutDetailsDao.update(checkoutDetails);
    }
    
    public void deleteCheckoutDetails(String checkoutId, String productId) throws DataAccessException {
        checkoutDetailsDao.delete(checkoutId, productId);
    }

    public List<CheckoutDetailsBean> searchCheckoutDetailsByProductId(String productId) throws DataAccessException {
        return checkoutDetailsDao.searchByProductId(productId);
    }

    public List<Map<String, Object>> getProductReturnRates() throws DataAccessException {
        return checkoutDetailsDao.getProductReturnRates();
    }
}