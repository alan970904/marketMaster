package com.MarketMaster.dao.schedule;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ScheduleDao {
	Connection conn;
	public List<String> getEmployeeNames() {
	    Set<String> employeeNamesSet = new LinkedHashSet<>();
	    String sql = "SELECT employee_name FROM employee";

	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            String employeeName = rs.getString("employee_name");
	            employeeNamesSet.add(employeeName); // 使用 Set 排除重複項
	        }
	    } catch (SQLException | NamingException e) {
	        e.printStackTrace();
	    }
	    return new LinkedList<>(employeeNamesSet);
	}

	


    private Connection getConnection() throws NamingException, SQLException {

    	
    	
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/ispan");
        return ds.getConnection();
    }
    

}



