package com.MarketMaster.controller.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.service.checkout.CheckoutService;
import com.MarketMaster.exception.DataAccessException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    // 顯示結帳主頁面
    @GetMapping("/checkoutMain")
    public String showCheckoutMain() {
        return "checkout/checkout/index";
    }

    // 獲取所有結帳記錄
    @GetMapping("/list")
    public String getAllCheckouts(Model model) {
        List<CheckoutBean> checkouts = checkoutService.getAllCheckouts();
        model.addAttribute("checkouts", checkouts);
        return "checkout/checkout/GetAllCheckouts";
    }

    // 獲取單個結帳記錄
    @GetMapping("/details")
    public String getCheckout(@RequestParam String checkoutId, Model model) {
        CheckoutBean checkout = checkoutService.getCheckout(checkoutId);
        model.addAttribute("checkout", checkout);
        return "checkout/checkout/GetCheckout";
    }

    // 顯示新增結帳表單
    @GetMapping("/add")
    public String showAddForm(Model model) {
        String nextId = checkoutService.generateNextCheckoutId();
        model.addAttribute("nextId", nextId);
        model.addAttribute("checkout", new CheckoutBean());
        return "checkout/checkout/InsertCheckout";
    }

    // 處理新增結帳請求
    @PostMapping("/add")
    public String addCheckout(@ModelAttribute CheckoutBean checkout, @RequestParam List<CheckoutDetailsBean> details, Model model) {
        try {
            boolean success = checkoutService.insertCheckoutWithDetails(checkout, details);
            if (success) {
                model.addAttribute("message", "結帳記錄新增成功");
            } else {
                model.addAttribute("message", "結帳記錄新增失敗");
            }
            return "checkout/Result";
        } catch (DataAccessException e) {
            model.addAttribute("error", "新增結帳記錄時發生錯誤：" + e.getMessage());
            return "checkout/checkout/error";
        }
    }

    // 顯示更新結帳表單
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String checkoutId, Model model) {
        CheckoutBean checkout = checkoutService.getCheckout(checkoutId);
        model.addAttribute("checkout", checkout);
        return "checkout/checkout/GetUpdateCheckout";
    }

    // 處理更新結帳請求
    @PostMapping("/update")
    @ResponseBody
    public String updateCheckout(@ModelAttribute CheckoutBean checkout) {
        try {
            boolean success = checkoutService.updateCheckout(checkout);
            if (success) {
                return "{\"status\":\"success\",\"message\":\"更新成功\"}";
            } else {
                return "{\"status\":\"error\",\"message\":\"更新失敗\"}";
            }
        } catch (DataAccessException e) {
            return "{\"status\":\"error\",\"message\":\"更新時發生錯誤: " + e.getMessage() + "\"}";
        }
    }

    // 刪除結帳記錄
    @PostMapping("/delete")
    @ResponseBody
    public String deleteCheckout(@RequestParam String checkoutId) {
        try {
            checkoutService.deleteCheckoutAndDetails(checkoutId);
            return "{\"status\":\"success\",\"message\":\"結帳記錄及其相關明細已成功刪除\"}";
        } catch (DataAccessException e) {
            return "{\"status\":\"error\",\"message\":\"刪除結帳記錄及其相關明細失敗: " + e.getMessage() + "\"}";
        }
    }

    // 根據電話號碼搜索結帳記錄
    @GetMapping("/search")
    public String searchCheckoutsByTel(@RequestParam String customerTel, Model model) {
        List<CheckoutBean> checkouts = checkoutService.searchCheckoutsByTel(customerTel);
        model.addAttribute("checkouts", checkouts);
        return "checkout/checkout/SearchResults";
    }

    // 獲取每日銷售報告
    @GetMapping("/dailySalesReport")
    public String getDailySalesReport(Model model) {
        List<Map<String, Object>> report = checkoutService.getDailySalesReport();
        model.addAttribute("dailySalesReport", report);
        return "checkout/checkout/DailySalesReport";
    }

    // 獲取結帳總表
    @GetMapping("/summary")
    public String getCheckoutSummary(Model model) {
        List<Map<String, Object>> summary = checkoutService.getCheckoutSummary();
        model.addAttribute("checkoutSummary", summary);
        return "checkout/checkout/CheckoutSummary";
    }

    // 獲取下一個結帳ID
    @GetMapping("/nextId")
    @ResponseBody
    public String getNextCheckoutId() {
        return checkoutService.generateNextCheckoutId();
    }

    // 獲取所有員工
    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        List<EmpBean> employees = checkoutService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/EmployeeList";
    }

    // 根據類別獲取所有產品名稱 & ID & 價錢
    @GetMapping("/products")
    @ResponseBody
    public List<ProductBean> getProductNames(@RequestParam String category) {
        try {
            return checkoutService.getProductNamesByCategory(category);
        } catch (DataAccessException e) {
            // 在實際應用中，你可能想要更好地處理這個異常
            return List.of();
        }
    }

    // 更新總金額和紅利點數
    @PostMapping("/updateTotalAndBonus")
    @ResponseBody
    public String updateTotalAndBonus(@RequestParam String checkoutId, @RequestParam BigDecimal totalAmount, @RequestParam int bonusPoints) {
        try {
            checkoutService.updateTotalAndBonus(checkoutId, totalAmount, bonusPoints);
            return "{\"status\":\"success\",\"message\":\"總金額和紅利點數已更新\"}";
        } catch (DataAccessException e) {
            return "{\"status\":\"error\",\"message\":\"更新失敗: " + e.getMessage() + "\"}";
        }
    }
}