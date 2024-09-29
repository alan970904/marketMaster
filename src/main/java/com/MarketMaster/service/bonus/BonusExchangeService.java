package com.MarketMaster.service.bonus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.bonus.BonusExchangeBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.dao.bonus.BonusExchangeDao;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Date;

@Service
@Transactional
public class BonusExchangeService {

    @Autowired
    private BonusExchangeDao bonusExchangeDao;
    
    public String generateNextExchangeId() {
        String lastId = bonusExchangeDao.getLastExchangeId();
        if (lastId == null || !lastId.matches("H\\d{8}")) {
            return "H00000001";
        }
        int nextNumber = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("H%08d", nextNumber);
    }
    //展示客戶目前總點數
    public int getCustomerTotalPoints(String customerTel) throws DataAccessException {
        return bonusExchangeDao.getCustomerPoints(customerTel);
    }
    
    
    public List<ProductBean> getExchangeableProducts(String customerTel) throws DataAccessException {
        // 首先獲取客戶的總積分
        int customerPoints = bonusExchangeDao.getCustomerPoints(customerTel);
        // 然後查詢可兌換的商品
        return bonusExchangeDao.findExchangeableProducts(customerPoints);
    }

    public void executeExchange(String customerTel, String productId, int exchangeQuantity) throws DataAccessException {
        // 檢查客戶積分
        int customerPoints = bonusExchangeDao.getCustomerPoints(customerTel);
        
        // 獲取產品信息
        ProductBean product = bonusExchangeDao.getProductById(productId);
        
        // 計算所需積分
        int requiredPoints = product.getProductPrice() * exchangeQuantity;
        
        if (customerPoints < requiredPoints) {
            throw new DataAccessException("積分不足");
        }
        
        // 更新客戶積分
        bonusExchangeDao.updateCustomerPoints(customerTel, customerPoints - requiredPoints);
        
        // 更新產品庫存
        bonusExchangeDao.updateProductInventory(productId, product.getNumberOfInventory() - exchangeQuantity);
        
        // 創建兌換記錄
        BonusExchangeBean exchange = new BonusExchangeBean();
        exchange.setExchangeId(generateNextExchangeId());
        exchange.setCustomerTel(customerTel);
        exchange.setProductId(productId);
        exchange.setUsePoints(requiredPoints);
        exchange.setNumberOfExchange(exchangeQuantity);
        exchange.setExchangeDate(new Date());
        
        bonusExchangeDao.saveExchange(exchange);
    }
    
    public List<BonusExchangeBean> getExchangeRecords(String customerTel) throws DataAccessException {
        return bonusExchangeDao.getExchangeRecords(customerTel);
    }

    public void updateExchange(String exchangeId, int newPoints) throws DataAccessException {
        // Implement update logic here
        // Use bonusExchangeDao methods as needed
    }

    public void deleteExchangeRecord(String exchangeId) throws DataAccessException {
        BonusExchangeBean exchangeRecord = bonusExchangeDao.getExchangeRecordById(exchangeId);
        if (exchangeRecord == null) {
            throw new DataAccessException("兌換記錄不存在");
        }

        // 恢復客戶積分
        int currentPoints = bonusExchangeDao.getCustomerPoints(exchangeRecord.getCustomerTel());
        int pointsToRestore = exchangeRecord.getUsePoints();
        bonusExchangeDao.updateCustomerPoints(exchangeRecord.getCustomerTel(), currentPoints + pointsToRestore);

        // 恢復商品庫存
        ProductBean product = bonusExchangeDao.getProductById(exchangeRecord.getProductId());
        int currentInventory = product.getNumberOfInventory();
        int quantityToRestore = exchangeRecord.getNumberOfExchange();
        bonusExchangeDao.updateProductInventory(exchangeRecord.getProductId(), currentInventory + quantityToRestore);

        // 刪除兌換記錄
        bonusExchangeDao.deleteExchangeRecord(exchangeId);
    }

    public void updateExchangeRecord(String exchangeId, String productId, int usePoints, int numberOfExchange) throws DataAccessException {
        BonusExchangeBean exchangeRecord = bonusExchangeDao.getExchangeRecordById(exchangeId);
        if (exchangeRecord == null) {
            throw new DataAccessException("兌換記錄不存在");
        }

        // 計算積分和庫存的差異
        int pointsDifference = usePoints - exchangeRecord.getUsePoints();
        int quantityDifference = numberOfExchange - exchangeRecord.getNumberOfExchange();

        // 更新客戶積分
        int currentPoints = bonusExchangeDao.getCustomerPoints(exchangeRecord.getCustomerTel());
        bonusExchangeDao.updateCustomerPoints(exchangeRecord.getCustomerTel(), currentPoints - pointsDifference);

        // 更新商品庫存
        ProductBean product = bonusExchangeDao.getProductById(productId);
        int currentInventory = product.getNumberOfInventory();
        bonusExchangeDao.updateProductInventory(productId, currentInventory - quantityDifference);

        // 更新兌換記錄
        exchangeRecord.setProductId(productId);
        exchangeRecord.setUsePoints(usePoints);
        exchangeRecord.setNumberOfExchange(numberOfExchange);
        bonusExchangeDao.updateExchangeRecord(exchangeRecord);
    }

	public List<ProductBean> getProductsByCategory(String category) throws DataAccessException {
        return bonusExchangeDao.getProductsByCategory(category);
    }

    public List<ProductBean> getProductNamesByCategory(String category) throws DataAccessException {
        return bonusExchangeDao.getProductNamesByCategory(category);
    }

    public ProductBean getProductById(String productId) throws DataAccessException {
        try {
            return bonusExchangeDao.getProductById(productId);
        } catch (Exception e) {
            throw new DataAccessException("獲取商品詳細信息失敗: " + e.getMessage(), e);
        }
    }
}