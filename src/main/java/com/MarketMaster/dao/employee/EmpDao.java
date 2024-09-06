package com.MarketMaster.dao.employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.employee.RankLevelBean;
import com.MarketMaster.exception.DataAccessException;
import com.MarketMaster.viewModel.EmployeeViewModel;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

//EmpDao 類：負責處理與員工相關的數據庫操作
@WebServlet("/EmpDao")
public class EmpDao extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 數據源的 JNDI 名稱，用於獲取數據庫連接
    private static final String DATASOURCE_JNDI = "java:/comp/env/jdbc/ispan";

    // 獲取數據庫連接的私有方法
    private Connection getConnection() throws DataAccessException {
        try {
        	// 使用 JNDI 查找數據源
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup(DATASOURCE_JNDI);
            return ds.getConnection();
        } catch (NamingException | SQLException e) {
        	// 如果獲取連接失敗，拋出自定義異常
            throw new DataAccessException("無法獲取數據庫連接", e);
        }
    }

    // 驗證員工登入
    public EmpBean validateEmployee(String employeeId, String password) throws DataAccessException {
        String sql = "SELECT * FROM employee WHERE employee_id = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    EmpBean emp = extractEmployeeFromResultSet(rs);
                    emp.setFirstLogin(rs.getBoolean("is_first_login"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("驗證員工失敗", e);
        }
        return null;
    }

    // 檢查是否為首次登入
    public boolean isFirstLogin(String employeeId) throws DataAccessException {
        String sql = "SELECT is_first_login FROM employee WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_first_login");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("檢查首次登入狀態失敗", e);
        }
        return false;
    }

    // 更新密碼
    public boolean updatePassword(String employeeId, String newPassword) throws DataAccessException {
        String sql = "UPDATE employee SET password = ?, is_first_login = 0 WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, employeeId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("更新密碼失敗", e);
        }
    }

    public boolean addEmployee(EmpBean emp) throws DataAccessException {
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emp.getEmployeeId());
            pstmt.setString(2, emp.getEmployeeName());
            pstmt.setString(3, emp.getEmployeeTel());
            pstmt.setString(4, emp.getEmployeeIdcard());
            pstmt.setString(5, emp.getEmployeeEmail());
            pstmt.setString(6, emp.getPassword());
            pstmt.setString(7, emp.getPositionId());
            pstmt.setDate(8, Date.valueOf(emp.getHiredate()));
            pstmt.setDate(9, emp.getResigndate() != null ? Date.valueOf(emp.getResigndate()) : null);
            pstmt.setBoolean(10, emp.isFirstLogin());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("添加員工失敗", e);
        }
    }

    public boolean deleteEmployee(String employeeId) throws DataAccessException {
    	String sql = "DELETE FROM employee WHERE employee_id=?";
    	try (Connection conn = getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, employeeId);

    		int affectedRows = pstmt.executeUpdate();
    		return affectedRows > 0;
    	} catch (SQLException e) {
    		throw new DataAccessException("刪除員工失敗", e);
    	}
    }

    public boolean updateEmployee(EmpBean emp) throws DataAccessException {
        String sql = "UPDATE employee SET employee_name=?, employee_tel=?, employee_idcard=?, employee_email=?, password=?, position_id=?, hiredate=?, resigndate=?, is_first_login=? WHERE employee_id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emp.getEmployeeName());
            pstmt.setString(2, emp.getEmployeeTel());
            pstmt.setString(3, emp.getEmployeeIdcard());
            pstmt.setString(4, emp.getEmployeeEmail());
            pstmt.setString(5, emp.getPassword());
            pstmt.setString(6, emp.getPositionId());
            pstmt.setDate(7, Date.valueOf(emp.getHiredate()));
            pstmt.setDate(8, emp.getResigndate() != null ? Date.valueOf(emp.getResigndate()) : null);
            pstmt.setBoolean(9, emp.isFirstLogin());
            pstmt.setString(10, emp.getEmployeeId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("更新員工失敗", e);
        }
    }

    public EmpBean getEmployee(String employeeId) throws DataAccessException {
        String sql = "SELECT * FROM employee WHERE employee_id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEmployeeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("獲取員工詳情失敗", e);
        }
        return null;
    }

    public List<EmpBean> getAllEmployees(boolean showAll) throws DataAccessException {
        List<EmpBean> employees = new ArrayList<>();
        // 根據 showAll 參數決定是否包括已離職員工
        String sql = showAll ? "SELECT * FROM employee" : "SELECT * FROM employee WHERE resigndate IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("獲取所有員工資訊失敗", e);
        }
        return employees;
    }

    public List<EmpBean> searchEmployees(String searchName, boolean showAll) throws DataAccessException {
        List<EmpBean> employees = new ArrayList<>();
        String sql = showAll
            ? "SELECT * FROM employee WHERE employee_name LIKE ?"
            : "SELECT * FROM employee WHERE employee_name LIKE ? AND resigndate IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(extractEmployeeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("查詢員工失敗", e);
        }
        return employees;
    }

    public List<RankLevelBean> getRankList() throws DataAccessException {
        List<RankLevelBean> rankList = new ArrayList<>();
        String sql = "SELECT r.position_id, r.position_name, r.limits_of_authority, " +
                     "COUNT(CASE WHEN e.resigndate IS NULL THEN 1 END) as active_employee_count, " +
                     "COUNT(e.employee_id) as total_employee_count " +
                     "FROM ranklevel r " +
                     "LEFT JOIN employee e ON r.position_id = e.position_id " +
                     "GROUP BY r.position_id, r.position_name, r.limits_of_authority";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RankLevelBean rank = new RankLevelBean();
                rank.setPositionId(rs.getString("position_id"));
                rank.setPositionName(rs.getString("position_name"));
                rank.setLimitsOfAuthority(rs.getInt("limits_of_authority"));
                rank.setActiveEmployeeCount(rs.getInt("active_employee_count"));
                rank.setTotalEmployeeCount(rs.getInt("total_employee_count"));
                rankList.add(rank);
            }
        } catch (SQLException e) {
            throw new DataAccessException("獲取職級列表失敗", e);
        }
        return rankList;
    }

    // 從 ResultSet 中提取員工信息的私有方法
    private EmpBean extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        EmpBean emp = new EmpBean();
        emp.setEmployeeId(rs.getString("employee_id"));
        emp.setEmployeeName(rs.getString("employee_name"));
        emp.setEmployeeTel(rs.getString("employee_tel"));
        emp.setEmployeeIdcard(rs.getString("employee_idcard"));
        emp.setEmployeeEmail(rs.getString("employee_email"));
        emp.setPassword(rs.getString("password"));
        emp.setPositionId(rs.getString("position_id"));
        emp.setHiredate(rs.getDate("hiredate").toLocalDate());
        Date resigndate = rs.getDate("resigndate");
        emp.setResigndate(resigndate != null ? resigndate.toLocalDate() : null);
        return emp;
    }

    // 獲取員工視圖模型的方法
    public EmployeeViewModel getEmployeeViewModel(String employeeId) throws DataAccessException {
        String sql = "SELECT e.*, r.position_name, r.salary_level FROM employee e JOIN ranklevel r ON e.position_id = r.position_id WHERE e.employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEmployeeViewModelFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("獲取員工視圖失敗", e);
        }
        return null;
    }

    // 從 ResultSet 中提取員工視圖模型的私有方法
    private EmployeeViewModel extractEmployeeViewModelFromResultSet(ResultSet rs) throws SQLException {
        EmployeeViewModel viewModel = new EmployeeViewModel();
        viewModel.setEmployeeId(rs.getString("employee_id"));
        viewModel.setEmployeeName(rs.getString("employee_name"));
        viewModel.setEmployeeTel(rs.getString("employee_tel"));
        viewModel.setEmployeeIdcard(rs.getString("employee_idcard"));
        viewModel.setEmployeeEmail(rs.getString("employee_email"));
        viewModel.setPositionName(rs.getString("position_name"));
        viewModel.setSalaryLevel(rs.getString("salary_level"));
        viewModel.setHiredate(rs.getDate("hiredate").toLocalDate());
        Date resigndate = rs.getDate("resigndate");
        viewModel.setResigndate(resigndate != null ? resigndate.toLocalDate() : null);
        return viewModel;
    }

    public String getLastEmployeeId() throws DataAccessException {
        String sql = "SELECT TOP 1 employee_id FROM employee ORDER BY employee_id DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("employee_id");
            }
        } catch (SQLException e) {
            throw new DataAccessException("獲取最後一個員工ID失敗", e);
        }
        return "E001"; // 如果沒有員工，返回初始值
    }
}