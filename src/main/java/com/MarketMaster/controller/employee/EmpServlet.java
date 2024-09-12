package com.MarketMaster.controller.employee;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.employee.RankLevelBean;
import com.MarketMaster.service.employee.EmpService;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.viewModel.EmployeeViewModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//使用 WebServlet 註解來配置 Servlet 的 URL 映射
@WebServlet("/EmpServlet")
public class EmpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 創建 EmpService 實例以處理業務邏輯
    private EmpService empService = new EmpService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    // 統一處理請求的方法
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
        	// 根據 action 參數決定執行哪個操作
            switch (action) {
                case "add":
                    addEmployee(request, response);
                    break;
                case "showAddForm":
                    showAddForm(request, response);
                    break;
                case "getForUpdate":
                    getEmployeeForUpdate(request, response);
                    break;    
                case "update":
                    updateEmployee(request, response);
                    break;
                case "delete":
                    deleteEmployee(request, response);
                    break;
                case "get":
                    getEmployee(request, response);
                    break;
                case "getAll":
                    getAllEmployees(request, response);
                    break;
                case "search":
                    searchEmployees(request, response);
                    break;    
                default:
                	// 如果 action 不匹配任何操作，重定向到索引頁面
                    response.sendRedirect("html/Index.html");
            }
        } catch (DataAccessException e) {
            request.setAttribute("message", "發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            EmpBean emp = new EmpBean();
            emp.setEmployeeId(request.getParameter("employeeId"));
            emp.setEmployeeName(request.getParameter("employeeName"));
            emp.setEmployeeTel(request.getParameter("employeeTel"));
            emp.setEmployeeIdcard(request.getParameter("employeeIdcard"));
            emp.setEmployeeEmail(request.getParameter("employeeEmail"));
            emp.setPositionId(request.getParameter("positionId"));
            emp.setHiredate(LocalDate.parse(request.getParameter("hiredate")));
            emp.setPassword(request.getParameter("password")); // 設置默認密碼
            emp.setFirstLogin(true); // 設置為首次登入
            // 調用服務層方法添加員工
            boolean success = empService.addEmployee(emp);
            if (success) {
                request.setAttribute("message", "員工新增成功，新員工編號為: " + emp.getEmployeeId());
            } else {
                request.setAttribute("message", "新增失敗");
            }
        } catch (DataAccessException e) {
            request.setAttribute("message", "新增失敗：" + e.getMessage());
        }
        request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
    }
    
    // 顯示添加員工表單的方法
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	// 生成新的員工 ID
            String newEmployeeId = empService.generateNewEmployeeId();
            request.setAttribute("newEmployeeId", newEmployeeId);
            request.getRequestDispatcher("employee/AddEmployee.jsp").forward(request, response);
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取新員工編號失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }

    private void getEmployeeForUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String employeeId = request.getParameter("employeeId");
        try {
            EmployeeViewModel empViewModel = empService.getEmployeeViewModel(employeeId);
            request.setAttribute("employeeToUpdate", empViewModel);
            request.getRequestDispatcher("employee/UpdateEmployee.jsp").forward(request, response);
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取員工訊息失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        EmployeeViewModel currentLoggedInEmployee = (EmployeeViewModel) session.getAttribute("employee");
        
        try {
            String employeeId = request.getParameter("employeeId");
            EmpBean emp = empService.getEmployee(employeeId);
            if (emp != null) {
                emp.setEmployeeName(request.getParameter("employeeName"));
                emp.setEmployeeTel(request.getParameter("employeeTel"));
                emp.setEmployeeIdcard(request.getParameter("employeeIdcard"));
                emp.setEmployeeEmail(request.getParameter("employeeEmail"));
                emp.setPassword(request.getParameter("password"));
                emp.setPositionId(request.getParameter("positionId"));
                emp.setHiredate(LocalDate.parse(request.getParameter("hiredate")));
                String resigndate = request.getParameter("resigndate");
                emp.setResigndate(resigndate != null && !resigndate.isEmpty() ? LocalDate.parse(resigndate) : null);
                
                boolean success = empService.updateEmployee(emp);
                if (success) {
                    request.setAttribute("message", "員工更新成功");
                    
                    // 如果更新的是當前登入的員工，更新session中的資訊
                    if (currentLoggedInEmployee != null && currentLoggedInEmployee.getEmployeeId().equals(employeeId)) {
                        EmployeeViewModel updatedEmployee = empService.getEmployeeViewModel(employeeId);
                        session.setAttribute("employee", updatedEmployee);
                    }
                } else {
                    request.setAttribute("message", "更新失敗，員工不存在");
                }
            } else {
                request.setAttribute("message", "找不到員工");
            }
        } catch (DataAccessException e) {
            request.setAttribute("message", "更新失敗：" + e.getMessage());
        }
        
        // 使用請求轉發而不是重定向
        request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String employeeId = request.getParameter("employeeId");
        try {
            EmpBean employee = empService.getEmployee(employeeId);
            
            if (employee != null && employee.getResigndate() == null) {
                boolean success = empService.deleteEmployee(employeeId);
                if (success) {
                    // 如果是AJAX請求，返回JSON響應
                    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"status\":\"success\",\"message\":\"員工刪除成功\"}");
                    } else {
                        request.setAttribute("message", "員工刪除成功");
                        // 重新獲取員工列表和職級列表
                        boolean showAll = "true".equals(request.getParameter("showAll"));
                        List<EmpBean> employees = empService.getAllEmployees(showAll);
                        List<RankLevelBean> ranks = empService.getRankList();
                        request.setAttribute("employees", employees);
                        request.setAttribute("ranks", ranks);
                        request.getRequestDispatcher("employee/EmployeeList.jsp").forward(request, response);
                    }
                } else {
                    handleDeleteError(request, response, "刪除失敗，可能是系統錯誤");
                }
            } else if (employee != null) {
                handleDeleteError(request, response, "無法刪除已離職的員工");
            } else {
                handleDeleteError(request, response, "員工不存在");
            }
        } catch (DataAccessException e) {
            handleDeleteError(request, response, "刪除失敗：" + e.getMessage());
        }
    }
    
    private void handleDeleteError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + errorMessage + "\"}");
        } else {
            request.setAttribute("message", errorMessage);
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }
    
    private void getEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String employeeId = request.getParameter("employeeId");
        try {
            EmployeeViewModel employeeViewModel = empService.getEmployeeViewModel(employeeId);
            request.setAttribute("employee", employeeViewModel);
            request.getRequestDispatcher("employee/EmployeeDetails.jsp").forward(request, response);
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取員工詳情失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }

    private void getAllEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            boolean showAll = "true".equals(request.getParameter("showAll"));
            List<EmpBean> employees = empService.getAllEmployees(showAll);
            List<RankLevelBean> ranks = empService.getRankList();
            request.setAttribute("employees", employees);
            request.setAttribute("ranks", ranks);
            request.setAttribute("showAll", showAll);
            request.getRequestDispatcher("employee/EmployeeList.jsp").forward(request, response);
        } catch (DataAccessException e) {
            request.setAttribute("message", "獲取員工列表失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }

    private void searchEmployees(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String searchName = request.getParameter("searchName");
        boolean showAll = "true".equals(request.getParameter("showAll"));
        try {
            List<EmpBean> employees = empService.searchEmployees(searchName, showAll);
            request.setAttribute("employees", employees);
            request.setAttribute("searchName", searchName);
            request.setAttribute("employeeCount", employees.size());
            request.setAttribute("showAll", showAll);
            request.setAttribute("isSearchResult", true);
            request.getRequestDispatcher("employee/EmployeeList.jsp").forward(request, response);
        } catch (DataAccessException e) {
            request.setAttribute("message", "查詢員工失敗：" + e.getMessage());
            request.getRequestDispatcher("employee/ResultEmp.jsp").forward(request, response);
        }
    }
}