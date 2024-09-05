package com.MarketMaster.dao.schedule;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.MarketMaster.bean.schedule.AskForLeaveBean;

public class AskForLeaveDao {
    
    Connection conn;

    // Generate the next leave ID
    public String generateNextLeaveId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT MAX(CAST(SUBSTRING(leave_id, 2, LEN(leave_id) - 1) AS INT)) AS max_id FROM ask_for_leave";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                Integer maxId = rs.getInt("max_id");
                if (maxId != null) {
                    return "L" + String.format("%05d", maxId + 1);
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        
        return "L00001"; 
    }

    // Get employee name by ID
    public String getEmployeeNameById(String employeeId) throws SQLException, ClassNotFoundException {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            return null; 
        }

        String sql = "SELECT employee_name FROM employee WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("employee_name");
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new leave record
    public void addLeaveRecordById(AskForLeaveBean leave) throws SQLException, ClassNotFoundException {
        String insertLeaveSql = "INSERT INTO ask_for_leave (leave_id, employee_id, start_datetime, end_datetime, leave_category, reason_of_leave, approved_status) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement insertLeaveStmt = conn.prepareStatement(insertLeaveSql)) {

            insertLeaveStmt.setString(1, leave.getLeaveId());
            insertLeaveStmt.setString(2, leave.getEmployeeId());
            insertLeaveStmt.setTimestamp(3, Timestamp.valueOf(leave.getStartDatetime()));
            insertLeaveStmt.setTimestamp(4, Timestamp.valueOf(leave.getEndDatetime()));
            insertLeaveStmt.setString(5, leave.getLeaveCategory());
            insertLeaveStmt.setString(6, leave.getReasonOfLeave());
            insertLeaveStmt.setString(7, leave.getApprovedStatus());

            insertLeaveStmt.executeUpdate();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
    // Update a leave record
    public void updateLeaveRecord(AskForLeaveBean leave) throws SQLException, ClassNotFoundException {
        String updateLeaveSql = "UPDATE ask_for_leave SET "
                                + "employee_id = ?, "
                                + "start_datetime = ?, "
                                + "end_datetime = ?, "
                                + "leave_category = ?, "
                                + "reason_of_leave = ?, "
                                + "approved_status = ? "
                                + "WHERE leave_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement updateLeaveStmt = conn.prepareStatement(updateLeaveSql)) {

            updateLeaveStmt.setString(1, leave.getEmployeeId());
            updateLeaveStmt.setTimestamp(2, Timestamp.valueOf(leave.getStartDatetime()));
            updateLeaveStmt.setTimestamp(3, Timestamp.valueOf(leave.getEndDatetime()));
            updateLeaveStmt.setString(4, leave.getLeaveCategory());
            updateLeaveStmt.setString(5, leave.getReasonOfLeave());
            updateLeaveStmt.setString(6, leave.getApprovedStatus());
            updateLeaveStmt.setString(7, leave.getLeaveId()); 

            updateLeaveStmt.executeUpdate();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Get leave records by employee ID
    public List<AskForLeaveBean> getLeaveRecordsById(String employeeId) throws SQLException, ClassNotFoundException {
        List<AskForLeaveBean> leaveList = new ArrayList<>();
        String sql = "SELECT a.leave_id, a.employee_id, e.employee_name, a.start_datetime, a.end_datetime, a.leave_category, a.reason_of_leave, a.approved_status "
                   + "FROM ask_for_leave a "
                   + "JOIN employee e ON a.employee_id = e.employee_id "
                   + "WHERE a.employee_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AskForLeaveBean leave = new AskForLeaveBean(
                            rs.getString("leave_id"),
                            rs.getString("employee_id"),
                            rs.getString("employee_name"),
                            rs.getObject("start_datetime", LocalDateTime.class),
                            rs.getObject("end_datetime", LocalDateTime.class),
                            rs.getString("leave_category"),
                            rs.getString("reason_of_leave"),
                            rs.getString("approved_status"));
                    leaveList.add(leave);
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return leaveList;
    }
    
    // Get leave records by leave ID
    public List<AskForLeaveBean> getLeaveRecordsByLeaveId(String leaveId) throws SQLException, ClassNotFoundException {
        List<AskForLeaveBean> leaveList = new ArrayList<>();
        String sql = "SELECT a.leave_id, a.employee_id, e.employee_name, a.start_datetime, a.end_datetime, a.leave_category, a.reason_of_leave, a.approved_status "
                   + "FROM ask_for_leave a "
                   + "JOIN employee e ON a.employee_id = e.employee_id "
                   + "WHERE a.leave_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, leaveId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AskForLeaveBean leave = new AskForLeaveBean(
                            rs.getString("leave_id"),
                            rs.getString("employee_id"),
                            rs.getString("employee_name"),
                            rs.getObject("start_datetime", LocalDateTime.class),
                            rs.getObject("end_datetime", LocalDateTime.class),
                            rs.getString("leave_category"),
                            rs.getString("reason_of_leave"),
                            rs.getString("approved_status"));
                    leaveList.add(leave);
                    
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return leaveList;
    }

    // Get all leave records
    public List<AskForLeaveBean> getLeaveRecords() throws SQLException, ClassNotFoundException {
        List<AskForLeaveBean> leaveList = new ArrayList<>();
        String sql = "SELECT a.leave_id, a.employee_id, e.employee_name, a.start_datetime, a.end_datetime, a.leave_category, a.reason_of_leave, a.approved_status "
                   + "FROM ask_for_leave a "
                   + "JOIN employee e ON a.employee_id = e.employee_id";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AskForLeaveBean leave = new AskForLeaveBean(
                        rs.getString("leave_id"),
                        rs.getString("employee_id"),
                        rs.getString("employee_name"),
                        rs.getObject("start_datetime", LocalDateTime.class),
                        rs.getObject("end_datetime", LocalDateTime.class),
                        rs.getString("leave_category"),
                        rs.getString("reason_of_leave"),
                        rs.getString("approved_status"));
                leaveList.add(leave);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return leaveList;
    }

    // Delete a leave record
    public void deleteLeaveRecord(String leaveId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ask_for_leave WHERE leave_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, leaveId);
            pstmt.executeUpdate();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Get database connection
    private Connection getConnection() throws NamingException, SQLException {
//    	String url = "jdbc:sqlserver://localhost:1433;DatabaseName=ispan;encrypt=false";
//        String user = "Java05";
//        String password = "0000";
//        return DriverManager.getConnection(url, user, password);
    	
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/ispan");
        return ds.getConnection(); 
    }

    public boolean updateLeaveRecord(String leaveIdUpdate, String employeeId, String employeeNameUpdate,
            String startDatetimeUpdate, String endDatetimeUpdate, String leaveCategoryUpdate,
            String reasonOfLeaveUpdate) {
        return false;
    }
    
    public static void main(String[] args) {
        AskForLeaveDao askForLeaveDao = new AskForLeaveDao();
        
        try {
            Connection conn = askForLeaveDao.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("连接成功！");
                conn.close(); 
            } else {
                System.out.println("连接失败！");
            }
        } catch (NamingException e) {
            System.err.println("NamingException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQLException occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}