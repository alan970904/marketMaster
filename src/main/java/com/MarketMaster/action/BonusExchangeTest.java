package com.MarketMaster.action;

import com.MarketMaster.service.bonus.BonusExchangeService;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;

public class BonusExchangeTest {
    public static void main(String[] args) {
        BonusExchangeService service = new BonusExchangeService();
        
        // 測試 getExchangeableProducts 方法
        try {
            // 假設 "0912345678" 是一個存在的客戶電話號碼
            String customerTel = "0911111111";	
            List<ProductBean> exchangeableProducts = service.getExchangeableProducts(customerTel);
            
            System.out.println("可兌換商品列表：");
            for (ProductBean product : exchangeableProducts) {
                System.out.println("商品ID: " + product.getProductId() + 
                                   ", 名稱: " + product.getProductName() + 
                                   ", 價格: " + product.getProductPrice() + 
                                   ", 庫存: " + product.getNumberOfInventory());
            }
        } catch (DataAccessException e) {
            System.err.println("錯誤：" + e.getMessage());
            e.printStackTrace();
        }
    }
}