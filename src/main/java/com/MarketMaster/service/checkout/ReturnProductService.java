package com.MarketMaster.service.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.checkout.ReturnProductBean;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.dao.checkout.ReturnProductDao;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReturnProductService {

    @Autowired
    private ReturnProductDao returnProductDao;

    public ReturnProductBean getReturnProduct(String returnId) throws DataAccessException {
        return returnProductDao.getOne(returnId);
    }

    public List<ReturnProductBean> getAllReturnProducts() throws DataAccessException {
        return returnProductDao.getAll();
    }

    public void addReturnProduct(ReturnProductBean returnProduct) throws DataAccessException {
        returnProductDao.insert(returnProduct);
    }

    public boolean updateReturnProduct(ReturnProductBean returnProduct) throws DataAccessException {
        return returnProductDao.update(returnProduct);
    }

    public void deleteReturnProduct(String returnId) throws DataAccessException {
        returnProductDao.delete(returnId);
    }

    public List<ReturnProductBean> searchReturnProductsByEmployeeId(String employeeId) throws DataAccessException {
        return returnProductDao.searchByEmployeeId(employeeId);
    }

    public void updateReturnTotalPrice(String returnId) throws DataAccessException {
        returnProductDao.updateTotalPrice(returnId);
    }

    public List<Map<String, Object>> getDailyReturnsReport() throws DataAccessException {
        return returnProductDao.getDailyReturnsReport();
    }

    public List<Map<String, Object>> getReturnSummary() throws DataAccessException {
        return returnProductDao.getReturnSummary();
    }

    public String getLastReturnId() throws DataAccessException {
        return returnProductDao.getLastReturnId();
    }

    public boolean insertReturnWithDetails(ReturnProductBean returnProduct, List<ReturnDetailsBean> details) throws DataAccessException {
        return returnProductDao.insertReturnWithDetails(returnProduct, details);
    }

    public void deleteReturnAndDetails(String returnId) throws DataAccessException {
        returnProductDao.deleteReturnAndDetails(returnId);
    }
}