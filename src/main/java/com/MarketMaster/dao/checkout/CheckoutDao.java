package com.MarketMaster.dao.checkout;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
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

public class CheckoutDao {
    private static final Logger logger = Logger.getLogger(CheckoutDao.class.getName());
    private SessionFactory sessionFactory;

    public CheckoutDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // 獲取單個結帳記錄
    public CheckoutBean getOne(String checkoutId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(CheckoutBean.class, checkoutId);
        } catch (Exception e) {
            logger.severe("獲取結帳記錄失敗: " + e.getMessage());
            return null;
        }
    }

    // 獲取所有結帳記錄
    public List<CheckoutBean> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from CheckoutBean", CheckoutBean.class).list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取所有結帳記錄失敗", e);
            return new ArrayList<>();
        }
    }

    // 插入新的結帳記錄
    public void insert(Session session, CheckoutBean checkout) {
        try {
            session.save(checkout);
            logger.info("結帳記錄插入成功: " + checkout.getCheckoutId());
        } catch (Exception e) {
            logger.severe("插入結帳記錄失敗: " + e.getMessage());
            throw new RuntimeException("插入結帳記錄失敗", e);
        }
    }

    // 刪除結帳記錄
    public void delete(String checkoutId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CheckoutBean checkout = session.get(CheckoutBean.class, checkoutId);
            if (checkout != null) {
                session.delete(checkout);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("刪除結帳記錄失敗: " + e.getMessage());
        }
    }

    // 更新結帳記錄
    public boolean update(CheckoutBean checkout) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(checkout);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("更新結帳記錄失敗: " + e.getMessage());
            return false;
        }
    }

    // 根據電話號碼搜索結帳記錄
    public List<CheckoutBean> searchByTel(String customerTel) {
        try (Session session = sessionFactory.openSession()) {
            Query<CheckoutBean> query = session.createQuery("from CheckoutBean where customerTel like :tel", CheckoutBean.class);
            query.setParameter("tel", "%" + customerTel + "%");
            return query.list();
        } catch (Exception e) {
            logger.severe("搜索結帳記錄失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void updateTotalPrice(String checkoutId) {
        logger.info("開始更新結帳ID為 " + checkoutId + " 的總價");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String hql = "SELECT SUM(cd.checkoutPrice) FROM CheckoutDetailsBean cd WHERE cd.id.checkoutId = :checkoutId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("checkoutId", checkoutId);
            Long totalPrice = query.uniqueResult();

            CheckoutBean checkout = session.get(CheckoutBean.class, checkoutId);
            if (checkout != null && totalPrice != null) {
                checkout.setCheckoutTotalPrice(totalPrice.intValue());
                session.update(checkout);
                logger.info("結帳總價更新成功");
            } else {
                logger.warning("未找到結帳記錄或計算總價為空");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "更新結帳總價時發生錯誤", e);
        }
    }

    // 獲取每日銷售報告
    public List<Map<String, Object>> getDailySalesReport() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select c.checkoutDate as checkoutDate, sum(c.checkoutTotalPrice) as totalSales " +
                         "from CheckoutBean c group by c.checkoutDate";
            return session.createQuery(hql, Object[].class)
                          .getResultList()
                          .stream()
                          .map(array -> {
                              Map<String, Object> map = new HashMap<>();
                              map.put("checkoutDate", array[0]);
                              map.put("totalSales", array[1]);
                              return map;
                          })
                          .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("獲取每日銷售報告失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 獲取結帳總表
    public List<Map<String, Object>> getCheckoutSummary() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select c.checkoutDate, c.employeeId, c.checkoutId, cd.productId, cd.numberOfCheckout, " +
                         "cd.checkoutPrice, c.checkoutTotalPrice " +
                         "from CheckoutBean c join c.checkoutDetails cd";
            
            return session.createQuery(hql, Object[].class)
                          .getResultList()
                          .stream()
                          .map(array -> {
                              Map<String, Object> map = new HashMap<>();
                              map.put("checkoutDate", array[0]);
                              map.put("employeeId", array[1]);
                              map.put("checkoutId", array[2]);
                              map.put("productId", array[3]);
                              map.put("numberOfCheckout", array[4]);
                              map.put("checkoutPrice", array[5]);
                              map.put("checkoutTotalPrice", array[6]);
                              return map;
                          })
                          .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("獲取結帳總表失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 獲取最新的結帳ID
    public String getLastCheckoutId() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select c.checkoutId from CheckoutBean c order by c.checkoutId desc";
            List<String> results = session.createQuery(hql, String.class).setMaxResults(1).list();
            return results.isEmpty() ? "C00000000" : results.get(0);
        } catch (Exception e) {
            logger.severe("獲取最新結帳ID失敗: " + e.getMessage());
            return "C00000000";
        }
    }

    // 獲取所有員工ID
    public List<EmpBean> getAllEmployees() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from EmpBean", EmpBean.class).list();
        } catch (Exception e) {
            logger.severe("獲取所有員工ID失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 根據類別獲取所有產品名稱 & ID & 價錢
    public List<ProductBean> getProductNamesByCategory(String category) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "from ProductBean where productCategory = :category";
            return session.createQuery(hql, ProductBean.class)
                          .setParameter("category", category)
                          .list();
        } catch (Exception e) {
            logger.severe("獲取產品列表失敗: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 插入結帳記錄和明細
    public boolean insertCheckoutWithDetails(CheckoutBean checkout, List<CheckoutDetailsBean> details) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(checkout);
            for (CheckoutDetailsBean detail : details) {
                detail.setCheckoutId(checkout.getCheckoutId());
                session.save(detail);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("插入結帳記錄和明細失敗: " + e.getMessage());
            return false;
        }
    }

    // 刪除結帳記錄和相關的結帳明細
    public void deleteCheckoutAndDetails(String checkoutId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CheckoutBean checkout = session.get(CheckoutBean.class, checkoutId);
            if (checkout != null) {
                session.delete(checkout); // 這將級聯刪除相關的結帳明細
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("刪除結帳記錄及其相關明細失敗: " + e.getMessage());
        }
    }

    // 更新總金額和紅利點數
    public void updateTotalAndBonus(String checkoutId, BigDecimal totalAmount, int bonusPoints) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CheckoutBean checkout = session.get(CheckoutBean.class, checkoutId);
            if (checkout != null) {
                checkout.setCheckoutTotalPrice(totalAmount.intValue());
                checkout.setBonusPoints(bonusPoints);
                session.update(checkout);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.severe("更新總金額和紅利點數失敗: " + e.getMessage());
        }
    }
}