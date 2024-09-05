package com.MarketMaster.dao.checkout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.MarketMaster.bean.checkout.CheckoutBean;

public class ReturnProductDao {
    private static final String GET_ONE_SQL = "SELECT * FROM checkout WHERE checkout_id = ?";
    private static final String GET_ALL_SQL = "SELECT * FROM checkout";
    private static final String DELETE_SQL = "DELETE FROM checkout WHERE checkout_id=?";
    private static final String INSERT_SQL = "INSERT INTO checkout(checkout_id,customer_tel,employee_id,checkout_total_price,checkout_date,bonus_points,points_due_date) VALUES(?,?,?,?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE checkout SET customer_tel=?,employee_id=?,checkout_total_price=?,checkout_date=?,bonus_points=?,points_due_date=? WHERE checkout_id=?";
    private static final String SEARCH_SQL = "SELECT * FROM checkout WHERE customer_tel LIKE ?";

    private static Connection getConnection() throws SQLException, NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/ispan");
        return ds.getConnection();
    }

    public static CheckoutBean getOne(String checkoutId) {
        CheckoutBean ck = new CheckoutBean();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ONE_SQL)) {
            stmt.setString(1, checkoutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ck.setCheckoutId(rs.getString("checkout_id"));
                    ck.setCustomerTel(rs.getString("customer_tel"));
                    ck.setEmployeeId(rs.getString("employee_id"));
//                    ck.setCheckoutTotalPrice(rs.getString("checkout_total_price"));
//                    ck.setCheckoutDate(rs.getString("checkout_date"));
//                    ck.setBonusPoints(rs.getString("bonus_points"));
//                    ck.setPointsDueDate(rs.getString("points_due_date"));
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return ck;
    }

    public static List<CheckoutBean> getAll() {
        List<CheckoutBean> cks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CheckoutBean ck = new CheckoutBean();
                ck.setCheckoutId(rs.getString("checkout_id"));
                ck.setCustomerTel(rs.getString("customer_tel"));
                ck.setEmployeeId(rs.getString("employee_id"));
//                ck.setCheckoutTotalPrice(rs.getString("checkout_total_price"));
//                ck.setCheckoutDate(rs.getString("checkout_date"));
//                ck.setBonusPoints(rs.getString("bonus_points"));
//                ck.setPointsDueDate(rs.getString("points_due_date"));
                cks.add(ck);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return cks;
    }

    public static void insert(CheckoutBean ck) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, ck.getCheckoutId());
            stmt.setString(2, ck.getCustomerTel());
            stmt.setString(3, ck.getEmployeeId());
//            stmt.setString(4, ck.getCheckoutTotalPrice());
//            stmt.setString(5, ck.getCheckoutDate());
//            stmt.setString(6, ck.getBonusPoints());
//            stmt.setString(7, ck.getPointsDueDate());
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String checkoutId) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, checkoutId);
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static CheckoutBean getUpdate(String checkoutId) {
        return getOne(checkoutId);
    }

    public static void update(CheckoutBean ck) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, ck.getCustomerTel());
            stmt.setString(2, ck.getEmployeeId());
//            stmt.setString(3, ck.getCheckoutTotalPrice());
//            stmt.setString(4, ck.getCheckoutDate());
//            stmt.setString(5, ck.getBonusPoints());
//            stmt.setString(6, ck.getPointsDueDate());
            stmt.setString(7, ck.getCheckoutId());
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static List<CheckoutBean> searchByTel(String customerTel) {
        List<CheckoutBean> cks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SEARCH_SQL)) {
            stmt.setString(1, "%" + customerTel + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CheckoutBean ck = new CheckoutBean();
                    ck.setCheckoutId(rs.getString("checkout_id"));
                    ck.setCustomerTel(rs.getString("customer_tel"));
                    ck.setEmployeeId(rs.getString("employee_id"));
//                    ck.setCheckoutTotalPrice(rs.getString("checkout_total_price"));
//                    ck.setCheckoutDate(rs.getString("checkout_date"));
//                    ck.setBonusPoints(rs.getString("bonus_points"));
//                    ck.setPointsDueDate(rs.getString("points_due_date"));
                    cks.add(ck);
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return cks;
    }

    public static void updateTotalPrice(String returnId) {
        String sql = "UPDATE returnProduct SET return_total_price = (SELECT SUM(rd.return_price) FROM return_details rd WHERE rd.return_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, returnId);
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> getDailyReturnsReport() {
        String sql = "SELECT return_date, SUM(return_total_price) AS total_returns FROM returnProduct GROUP BY return_date";
        List<Map<String, Object>> report = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("date", rs.getDate("return_date"));
                row.put("total_returns", rs.getBigDecimal("total_returns"));
                report.add(row);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return report;
    }

    public static List<Map<String, Object>> getReturnSummary() {
        String sql = "SELECT rp.return_date, rp.employee_id, rp.return_id, rd.product_id, rd.number_of_return, rd.return_price, rp.return_total_price, rd.reason_for_return " +
                     "FROM returnProduct rp JOIN return_details rd ON rp.return_id = rd.return_id";
        List<Map<String, Object>> summary = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("return_date", rs.getDate("return_date"));
                row.put("employee_id", rs.getString("employee_id"));
                row.put("return_id", rs.getString("return_id"));
                row.put("product_id", rs.getString("product_id"));
                row.put("number_of_return", rs.getInt("number_of_return"));
                row.put("return_price", rs.getBigDecimal("return_price"));
                row.put("return_total_price", rs.getBigDecimal("return_total_price"));
                row.put("reason_for_return", rs.getString("reason_for_return"));
                summary.add(row);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return summary;
    }
}