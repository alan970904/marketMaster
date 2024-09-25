package com.MarketMaster.controller.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.service.checkout.CheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.MarketMaster.exception.DataAccessException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	private static final Logger logger = Logger.getLogger(CheckoutController.class.getName());

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/checkoutMain")
    public String showCheckoutMain() {
        return "checkout/checkout/index";
    }

    @GetMapping("/list")
    public String getAllCheckouts(Model model) {
        try {
            model.addAttribute("checkouts", checkoutService.getAllCheckouts());
            return "checkout/checkout/GetAllCheckouts";
        } catch (DataAccessException e) {
            logger.severe("獲取所有結帳記錄失敗: " + e.getMessage());
            model.addAttribute("error", "獲取結帳記錄失敗，請稍後再試");
            return "error";
        }
    }

    @GetMapping("/details")
    public String getCheckout(@RequestParam String checkoutId, Model model) {
        try {
            model.addAttribute("checkout", checkoutService.getCheckout(checkoutId));
            return "checkout/checkout/GetCheckout";
        } catch (DataAccessException e) {
            logger.severe("獲取結帳記錄失敗: " + e.getMessage());
            model.addAttribute("error", "獲取結帳記錄失敗，請稍後再試");
            return "error";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        try {
            model.addAttribute("nextId", checkoutService.generateNextCheckoutId());
            model.addAttribute("checkout", new CheckoutBean());
            model.addAttribute("employees", checkoutService.getAllEmployees());
            return "checkout/checkout/InsertCheckout";
        } catch (DataAccessException e) {
            logger.severe("準備新增結帳表單失敗: " + e.getMessage());
            model.addAttribute("error", "準備新增結帳表單失敗，請稍後再試");
            return "error";
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addCheckoutWithDetails(@RequestBody Map<String, Object> requestData) {
        logger.info("開始處理新增結帳請求");
        
        try {
            // 解析並驗證結帳基本資訊
            CheckoutBean checkout = parseCheckoutData(requestData);
            
            // 解析並驗證結帳明細
            List<CheckoutDetailsBean> detailsList = parseCheckoutDetails(requestData);
            
            // 驗證結帳資料的完整性
            validateCheckoutData(checkout, detailsList);
            
            // 執行結帳新增操作
            boolean success = checkoutService.insertCheckoutWithDetails(checkout, detailsList);
            
            // 根據操作結果返回響應
            if (success) {
                logger.info("結帳記錄新增成功，ID: " + checkout.getCheckoutId());
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "結帳記錄新增成功",
                    "checkoutId", checkout.getCheckoutId()
                ));
            } else {
                logger.warning("結帳記錄新增失敗");
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "結帳記錄新增失敗，請檢查輸入資料並重試"
                ));
            }
        } catch (InvalidCheckoutDataException e) {
            logger.severe("結帳資料驗證失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "結帳資料無效: " + e.getMessage()
            ));
        } catch (DataAccessException e) {
            logger.severe("資料存取錯誤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "新增結帳記錄時發生資料庫錯誤，請稍後再試"
            ));
        } catch (Exception e) {
            logger.severe("處理結帳請求時發生未預期的錯誤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "處理請求時發生未知錯誤，請聯繫系統管理員"
            ));
        }
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String checkoutId, Model model) {
        try {
            model.addAttribute("checkout", checkoutService.getCheckout(checkoutId));
            return "checkout/checkout/GetUpdateCheckout";
        } catch (DataAccessException e) {
            logger.severe("獲取更新結帳表單失敗: " + e.getMessage());
            model.addAttribute("error", "獲取更新結帳表單失敗，請稍後再試");
            return "error";
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateCheckout(@ModelAttribute CheckoutBean checkout) {
        try {
            boolean success = checkoutService.updateCheckout(checkout);
            if (success) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "更新成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "更新失敗"));
            }
        } catch (DataAccessException e) {
            logger.severe("更新結帳記錄失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "更新時發生錯誤: " + e.getMessage()));
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteCheckout(@RequestParam String checkoutId) {
        try {
            checkoutService.deleteCheckoutAndDetails(checkoutId);
            return ResponseEntity.ok(Map.of("status", "success", "message", "結帳記錄及其相關明細已成功刪除"));
        } catch (DataAccessException e) {
            logger.severe("刪除結帳記錄失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "刪除結帳記錄及其相關明細失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public String searchCheckoutsByTel(@RequestParam String customerTel, Model model) {
        try {
            model.addAttribute("checkouts", checkoutService.searchCheckoutsByTel(customerTel));
            return "checkout/checkout/SearchResults";
        } catch (DataAccessException e) {
            logger.severe("搜索結帳記錄失敗: " + e.getMessage());
            model.addAttribute("error", "搜索結帳記錄失敗，請稍後再試");
            return "error";
        }
    }

    @GetMapping("/dailySalesReport")
    public String getDailySalesReport(Model model) {
        try {
            model.addAttribute("dailySalesReport", checkoutService.getDailySalesReport());
            return "checkout/checkout/DailySalesReport";
        } catch (DataAccessException e) {
            logger.severe("獲取每日銷售報告失敗: " + e.getMessage());
            model.addAttribute("error", "獲取每日銷售報告失敗，請稍後再試");
            return "error";
        }
    }

    @GetMapping("/summary")
    public String getCheckoutSummary(Model model) {
        try {
            model.addAttribute("checkoutSummary", checkoutService.getCheckoutSummary());
            return "checkout/checkout/CheckoutSummary";
        } catch (DataAccessException e) {
            logger.severe("獲取結帳總表失敗: " + e.getMessage());
            model.addAttribute("error", "獲取結帳總表失敗，請稍後再試");
            return "error";
        }
    }

    @GetMapping("/nextId")
    @ResponseBody
    public ResponseEntity<String> getNextCheckoutId() {
        try {
            return ResponseEntity.ok(checkoutService.generateNextCheckoutId());
        } catch (DataAccessException e) {
            logger.severe("生成下一個結帳ID失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成結帳ID失敗");
        }
    }

    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        try {
            model.addAttribute("employees", checkoutService.getAllEmployees());
            return "employee/EmployeeList";
        } catch (DataAccessException e) {
            logger.severe("獲取所有員工資料失敗: " + e.getMessage());
            model.addAttribute("error", "獲取員工資料失敗，請稍後再試");
            return "error";
        }
    }

    @GetMapping("/products")
    @ResponseBody
    public ResponseEntity<List<ProductBean>> getProductNames(@RequestParam String category) {
        try {
            return ResponseEntity.ok(checkoutService.getProductNamesByCategory(category));
        } catch (DataAccessException e) {
            logger.severe("獲取產品名稱失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping("/updateTotalAndBonus")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateTotalAndBonus(
            @RequestParam String checkoutId,
            @RequestParam BigDecimal totalAmount,
            @RequestParam int bonusPoints) {
        try {
            checkoutService.updateTotalAndBonus(checkoutId, totalAmount, bonusPoints);
            return ResponseEntity.ok(Map.of("status", "success", "message", "總金額和紅利點數已更新"));
        } catch (DataAccessException e) {
            logger.severe("更新總金額和紅利點數失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "更新失敗: " + e.getMessage()));
        }
    }
    
    // 輔助方法

    private CheckoutBean parseCheckoutData(Map<String, Object> requestData) throws InvalidCheckoutDataException {
        CheckoutBean checkout = new CheckoutBean();
        try {
            checkout.setCheckoutId((String) requestData.get("checkoutId"));
            checkout.setCustomerTel((String) requestData.get("customerTel"));
            checkout.setEmployeeId((String) requestData.get("employeeId"));
            checkout.setCheckoutDate(new Date()); // 設置為當前日期，或從請求中獲取
            // 設置其他必要的屬性...
        } catch (Exception e) {
            throw new InvalidCheckoutDataException("解析結帳基本資訊失敗: " + e.getMessage());
        }
        return checkout;
    }

    private List<CheckoutDetailsBean> parseCheckoutDetails(Map<String, Object> requestData) throws InvalidCheckoutDataException {
        try {
            String detailsJson = objectMapper.writeValueAsString(requestData.get("details"));
            return checkoutService.parseCheckoutDetails(detailsJson);
        } catch (Exception e) {
            throw new InvalidCheckoutDataException("解析結帳明細失敗: " + e.getMessage());
        }
    }

    private void validateCheckoutData(CheckoutBean checkout, List<CheckoutDetailsBean> details) throws InvalidCheckoutDataException {
        if (checkout.getCheckoutId() == null || checkout.getCheckoutId().isEmpty()) {
            throw new InvalidCheckoutDataException("結帳 ID 不能為空");
        }
        if (checkout.getCustomerTel() == null || checkout.getCustomerTel().isEmpty()) {
            throw new InvalidCheckoutDataException("顧客電話不能為空");
        }
        if (checkout.getEmployeeId() == null || checkout.getEmployeeId().isEmpty()) {
            throw new InvalidCheckoutDataException("員工 ID 不能為空");
        }
        if (details.isEmpty()) {
            throw new InvalidCheckoutDataException("結帳明細不能為空");
        }
        // 可以添加更多的驗證邏輯...
    }

    // 自定義異常類
    private static class InvalidCheckoutDataException extends Exception {
        public InvalidCheckoutDataException(String message) {
            super(message);
        }
    }

}