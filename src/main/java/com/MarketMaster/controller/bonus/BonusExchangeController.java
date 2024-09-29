package com.MarketMaster.controller.bonus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.MarketMaster.bean.bonus.BonusExchangeBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.service.bonus.BonusExchangeService;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/bonusExchange")
public class BonusExchangeController {

    @Autowired
    private BonusExchangeService bonusExchangeService;
    private static final Logger logger = Logger.getLogger(BonusExchangeController.class.getName());
    
    @GetMapping
    public String showBonusExchangeInput() {
        return "bonus/BonusExchangeInput";
    }
    
    @PostMapping("/queryProducts")//電話查詢可兌換商品 輸入頁面
    public String queryExchangeableProducts(@RequestParam String customerTel, Model model) {
        try {
            List<ProductBean> exchangeableProducts = bonusExchangeService.getExchangeableProducts(customerTel);
            int customerTotalPoints = bonusExchangeService.getCustomerTotalPoints(customerTel);//目前總點數
            model.addAttribute("exchangeableProducts", exchangeableProducts);
            model.addAttribute("customerTotalPoints", customerTotalPoints);//目前總點數
            model.addAttribute("customerTel", customerTel);//目前總點數
            return "bonus/BonusExchangeList";
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "查詢可兌換商品失敗: " + e.getMessage());
            return "bonus/BonusExchangeInput";
        }
    }

    @PostMapping("/execute") //該會員可兌換商品清單 兌換商品button
    public String executeExchange(@RequestParam String customerTel, 
                                  @RequestParam String productId, 
                                  @RequestParam int exchangeQuantity, 
                                  Model model) {
    	 logger.info("Received execute request for customer: " + customerTel);
        try {
            bonusExchangeService.executeExchange(customerTel, productId, exchangeQuantity);
            model.addAttribute("successMessage", "兌換成功！");
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "兌換失敗：" + e.getMessage());
        }
        return queryExchangeableProducts(customerTel, model);
    }
    
    @RequestMapping(value = "/records", method = {RequestMethod.GET, RequestMethod.POST})
    //@PostMapping("/records")	//獲取該會員兌換歷史紀錄 //改Request同時可以轉發Get& Post方法
    public String showExchangeRecords(@RequestParam String customerTel, Model model) {
        try {
            List<BonusExchangeBean> exchangeRecords = bonusExchangeService.getExchangeRecords(customerTel);
            int customerTotalPoints = bonusExchangeService.getCustomerTotalPoints(customerTel);//目前總點數
            model.addAttribute("exchangeRecords", exchangeRecords);
            model.addAttribute("customerTotalPoints", customerTotalPoints);//目前總點數保持輸入號碼
            model.addAttribute("customerTel", customerTel); // 添加這行,將customerTel傳遞給視圖
            return "bonus/BonusExchangeRecord";
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "獲取兌換記錄失敗: " + e.getMessage());
            return "bonus/BonusExchangeInput";
        }
    }
    
    @PostMapping("/list")		//返回會員可兌換商品結果
    public String showBonusExchangeList(@RequestParam String customerTel, Model model) {
    	try {
    		List<ProductBean> exchangeableProducts = bonusExchangeService.getExchangeableProducts(customerTel);
    		int customerTotalPoints = bonusExchangeService.getCustomerTotalPoints(customerTel);
    		model.addAttribute("customerTotalPoints", customerTotalPoints);
    		model.addAttribute("exchangeableProducts", exchangeableProducts);
    		model.addAttribute("customerTel", customerTel);
    		return "bonus/BonusExchangeList";
    	} catch (DataAccessException e) {
    		model.addAttribute("errorMessage", "查詢可兌換商品失敗: " + e.getMessage());
    		return "bonus/BonusExchangeInput";
    	}
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateExchange(@RequestParam String exchangeId, @RequestParam int newPoints) {
        try {
            bonusExchangeService.updateExchange(exchangeId, newPoints);
            return "{\"status\":\"success\",\"message\":\"兌換記錄更新成功！\"}";
        } catch (DataAccessException e) {
            return "{\"status\":\"error\",\"message\":\"更新兌換記錄失敗: " + e.getMessage() + "\"}";
        }
    }
    
    @PostMapping("/updateExchangeRecord")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateExchangeRecord(
            @RequestParam String exchangeId,
            @RequestParam String productId,
            @RequestParam int usePoints,
            @RequestParam int numberOfExchange) {
        try {
            bonusExchangeService.updateExchangeRecord(exchangeId, productId, usePoints, numberOfExchange);
            return ResponseEntity.ok(Map.of("status", "success", "message", "兌換記錄更新成功！"));
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "更新兌換記錄失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "更新兌換記錄失敗: " + e.getMessage()));
        }
    }
    
    @GetMapping("/products")  //兌換紀錄更改子選單 取得兌換商品類別
    @ResponseBody
    public ResponseEntity<List<ProductBean>> getProductsByCategory(@RequestParam String category) {
        try {
            List<ProductBean> products = bonusExchangeService.getProductsByCategory(category);
            return ResponseEntity.ok(products);
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "獲取商品列表失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
    
    @GetMapping("/productNames")
    @ResponseBody
    public ResponseEntity<List<ProductBean>> getProductNames(@RequestParam String category) {
        try {
            List<ProductBean> products = bonusExchangeService.getProductNamesByCategory(category);
            return ResponseEntity.ok(products);
        } catch (DataAccessException e) {
            logger.severe("獲取產品名稱失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
    
    @GetMapping("/getProductDetails")
    @ResponseBody
    public ResponseEntity<ProductBean> getProductDetails(@RequestParam String productId) {
        try {
            ProductBean product = bonusExchangeService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (DataAccessException e) {
            logger.severe("獲取商品詳細信息失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PostMapping("/deleteExchangeRecords")
    @ResponseBody
    public String deleteExchange(@RequestParam String exchangeId) {
        try {
            bonusExchangeService.deleteExchangeRecord(exchangeId);
            return "{\"status\":\"success\",\"message\":\"兌換記錄刪除成功！\\n\\n紅利點數已退還\\n已轉回庫存商品\"}";
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "刪除兌換記錄失敗", e);
            return "{\"status\":\"error\",\"message\":\"刪除兌換記錄失敗: " + e.getMessage() + "\"}";
        }
    }
    
}