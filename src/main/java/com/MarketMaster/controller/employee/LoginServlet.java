package com.MarketMaster.controller.employee;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.service.employee.EmpService;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.viewModel.EmployeeViewModel;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmpService empService = new EmpService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeId = request.getParameter("employeeId");
        String password = request.getParameter("password");

        try {
            EmpBean employee = empService.login(employeeId, password);
            if (employee != null) {
                try {
                    EmployeeViewModel employeeViewModel = empService.getEmployeeViewModel(employeeId);
                    HttpSession session = request.getSession();
                    session.setAttribute("employee", employeeViewModel);

                    if (employee.isFirstLogin()) {
                        response.sendRedirect(request.getContextPath() + "/employee/ChangePassword.jsp");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/body/HomePage.jsp");
                    }
                } catch (DataAccessException e) {
                    System.out.println("Error getting employee view model: " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "系統錯誤: " + e.getMessage());
                    request.getRequestDispatcher("/employee/Login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "員工編號或密碼錯誤");
                request.getRequestDispatcher("/employee/Login.jsp").forward(request, response);
            }
        } catch (DataAccessException e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "系統錯誤: " + e.getMessage());
            request.getRequestDispatcher("/employee/Login.jsp").forward(request, response);
        }
    }
}