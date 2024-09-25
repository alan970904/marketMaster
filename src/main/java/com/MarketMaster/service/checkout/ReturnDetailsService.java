package com.MarketMaster.service.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.dao.checkout.ReturnDetailsDao;
import com.MarketMaster.exception.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReturnDetailsService {

    @Autowired
    private ReturnDetailsDao returnDetailsDao;

    public ReturnDetailsBean getReturnDetails(String returnId, String checkoutId, String productId) throws DataAccessException {
        return returnDetailsDao.getOne(returnId, checkoutId, productId);
    }

    public List<ReturnDetailsBean> getReturnDetailsByReturnId(String returnId) throws DataAccessException {
        return returnDetailsDao.getPart(returnId);
    }

    public List<ReturnDetailsBean> getAllReturnDetails() throws DataAccessException {
        return returnDetailsDao.getAll();
    }

    public void addReturnDetails(ReturnDetailsBean returnDetails) throws DataAccessException {
        returnDetailsDao.insert(returnDetails);
    }

    public void updateReturnDetails(ReturnDetailsBean returnDetails) throws DataAccessException {
        returnDetailsDao.update(returnDetails);
    }

    public void deleteReturnDetails(String returnId, String checkoutId, String productId) throws DataAccessException {
        returnDetailsDao.delete(returnId, checkoutId, productId);
    }

    public List<ReturnDetailsBean> searchReturnDetailsByProductId(String productId) throws DataAccessException {
        return returnDetailsDao.searchByProductId(productId);
    }

    public List<Map<String, Object>> getReturnComparisonReport() throws DataAccessException {
        return returnDetailsDao.getReturnComparisonReport();
    }

    public List<Map<String, Object>> getReturnStatistics(Date startDate, Date endDate) throws DataAccessException {
        return returnDetailsDao.getReturnStatistics(startDate, endDate);
    }

    public List<Map<String, Object>> getMostCommonReturnReasons(int limit) throws DataAccessException {
        return returnDetailsDao.getMostCommonReturnReasons(limit);
    }

    public void updateInventoryAfterReturn(String productId, int returnQuantity) throws DataAccessException {
        returnDetailsDao.updateInventoryAfterReturn(productId, returnQuantity);
    }
}