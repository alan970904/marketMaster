package com.MarketMaster.dao.checkout;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;

public class CheckoutDetailsDao {
	private static final Logger logger = Logger.getLogger(CheckoutDetailsDao.class.getName());
    private static final String GET_ONE_SQL = "SELECT * FROM checkout_details WHERE checkout_id = ? AND product_id = ?";
    private static final String GET_PART_SQL = "SELECT * FROM checkout_details WHERE checkout_id = ?";
    private static final String GET_ALL_SQL = "SELECT * FROM checkout_details";
    private static final String DELETE_SQL = "DELETE FROM checkout_details WHERE checkout_id=? AND product_id=?";
    private static final String INSERT_SQL = "INSERT INTO checkout_details(checkout_id,product_id,number_of_checkout,product_price,checkout_price) VALUES(?,?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE checkout_details SET number_of_checkout=?, product_price=?, checkout_price=? WHERE checkout_id=? AND product_id=?";
    private static final String SEARCH_SQL = "SELECT * FROM checkout_details WHERE product_id LIKE ?";

    private static Connection getConnection() throws SQLException, NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/ispan");
        return ds.getConnection();
    }

    public static CheckoutDetailsBean getOne(String checkoutId, String productId) {
        CheckoutDetailsBean ckd = new CheckoutDetailsBean();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ONE_SQL)) {
            stmt.setString(1, checkoutId);
            stmt.setString(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ckd.setCheckoutId(rs.getString("checkout_id"));
                    ckd.setProductId(rs.getString("product_id"));
                    ckd.setNumberOfCheckout(rs.getInt("number_of_checkout"));
                    ckd.setProductPrice(rs.getInt("product_price"));
                    ckd.setCheckoutPrice(rs.getInt("checkout_price"));
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return ckd;
    }

    public static List<CheckoutDetailsBean> getPart(String checkoutId) {
        List<CheckoutDetailsBean> checkoutDetailsList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_PART_SQL)) {
            stmt.setString(1, checkoutId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CheckoutDetailsBean ckd = new CheckoutDetailsBean();
                    ckd.setCheckoutId(rs.getString("checkout_id"));
                    ckd.setProductId(rs.getString("product_id"));
                    ckd.setNumberOfCheckout(rs.getInt("number_of_checkout"));
                    ckd.setProductPrice(rs.getInt("product_price"));
                    ckd.setCheckoutPrice(rs.getInt("checkout_price"));
                    checkoutDetailsList.add(ckd);
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return checkoutDetailsList;
    }

    public static List<CheckoutDetailsBean> getAll() {
        List<CheckoutDetailsBean> ckds = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CheckoutDetailsBean ckd = new CheckoutDetailsBean();
                ckd.setCheckoutId(rs.getString("checkout_id"));
                ckd.setProductId(rs.getString("product_id"));
                ckd.setNumberOfCheckout(rs.getInt("number_of_checkout"));
                ckd.setProductPrice(rs.getInt("product_price"));
                ckd.setCheckoutPrice(rs.getInt("checkout_price"));
                ckds.add(ckd);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return ckds;
    }

    public static void insert(CheckoutDetailsBean ckd) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
                stmt.setString(1, ckd.getCheckoutId());
                stmt.setString(2, ckd.getProductId());
                stmt.setInt(3, ckd.getNumberOfCheckout());
                stmt.setInt(4, ckd.getProductPrice());
                stmt.setInt(5, ckd.getCheckoutPrice());
                stmt.executeUpdate();
            }

            updateCheckoutTotalAndBonus(conn, ckd.getCheckoutId());
            conn.commit();
            logger.info("結帳明細插入成功，總金額和紅利點數已更新");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "插入結帳明細時發生錯誤", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            throw new RuntimeException("插入結帳明細失敗", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "關閉數據庫連接時發生錯誤", e);
                }
            }
        }
    }

    public void delete(String checkoutId, String productId) {
        logger.info("刪除結帳ID為 " + checkoutId + " 和產品ID為 " + productId + " 的結帳明細");
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
                stmt.setString(1, checkoutId);
                stmt.setString(2, productId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("刪除結帳明細失敗，沒有找到匹配的記錄。");
                }
            }
            updateCheckoutTotalAndBonus(conn, checkoutId);
            conn.commit();
            logger.info("結帳明細刪除成功，總金額和紅利點數已更新");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "刪除結帳明細時發生錯誤", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            throw new RuntimeException("刪除結帳明細失敗", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "關閉數據庫連接時發生錯誤", e);
                }
            }
        }
    }

    public void update(CheckoutDetailsBean ckd) {
        logger.info("開始更新結帳明細: " + ckd);
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 更新 checkout_details 表
            String updateDetailsSql = "UPDATE checkout_details SET number_of_checkout=?, product_price=?, checkout_price=? WHERE checkout_id=? AND product_id=?";
            try (PreparedStatement stmt = conn.prepareStatement(updateDetailsSql)) {
                stmt.setInt(1, ckd.getNumberOfCheckout());
                stmt.setInt(2, ckd.getProductPrice());
                stmt.setInt(3, ckd.getCheckoutPrice());
                stmt.setString(4, ckd.getCheckoutId());
                stmt.setString(5, ckd.getProductId());
                
                logger.info("執行 SQL: " + updateDetailsSql);
                logger.info("參數: numberOfCheckout=" + ckd.getNumberOfCheckout() + 
                            ", productPrice=" + ckd.getProductPrice() + 
                            ", checkoutPrice=" + ckd.getCheckoutPrice() + 
                            ", checkoutId=" + ckd.getCheckoutId() + 
                            ", productId=" + ckd.getProductId());
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("更新結帳明細失敗，沒有找到匹配的記錄。");
                }
                logger.info("結帳明細更新成功，影響的行數: " + affectedRows);
            }

            // 更新 checkout 表的總金額和紅利點數
            updateCheckoutTotalAndBonus(conn, ckd.getCheckoutId());

            conn.commit();
            logger.info("事務提交成功，結帳明細更新完成");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "更新結帳明細時發生錯誤", e);
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.info("事務已回滾");
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            throw new RuntimeException("更新結帳明細失敗", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    logger.info("數據庫連接已關閉");
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "關閉數據庫連接時發生錯誤", e);
                }
            }
        }
    }

    public static List<CheckoutDetailsBean> searchByProductId(String productId) {
        List<CheckoutDetailsBean> ckds = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SEARCH_SQL)) {
            stmt.setString(1, "%" + productId + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CheckoutDetailsBean ckd = new CheckoutDetailsBean();
                    ckd.setCheckoutId(rs.getString("checkout_id"));
                    ckd.setProductId(rs.getString("product_id"));
                    ckd.setNumberOfCheckout(rs.getInt("number_of_checkout"));
                    ckd.setProductPrice(rs.getInt("product_price"));
                    ckd.setCheckoutPrice(rs.getInt("checkout_price"));
                    ckds.add(ckd);
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return ckds;
    }

    // 更新退貨後的結帳明細
    public static void updateAfterReturn(String checkoutId, String productId, int returnQuantity, BigDecimal returnPrice) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            String sql = "UPDATE checkout_details SET number_of_checkout = number_of_checkout - ?, checkout_price = checkout_price - ? "
                    + "WHERE checkout_id = ? AND product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, returnQuantity);
                stmt.setBigDecimal(2, returnPrice);
                stmt.setString(3, checkoutId);
                stmt.setString(4, productId);
                stmt.executeUpdate();
            }

            updateCheckoutTotalAndBonus(conn, checkoutId);
            conn.commit();
            logger.info("退貨後結帳明細更新成功，總金額和紅利點數已更新");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "更新退貨後結帳明細時發生錯誤", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            throw new RuntimeException("更新退貨後結帳明細失敗", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "關閉數據庫連接時發生錯誤", e);
                }
            }
        }
    }

    // 取消退貨並更新結帳明細
    public static void cancelReturn(String checkoutId, String productId, int returnQuantity, BigDecimal returnPrice) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            String sql = "UPDATE checkout_details SET number_of_checkout = number_of_checkout + ?, checkout_price = checkout_price + ? "
                    + "WHERE checkout_id = ? AND product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, returnQuantity);
                stmt.setBigDecimal(2, returnPrice);
                stmt.setString(3, checkoutId);
                stmt.setString(4, productId);
                stmt.executeUpdate();
            }

            updateCheckoutTotalAndBonus(conn, checkoutId);
            conn.commit();
            logger.info("取消退貨並更新結帳明細成功，總金額和紅利點數已更新");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "取消退貨並更新結帳明細時發生錯誤", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            throw new RuntimeException("取消退貨並更新結帳明細失敗", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "關閉數據庫連接時發生錯誤", e);
                }
            }
        }
    }

    // 計算商品退貨率
    public static List<Map<String, Object>> getProductReturnRates() {
        String sql = "SELECT cd.product_id, CAST(ROUND((SUM(CASE WHEN rd.return_id IS NOT NULL THEN rd.number_of_return ELSE 0 END) * 100.0) / SUM(cd.number_of_checkout), 2) AS DECIMAL(5, 2)) AS return_rate "
                + "FROM checkout_details cd LEFT JOIN return_details rd ON cd.product_id = rd.product_id AND cd.checkout_id = rd.checkout_id "
                + "GROUP BY cd.product_id";
        List<Map<String, Object>> returnRates = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("product_id", rs.getString("product_id"));
                row.put("return_rate", rs.getBigDecimal("return_rate"));
                returnRates.add(row);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return returnRates;
    }
    
    public int calculateCheckoutTotal(String checkoutId) {
        logger.info("計算結帳ID為 " + checkoutId + " 的總金額");
        String sql = "SELECT SUM(checkout_price) AS total FROM checkout_details WHERE checkout_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, checkoutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "計算結帳總金額時發生錯誤", e);
        }
        return 0;
    }

    private static void updateCheckoutTotalAndBonus(Connection conn, String checkoutId) throws SQLException {
        int totalAmount = calculateCheckoutTotal(conn, checkoutId);
        int bonusPoints = calculateBonusPoints(totalAmount);
        String sql = "UPDATE checkout SET checkout_total_price = ?, bonus_points = ? WHERE checkout_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, totalAmount);
            stmt.setInt(2, bonusPoints);
            stmt.setString(3, checkoutId);
            
            logger.info("執行 SQL: " + sql);
            logger.info("參數: totalAmount=" + totalAmount + 
                        ", bonusPoints=" + bonusPoints + 
                        ", checkoutId=" + checkoutId);
            
            int affectedRows = stmt.executeUpdate();
            logger.info("更新 checkout 表成功，影響的行數: " + affectedRows);
        }
    }
    
    private static int calculateCheckoutTotal(Connection conn, String checkoutId) throws SQLException {
        String sql = "SELECT SUM(checkout_price) AS total FROM checkout_details WHERE checkout_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, checkoutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    private static int calculateBonusPoints(int totalAmount) {
        // 假設每100元可得1點紅利
        return totalAmount / 100;
    }
    
}
