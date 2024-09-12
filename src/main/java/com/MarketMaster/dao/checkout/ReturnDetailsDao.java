package com.MarketMaster.dao.checkout;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.ReturnProductBean;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnDetailsDao {
    private static final Logger logger = Logger.getLogger(ReturnDetailsDao.class.getName());

    // 獲取單一退貨明細
    public ReturnDetailsBean getOne(Session session, String returnId, String checkoutId, String productId) {
        try {
            String hql = "FROM ReturnDetailsBean WHERE returnId = :returnId AND checkoutId = :checkoutId AND productId = :productId";
            Query<ReturnDetailsBean> query = session.createQuery(hql, ReturnDetailsBean.class);
            query.setParameter("returnId", returnId);
            query.setParameter("checkoutId", checkoutId);
            query.setParameter("productId", productId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取單一退貨明細時發生錯誤", e);
            return null;
        }
    }

    // 獲取特定退貨ID的所有明細
    public List<ReturnDetailsBean> getPart(Session session, String returnId) {
        try {
            Query<ReturnDetailsBean> query = session.createQuery("FROM ReturnDetailsBean rd WHERE rd.returnId = :returnId", ReturnDetailsBean.class);
            query.setParameter("returnId", returnId);
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取特定退貨ID的所有明細時發生錯誤", e);
            return new ArrayList<>();
        }
    }

    // 獲取所有退貨明細
    public List<ReturnDetailsBean> getAll(Session session) {
        try {
            return session.createQuery("FROM ReturnDetailsBean", ReturnDetailsBean.class).list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取所有退貨明細時發生錯誤", e);
            return new ArrayList<>();
        }
    }

    // 插入新的退貨明細
    public void insert(Session session, ReturnDetailsBean detail) {
        try {
            session.save(detail);
            logger.info("退貨明細插入成功: " + detail.getReturnId() + ", " + detail.getProductId());
        } catch (Exception e) {
            logger.severe("插入退貨明細失敗: " + e.getMessage());
            throw new RuntimeException("插入退貨明細失敗", e);
        }
    }

    // 刪除退貨明細
    public void delete(Session session, String returnId, String checkoutId, String productId) {
        try {
            ReturnDetailsBean rd = getOne(session, returnId, checkoutId, productId);
            if (rd != null) {
                session.delete(rd);
                updateReturnTotal(session, returnId);
            }
            logger.info("退貨明細刪除成功，總金額已更新");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除退貨明細時發生錯誤", e);
            throw new RuntimeException("刪除退貨明細失敗", e);
        }
    }


    // 更新退貨明細
    public void update(Session session, ReturnDetailsBean rd) {
        try {
            session.update(rd);
            updateReturnTotal(session, rd.getReturnId());
            logger.info("退貨明細更新成功，總金額已更新");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "更新退貨明細時發生錯誤", e);
            throw new RuntimeException("更新退貨明細失敗", e);
        }
    }

    // 搜尋特定產品ID的退貨明細
    public List<ReturnDetailsBean> searchByProductId(Session session, String productId) {
        try {
            Query<ReturnDetailsBean> query = session.createQuery("FROM ReturnDetailsBean rd WHERE rd.productId LIKE :productId", ReturnDetailsBean.class);
            query.setParameter("productId", "%" + productId + "%");
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "搜尋特定產品ID的退貨明細時發生錯誤", e);
            return new ArrayList<>();
        }
    }

 // 計算退貨總金額
    private int calculateReturnTotal(Session session, String returnId) {
        Query<Integer> query = session.createQuery(
            "SELECT COALESCE(CAST(SUM(rd.returnPrice) AS int), 0) " +
            "FROM ReturnDetailsBean rd WHERE rd.returnId = :returnId", 
            Integer.class
        );
        query.setParameter("returnId", returnId);
        return query.uniqueResult();
    }

    // 更新退貨總金額
    private void updateReturnTotal(Session session, String returnId) {
        int totalAmount = calculateReturnTotal(session, returnId);
        
        ReturnProductBean returnProduct = session.get(ReturnProductBean.class, returnId);
        if (returnProduct != null) {
            returnProduct.setReturnTotalPrice(totalAmount);
            session.update(returnProduct);
        }
        
        logger.info("更新退貨總金額成功");
    }

    // 獲取退貨與原始購買比較報告
    public List<Map<String, Object>> getReturnComparisonReport(Session session) {
        try {
            String hql = "SELECT rd.returnId, rd.checkoutId, rd.productId, rd.numberOfReturn, rd.returnPrice, " +
                         "cd.numberOfCheckout, cd.checkoutPrice, rd.reasonForReturn " +
                         "FROM ReturnDetailsBean rd JOIN CheckoutDetailsBean cd " +
                         "ON rd.checkoutId = cd.checkoutId AND rd.productId = cd.productId";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();
            List<Map<String, Object>> report = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("return_id", row[0]);
                map.put("checkout_id", row[1]);
                map.put("product_id", row[2]);
                map.put("number_of_return", row[3]);
                map.put("return_price", row[4]);
                map.put("number_of_checkout", row[5]);
                map.put("checkout_price", row[6]);
                map.put("reason_for_return", row[7]);
                report.add(map);
            }
            return report;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取退貨比較報告失敗", e);
            return new ArrayList<>();
        }
    }

    // 獲取特定時間範圍內的退貨統計
    public List<Map<String, Object>> getReturnStatistics(Session session, Date startDate, Date endDate) {
        try {
            String hql = "SELECT rd.productId, SUM(rd.numberOfReturn) as totalReturns, " +
                         "SUM(rd.returnPrice) as totalReturnAmount " +
                         "FROM ReturnDetailsBean rd " +
                         "JOIN ReturnProductBean rp ON rd.returnId = rp.returnId " +
                         "WHERE rp.returnDate BETWEEN :startDate AND :endDate " +
                         "GROUP BY rd.productId";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Object[]> results = query.list();
            List<Map<String, Object>> statistics = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("product_id", row[0]);
                map.put("total_returns", row[1]);
                map.put("total_return_amount", row[2]);
                statistics.add(map);
            }
            return statistics;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取退貨統計失敗", e);
            return new ArrayList<>();
        }
    }

    // 獲取最常見的退貨原因
    public List<Map<String, Object>> getMostCommonReturnReasons(Session session, int limit) {
        try {
            String hql = "SELECT rd.reasonForReturn, COUNT(rd) as count " +
                         "FROM ReturnDetailsBean rd " +
                         "GROUP BY rd.reasonForReturn " +
                         "ORDER BY count DESC";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setMaxResults(limit);
            List<Object[]> results = query.list();
            List<Map<String, Object>> reasons = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("reason", row[0]);
                map.put("count", row[1]);
                reasons.add(map);
            }
            return reasons;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取最常見退貨原因失敗", e);
            return new ArrayList<>();
        }
    }

    // 更新退貨後的庫存
    public void updateInventoryAfterReturn(Session session, String productId, int returnQuantity) {
        try {
            String hql = "UPDATE ProductBean p SET p.productQuantity = p.productQuantity + :returnQuantity " +
                         "WHERE p.productId = :productId";
            Query<?> query = session.createQuery(hql);
            query.setParameter("returnQuantity", returnQuantity);
            query.setParameter("productId", productId);
            int result = query.executeUpdate();
            if (result > 0) {
                logger.info("商品 " + productId + " 的庫存已更新，增加了 " + returnQuantity + " 個單位");
            } else {
                logger.warning("更新商品 " + productId + " 的庫存失敗");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "更新庫存時發生錯誤", e);
            throw new RuntimeException("更新庫存失敗", e);
        }
    }
}