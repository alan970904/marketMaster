package com.MarketMaster.controller.employee;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.MarketMaster.viewModel.EmployeeViewModel;
import com.MarketMaster.service.employee.EmpService;
import com.MarketMaster.exception.DataAccessException;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmpService empService = new EmpService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeViewModel employee = (EmployeeViewModel) session.getAttribute("employee");

        if (employee == null) {
            response.sendRedirect(request.getContextPath() + "/employee/Login.jsp");
            return;
        }

        String newPassword = request.getParameter("newPassword");

        try {
            boolean success = empService.updatePassword(employee.getEmployeeId(), newPassword);
            if (success) {
                // 更新 session 中的員工資訊
                EmployeeViewModel updatedEmployee = empService.getEmployeeViewModel(employee.getEmployeeId());
                session.setAttribute("employee", updatedEmployee);
                response.sendRedirect(request.getContextPath() + "/body/HomePage.jsp");
            } else {
                request.setAttribute("errorMessage", "修改密碼失敗");
                request.getRequestDispatcher("/employee/ChangePassword.jsp").forward(request, response);
            }
        } catch (DataAccessException e) {
            request.setAttribute("errorMessage", "修改密碼失敗: " + e.getMessage());
            request.getRequestDispatcher("/employee/ChangePassword.jsp").forward(request, response);
        }
    }
}