package com.MarketMaster.controller.employee;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.employee.RankLevelBean;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.service.employee.EmpService;
import com.MarketMaster.viewModel.EmployeeViewModel;

@Controller
@RequestMapping("/employee")
public class EmpController {

    @Autowired
    private EmpService empService;
    
    @GetMapping("/empMain")
    public String showEmployeeMain() {
        return "employee/EmployeeMain";
    }

    @GetMapping("/list")
    public String getAllEmployees(@RequestParam(defaultValue = "false") boolean showAll, Model model) {
        List<EmpBean> employees = empService.getAllEmployees(showAll);
        List<RankLevelBean> ranks = empService.getRankList();
        model.addAttribute("employees", employees);
        model.addAttribute("ranks", ranks);
        model.addAttribute("showAll", showAll);
        return "employee/EmployeeList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        String newEmployeeId = empService.generateNewEmployeeId();
        model.addAttribute("newEmployeeId", newEmployeeId);
        model.addAttribute("emp", new EmpBean());
        return "employee/AddEmployee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("emp") EmpBean emp, Model model) {
        emp.setHiredate(LocalDate.now());
        emp.setPassword("0000");
        emp.setFirstLogin(true);
        boolean success = empService.addEmployee(emp);
        if (success) {
            model.addAttribute("message", "員工新增成功，新員工編號為: " + emp.getEmployeeId());
        } else {
            model.addAttribute("message", "新增失敗");
        }
        return "employee/ResultEmp";
    }

    @GetMapping("/update")
    public String getEmployeeForUpdate(@RequestParam String employeeId, Model model) {
        try {
            EmployeeViewModel empViewModel = empService.getEmployeeViewModel(employeeId);
            model.addAttribute("employeeToUpdate", empViewModel);
            return "employee/UpdateEmployee";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "獲取員工資料時出錯：" + e.getMessage());
            return "employee/ErrorPage";
        }
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("emp") EmpBean emp, Model model) {
        try {
            boolean success = empService.updateEmployee(emp);
            if (success) {
                model.addAttribute("message", "員工更新成功");
            } else {
                model.addAttribute("message", "更新失敗，員工不存在");
            }
        } catch (DataAccessException e) {
            model.addAttribute("message", "更新失敗：" + e.getMessage());
        }
        return "employee/ResultEmp";
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam String employeeId, Model model) {
        boolean success = empService.deleteEmployee(employeeId);
        if (success) {
            model.addAttribute("message", "員工刪除成功");
        } else {
            model.addAttribute("message", "刪除失敗，員工不存在");
        }
        return "redirect:/employee/list";
    }

    @GetMapping("/details")
    public String getEmployee(@RequestParam String employeeId, Model model) {
        EmployeeViewModel employeeViewModel = empService.getEmployeeViewModel(employeeId);
        model.addAttribute("viewEmployee", employeeViewModel);  // 使用新的屬性名稱
        return "employee/EmployeeDetails";
    }

    @GetMapping("/search")
    public String searchEmployees(@RequestParam String searchName, @RequestParam(defaultValue = "false") boolean showAll, Model model) {
        List<EmpBean> employees = empService.searchEmployees(searchName, showAll);
        model.addAttribute("employees", employees);
        model.addAttribute("searchName", searchName);
        model.addAttribute("employeeCount", employees.size());
        model.addAttribute("showAll", showAll);
        model.addAttribute("isSearchResult", true);
        return "employee/EmployeeList";
    }
}