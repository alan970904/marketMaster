package com.MarketMaster.controller.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.service.checkout.CheckoutDetailsService;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkoutDetails")
public class CheckoutDetailsController {

    @Autowired
    private CheckoutDetailsService checkoutDetailsService;

    // 獲取所有結帳明細
    @GetMapping("/list")
    public String getAllCheckoutDetails(Model model) {
        List<CheckoutDetailsBean> checkoutDetails = checkoutDetailsService.getAllCheckoutDetails();
        model.addAttribute("checkoutDetails", checkoutDetails);
        return "checkoutDetails/GetAllCheckoutDetails";
    }

    // 獲取特定結帳ID的所有明細
    @GetMapping("/listByCheckoutId")
    public String getPartCheckoutDetails(@RequestParam String checkoutId, Model model) {
        List<CheckoutDetailsBean> checkoutDetailsList = checkoutDetailsService.getPartCheckoutDetails(checkoutId);
        model.addAttribute("checkoutDetailsList", checkoutDetailsList);
        return "checkoutDetails/GetPartCheckoutDetails";
    }

    // 獲取單個結帳明細
    @GetMapping("/details")
    public String getCheckoutDetails(@RequestParam String checkoutId, @RequestParam String productId, Model model) {
        CheckoutDetailsBean checkoutDetails = checkoutDetailsService.getCheckoutDetails(checkoutId, productId);
        model.addAttribute("checkoutDetails", checkoutDetails);
        return "checkoutDetails/GetCheckoutDetails";
    }

    // 顯示新增結帳明細表單
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("checkoutDetails", new CheckoutDetailsBean());
        return "checkoutDetails/AddCheckoutDetails";
    }

    // 處理新增結帳明細請求
    @PostMapping("/add")
    public String addCheckoutDetails(@ModelAttribute CheckoutDetailsBean checkoutDetails, Model model) {
        try {
            checkoutDetailsService.addCheckoutDetails(checkoutDetails);
            model.addAttribute("message", "結帳明細新增成功");
            return "checkoutDetails/Result";
        } catch (DataAccessException e) {
            model.addAttribute("error", "新增結帳明細時發生錯誤：" + e.getMessage());
            return "checkoutDetails/Error";
        }
    }

    // 顯示更新結帳明細表單
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String checkoutId, @RequestParam String productId, Model model) {
        CheckoutDetailsBean checkoutDetails = checkoutDetailsService.getCheckoutDetails(checkoutId, productId);
        model.addAttribute("checkoutDetails", checkoutDetails);
        return "checkoutDetails/UpdateCheckoutDetails";
    }

    // 處理更新結帳明細請求
    @PostMapping("/update")
    @ResponseBody
    public String updateCheckoutDetails(@ModelAttribute CheckoutDetailsBean checkoutDetails) {
        try {
            checkoutDetailsService.updateCheckoutDetails(checkoutDetails);
            return "{\"status\":\"success\",\"message\":\"更新成功\"}";
        } catch (DataAccessException e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    // 刪除結帳明細
    @PostMapping("/delete")
    @ResponseBody
    public String deleteCheckoutDetails(@RequestParam String checkoutId, @RequestParam String productId) {
        try {
            checkoutDetailsService.deleteCheckoutDetails(checkoutId, productId);
            return "{\"status\":\"success\",\"message\":\"結帳明細已成功刪除\"}";
        } catch (DataAccessException e) {
            return "{\"status\":\"error\",\"message\":\"刪除結帳明細失敗: " + e.getMessage() + "\"}";
        }
    }

    // 根據產品ID搜索結帳明細
    @GetMapping("/search")
    public String searchByProductId(@RequestParam String productId, Model model) {
        List<CheckoutDetailsBean> checkoutDetails = checkoutDetailsService.searchCheckoutDetailsByProductId(productId);
        model.addAttribute("checkoutDetails", checkoutDetails);
        return "checkoutDetails/SearchResults";
    }

    // 獲取產品退貨率
    @GetMapping("/returnRates")
    public String getProductReturnRates(Model model) {
        List<Map<String, Object>> returnRates = checkoutDetailsService.getProductReturnRates();
        model.addAttribute("returnRates", returnRates);
        return "checkoutDetails/ProductReturnRates";
    }
}