package com.MarketMaster.controller.employee;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.MarketMaster.bean.employee.CustomerBean;
import com.MarketMaster.service.employee.CustomerService;
import com.MarketMaster.exception.DataAccessException;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
    private CustomerService customerService;
    
	@GetMapping("/cusMain")
	public String showCustomerMain() {
		return "employee/CustomerMain";
	}
	
    @GetMapping("/add")
    public String showAddCustomerForm() {
        return "employee/AddCustomer";
    }
	
    @PostMapping("/add")
    private String addCustomer(@RequestParam String customerTel, @RequestParam String customerName,
    						@RequestParam String customerEmail, Model model) {
    	try {
	        CustomerBean customer = new CustomerBean();
	        customer.setCustomerTel(customerTel);
	        customer.setCustomerName(customerName);
	        customer.setCustomerEmail(customerEmail);
	        customer.setDateOfRegistration(LocalDate.now());
	        customer.setTotalPoints(0);

            boolean success = customerService.addCustomer(customer);
            if (success) {
                model.addAttribute("message", "會員新增成功");
            } else {
            	model.addAttribute("message", "會員新增失敗");
            }
        } catch (DataAccessException e) {
        	model.addAttribute("message", "新增失敗：" + e.getMessage());
        }
    	return "employee/ResultCustomer";
    }

    @GetMapping("/get")
    private String getCustomer(@RequestParam String customerTel, Model model) {
        try {
            CustomerBean customer = customerService.getCustomer(customerTel);
            if (customer != null) {
            	model.addAttribute("customer", customer);
            	return "employee/CustomerDetails";
            } else {
            	model.addAttribute("message", "找不到此會員");
            	return "employee/ResultCustomer";
            }
        } catch (DataAccessException e) {
        	model.addAttribute("message", "獲取會員詳情失敗：" + e.getMessage());
        	return "employee/ResultCustomer";
        }
    }

    @GetMapping("/getAll")
    private String getAllCustomers(Model model) {
        try {
            List<CustomerBean> customers = customerService.getAllCustomers();
            model.addAttribute("customers", customers);
            return "employee/CustomerList";
        } catch (DataAccessException e) {
        	model.addAttribute("message", "獲取會員列表失敗：" + e.getMessage());
        	return "employee/ResultCustomer";
        }
    }

    @PostMapping("/update")
    private String updateCustomer(@RequestParam String customerTel, @RequestParam String customerName,
								@RequestParam String customerEmail, @RequestParam String originalTel, Model model) {
    	try {
            CustomerBean customer = customerService.getCustomer(originalTel);
            if (customer != null) {
            	customer.setCustomerTel(customerTel);
            	customer.setCustomerName(customerName);
            	customer.setCustomerEmail(customerEmail);
                
                boolean success = customerService.updateCustomer(customer, originalTel);
                if (success) {
                    model.addAttribute("message", "會員更新成功");
                } else {
                	model.addAttribute("message", "會員更新失敗");
                }
            } else {
            	model.addAttribute("message", "會員更新失敗：找不到指定會員");
            }
        } catch (DataAccessException e) {
        	model.addAttribute("message", "更新失敗：" + e.getMessage());
        }
        return "employee/ResultCustomer";
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteCustomer(@RequestParam String customerTel) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = customerService.deleteCustomer(customerTel);
            if (success) {
                response.put("status", "success");
                response.put("message", "會員刪除成功");
            } else {
                response.put("status", "error");
                response.put("message", "會員刪除失敗");
            }
        } catch (DataAccessException e) {
            response.put("status", "error");
            response.put("message", "刪除失敗：" + e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/getForUpdate")
    private String getCustomerForUpdate(@RequestParam String customerTel, Model model) {
    	try {
            CustomerBean customer = customerService.getCustomer(customerTel);
            if (customer != null) {
                model.addAttribute("customer", customer);
                return "employee/UpdateCustomer";
            } else {
            	model.addAttribute("message", "找不到此會員");
            	return "employee/ResultCustomer";
            }
        } catch (DataAccessException e) {
        	model.addAttribute("message", "獲取會員詳情失敗：" + e.getMessage());
        	return "employee/ResultCustomer";
        }
    }
}