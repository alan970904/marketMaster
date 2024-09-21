package com.MarketMaster.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.service.employee.EmpService;
import com.MarketMaster.viewModel.EmployeeViewModel;

import jakarta.servlet.http.HttpSession;

//控制登入、登出及首頁的連結
@Controller
public class LoginController {

    @Autowired
    private EmpService empService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "employee/Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String employeeId, @RequestParam String password, 
                        HttpSession session, Model model) {
        try {
            EmpBean employee = empService.login(employeeId, password);
            if (employee != null) {
                EmployeeViewModel employeeViewModel = empService.getEmployeeViewModel(employeeId);
                session.setAttribute("employee", employeeViewModel);

                if (employee.isFirstLogin()) {
                    return "redirect:/changePassword";
                } else {
                    return "redirect:/homePage";
                }
            } else {
                model.addAttribute("errorMessage", "員工編號或密碼錯誤");
                return "employee/Login";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "系統錯誤: " + e.getMessage());
            return "employee/Login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/homePage")
    public String homePage() {
        // 返回主頁視圖或內容
        return "body/HomePage";  // 返回頁面的名稱，若用視圖解析器，需配置相應的頁面
    }
}