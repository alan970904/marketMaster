package com.MarketMaster.controller.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.service.checkout.CheckoutDetailsService;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout/checkoutDetails")
public class CheckoutDetailsController {

    @Autowired
    private CheckoutDetailsService checkoutDetailsService;

    @GetMapping("/list")
    public String getAllCheckoutDetails(Model model) {
        model.addAttribute("checkoutDetails", checkoutDetailsService.getAllCheckoutDetails());
        return "checkout/checkoutDetails/GetAllCheckoutDetails";
    }

    @GetMapping("/listByCheckoutId")
    public String getPartCheckoutDetails(@RequestParam String checkoutId, Model model) {
        model.addAttribute("checkoutDetailsList", checkoutDetailsService.getPartCheckoutDetails(checkoutId));
        return "checkout/checkoutDetails/GetPartCheckoutDetails";
    }

    @GetMapping("/details")
    public String getCheckoutDetails(@RequestParam String checkoutId, @RequestParam String productId, Model model) {
        model.addAttribute("checkoutDetails", checkoutDetailsService.getCheckoutDetails(checkoutId, productId));
        return "checkout/checkoutDetails/GetCheckoutDetails";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("checkoutDetails", new CheckoutDetailsBean());
        return "checkout/checkoutDetails/AddCheckoutDetails";
    }

    @PostMapping("/add")
    public String addCheckoutDetails(@ModelAttribute CheckoutDetailsBean checkoutDetails, Model model) {
        try {
            checkoutDetailsService.addCheckoutDetails(checkoutDetails);
            model.addAttribute("message", "結帳明細新增成功");
            return "checkout/checkoutDetails/Result";
        } catch (DataAccessException e) {
            model.addAttribute("error", "新增結帳明細時發生錯誤：" + e.getMessage());
            return "checkout/checkoutDetails/Error";
        }
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String checkoutId, @RequestParam String productId, Model model) {
        model.addAttribute("checkoutDetails", checkoutDetailsService.getCheckoutDetails(checkoutId, productId));
        return "checkout/checkoutDetails/UpdateCheckoutDetails";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateCheckoutDetails(@ModelAttribute CheckoutDetailsBean checkoutDetails) {
        try {
            checkoutDetailsService.updateCheckoutDetails(checkoutDetails);
            return ResponseEntity.ok(Map.of("status", "success", "message", "更新成功"));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteCheckoutDetails(@RequestParam String checkoutId, @RequestParam String productId) {
        try {
            checkoutDetailsService.deleteCheckoutDetails(checkoutId, productId);
            return ResponseEntity.ok(Map.of("status", "success", "message", "結帳明細已成功刪除"));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "刪除結帳明細失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public String searchByProductId(@RequestParam String productId, Model model) {
        model.addAttribute("checkoutDetails", checkoutDetailsService.searchCheckoutDetailsByProductId(productId));
        return "checkout/checkoutDetails/SearchResults";
    }

    @GetMapping("/returnRates")
    public String getProductReturnRates(Model model) {
        model.addAttribute("returnRates", checkoutDetailsService.getProductReturnRates());
        return "checkout/checkoutDetails/ProductReturnRates";
    }
}