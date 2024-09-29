//package com.MarketMaster.service.bonus;
//
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import com.MarketMaster.bean.product.ProductBean;
//import com.MarketMaster.dao.bonus.BonusExchangeDao;
//import com.MarketMaster.bean.employee.CustomerBean;
//import com.MarketMaster.bean.bonus.BonusExchangeBean;
//import com.MarketMaster.bean.bonus.PointsHistoryBean;
//import com.MarketMaster.exception.DataAccessException;
//import com.MarketMaster.util.HibernateUtil;
//
//public class BonusExchangeService {
//    
//    private BonusExchangeDao bonusExchangeDao;
//    private static final Logger logger = Logger.getLogger(BonusExchangeService.class.getName());
//    
//    public BonusExchangeService() {
//        this.bonusExchangeDao = new BonusExchangeDao();
//    }
//
//    public String generateNextExchangeId() {
//        logger.info("開始生成下一個兌換ID");
//        String lastId = bonusExchangeDao.getLastExchangeId();
//
//        if (lastId == null || !lastId.matches("H\\d{8}")) {
//            logger.info("無效的上一個ID或沒有現有ID，返回初始ID: H00000001");
//            return "H00000001";
//        }
//
//        try {
//            int nextNumber = Integer.parseInt(lastId.substring(1)) + 1;
//            String nextId = String.format("H%08d", nextNumber);
//            logger.info("成功生成下一個兌換ID: " + nextId);
//            return nextId;
//        } catch (NumberFormatException e) {
//            logger.log(Level.SEVERE, "解析上一個ID時發生錯誤", e);
//            return "H00000001";
//        }
//    }
//    
//    public List<ProductBean> getExchangeableProducts(String customerTel) throws DataAccessException {
//        logger.info("Starting getExchangeableProducts for customer: " + customerTel);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            CustomerBean customer = session.get(CustomerBean.class, customerTel);
//            if (customer == null) {
//                logger.warning("Customer not found with tel: " + customerTel);
//                throw new DataAccessException("Customer not found with tel: " + customerTel);
//            }
//            int customerPoints = customer.getTotalPoints();
//            logger.info("Customer " + customerTel + " has " + customerPoints + " points");
//            
//            String hql = "FROM ProductBean p WHERE p.productPrice <= :customerPoints AND p.numberOfInventory > 0";
//            List<ProductBean> products = session.createQuery(hql, ProductBean.class)
//                                                .setParameter("customerPoints", customerPoints)
//                                                .list();
//            logger.info("Found " + products.size() + " exchangeable products for customer " + customerTel);
//            return products;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error in getExchangeableProducts", e);
//            throw new DataAccessException("Error querying exchangeable products: " + e.getMessage(), e);
//        }
//    }
//
//    public void executeExchange(String customerTel, String productId, int exchangeQuantity) throws DataAccessException {
//        logger.info("Starting exchange for customer: " + customerTel + ", product: " + productId + ", quantity: " + exchangeQuantity);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            CustomerBean customer = session.get(CustomerBean.class, customerTel);
//            ProductBean product = session.get(ProductBean.class, productId);
//            
//            if (customer == null || product == null) {
//                throw new DataAccessException("Customer or Product not found");
//            }
//            
//            int requiredPoints = product.getProductPrice() * exchangeQuantity;
//            if (customer.getTotalPoints() < requiredPoints) {
//                throw new DataAccessException("Insufficient points for exchange");
//            }
//            
//            if (product.getNumberOfInventory() < exchangeQuantity) {
//                throw new DataAccessException("Insufficient inventory");
//            }
//            
//         // Update customer points
//            customer.setTotalPoints(customer.getTotalPoints() - requiredPoints);
//            
//            // Update product inventory
//            product.setNumberOfInventory(product.getNumberOfInventory() - exchangeQuantity);
//            product.setNumberOfExchange(product.getNumberOfExchange() + exchangeQuantity);
//            
//            // Create exchange record
//            BonusExchangeBean exchange = new BonusExchangeBean();
//            exchange.setExchangeId(generateNextExchangeId());
//            exchange.setCustomer(customer);
//            exchange.setProduct(product);
//            exchange.setCustomerTel(customerTel);
//            exchange.setProductId(productId);
//            exchange.setUsePoints(requiredPoints);
//            exchange.setNumberOfExchange(exchangeQuantity);
//            exchange.setExchangeDate(new Date());
//            
//            // Create points history record
//            PointsHistoryBean pointsHistory = new PointsHistoryBean();
//            pointsHistory.setCustomerTel(customerTel);
//            pointsHistory.setExchangeId(exchange.getExchangeId());
//            pointsHistory.setPointsChange(-requiredPoints);
//            pointsHistory.setTransactionDate(new Date());
//            pointsHistory.setTransactionType("use");
//            
//            session.save(exchange);
//            session.update(customer);
//            session.update(product);
//            session.save(pointsHistory);
//            
//            logger.info("Exchange completed successfully");
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error in executeExchange", e);
//            throw new DataAccessException("Error executing exchange: " + e.getMessage(), e);
//        }
//    }
//
//    public List<BonusExchangeBean> getExchangeRecords(String customerTel) throws DataAccessException {
//        logger.info("Getting exchange records for customer: " + customerTel);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            String hql = "FROM BonusExchangeBean b WHERE b.customerTel = :customerTel";
//            List<BonusExchangeBean> records = session.createQuery(hql, BonusExchangeBean.class)
//                                                     .setParameter("customerTel", customerTel)
//                                                     .list();
//            logger.info("Found " + records.size() + " exchange records for customer " + customerTel);
//            return records;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error getting exchange records", e);
//            throw new DataAccessException("Error getting exchange records: " + e.getMessage(), e);
//        }
//    }
//    
//    public void updateExchange(String exchangeId, int newPoints) throws DataAccessException {
//        logger.info("Starting updateExchange for exchange: " + exchangeId + ", new points: " + newPoints);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            BonusExchangeBean exchange = session.get(BonusExchangeBean.class, exchangeId);
//            if (exchange == null) {
//                logger.warning("Exchange record not found: " + exchangeId);
//                throw new DataAccessException("Exchange record not found");
//            }
//            
//            CustomerBean customer = session.get(CustomerBean.class, exchange.getCustomerTel());
//            ProductBean product = session.get(ProductBean.class, exchange.getProductId());
//            
//            int oldPoints = exchange.getUsePoints();
//            int pointsDifference = newPoints - oldPoints;
//            
//            if (customer.getTotalPoints() < pointsDifference) {
//                logger.warning("Insufficient points for updated exchange. Required: " + pointsDifference + ", Available: " + customer.getTotalPoints());
//                throw new DataAccessException("Insufficient points for updated exchange");
//            }
//            
//            int newExchangeQuantity = newPoints / product.getProductPrice();
//            int inventoryDifference = newExchangeQuantity - exchange.getNumberOfExchange();
//            if (product.getNumberOfInventory() < inventoryDifference) {
//                logger.warning("Insufficient inventory for updated exchange. Required: " + inventoryDifference + ", Available: " + product.getNumberOfInventory());
//                throw new DataAccessException("Insufficient inventory for updated exchange");
//            }
//            
//            // Update exchange record
//            exchange.setUsePoints(newPoints);
//            exchange.setNumberOfExchange(newExchangeQuantity);
//            
//            // Update customer points
//            customer.setTotalPoints(customer.getTotalPoints() - pointsDifference);
//            
//            // Update product inventory
//            product.setNumberOfInventory(product.getNumberOfInventory() - inventoryDifference);
//            product.setNumberOfExchange(product.getNumberOfExchange() + inventoryDifference);
//            
//            // Update points history record
//            PointsHistoryBean pointsHistory = session.createQuery("FROM PointsHistoryBean WHERE exchangeId = :exchangeId", PointsHistoryBean.class)
//                                                    .setParameter("exchangeId", exchangeId)
//                                                    .uniqueResult();
//            if (pointsHistory != null) {
//                pointsHistory.setPointsChange(-newPoints);
//                session.update(pointsHistory);
//            }
//            
//            session.update(exchange);
//            session.update(customer);
//            session.update(product);
//            
//            logger.info("Exchange updated successfully for exchange: " + exchangeId);
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error in updateExchange", e);
//            throw new DataAccessException("Error updating exchange: " + e.getMessage(), e);
//        }
//    }
//
//    public void deleteExchange(String exchangeId) throws DataAccessException {
//        logger.info("Starting deleteExchange for exchange: " + exchangeId);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            BonusExchangeBean exchange = session.get(BonusExchangeBean.class, exchangeId);
//            if (exchange == null) {
//                logger.warning("Exchange record not found: " + exchangeId);
//                throw new DataAccessException("Exchange record not found");
//            }
//            
//            CustomerBean customer = session.get(CustomerBean.class, exchange.getCustomerTel());
//            ProductBean product = session.get(ProductBean.class, exchange.getProductId());
//            
//            // Restore customer points
//            customer.setTotalPoints(customer.getTotalPoints() + exchange.getUsePoints());
//            
//            // Restore product inventory
//            product.setNumberOfInventory(product.getNumberOfInventory() + exchange.getNumberOfExchange());
//            product.setNumberOfExchange(product.getNumberOfExchange() - exchange.getNumberOfExchange());
//            
//            // Delete points history record
//            PointsHistoryBean pointsHistory = session.createQuery("FROM PointsHistoryBean WHERE exchangeId = :exchangeId", PointsHistoryBean.class)
//                                                    .setParameter("exchangeId", exchangeId)
//                                                    .uniqueResult();
//            if (pointsHistory != null) {
//                session.delete(pointsHistory);
//            }
//            
//            session.delete(exchange);
//            session.update(customer);
//            session.update(product);
//            
//            logger.info("Exchange deleted successfully for exchange: " + exchangeId);
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error in deleteExchange", e);
//            throw new DataAccessException("Error deleting exchange: " + e.getMessage(), e);
//        }
//    }
//}