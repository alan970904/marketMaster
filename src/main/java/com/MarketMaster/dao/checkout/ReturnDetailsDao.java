package com.MarketMaster.dao.checkout;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.util.HibernateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnDetailsDao {
    private static final Logger logger = Logger.getLogger(ReturnDetailsDao.class.getName());
    private SessionFactory sessionFactory;

    public ReturnDetailsDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public ReturnDetailsBean getOne(String returnId, String checkoutId, String productId) {
        try (Session session = sessionFactory.openSession()) {
            ReturnDetailsBean.ReturnDetailsId id = new ReturnDetailsBean.ReturnDetailsId(returnId, checkoutId, productId);
            return session.get(ReturnDetailsBean.class, id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取退貨明細失敗", e);
            return null;
        }
    }

    public List<ReturnDetailsBean> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ReturnDetailsBean", ReturnDetailsBean.class).list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取所有退貨明細失敗", e);
            return new ArrayList<>();
        }
    }

    public void insert(ReturnDetailsBean returnDetails) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(returnDetails);
            transaction.commit();
            logger.info("退貨明細插入成功: " + returnDetails.getReturnId() + ", " + returnDetails.getProductId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "插入退貨明細失敗", e);
            throw new RuntimeException("插入退貨明細失敗", e);
        }
    }

    public void delete(String returnId, String checkoutId, String productId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ReturnDetailsBean.ReturnDetailsId id = new ReturnDetailsBean.ReturnDetailsId(returnId, checkoutId, productId);
            ReturnDetailsBean returnDetails = session.get(ReturnDetailsBean.class, id);
            if (returnDetails != null) {
                session.delete(returnDetails);
            }
            transaction.commit();
            logger.info("退貨明細刪除成功: " + returnId + ", " + checkoutId + ", " + productId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "刪除退貨明細失敗", e);
        }
    }

    public void update(ReturnDetailsBean returnDetails) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(returnDetails);
            transaction.commit();
            logger.info("退貨明細更新成功: " + returnDetails.getReturnId() + ", " + returnDetails.getProductId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "更新退貨明細失敗", e);
        }
    }

    public List<ReturnDetailsBean> searchByProductId(String productId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM ReturnDetailsBean r WHERE r.productId LIKE :productId";
            Query<ReturnDetailsBean> query = session.createQuery(hql, ReturnDetailsBean.class);
            query.setParameter("productId", "%" + productId + "%");
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "搜索退貨明細失敗", e);
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getReturnComparisonReport() {
        try (Session session = sessionFactory.openSession()) {
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
}