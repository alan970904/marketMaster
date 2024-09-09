package com.MarketMaster.controller.employee;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.MarketMaster.bean.employee.CustomerBean;
import com.MarketMaster.service.employee.CustomerService;
import com.MarketMaster.exception.DataAccessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerService customerService = new CustomerService();
    private Gson gson = new Gson();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addCustomer(request, response);
                    break;
                case "get":
                    getCustomer(request, response);
                    break;
                case "getAll":
                    getAllCustomers(request, response);
                    break;
                case "update":
                    updateCustomer(request, response);
                    break;
                case "delete":
                    deleteCustomer(request, response);
                    break;
                case "getForUpdate":
                    getCustomerForUpdate(request, response);
                    break;
                default:
                    response.sendRedirect("employee/CustomerMain.jsp");
            }
        } catch (DataAccessException e) {
            request.setAttribute("errorMessage", "發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
        }
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerBean customer = new CustomerBean();
        customer.setCustomerTel(request.getParameter("customerTel"));
        customer.setCustomerName(request.getParameter("customerName"));
        customer.setCustomerEmail(request.getParameter("customerEmail"));
        customer.setDateOfRegistration(LocalDate.now());
        customer.setTotalPoints(0);

        try {
            boolean success = customerService.addCustomer(customer);
            if (success) {
                request.setAttribute("message", "會員新增成功");
            } else {
                request.setAttribute("message", "會員新增失敗");
            }
        } catch (DataAccessException e) {
            request.setAttribute("message", "新增失敗：" + e.getMessage());
        }
        request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
    }

    private void getCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerTel = request.getParameter("customerTel");
        try {
            CustomerBean customer = customerService.getCustomer(customerTel);
            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("employee/CustomerDetails.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "找不到此會員");
                request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
            }
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取會員詳情失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
        }
    }

    private void getAllCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<CustomerBean> customers = customerService.getAllCustomers();
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("employee/CustomerList.jsp").forward(request, response);
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取會員列表失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
        }
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String originalTel = request.getParameter("originalTel");
        String newTel = request.getParameter("customerTel");
        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");

        try {
            CustomerBean originalCustomer = customerService.getCustomer(originalTel);
            if (originalCustomer != null) {
                CustomerBean updatedCustomer = new CustomerBean();
                updatedCustomer.setCustomerTel(newTel);
                updatedCustomer.setCustomerName(customerName);
                updatedCustomer.setCustomerEmail(customerEmail);
                // 保留原始的註冊日期和積分
                updatedCustomer.setDateOfRegistration(originalCustomer.getDateOfRegistration());
                updatedCustomer.setTotalPoints(originalCustomer.getTotalPoints());
                
                boolean success = customerService.updateCustomer(updatedCustomer, originalTel);
                if (success) {
                    request.setAttribute("message", "會員更新成功");
                } else {
                    request.setAttribute("message", "會員更新失敗");
                }
            } else {
                request.setAttribute("message", "會員更新失敗：找不到指定會員");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            request.setAttribute("message", "更新失敗：" + e.getMessage());
        }
        request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerTel = request.getParameter("customerTel");
        JsonObject jsonResponse = new JsonObject();

        try {
            boolean success = customerService.deleteCustomer(customerTel);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "會員刪除成功");
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "會員刪除失敗");
            }
        } catch (DataAccessException e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "刪除失敗：" + e.getMessage());
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(jsonResponse));
    }
    
    private void getCustomerForUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerTel = request.getParameter("customerTel");
        try {
            CustomerBean customer = customerService.getCustomer(customerTel);
            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("employee/UpdateCustomer.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "找不到此會員");
                request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
            }
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取會員詳情失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultCustomer.jsp").forward(request, response);
        }
    }
}