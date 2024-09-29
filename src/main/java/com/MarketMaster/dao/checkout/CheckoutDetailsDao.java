package com.MarketMaster.dao.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Repository
public class CheckoutDetailsDao {
	
	@Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

	// 獲取單一結帳明細
    public CheckoutDetailsBean getOne(String checkoutId, String productId) throws DataAccessException {
        try {
            return getSession().get(CheckoutDetailsBean.class,
                    new CheckoutDetailsBean.CheckoutDetailsId(checkoutId, productId));
        } catch (Exception e) {
            throw new DataAccessException("獲取單一結帳明細失敗", e);
        }
    }

	// 獲取特定結帳ID的所有明細
    public List<CheckoutDetailsBean> getPart(String checkoutId) throws DataAccessException {
        try {
            Query<CheckoutDetailsBean> query = getSession().createQuery(
                    "FROM CheckoutDetailsBean cd WHERE cd.checkoutId = :checkoutId", CheckoutDetailsBean.class);
            query.setParameter("checkoutId", checkoutId);
            return query.list();
        } catch (Exception e) {
            throw new DataAccessException("獲取特定結帳ID的所有明細失敗", e);
        }
    }

	// 獲取所有結帳明細
    public List<CheckoutDetailsBean> getAll() throws DataAccessException {
        try {
            return getSession().createQuery("FROM CheckoutDetailsBean", CheckoutDetailsBean.class).list();
        } catch (Exception e) {
            throw new DataAccessException("獲取所有結帳明細失敗", e);
        }
    }

	// 插入新的結帳明細
    public void insert(CheckoutDetailsBean detail) throws DataAccessException {
        try {
            getSession().save(detail);
        } catch (Exception e) {
            throw new DataAccessException("插入結帳明細失敗", e);
        }
    }

	// 刪除結帳明細
    public void delete(String checkoutId, String productId) throws DataAccessException {
        try {
            CheckoutDetailsBean ckd = getSession().get(CheckoutDetailsBean.class,
                    new CheckoutDetailsBean.CheckoutDetailsId(checkoutId, productId));
            if (ckd != null) {
                getSession().delete(ckd);
                updateCheckoutTotalAndBonus(checkoutId);
            }
        } catch (Exception e) {
            throw new DataAccessException("刪除結帳明細失敗", e);
        }
    }

	// 更新結帳明細
    public void update(CheckoutDetailsBean ckd) throws DataAccessException {
        try {
            getSession().update(ckd);
            updateCheckoutTotalAndBonus(ckd.getCheckoutId());
        } catch (Exception e) {
            throw new DataAccessException("更新結帳明細失敗", e);
        }
    }

	// 搜尋特定產品ID的結帳明細
    public List<CheckoutDetailsBean> searchByProductId(String productId) throws DataAccessException {
        try {
            Query<CheckoutDetailsBean> query = getSession().createQuery(
                    "FROM CheckoutDetailsBean cd WHERE cd.productId LIKE :productId", CheckoutDetailsBean.class);
            query.setParameter("productId", "%" + productId + "%");
            return query.list();
        } catch (Exception e) {
            throw new DataAccessException("搜尋特定產品ID的結帳明細失敗", e);
        }
    }

	// 更新退貨後的結帳明細
//    public void updateAfterReturn(String checkoutId, String productId, int returnQuantity, int returnPrice) {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            CheckoutDetailsBean ckd = session.get(CheckoutDetailsBean.class, new CheckoutDetailsBean.CheckoutDetailsId(checkoutId, productId));
//            if (ckd != null) {
//                ckd.setNumberOfCheckout(ckd.getNumberOfCheckout() - returnQuantity);
//                ckd.setCheckoutPrice(ckd.getCheckoutPrice() - returnPrice);
//                session.update(ckd);
//                updateCheckoutTotalAndBonus(session, checkoutId);
//            }
//            transaction.commit();
//            logger.info("退貨後結帳明細更新成功，總金額和紅利點數已更新");
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            logger.log(Level.SEVERE, "更新退貨後結帳明細時發生錯誤", e);
//            throw new RuntimeException("更新退貨後結帳明細失敗", e);
//        }
//    }

