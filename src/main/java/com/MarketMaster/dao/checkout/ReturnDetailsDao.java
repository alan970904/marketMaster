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

import com.MarketMaster.bean.checkout.ReturnDetailsBean;

public class ReturnDetailsDao {
    private static final String GET_ONE_SQL = "SELECT * FROM return_details WHERE return_id = ?";
    private static final String GET_ALL_SQL = "SELECT * FROM return_details";
    private static final String DELETE_SQL = "DELETE FROM return_details WHERE return_id=?";
    private static final String INSERT_SQL = "INSERT INTO return_details(return_id,checkout_id,product_id,reason_for_return,number_of_return,product_price,return_price) VALUES(?,?,?,?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE return_details SET checkout_id=?,product_id=?,reason_for_return=?,number_of_return=?,product_price=?,return_price=? WHERE return_id=?";
    private static final String SEARCH_SQL = "SELECT * FROM return_details WHERE product_id LIKE ?";

    private static Connection getConnection() throws SQLException, NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/ispan");
        return ds.getConnection();
    }

    public static ReturnDetailsBean getOne(String returnId) {
        ReturnDetailsBean rtd = new ReturnDetailsBean();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ONE_SQL)) {
            stmt.setString(1, returnId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rtd.setReturnId(rs.getString("return_id"));
                    rtd.setCheckoutId(rs.getString("checkout_id"));
                    rtd.setProductId(rs.getString("product_id"));
                    rtd.setReasonForReturn(rs.getString("reason_for_return"));
                    rtd.setNumberOfReturn(rs.getString("number_of_return"));
                    rtd.setProductPrice(rs.getString("product_price"));
                    rtd.setReturnPrice(rs.getString("return_price"));
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return rtd;
    }

    public static List<ReturnDetailsBean> getAll() {
        List<ReturnDetailsBean> rtds = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ReturnDetailsBean rtd = new ReturnDetailsBean();
                rtd.setReturnId(rs.getString("return_id"));
                rtd.setCheckoutId(rs.getString("checkout_id"));
                rtd.setProductId(rs.getString("product_id"));
                rtd.setReasonForReturn(rs.getString("reason_for_return"));
                rtd.setNumberOfReturn(rs.getString("number_of_return"));
                rtd.setProductPrice(rs.getString("product_price"));
                rtd.setReturnPrice(rs.getString("return_price"));
                rtds.add(rtd);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return rtds;
    }

    public static void insert(ReturnDetailsBean rtd) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, rtd.getReturnId());
            stmt.setString(2, rtd.getCheckoutId());
            stmt.setString(3, rtd.getProductId());
            stmt.setString(4, rtd.getReasonForReturn());
            stmt.setString(5, rtd.getNumberOfReturn());
            stmt.setString(6, rtd.getProductPrice());
            stmt.setString(7, rtd.getReturnPrice());
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String returnId) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, returnId);
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static ReturnDetailsBean getUpdate(String returnId) {
        return getOne(returnId);
    }

    public static void update(ReturnDetailsBean rtd) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, rtd.getCheckoutId());
            stmt.setString(2, rtd.getProductId());
            stmt.setString(3, rtd.getReasonForReturn());
            stmt.setString(4, rtd.getNumberOfReturn());
            stmt.setString(5, rtd.getProductPrice());
            stmt.setString(6, rtd.getReturnPrice());
            stmt.setString(7, rtd.getReturnId());
            stmt.executeUpdate();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static List<ReturnDetailsBean> searchByProductId(String productId) {
        List<ReturnDetailsBean> rtds = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SEARCH_SQL)) {
            stmt.setString(1, "%" + productId + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReturnDetailsBean rtd = new ReturnDetailsBean();
                    rtd.setReturnId(rs.getString("return_id"));
                    rtd.setCheckoutId(rs.getString("checkout_id"));
                    rtd.setProductId(rs.getString("product_id"));
                    rtd.setReasonForReturn(rs.getString("reason_for_return"));
                    rtd.setNumberOfReturn(rs.getString("number_of_return"));
                    rtd.setProductPrice(rs.getString("product_price"));
                    rtd.setReturnPrice(rs.getString("return_price"));
                    rtds.add(rtd);
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return rtds;
    }

    public static List<Map<String, Object>> getReturnComparisonReport() {
        String sql = "SELECT rd.return_id, rd.checkout_id, rd.product_id, rd.number_of_return, rd.return_price, cd.number_of_checkout, cd.checkout_price, rd.reason_for_return " +
                     "FROM return_details rd JOIN checkout_details cd ON rd.checkout_id = cd.checkout_id AND rd.product_id = cd.product_id";
        List<Map<String, Object>> report = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("return_id", rs.getString("return_id"));
                row.put("checkout_id", rs.getString("checkout_id"));
                row.put("product_id", rs.getString("product_id"));
                row.put("number_of_return", rs.getInt("number_of_return"));
                row.put("return_price", rs.getBigDecimal("return_price"));
                row.put("number_of_checkout", rs.getInt("number_of_checkout"));
                row.put("checkout_price", rs.getBigDecimal("checkout_price"));
                row.put("reason_for_return", rs.getString("reason_for_return"));
                report.add(row);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return report;
    }
}