package com.MarketMaster.dao.restock;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import com.MarketMaster.DTO.restock.EmployeeDTO;
import com.MarketMaster.DTO.restock.ProductCategoryDTO;
import com.MarketMaster.DTO.restock.ProductNameDTO;
import com.MarketMaster.bean.restock.RestockBean;
import com.MarketMaster.bean.restock.RestockDetailsBean;
import jakarta.servlet.http.HttpServletRequest;

import com.MarketMaster.bean.restock.RestockDetailViewBean;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.MarketMaster.util.HibernateUtil;

@SuppressWarnings("unused")
public class RestockDAO {
    private static final Logger logger = Logger.getLogger(RestockDAO.class.getName());

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        logger.info("嘗試建立數據庫連接");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ispan;encrypt=false", "Java05", "0000");
        logger.info("數據庫連接成功建立");
        return conn;
    }
    public List<EmployeeDTO> getEmployees() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            List<EmployeeDTO> employees = session.createQuery(
                    "select new com.MarketMaster.DTO.restock.EmployeeDTO(e.employeeName, e.employeeId) from Employee e", EmployeeDTO.class
            ).list();
            session.getTransaction().commit();
            return employees;
        } finally {
            session.close();
        }
    }
//    public List<Employee> getEmployees() throws SQLException, ClassNotFoundException {
//        logger.info("開始獲取員工列表");
//        List<Employee> employees = new ArrayList<>();
//        String sql = "SELECT employee_id, employee_name FROM employee";
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                Employee emp = new Employee();
//                emp.setEmployeeId(rs.getString("employee_id"));
//                emp.setEmployeeName(rs.getString("employee_name"));
//                employees.add(emp);
//            }
//        }
//        logger.info("成功獲取 " + employees.size() + " 名員工信息");
//        return employees;
//    }
public boolean delete(String restockId) {
    Session session = null;
    Transaction transaction = null;
    boolean success = false;

    try {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();

        // 首先刪除 RestockDetailsBean
        String detailsHql = "DELETE FROM RestockDetailsBean r WHERE r.restockId = :restockId";
        Query detailsQuery = session.createQuery(detailsHql);
        detailsQuery.setParameter("restockId", restockId);
        int detailsDeleted = detailsQuery.executeUpdate();

        // 然後刪除 RestockBean
        String restockHql = "DELETE FROM RestockBean r WHERE r.restockId = :restockId";
        Query restockQuery = session.createQuery(restockHql);
        restockQuery.setParameter("restockId", restockId);
        int restocksDeleted = restockQuery.executeUpdate();

        transaction.commit();
        success = (detailsDeleted > 0 || restocksDeleted > 0);
    } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception rollbackException) {
            }
        }
        throw new RuntimeException("Failed to delete restock", e);
    } finally {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    return success;
}
//    public boolean delete(String restockId) throws SQLException, ClassNotFoundException {
//        String sql = "DELETE FROM restock_details WHERE restock_id = ?";
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, restockId);
//            int affectedRows = pstmt.executeUpdate();
//
//            return affectedRows > 0;
//        }
//        }

    public String getLatestRestockId() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hql="FROM RestockBean r WHERE r.restockId LIKE :restockId ORDER BY r.restockId DESC";
        String today = LocalDate.now().now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        Query query = session.createQuery(hql);
        query.setParameter("restockId", "%" + today + "%");
        query.setMaxResults(1);

        RestockBean result = (RestockBean) query.uniqueResult();
        String newId;
        if(result!=null){
            String lastestId = result.getRestockId();
            int squence = Integer.parseInt(lastestId.substring(8))+1;
            newId =String.format("%s%03d", today, squence);

        }else {
            newId=today+"001";
        }
        transaction.commit();
        return newId;

    }

//    public String getLatestRestockId() throws SQLException, ClassNotFoundException {
//        logger.info("開始獲取最新進貨ID");
//        String sql = "SELECT TOP 1 restock_id FROM restocks WHERE restock_id LIKE ? ORDER BY restock_id DESC";
//        String today = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
//
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, today + "%");
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    String latestId = rs.getString("restock_id");
//                    int sequence = Integer.parseInt(latestId.substring(8)) + 1;
//                    String newId = String.format("%s%03d", today, sequence);
//                    logger.info("生成新的進貨ID: " + newId);
//                    return newId;
//                } else {
//                    String newId = today + "001";
//                    logger.info("生成新的進貨ID: " + newId);
//                    return newId;
//                }
//            }
//        }
//    }

    public List<ProductCategoryDTO> getProductCategory() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT DISTINCT p.productCategory FROM ProductBean p";
            List<String> categories = session.createQuery(hql, String.class).list();

            return categories.stream()
                    .map(ProductCategoryDTO::new)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }
