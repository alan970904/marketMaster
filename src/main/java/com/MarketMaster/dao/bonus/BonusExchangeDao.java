//package com.MarketMaster.dao.bonus;
//
//import java.util.List;
//import java.util.logging.Logger;
//import java.util.logging.Level;
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//
//import com.MarketMaster.bean.bonus.BonusExchangeBean;
//import com.MarketMaster.bean.bonus.PointsHistoryBean;
//import com.MarketMaster.bean.product.ProductBean;
//import com.MarketMaster.util.HibernateUtil;
//import com.MarketMaster.exception.DataAccessException;
//
//public class BonusExchangeDao {
//    private static final Logger logger = Logger.getLogger(BonusExchangeDao.class.getName());
//
//    public List<ProductBean> findExchangeableProducts(int customerPoints) throws DataAccessException {
//        logger.info("查詢可兌換商品，客戶積分：" + customerPoints);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            String hql = "FROM ProductBean p WHERE p.productPrice <= :customerPoints AND p.numberOfInventory > 0";
//            List<ProductBean> products = session.createQuery(hql, ProductBean.class)
//                .setParameter("customerPoints", customerPoints)
//                .list();
//            logger.info("成功找到 " + products.size() + " 個可兌換商品");
//            return products;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "查詢可兌換商品時發生錯誤", e);
//            throw new DataAccessException("查詢可兌換商品失敗: " + e.getMessage(), e);
//        }
//    }
//
//    public String getLastExchangeId() {
//        logger.info("獲取最後一個兌換ID");
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            String hql = "SELECT b.exchangeId FROM BonusExchangeBean b ORDER BY b.exchangeId DESC";
//            Query<String> query = session.createQuery(hql, String.class);
//            query.setMaxResults(1);
//            List<String> results = query.getResultList();
//            if (!results.isEmpty()) {
//                String lastId = results.get(0);
//                logger.info("找到最後的兌換ID: " + lastId);
//                return lastId;
//            } else {
//                logger.info("沒有找到兌換ID，返回null");
//                return null;
//            }
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "獲取最後兌換ID時發生錯誤", e);
//            return null;
//        }
//    }
//
//    public void saveExchange(BonusExchangeBean exchange) throws DataAccessException {
//        logger.info("保存兌換記錄: " + exchange.getExchangeId());
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.save(exchange);
//            logger.info("兌換記錄保存成功");
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "保存兌換記錄時發生錯誤", e);
//            throw new DataAccessException("保存兌換記錄失敗: " + e.getMessage(), e);
//        }
//    }
//
//    public void updateProductInventory(String productId, int quantity) throws DataAccessException {
//        logger.info("更新商品庫存，商品ID: " + productId + ", 數量變化: " + quantity);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            ProductBean product = session.get(ProductBean.class, productId);
//            if (product != null) {
//                product.setNumberOfInventory(product.getNumberOfInventory() - quantity);
//                product.setNumberOfExchange(product.getNumberOfExchange() + quantity);
//                session.update(product);
//                logger.info("商品庫存更新成功");
//            } else {
//                throw new DataAccessException("未找到商品: " + productId);
//            }
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "更新商品庫存時發生錯誤", e);
//            throw new DataAccessException("更新商品庫存失敗: " + e.getMessage(), e);
//        }
//    }
//
//    public void savePointsHistory(PointsHistoryBean pointsHistory) throws DataAccessException {
//        logger.info("保存積分歷史記錄");
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.save(pointsHistory);
//            logger.info("積分歷史記錄保存成功");
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "保存積分歷史記錄時發生錯誤", e);
//            throw new DataAccessException("保存積分歷史記錄失敗: " + e.getMessage(), e);
//        }
//    }
//
//    public List<BonusExchangeBean> getExchangeRecords(String customerTel) throws DataAccessException {
//        logger.info("獲取客戶兌換記錄，客戶電話: " + customerTel);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            String hql = "FROM BonusExchangeBean b WHERE b.customerTel = :customerTel";
//            List<BonusExchangeBean> records = session.createQuery(hql, BonusExchangeBean.class)
//                .setParameter("customerTel", customerTel)
//                .list();
//            logger.info("成功找到 " + records.size() + " 條兌換記錄");
//            return records;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "獲取兌換記錄時發生錯誤", e);
//            throw new DataAccessException("獲取兌換記錄失敗: " + e.getMessage(), e);
//        }
//    }
//
//    // 可以根據需要添加更多的方法
//}
