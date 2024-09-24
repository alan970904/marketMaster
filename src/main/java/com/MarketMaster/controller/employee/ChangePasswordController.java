package com.MarketMaster.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MarketMaster.service.employee.EmpService;
import com.MarketMaster.viewModel.EmployeeViewModel;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChangePasswordController {

    @Autowired
    private EmpService empService;

    @GetMapping("/changePassword")
    public String showChangePasswordPage() {
        return "employee/ChangePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String newPassword, HttpSession session, Model model) {
        EmployeeViewModel employee = (EmployeeViewModel) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/login";
        }

        try {
            boolean success = empService.updatePassword(employee.getEmployeeId(), newPassword);
            if (success) {
                EmployeeViewModel updatedEmployee = empService.getEmployeeViewModel(employee.getEmployeeId());
                session.setAttribute("employee", updatedEmployee);
                return "redirect:/homePage";
            } else {
                model.addAttribute("errorMessage", "修改密碼失敗");
                return "employee/ChangePassword";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "修改密碼失敗: " + e.getMessage());
            return "employee/ChangePassword";
        }
    }
}