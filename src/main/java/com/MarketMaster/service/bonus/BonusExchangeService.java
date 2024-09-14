package com.MarketMaster.service.bonus;

import java.util.List;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.bean.employee.CustomerBean;
import com.MarketMaster.dao.bonus.BonusExchangeDao;
import com.MarketMaster.dao.employee.CustomerDao;
import com.MarketMaster.exception.DataAccessException;

public class BonusExchangeService {
    private BonusExchangeDao bonusExchangeDao = new BonusExchangeDao();
    private CustomerDao customerDao = new CustomerDao();

    public List<ProductBean> getExchangeableProducts(String customerTel) throws DataAccessException {
        CustomerBean customer = customerDao.getCustomer(customerTel);
        if (customer == null) {
            throw new DataAccessException("Customer not found with tel: " + customerTel);
        }
        int customerPoints = customer.getTotalPoints();
        return bonusExchangeDao.findExchangeableProducts(customerPoints);
    }
    
    public void executeBatchExchange(String productId, String customerTel) throws DataAccessException {
        // 實現批量兌換邏輯
        // 1. 檢查庫存
        // 2. 檢查客戶積分
        // 3. 創建兌換記錄
        // 4. 更新庫存和客戶積分
        // 5. 保存兌換記錄
        // 注意：這裡需要實現具體的邏輯，可能需要調用其他 DAO 方法
    }

    // 其他需要的方法...
}
