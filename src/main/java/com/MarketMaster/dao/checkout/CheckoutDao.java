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

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;

public class CheckoutDao {
    private static final Logger logger = Logger.getLogger(CheckoutDao.class.getName());

    // SQL 語句常量
    private static final String GET_ONE_SQL = "SELECT * FROM checkout WHERE checkout_id = ?";
    private static final String GET_ALL_SQL = "SELECT * FROM checkout";
    private static final String DELETE_SQL = "DELETE FROM checkout WHERE checkout_id=?";
    private static final String INSERT_SQL = "INSERT INTO checkout(checkout_id,customer_tel,employee_id,checkout_total_price,checkout_date,bonus_points,points_due_date) VALUES(?,?,?,?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE checkout SET customer_tel=?,employee_id=?,checkout_total_price=?,checkout_date=?,bonus_points=?,points_due_date=? WHERE checkout_id=?";
    private static final String SEARCH_SQL = "SELECT * FROM checkout WHERE customer_tel LIKE ?";

    // 獲取數據庫連接
    private Connection getConnection() throws SQLException, NamingException {
        logger.info("嘗試建立數據庫連接");
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/ispan");
        Connection conn = ds.getConnection();
        logger.info("數據庫連接成功建立");
        return conn;
    }
    public static java.sql.Date convertToSqlDate(java.util.Date utilDate) {
        if (utilDate == null) {
            return null;  // 如果輸入為 null，則返回 null
        }
        return new java.sql.Date(utilDate.getTime());
    }

