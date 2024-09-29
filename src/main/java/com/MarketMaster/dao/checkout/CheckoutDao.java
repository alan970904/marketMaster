package com.MarketMaster.dao.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.MarketMaster.bean.checkout.CheckoutBean;
import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;

@Repository
public class CheckoutDao {
	
	 	@Autowired
	    private SessionFactory sessionFactory;

	    private Session getSession() {
	        return sessionFactory.getCurrentSession();
	    }


    // 獲取單個結帳記錄
	    public CheckoutBean getOne(String checkoutId) throws DataAccessException {
	        try {
	            return getSession().get(CheckoutBean.class, checkoutId);
	        } catch (Exception e) {
	            throw new DataAccessException("獲取結帳記錄失敗", e);
	        }
	    }

    // 獲取所有結帳記錄
	    public List<CheckoutBean> getAll() throws DataAccessException {
	        try {
	            return getSession().createQuery("from CheckoutBean", CheckoutBean.class).list();
	        } catch (Exception e) {
	            throw new DataAccessException("獲取所有結帳記錄失敗", e);
	        }
	    }

    // 插入新的結帳記錄
	    public void insert(CheckoutBean checkout) throws DataAccessException {
	        try {
	            getSession().save(checkout);
	        } catch (Exception e) {
	            throw new DataAccessException("插入結帳記錄失敗", e);
	        }
	    }

    // 刪除結帳記錄
	    public void delete(String checkoutId) throws DataAccessException {
	        try {
	            CheckoutBean checkout = getSession().get(CheckoutBean.class, checkoutId);
	            if (checkout != null) {
	                getSession().delete(checkout);
	            }
	        } catch (Exception e) {
	            throw new DataAccessException("刪除結帳記錄失敗", e);
	        }
	    }


    // 更新結帳記錄
	    public boolean update(CheckoutBean checkout) throws DataAccessException {
	        try {
	            getSession().update(checkout);
	            return true;
	        } catch (Exception e) {
	            throw new DataAccessException("更新結帳記錄失敗", e);
	        }
	    }


    // 根據電話號碼搜索結帳記錄
	    public List<CheckoutBean> searchByTel(String customerTel) throws DataAccessException {
	        try {
	            Query<CheckoutBean> query = getSession().createQuery("from CheckoutBean where customerTel like :tel", CheckoutBean.class);
	            query.setParameter("tel", "%" + customerTel + "%");
	            return query.list();
	        } catch (Exception e) {
	            throw new DataAccessException("搜索結帳記錄失敗", e);
	        }
	    }

	    public void updateTotalPrice(String checkoutId) throws DataAccessException {
	        try {
	            String hql = "SELECT SUM(cd.checkoutPrice) FROM CheckoutDetailsBean cd WHERE cd.id.checkoutId = :checkoutId";
	            Query<Long> query = getSession().createQuery(hql, Long.class);
	            query.setParameter("checkoutId", checkoutId);
	            Long totalPrice = query.uniqueResult();

	            CheckoutBean checkout = getSession().get(CheckoutBean.class, checkoutId);
	            if (checkout != null && totalPrice != null) {
	                checkout.setCheckoutTotalPrice(totalPrice.intValue());
	                getSession().update(checkout);
	            }
	        } catch (Exception e) {
	            throw new DataAccessException("更新結帳總價失敗", e);
	        }
	    }

    // 獲取每日銷售報告
	    public List<Map<String, Object>> getDailySalesReport() throws DataAccessException {
	        try {
	            String hql = "select c.checkoutDate as checkoutDate, sum(c.checkoutTotalPrice) as totalSales " +
	                         "from CheckoutBean c group by c.checkoutDate";
	            return getSession().createQuery(hql, Object[].class)
	                          .getResultList()
	                          .stream()
	                          .map(array -> {
	                              Map<String, Object> map = new HashMap<>();
	                              map.put("checkoutDate", array[0]);
	                              map.put("totalSales", array[1]);
	                              return map;
	                          })
	                          .collect(java.util.stream.Collectors.toList());
	        } catch (Exception e) {
	            throw new DataAccessException("獲取每日銷售報告失敗", e);
	        }
	    }

    // 獲取結帳總表
	    public List<Map<String, Object>> getCheckoutSummary() throws DataAccessException {
	        try {
	            String hql = "select c.checkoutDate, c.employeeId, c.checkoutId, cd.productId, cd.numberOfCheckout, " +
	                         "cd.checkoutPrice, c.checkoutTotalPrice " +
	                         "from CheckoutBean c join c.checkoutDetails cd";
	            
	            return getSession().createQuery(hql, Object[].class)
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
	                          .collect(java.util.stream.Collectors.toList());
	        } catch (Exception e) {
	            throw new DataAccessException("獲取結帳總表失敗", e);
	        }
	    }

    // 獲取最新的結帳ID
	    public String getLastCheckoutId() throws DataAccessException {
	        try {
	            String hql = "select c.checkoutId from CheckoutBean c order by c.checkoutId desc";
	            List<String> results = getSession().createQuery(hql, String.class).setMaxResults(1).list();
	            return results.isEmpty() ? "C00000000" : results.get(0);
	        } catch (Exception e) {
	            throw new DataAccessException("獲取最新結帳ID失敗", e);
	        }
	    }

    // 獲取所有員工ID
	    public List<EmpBean> getAllEmployees() throws DataAccessException {
	        try {
	            return getSession().createQuery("from EmpBean", EmpBean.class).list();
	        } catch (Exception e) {
	            throw new DataAccessException("獲取所有員工ID失敗", e);
	        }
	    }


    // 根據類別獲取所有產品名稱 & ID & 價錢
	    public List<ProductBean> getProductNamesByCategory(String category) throws DataAccessException {
	        try {
	            String hql = "from ProductBean where productCategory = :category";
	            return getSession().createQuery(hql, ProductBean.class)
	                          .setParameter("category", category)
	                          .list();
	        } catch (Exception e) {
	            throw new DataAccessException("獲取產品列表失敗", e);
	        }
	    }


    // 插入結帳記錄和明細
	    public boolean insertCheckoutWithDetails(CheckoutBean checkout, List<CheckoutDetailsBean> details) throws DataAccessException {
	        try {
	            getSession().save(checkout);
	            for (CheckoutDetailsBean detail : details) {
	                detail.setCheckoutId(checkout.getCheckoutId());
	                getSession().save(detail);
	            }
	            return true;
	        } catch (Exception e) {
	            throw new DataAccessException("插入結帳記錄和明細失敗", e);
	        }
	    }

    // 刪除結帳記錄和相關的結帳明細
	    public void deleteCheckoutAndDetails(String checkoutId) throws DataAccessException {
	        try {
	            CheckoutBean checkout = getSession().get(CheckoutBean.class, checkoutId);
	            if (checkout != null) {
	                getSession().delete(checkout); // 這將級聯刪除相關的結帳明細
	            }
	        } catch (Exception e) {
	            throw new DataAccessException("刪除結帳記錄及其相關明細失敗", e);
	        }
	    }

    // 更新總金額和紅利點數
	    public void updateTotalAndBonus(String checkoutId, BigDecimal totalAmount, int bonusPoints) throws DataAccessException {
	        try {
	            CheckoutBean checkout = getSession().get(CheckoutBean.class, checkoutId);
	            if (checkout != null) {
	                checkout.setCheckoutTotalPrice(totalAmount.intValue());
	                checkout.setBonusPoints(bonusPoints);
	                getSession().update(checkout);
	            }
	        } catch (Exception e) {
	            throw new DataAccessException("更新總金額和紅利點數失敗", e);
	        }
	    }
}