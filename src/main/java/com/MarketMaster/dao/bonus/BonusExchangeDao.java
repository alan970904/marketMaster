package com.MarketMaster.dao.bonus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.MarketMaster.bean.bonus.BonusExchangeBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.exception.DataAccessException;

import java.util.List;

@Repository
public class BonusExchangeDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public BonusExchangeDao() {
        System.out.println("BonusExchangeDao 被创建，sessionFactory 是否为 null：" + (sessionFactory == null));
    }//檢查 sessionFactory 是否为 null
    
    
    public int getCustomerPoints(String customerTel) throws DataAccessException {
        try {
            String hql = "SELECT c.totalPoints FROM CustomerBean c WHERE c.customerTel = :customerTel";
            return getSession().createQuery(hql, Integer.class)
                .setParameter("customerTel", customerTel)
                .uniqueResult();
        } catch (Exception e) {
            throw new DataAccessException("獲取客戶積分失敗: " + e.getMessage(), e);
        }
    }

    public List<ProductBean> findExchangeableProducts(int customerPoints) throws DataAccessException {
        try {
            String hql = "FROM ProductBean p WHERE p.productPrice <= :customerPoints AND p.numberOfInventory > 0";
            return getSession().createQuery(hql, ProductBean.class)
                .setParameter("customerPoints", customerPoints)
                .list();
        } catch (Exception e) {
            throw new DataAccessException("查詢可兌換商品失敗: " + e.getMessage(), e);
        }
    }

    public String getLastExchangeId() {
        try {
            String hql = "SELECT b.exchangeId FROM BonusExchangeBean b ORDER BY b.exchangeId DESC";
            Query<String> query = getSession().createQuery(hql, String.class);
            query.setMaxResults(1);
            List<String> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new DataAccessException("獲取最後兌換ID時發生錯誤", e);
        }
    }

    public void saveExchange(BonusExchangeBean exchange) throws DataAccessException {
        try {
            getSession().save(exchange);
        } catch (Exception e) {
            throw new DataAccessException("保存兌換記錄失敗: " + e.getMessage(), e);
        }
    }

    public List<BonusExchangeBean> getExchangeRecords(String customerTel) throws DataAccessException {
        try {
            String hql = "FROM BonusExchangeBean b WHERE b.customerTel = :customerTel";
            return getSession().createQuery(hql, BonusExchangeBean.class)
                .setParameter("customerTel", customerTel)
                .list();
        } catch (Exception e) {
            throw new DataAccessException("獲取兌換記錄失敗: " + e.getMessage(), e);
        }
    }

    public ProductBean getProductById(String productId) throws DataAccessException {
        try {
            return getSession().get(ProductBean.class, productId);
        } catch (Exception e) {
            throw new DataAccessException("獲取商品信息失敗: " + e.getMessage(), e);
        }
    }
    
    public void updateCustomerPoints(String customerTel, int newPoints) throws DataAccessException {
        try {
            String hql = "UPDATE CustomerBean c SET c.totalPoints = :newPoints WHERE c.customerTel = :customerTel";
            getSession().createQuery(hql)
                .setParameter("newPoints", newPoints)
                .setParameter("customerTel", customerTel)
                .executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("更新客戶積分失敗: " + e.getMessage(), e);
        }
    }

    public void updateProductInventory(String productId, int newInventory) throws DataAccessException {
        try {
            String hql = "UPDATE ProductBean p SET p.numberOfInventory = :newInventory WHERE p.productId = :productId";
            getSession().createQuery(hql)
                .setParameter("newInventory", newInventory)
                .setParameter("productId", productId)
                .executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("更新商品庫存失敗: " + e.getMessage(), e);
        }
    }
    
    public BonusExchangeBean getExchangeRecordById(String exchangeId) throws DataAccessException {
        try {
            return getSession().get(BonusExchangeBean.class, exchangeId);
        } catch (Exception e) {
            throw new DataAccessException("獲取兌換記錄失敗: " + e.getMessage(), e);
        }
    }


	public List<ProductBean> getProductsByCategory(String category) throws DataAccessException {
        try {
            String hql = "FROM ProductBean WHERE productCategory = :category";
            return getSession().createQuery(hql, ProductBean.class)
                .setParameter("category", category)
                .list();
        } catch (Exception e) {
            throw new DataAccessException("獲取商品列表失敗: " + e.getMessage(), e);
        }
    }
	
	public void updateExchangeRecord(BonusExchangeBean exchangeRecord) throws DataAccessException {
        try {
            getSession().update(exchangeRecord);
        } catch (Exception e) {
            throw new DataAccessException("更新兌換記錄失敗: " + e.getMessage(), e);
        }
    }
    public List<ProductBean> getProductNamesByCategory(String category) throws DataAccessException {
        try {
            String hql = "FROM ProductBean WHERE productCategory = :category";
            return getSession().createQuery(hql, ProductBean.class)
                          .setParameter("category", category)
                          .list();
        } catch (Exception e) {
            throw new DataAccessException("獲取產品列表失敗: " + e.getMessage(), e);
        }
    }
    
    public void deleteExchangeRecord(String exchangeId) throws DataAccessException {
        try {
            BonusExchangeBean exchangeRecord = getSession().get(BonusExchangeBean.class, exchangeId);
            if (exchangeRecord != null) {
                getSession().delete(exchangeRecord);
            } else {
                throw new DataAccessException("未找到指定的兌換記錄");
            }
        } catch (Exception e) {
            throw new DataAccessException("刪除兌換記錄失敗: " + e.getMessage(), e);
        }
    }



	


    // Implement other methods (updateExchange, deleteExchange, etc.) as needed
}
