package com.MarketMaster.dao.product;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.MarketMaster.bean.product.ProductBean;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

@Repository
public class ProductDao {
    private static final Logger logger = Logger.getLogger(ProductDao.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public ProductBean insertProduct(ProductBean product) {
        try {
            if (product != null) {
                getCurrentSession().persist(product);
                logger.info("商品插入成功: " + product.getProductId());
                return product;
            }
            return null;
        } catch (Exception e) {
            logger.severe("插入商品失敗: " + e.getMessage());
            throw new RuntimeException("插入商品失敗", e);
        }
    }

    @Transactional(readOnly = true)
    public ProductBean getOne(String productId) {
        try {
            ProductBean product = getCurrentSession().get(ProductBean.class, productId);
            if (product != null) {
                return product;
            }
            return null;
        } catch (Exception e) {
            logger.severe("獲取商品失敗: " + e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<ProductBean> getAll() {
        try {
            Query<ProductBean> query = getCurrentSession().createQuery("from ProductBean", ProductBean.class);
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取所有商品失敗", e);
            return new ArrayList<>();
        }
    }

    @Transactional
    public ProductBean updateProduct(ProductBean product) {
        try {
            String productId = product.getProductId();
            ProductBean productBean = getCurrentSession().get(ProductBean.class, productId);
            if (productBean != null) {
                productBean.setProductName(product.getProductName());
                productBean.setproductSafeInventory(product.getproductSafeInventory());
                productBean.setProductPrice(product.getProductPrice());
                getCurrentSession().merge(productBean);
                logger.info("商品更新成功: " + productId);
                return productBean;
            }
            return null;
        } catch (Exception e) {
            logger.severe("更新商品失敗: " + e.getMessage());
            throw new RuntimeException("更新商品失敗", e);
        }
    }

    @Transactional
    public ProductBean shelveProduct(String productId, int shelveNumber) {
        try {
            ProductBean productBean = getCurrentSession().get(ProductBean.class, productId);
            if (productBean != null) {
                int inventory = productBean.getNumberOfInventory();
                int shelve = productBean.getNumberOfShelve();
                productBean.setNumberOfInventory(inventory - shelveNumber);
                productBean.setNumberOfShelve(shelve + shelveNumber);
                getCurrentSession().merge(productBean);
                logger.info("商品上架成功: " + productId);
                return productBean;
            }
            return null;
        } catch (Exception e) {
            logger.severe("商品上架失敗: " + e.getMessage());
            throw new RuntimeException("商品上架失敗", e);
        }
    }

    @Transactional
    public ProductBean removeProduct(String productId) {
        try {
            ProductBean productBean = getCurrentSession().get(ProductBean.class, productId);
            if (productBean != null) {
                int inventory = productBean.getNumberOfInventory();
                int shelve = productBean.getNumberOfShelve();
                int remove = productBean.getNumberOfRemove();
                productBean.setNumberOfInventory(0);
                productBean.setNumberOfShelve(0);
                productBean.setNumberOfRemove(remove + shelve + inventory);
                getCurrentSession().merge(productBean);
                logger.info("商品移除成功: " + productId);
            }
            return productBean;
        } catch (Exception e) {
            logger.severe("移除商品失敗: " + e.getMessage());
            throw new RuntimeException("移除商品失敗", e);
        }
    }
}