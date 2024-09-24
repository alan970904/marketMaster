package com.MarketMaster.dao.checkout;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.ReturnProductBean;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.util.HibernateUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.logging.Level;

public class ReturnProductDao {
    private static final Logger logger = Logger.getLogger(ReturnProductDao.class.getName());
    private SessionFactory sessionFactory;

    public ReturnProductDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // 獲取單個退貨記錄
    public ReturnProductBean getOne(String returnId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(ReturnProductBean.class, returnId);
        } catch (Exception e) {
            logger.severe("獲取退貨記錄失敗: " + e.getMessage());
            return null;
        }
    }

    // 獲取所有退貨記錄
    public List<ReturnProductBean> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ReturnProductBean", ReturnProductBean.class).list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取所有退貨記錄失敗", e);
            return new ArrayList<>();
        }
    }

    // 插入新的退貨記錄
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
            logger.severe("插入退貨記錄失敗: " + e.getMessage());
            throw new RuntimeException("插入退貨記錄失敗", e);
        }
    }

    // 刪除退貨記錄
    public void delete(String returnId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ReturnProductBean returnProduct = session.get(ReturnProductBean.class, returnId);
            if (returnProduct != null) {
                session.delete(returnProduct);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("刪除退貨記錄失敗: " + e.getMessage());
        }
    }

    // 更新退貨記錄
    public boolean update(ReturnProductBean returnProduct) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(returnProduct);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("更新退貨記錄失敗: " + e.getMessage());
            return false;
        }
    }

    // 根據員工ID搜索退貨記錄
    public List<ReturnProductBean> searchByEmployeeId(String employeeId) {
        try (Session session = sessionFactory.openSession()) {
            Query<ReturnProductBean> query = session.createQuery("from ReturnProductBean where employeeId like :employeeId", ReturnProductBean.class);
            query.setParameter("employeeId", "%" + employeeId + "%");
            return query.list();
        } catch (Exception e) {
            logger.severe("搜索退貨記錄失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 更新總價
    public void updateTotalPrice(String returnId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ReturnProductBean returnProduct = session.get(ReturnProductBean.class, returnId);
            if (returnProduct != null) {
                int totalAmount = calculateReturnTotal(session, returnId);
                returnProduct.setReturnTotalPrice(totalAmount);
                session.update(returnProduct);
            }
            transaction.commit();
            logger.info("退貨總金額更新成功");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("更新退貨總金額失敗: " + e.getMessage());
        }
    }

    // 獲取每日退貨報告
    public List<Map<String, Object>> getDailyReturnsReport() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select r.returnDate as returnDate, sum(r.returnTotalPrice) as totalReturns " +
                         "from ReturnProductBean r group by r.returnDate";
            return session.createQuery(hql, Object[].class)
                          .getResultList()
                          .stream()
                          .map(array -> {
                              Map<String, Object> map = new HashMap<>();
                              map.put("returnDate", array[0]);
                              map.put("totalReturns", array[1]);
                              return map;
                          })
                          .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("獲取每日退貨報告失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 獲取退貨總表
    public List<Map<String, Object>> getReturnSummary() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select r.returnDate, r.employeeId, r.returnId, rd.productId, rd.numberOfReturn, " +
                         "rd.returnPrice, r.returnTotalPrice " +
                         "from ReturnProductBean r join r.returnDetails rd";
            
            return session.createQuery(hql, Object[].class)
                          .getResultList()
                          .stream()
                          .map(array -> {
                              Map<String, Object> map = new HashMap<>();
                              map.put("returnDate", array[0]);
                              map.put("employeeId", array[1]);
                              map.put("returnId", array[2]);
                              map.put("productId", array[3]);
                              map.put("numberOfReturn", array[4]);
                              map.put("returnPrice", array[5]);
                              map.put("returnTotalPrice", array[6]);
                              return map;
                          })
                          .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("獲取退貨總表失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 獲取最新的退貨ID
    public String getLastReturnId() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select r.returnId from ReturnProductBean r order by r.returnId desc";
            List<String> results = session.createQuery(hql, String.class).setMaxResults(1).list();
            return results.isEmpty() ? "R00000000" : results.get(0);
        } catch (Exception e) {
            logger.severe("獲取最新退貨ID失敗: " + e.getMessage());
            return "R00000000";
        }
    }

    // 插入退貨記錄和明細
    public boolean insertReturnWithDetails(ReturnProductBean returnProduct, List<ReturnDetailsBean> details) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(returnProduct);
            for (ReturnDetailsBean detail : details) {
                detail.setReturnId(returnProduct.getReturnId());
                session.save(detail);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("插入退貨記錄和明細失敗: " + e.getMessage());
            return false;
        }
    }

    // 刪除退貨記錄和相關的退貨明細
    public void deleteReturnAndDetails(String returnId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ReturnProductBean returnProduct = session.get(ReturnProductBean.class, returnId);
            if (returnProduct != null) {
                session.delete(returnProduct); // 這將級聯刪除相關的退貨明細
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("刪除退貨記錄及其相關明細失敗: " + e.getMessage());
        }
    }

    private int calculateReturnTotal(Session session, String returnId) {
        Query<Integer> query = session.createQuery(
            "SELECT COALESCE(CAST(SUM(rd.returnPrice) AS int), 0) " +
            "FROM ReturnDetailsBean rd WHERE rd.returnId = :returnId", 
            Integer.class
        );
        query.setParameter("returnId", returnId);
        return query.uniqueResult();
    }
}