    // 獲取單個結帳記錄
    public CheckoutBean getOne(String checkoutId) {
        logger.info("開始獲取結帳ID為 " + checkoutId + " 的記錄");
        CheckoutBean ck = new CheckoutBean();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ONE_SQL)) {
            stmt.setString(1, checkoutId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ck.setCheckoutId(rs.getString("checkout_id"));
                    ck.setCustomerTel(rs.getString("customer_tel"));
                    ck.setEmployeeId(rs.getString("employee_id"));
                    ck.setCheckoutTotalPrice(rs.getInt("checkout_total_price"));
                    ck.setCheckoutDate(rs.getDate("checkout_date"));
                    ck.setBonusPoints(rs.getInt("bonus_points"));
                    ck.setPointsDueDate(rs.getDate("points_due_date"));
                }
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "獲取結帳記錄時發生錯誤", e);
        }
        logger.info("成功獲取結帳記錄");
        return ck;
    }

    // 獲取所有結帳記錄
    public List<CheckoutBean> getAll() {
        logger.info("開始獲取所有結帳記錄");
        List<CheckoutBean> cks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CheckoutBean ck = new CheckoutBean();
                ck.setCheckoutId(rs.getString("checkout_id"));
                ck.setCustomerTel(rs.getString("customer_tel"));
                ck.setEmployeeId(rs.getString("employee_id"));
                ck.setCheckoutTotalPrice(rs.getInt("checkout_total_price"));
                ck.setCheckoutDate(rs.getDate("checkout_date"));
                ck.setBonusPoints(rs.getInt("bonus_points"));
                ck.setPointsDueDate(rs.getDate("points_due_date"));
                cks.add(ck);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "獲取所有結帳記錄時發生錯誤", e);
        }
        logger.info("成功獲取 " + cks.size() + " 筆結帳記錄");
        return cks;
    }

    // 插入新的結帳記錄
    public boolean insert(CheckoutBean checkout) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 插入 checkout 表
            String insertCheckoutSql = "INSERT INTO checkout(checkout_id, customer_tel, employee_id, checkout_total_price, checkout_date, bonus_points, points_due_date) VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCheckoutSql)) {
                stmt.setString(1, checkout.getCheckoutId());
                stmt.setString(2, checkout.getCustomerTel());
                stmt.setString(3, checkout.getEmployeeId());
                stmt.setInt(4, checkout.getCheckoutTotalPrice());
                stmt.setDate(5, new java.sql.Date(checkout.getCheckoutDate().getTime()));
                stmt.setInt(6, checkout.getBonusPoints());
                stmt.setDate(7, checkout.getPointsDueDate() != null ? new java.sql.Date(checkout.getPointsDueDate().getTime()) : null);
                stmt.executeUpdate();
            }

            // 插入 checkout_details 表
            String insertDetailsSql = "INSERT INTO checkout_details(checkout_id, product_id, number_of_checkout, checkout_price) VALUES(?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertDetailsSql)) {
                for (CheckoutDetailsBean detail : checkout.getCheckoutDetails()) {
                    stmt.setString(1, checkout.getCheckoutId());
                    stmt.setString(2, detail.getProductId());
                    stmt.setInt(3, detail.getNumberOfCheckout());
                    stmt.setInt(4, detail.getCheckoutPrice());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "插入結帳記錄時發生錯誤", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            return false;
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

    // 刪除結帳記錄
    public void delete(String checkoutId) {
        logger.info("開始刪除結帳ID為 " + checkoutId + " 的記錄");
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, checkoutId);
            stmt.executeUpdate();
            logger.info("結帳記錄刪除成功");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "刪除結帳記錄時發生錯誤", e);
        }
    }

    // 獲取要更新的結帳記錄
    public CheckoutBean getUpdate(String checkoutId) {
        logger.info("開始獲取要更新的結帳記錄，ID為 " + checkoutId);
        return getOne(checkoutId);
    }

    // 更新結帳記錄
    public boolean update(CheckoutBean ck) {
        logger.info("開始更新結帳記錄，ID為 " + ck.getCheckoutId());
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, ck.getCustomerTel());
            stmt.setString(2, ck.getEmployeeId());
            stmt.setInt(3, ck.getCheckoutTotalPrice());
            stmt.setDate(4, convertToSqlDate(ck.getCheckoutDate()));
            stmt.setInt(5, ck.getBonusPoints());
            stmt.setDate(6, convertToSqlDate(ck.getPointsDueDate()));
            stmt.setString(7, ck.getCheckoutId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("結帳記錄更新成功");
                return true;
            } else {
                logger.warning("結帳記錄更新失敗，沒有記錄被更新");
                return false;
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "更新結帳記錄時發生錯誤", e);
            return false;
        }
    }

    // 根據電話號碼搜索結帳記錄
    public List<CheckoutBean> searchByTel(String customerTel) {
        logger.info("開始根據電話號碼搜索結帳記錄: " + customerTel);
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
                    ck.setCheckoutTotalPrice(rs.getInt("checkout_total_price")); // 使用 int
                    ck.setCheckoutDate(rs.getDate("checkout_date"));
                    ck.setBonusPoints(rs.getInt("bonus_points"));
                    ck.setPointsDueDate(rs.getDate("points_due_date"));
                    cks.add(ck);
                }
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "搜索結帳記錄時發生錯誤", e);
        }
        logger.info("成功找到 " + cks.size() + " 筆符合條件的結帳記錄");
        return cks;
    }

    // 更新結帳總價
    public void updateTotalPrice(String checkoutId) {
        logger.info("開始更新結帳ID為 " + checkoutId + " 的總價");
        String sql = "UPDATE checkout SET checkout_total_price = (SELECT SUM(cd.checkout_price) FROM checkout_details cd WHERE cd.checkout_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, checkoutId);
            stmt.executeUpdate();
            logger.info("結帳總價更新成功");
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "更新結帳總價時發生錯誤", e);
        }
    }

    // 獲取每日銷售報告
    public List<Map<String, Object>> getDailySalesReport() {
        logger.info("開始獲取每日銷售報告");
        String sql = "SELECT checkout_date, SUM(CAST(checkout_total_price AS DECIMAL(10,2))) AS total_sales FROM checkout GROUP BY checkout_date";
        List<Map<String, Object>> report = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("checkoutDate", rs.getDate("checkout_date").toString());
                row.put("totalSales", rs.getBigDecimal("total_sales").toString());
                report.add(row);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "獲取每日銷售報告時發生錯誤", e);
        }
        logger.info("成功獲取 " + report.size() + " 天的銷售報告");
        return report;
    }

    // 獲取結帳總表
    public List<Map<String, Object>> getCheckoutSummary() {
        logger.info("開始獲取結帳總表");
        String sql = "SELECT c.checkout_date, c.employee_id, c.checkout_id, cd.product_id, cd.number_of_checkout, cd.checkout_price, c.checkout_total_price " +
                     "FROM checkout c JOIN checkout_details cd ON c.checkout_id = cd.checkout_id";
        List<Map<String, Object>> summary = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("checkoutDate", rs.getDate("checkout_date").toString());
                row.put("employeeId", rs.getString("employee_id"));
                row.put("checkoutId", rs.getString("checkout_id"));
                row.put("productId", rs.getString("product_id"));
                row.put("numberOfCheckout", rs.getInt("number_of_checkout"));
                row.put("checkoutPrice", rs.getBigDecimal("checkout_price").toString());
                row.put("checkoutTotalPrice", rs.getBigDecimal("checkout_total_price").toString());
                summary.add(row);
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "獲取結帳總表時發生錯誤", e);
        }
        logger.info("成功獲取 " + summary.size() + " 筆結帳總表記錄");
        return summary;
    }

 // 獲取最新的結帳ID
    public String getLastCheckoutId() {
        logger.info("開始獲取最新的結帳ID");
        String sql = "SELECT TOP 1 checkout_id FROM checkout ORDER BY checkout_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("checkout_id");
                logger.info("成功獲取最新的結帳ID: " + lastId);
                return lastId;
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "獲取最新結帳ID時發生錯誤", e);
        }
        logger.info("未找到結帳記錄，返回初始值: C00000000");
        return "C00000000"; // 如果沒有記錄，返回初始值
    }


    // 獲取所有員工ID
    public List<EmpBean> getAllEmployees() {
        logger.info("開始獲取所有員工ID");
        List<EmpBean> employees = new ArrayList<>();
        String sql = "SELECT employee_id, employee_name FROM employee";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
            	EmpBean emp = new EmpBean();
                emp.setEmployeeId(rs.getString("employee_id"));
                emp.setEmployeeName(rs.getString("employee_name"));
                employees.add(emp);
            }
        } catch (SQLException | NamingException e) {logger.log(Level.SEVERE, "獲取所有員工ID時發生錯誤", e);
        }
        logger.info("成功獲取 " + employees.size() + " 名員工信息");
        return employees;
    }

    // 獲取所有產品類別