//    public List<ProductBean> getProductCategory() throws SQLException, ClassNotFoundException {
//        logger.info("開始獲取產品類別列表");
//        List<ProductBean> categoryList = new ArrayList<>();
//        String sql = "SELECT product_category FROM products GROUP BY product_category";
//
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                ProductBean product = new ProductBean();
//                product.setProductCategory(rs.getString("product_category"));
//                categoryList.add(product);
//            }
//        }
//        logger.info("成功獲取 " + categoryList.size() + " 個產品類別");
//        return categoryList;
//    }
public List<ProductNameDTO> getProductNamesByCategory(String category) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
        String hql = "SELECT NEW com.MarketMaster.DTO.restock.ProductNameDTO(p.productId, p.productName) FROM ProductBean p WHERE p.productCategory = :category";
        Query<ProductNameDTO> query = session.createQuery(hql, ProductNameDTO.class);
        query.setParameter("category", category);
        return query.list();
    } finally {
        session.close();
    }
}
//    public List<ProductBean> getProductNamesByCategory(String category) throws SQLException, ClassNotFoundException {
//        logger.info("開始獲取類別為 '" + category + "' 的產品列表");
//        List<ProductBean> productList = new ArrayList<>();
//        String sql = "SELECT product_id, product_name FROM products WHERE product_category = ?";
//
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, category);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    ProductBean product = new ProductBean();
//                    product.setProductId(rs.getString("product_id"));
//                    product.setProductName(rs.getString("product_name"));
//                    productList.add(product);
//                }
//            }
//        }
//        logger.info("成功獲取 " + productList.size() + " 個產品");
//        return productList;
//    }


    public  void insertRestockData (String restockId,HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction transaction = session.beginTransaction();
            insertRestock(session,restockId,request);
            insertRestockDetails(session,restockId,request);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {session.close();
        }

    }
//    public void insertRestockData(String restockId, HttpServletRequest request) throws SQLException, ClassNotFoundException {
//        logger.info("開始插入進貨數據，進貨ID: " + restockId);
//        try (Connection conn = getConnection()) {
//            conn.setAutoCommit(false);
//            try {
//                insertRestock(conn, restockId, request);
//                insertRestockDetails(conn, restockId, request);
//                conn.commit();
//                logger.info("進貨數據插入成功");
//            } catch (SQLException e) {
//                conn.rollback();
//                logger.log(Level.SEVERE, "插入進貨數據時發生錯誤，執行回滾", e);
//                throw e;
//            }
//        }
//    }

    private void insertRestock(Session session, String restockId, HttpServletRequest request) {
        RestockBean restock = new RestockBean();
        restock.setRestockId(restockId);
        restock.setEmployeeId(request.getParameter("employee_id"));
        restock.setRestockTotalPrice(Double.parseDouble(request.getParameter("restock_total_price")));
        restock.setRestockDate(LocalDate.parse(request.getParameter("restock_date")));
        session.save(restock);
        logger.info("進貨主表插入完成，ID: " + restock.getRestockId());
    }