	// 取消退貨並更新結帳明細
//    public void cancelReturn(String checkoutId, String productId, int returnQuantity, int returnPrice) {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            CheckoutDetailsBean ckd = session.get(CheckoutDetailsBean.class, new CheckoutDetailsBean.CheckoutDetailsId(checkoutId, productId));
//            if (ckd != null) {
//                ckd.setNumberOfCheckout(ckd.getNumberOfCheckout() + returnQuantity);
//                ckd.setCheckoutPrice(ckd.getCheckoutPrice() + returnPrice);
//                session.update(ckd);
//                updateCheckoutTotalAndBonus(session, checkoutId);
//            }
//            transaction.commit();
//            logger.info("取消退貨並更新結帳明細成功，總金額和紅利點數已更新");
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            logger.log(Level.SEVERE, "取消退貨並更新結帳明細時發生錯誤", e);
//            throw new RuntimeException("取消退貨並更新結帳明細失敗", e);
//        }
//    }

	// 計算商品退貨率
    public List<Map<String, Object>> getProductReturnRates() throws DataAccessException {
        try {
            String hql = "SELECT cd.productId, CAST(ROUND((SUM(CASE WHEN rd.returnId IS NOT NULL THEN rd.numberOfReturn ELSE 0 END) * 100.0) / SUM(cd.numberOfCheckout), 2) AS java.math.BigDecimal) AS returnRate "
                    + "FROM CheckoutDetailsBean cd LEFT JOIN cd.returnDetails rd "
                    + "GROUP BY cd.productId";
            Query<Object[]> query = getSession().createQuery(hql, Object[].class);
            List<Object[]> results = query.list();
            List<Map<String, Object>> returnRates = new ArrayList<>();
            for (Object[] result : results) {
                Map<String, Object> row = new HashMap<>();
                row.put("product_id", result[0]);
                row.put("return_rate", result[1]);
                returnRates.add(row);
            }
            return returnRates;
        } catch (Exception e) {
            throw new DataAccessException("計算商品退貨率失敗", e);
        }
    }

	// 計算結帳總金額
//    public int calculateCheckoutTotal(String checkoutId) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<Integer> query = session.createQuery("SELECT SUM(cd.checkoutPrice) FROM CheckoutDetailsBean cd WHERE cd.checkoutId = :checkoutId", Integer.class);
//            query.setParameter("checkoutId", checkoutId);
//            Integer total = query.uniqueResult();
//            return total != null ? total : 0;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "計算結帳總金額時發生錯誤", e);
//            return 0;
//        }
//    }

	// 更新結帳總金額和紅利點數
    private void updateCheckoutTotalAndBonus(String checkoutId) throws DataAccessException {
        try {
            int totalAmount = calculateCheckoutTotal(checkoutId);
            int bonusPoints = calculateBonusPoints(totalAmount);

            CheckoutBean checkout = getSession().get(CheckoutBean.class, checkoutId);
            if (checkout != null) {
                checkout.setCheckoutTotalPrice(totalAmount);
                checkout.setBonusPoints(bonusPoints);
                getSession().update(checkout);
            }
        } catch (Exception e) {
            throw new DataAccessException("更新結帳總金額和紅利點數失敗", e);
        }
    }

	// 計算結帳總金額（內部方法）
    private int calculateCheckoutTotal(String checkoutId) throws DataAccessException {
        try {
            Query<Integer> query = getSession().createQuery(
                    "SELECT COALESCE(CAST(SUM(cd.checkoutPrice) AS int), 0) " +
                    "FROM CheckoutDetailsBean cd WHERE cd.checkoutId = :checkoutId", Integer.class);
            query.setParameter("checkoutId", checkoutId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DataAccessException("計算結帳總金額失敗", e);
        }
    }

	// 計算紅利點數
	private int calculateBonusPoints(int totalAmount) {
		// 假設每100元可得1點紅利
		return totalAmount / 100;
	}
}