//    public List<ProductBean> getProductCategories() {
//        logger.info("開始獲取所有產品類別");
//        List<ProductBean> categories = new ArrayList<>();
//        String sql = "SELECT product_category FROM products GROUP BY product_category";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//            	ProductBean product = new ProductBean();
//                product.setProductCategory(rs.getString("product_category"));
//                categories.add(product);
//            }
//        } catch (SQLException | NamingException e) {
//            logger.log(Level.SEVERE, "獲取產品類別時發生錯誤", e);
//        }
//        logger.info("成功獲取 " + categories.size() + " 個產品類別");
//        return categories;
//    }

 // 根據類別獲取所有產品名稱 & ID & 價錢
    public List<ProductBean> getProductNamesByCategory(String category) throws SQLException, ClassNotFoundException, NamingException {
        logger.info("開始獲取類別為 '" + category + "' 的產品列表");
        List<ProductBean> productList = new ArrayList<>();
        String sql = "SELECT product_id, product_name,product_price FROM products WHERE product_category = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProductBean product = new ProductBean();
                    product.setProductId(rs.getString("product_id"));
                    product.setProductName(rs.getString("product_name"));
                    product.setProductPrice(rs.getInt("product_price"));
                    productList.add(product);
                }
            }
        }
        logger.info("成功獲取 " + productList.size() + " 個產品");
        return productList;
    }


 // 新增方法來同時插入結帳記錄和明細
    public boolean insertCheckoutWithDetails(CheckoutBean checkout, List<CheckoutDetailsBean> details) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 插入結帳記錄
            String insertCheckoutSql = "INSERT INTO checkout(checkout_id, customer_tel, employee_id, checkout_total_price, checkout_date, bonus_points, points_due_date) VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCheckoutSql)) {
                stmt.setString(1, checkout.getCheckoutId());
                stmt.setString(2, checkout.getCustomerTel());
                stmt.setString(3, checkout.getEmployeeId());
                stmt.setInt(4, checkout.getCheckoutTotalPrice());
                stmt.setDate(5, new java.sql.Date(checkout.getCheckoutDate().getTime()));
                stmt.setInt(6, checkout.getBonusPoints());
                stmt.setDate(7, checkout.getPointsDueDate() != null ? new java.sql.Date(checkout.getPointsDueDate().getTime()) : null);
                stmt.executeUpdate();
            }

            // 插入結帳明細
            String insertDetailsSql = "INSERT INTO checkout_details(checkout_id, product_id, number_of_checkout, product_price, checkout_price) VALUES(?,?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertDetailsSql)) {
                for (CheckoutDetailsBean detail : details) {
                    stmt.setString(1, detail.getCheckoutId());
                    stmt.setString(2, detail.getProductId());
                    stmt.setInt(3, detail.getNumberOfCheckout());
                    stmt.setInt(4, detail.getProductPrice());
                    stmt.setInt(5, detail.getCheckoutPrice());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException | NamingException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾交易時發生錯誤", ex);
                }
            }
            logger.log(Level.SEVERE, "插入結帳記錄和明細時發生錯誤", e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "關閉連接時發生錯誤", e);
                }
            }
        }
    }


    // 更新產品庫存
    public void updateProductStock(String productId, int quantity) {
        logger.info("開始更新產品ID為 " + productId + " 的庫存，數量變化: " + quantity);
        String sql = "UPDATE products SET product_stock = product_stock - ? WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("產品庫存更新成功");
            } else {
                logger.warning("未找到對應的產品，庫存更新失敗");
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "更新產品庫存時發生錯誤", e);
        }
    }


 // 刪除結帳記錄和相關的結帳明細
    public void deleteCheckoutAndDetails(String checkoutId) {
        logger.info("開始刪除結帳記錄及其相關明細，結帳ID: " + checkoutId);
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 首先刪除相關的結帳明細
            String deleteDetailsSql = "DELETE FROM checkout_details WHERE checkout_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteDetailsSql)) {
                stmt.setString(1, checkoutId);
                int detailsDeleted = stmt.executeUpdate();
                logger.info("已刪除 " + detailsDeleted + " 條結帳明細記錄");
            }

            // 然後刪除結帳記錄
            String deleteCheckoutSql = "DELETE FROM checkout WHERE checkout_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteCheckoutSql)) {
                stmt.setString(1, checkoutId);
                int checkoutDeleted = stmt.executeUpdate();
                if (checkoutDeleted == 0) {
                    throw new SQLException("未找到要刪除的結帳記錄");
                }
                logger.info("已刪除結帳記錄");
            }

            conn.commit();
            logger.info("成功刪除結帳記錄及其相關明細，結帳ID: " + checkoutId);
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "刪除結帳記錄及其相關明細時發生錯誤，結帳ID: " + checkoutId, e);
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.info("事務已回滾");
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "回滾事務時發生錯誤", ex);
                }
            }
            throw new RuntimeException("刪除結帳記錄及其相關明細失敗", e);
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

    // 更新總金額和紅利點數
    public void updateTotalAndBonus(String checkoutId, BigDecimal totalAmount, int bonusPoints) {
        logger.info("開始更新結帳ID為 " + checkoutId + " 的總金額和紅利點數");
        String sql = "UPDATE checkout SET checkout_total_price = ?, bonus_points = ? WHERE checkout_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, totalAmount);
            stmt.setInt(2, bonusPoints);
            stmt.setString(3, checkoutId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("總金額和紅利點數更新成功");
            } else {
                logger.warning("未找到對應的結帳記錄，更新失敗");
            }
        } catch (SQLException | NamingException e) {
            logger.log(Level.SEVERE, "更新總金額和紅利點數時發生錯誤", e);
        }
    }



}