//    private void insertRestock(Connection conn, String restockId, HttpServletRequest request) throws SQLException {
//        logger.info("插入進貨主表數據");
//        String sql = "INSERT INTO restocks (restock_id, employee_id, restock_total_price, restock_date) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, restockId);
//            pstmt.setString(2, request.getParameter("employee_id"));
//            pstmt.setBigDecimal(3, new BigDecimal(request.getParameter("restock_total_price")));
//            pstmt.setDate(4, Date.valueOf(request.getParameter("restock_date")));
//            int rowsAffected = pstmt.executeUpdate();
//            logger.info("進貨主表插入完成，影響 " + rowsAffected + " 行");
//        }
//    }
private void insertRestockDetails(Session session, String restockId, HttpServletRequest request) {
    RestockDetailsBean restockDetails = new RestockDetailsBean();
    restockDetails.setRestockId(restockId);
    restockDetails.setProductId(request.getParameter("product_id"));
    restockDetails.setNumberOfRestock(Integer.parseInt(request.getParameter("number_of_restock")));
    restockDetails.setProductName(request.getParameter("product_name"));
    restockDetails.setProductPrice(Integer.parseInt(request.getParameter("product_price")));
    restockDetails.setProductionDate(LocalDate.parse(request.getParameter("production_date")));
    restockDetails.setRestockDate(LocalDate.parse(request.getParameter("restock_date")));
    restockDetails.setDueDate(LocalDate.parse(request.getParameter("due_date")));
    session.save(restockDetails);
    logger.info("進貨明細表插入完成，ID: " + restockId);
}
//    private void insertRestockDetails(Connection conn, String restockId, HttpServletRequest request) throws SQLException {
//        logger.info("插入進貨明細表數據");
//        String sql = "INSERT INTO restock_details (restock_id, product_id, number_of_restock, product_name, product_original_price, restock_price, production_date, due_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, restockId);
//            String productId = request.getParameter("product_id");
//            pstmt.setString(2, productId);
//            pstmt.setInt(3, Integer.parseInt(request.getParameter("number_of_restock")));
//
//            // 獲取正確的產品名稱
//            String productName = getProductNameById(conn, productId);
//            pstmt.setString(4, productName);
//
//            pstmt.setBigDecimal(5, new BigDecimal(request.getParameter("product_price")));
//            BigDecimal restockPrice = new BigDecimal(request.getParameter("product_price"))
//                                        .multiply(new BigDecimal(request.getParameter("number_of_restock")));
//            pstmt.setBigDecimal(6, restockPrice);
//            pstmt.setDate(7, Date.valueOf(request.getParameter("production_date")));
//            pstmt.setDate(8, Date.valueOf(request.getParameter("due_date")));
//            int rowsAffected = pstmt.executeUpdate();
//            logger.info("進貨明細表插入完成，影響 " + rowsAffected + " 行");
//        }
//    }


    public List<RestockDetailViewBean> getAllRestockDetails() {
        logger.info("開始獲取所有進貨明細");
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT new com.MarketMaster.bean.restock.RestockDetailViewBean(" +
                    "r.restockId, r.employeeId, rd.product.productId, rd.product.productName, rd.product.productCategory, " +
                    "rd.numberOfRestock, rd.productPrice, " +
                    "r.restockDate, rd.productionDate, rd.dueDate) " +
                    "FROM RestockDetailsBean rd " +
                    "JOIN rd.restock r " +
                    "ORDER BY r.restockId DESC";

            Query<RestockDetailViewBean> query = session.createQuery(hql, RestockDetailViewBean.class);
            List<RestockDetailViewBean> restockDetailsList = query.getResultList();

            logger.info("成功獲取 " + restockDetailsList.size() + " 條進貨明細記錄");
            if (!restockDetailsList.isEmpty()) {
                logger.info("第一條記錄: " + restockDetailsList.get(0));
            } else {
                logger.warning("沒有找到任何進貨明細記錄");
            }
            return restockDetailsList;
        } catch (Exception e) {
            logger.severe("獲取進貨明細時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


//    public List<RestockDetailViewBean> getAllRestockDetails() throws SQLException, ClassNotFoundException {
//        logger.info("開始獲取所有進貨明細");
//        List<RestockDetailViewBean> restockDetailsList = new ArrayList<>();
//        String sql = "SELECT " +
//                     "    r.restock_id, " +
//                     "    r.employee_id, " +
//                     "    rd.product_id, " +
//                     "    p.product_name, " +
//                     "    p.product_category, " +
//                     "    rd.number_of_restock, " +
//                     "    rd.restock_price, " +
//                     "    r.restock_date, " +
//                     "    rd.production_date, " +
//                     "    rd.due_date " +
//                     "FROM " +
//                     "    restock_details rd " +
//                     "INNER JOIN " +
//                     "    restocks r ON rd.restock_id = r.restock_id " +
//                     "INNER JOIN " +
//                     "    products p ON rd.product_id = p.product_id " +
//                     "ORDER BY " +
//                     "    r.restock_date";
//
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//            while (rs.next()) {
//            	RestockDetailViewBean detail = new RestockDetailViewBean();
//                detail.setRestockId(rs.getString("restock_id"));
//                detail.setEmployeeId(rs.getString("employee_id"));
//                detail.setProductId(rs.getString("product_id"));
//                detail.setProductName(rs.getString("product_name"));
//                detail.setProductCategory(rs.getString("product_category"));
//                detail.setNumberOfRestock(rs.getInt("number_of_restock"));
//                detail.setRestockPrice(rs.getBigDecimal("restock_price"));
//                detail.setRestockDate(rs.getDate("restock_date"));
//                detail.setProductionDate(rs.getDate("production_date"));
//                detail.setDueDate(rs.getDate("due_date"));
//
//                restockDetailsList.add(detail);
//
//            }
//        }
//        logger.info("成功獲取 " + restockDetailsList.size() + " 條進貨明細記錄");
//            return restockDetailsList;
//    }
}