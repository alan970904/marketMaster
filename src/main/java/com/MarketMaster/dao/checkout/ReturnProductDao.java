package com.MarketMaster.dao.checkout;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.ReturnProductBean;
import com.MarketMaster.util.HibernateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnProductDao {
    private static final Logger logger = Logger.getLogger(ReturnProductDao.class.getName());
    private SessionFactory sessionFactory;

    public ReturnProductDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public ReturnProductBean getOne(String returnId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(ReturnProductBean.class, returnId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取退貨記錄失敗", e);
            return null;
        }
    }

    public List<ReturnProductBean> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ReturnProductBean", ReturnProductBean.class).list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取所有退貨記錄失敗", e);
            return new ArrayList<>();
        }
    }

    public void insert(ReturnProductBean returnProduct) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(returnProduct);
            transaction.commit();
            logger.info("退貨記錄插入成功: " + returnProduct.getReturnId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "插入退貨記錄失敗", e);
            throw new RuntimeException("插入退貨記錄失敗", e);
        }
    }

    public void delete(String returnId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ReturnProductBean returnProduct = session.get(ReturnProductBean.class, returnId);
            if (returnProduct != null) {
                session.delete(returnProduct);
            }
            transaction.commit();
            logger.info("退貨記錄刪除成功: " + returnId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "刪除退貨記錄失敗", e);
        }
    }

    public void update(ReturnProductBean returnProduct) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(returnProduct);
            transaction.commit();
            logger.info("退貨記錄更新成功: " + returnProduct.getReturnId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "更新退貨記錄失敗", e);
        }
    }

    public void updateTotalPrice(String returnId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "UPDATE ReturnProductBean r SET r.returnTotalPrice = (SELECT SUM(rd.returnPrice) FROM ReturnDetailsBean rd WHERE rd.returnId = :returnId)";
            Query query = session.createQuery(hql);
            query.setParameter("returnId", returnId);
            query.executeUpdate();
            transaction.commit();
            logger.info("退貨總價更新成功: " + returnId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "更新退貨總價失敗", e);
        }
    }

    public List<Map<String, Object>> getDailyReturnsReport() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT r.returnDate as date, SUM(r.returnTotalPrice) as totalReturns FROM ReturnProductBean r GROUP BY r.returnDate";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();
            List<Map<String, Object>> report = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", row[0]);
                map.put("total_returns", row[1]);
                report.add(map);
            }
            return report;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取每日退貨報告失敗", e);
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getReturnSummary() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT rp.returnDate, rp.employeeId, rp.returnId, rd.productId, rd.numberOfReturn, rd.returnPrice, rp.returnTotalPrice, rd.reasonForReturn " +
                         "FROM ReturnProductBean rp JOIN rp.returnDetails rd";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();
            List<Map<String, Object>> summary = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("return_date", row[0]);
                map.put("employee_id", row[1]);
                map.put("return_id", row[2]);
                map.put("product_id", row[3]);
                map.put("number_of_return", row[4]);
                map.put("return_price", row[5]);
                map.put("return_total_price", row[6]);
                map.put("reason_for_return", row[7]);
                summary.add(map);
            }
            return summary;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取退貨總表失敗", e);
            return new ArrayList<>();
        }
    }
}