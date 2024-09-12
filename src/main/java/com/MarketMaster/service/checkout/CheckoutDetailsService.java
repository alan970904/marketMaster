package com.MarketMaster.service.checkout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.MarketMaster.bean.checkout.CheckoutDetailsBean;
import com.MarketMaster.dao.checkout.CheckoutDetailsDao;
import com.MarketMaster.util.HibernateUtil;

public class CheckoutDetailsService {
	private static final Logger logger = Logger.getLogger(CheckoutDetailsService.class.getName());
    private CheckoutDetailsDao checkoutDetailsDao;
    private SessionFactory sessionFactory;

    public CheckoutDetailsService() {
        this.checkoutDetailsDao = new CheckoutDetailsDao();
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public CheckoutDetailsBean getCheckoutDetails(String checkoutId, String productId) {
        try (Session session = sessionFactory.openSession()) {
            return checkoutDetailsDao.getOne(session, checkoutId, productId);
        }
    }

    public List<CheckoutDetailsBean> getPartCheckoutDetails(String checkoutId) {
        try (Session session = sessionFactory.openSession()) {
            return checkoutDetailsDao.getPart(session, checkoutId);
        }
    }

    public List<CheckoutDetailsBean> getAllCheckoutDetails() {
        try (Session session = sessionFactory.openSession()) {
            return checkoutDetailsDao.getAll(session);
        }
    }


    public void addCheckoutDetails(CheckoutDetailsBean checkoutDetails) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            checkoutDetailsDao.insert(session, checkoutDetails);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "新增結帳明細失敗", e);
            throw new RuntimeException("新增結帳明細失敗", e);
        }
    }

    public void updateCheckoutDetails(CheckoutDetailsBean checkoutDetails) {
        logger.info("開始更新結帳明細: " + checkoutDetails);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            int newCheckoutPrice = checkoutDetails.getNumberOfCheckout() * checkoutDetails.getProductPrice();
            checkoutDetails.setCheckoutPrice(newCheckoutPrice);
            checkoutDetailsDao.update(session, checkoutDetails);
            transaction.commit();
            logger.info("結帳明細更新成功: " + checkoutDetails);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "更新結帳明細失敗", e);
            throw new RuntimeException("更新結帳明細失敗", e);
        }
    }
    
    public void deleteCheckoutDetails(String checkoutId, String productId) {
        logger.info("刪除結帳ID為 " + checkoutId + " 和產品ID為 " + productId + " 的結帳明細");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            checkoutDetailsDao.delete(session, checkoutId, productId);
            transaction.commit();
            logger.info("結帳明細刪除成功");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "刪除結帳明細失敗", e);
            throw new RuntimeException("刪除結帳明細失敗", e);
        }
    }


    public List<CheckoutDetailsBean> searchCheckoutDetailsByProductId(String productId) {
        try (Session session = sessionFactory.openSession()) {
            return checkoutDetailsDao.searchByProductId(session, productId);
        }
    }
    
//    public void updateAfterReturn(String checkoutId, String productId, int returnQuantity, BigDecimal returnPrice) {
//        CheckoutDetailsDao.updateAfterReturn(checkoutId, productId, returnQuantity, returnPrice);
//    }
//
//    public void cancelReturn(String checkoutId, String productId, int returnQuantity, BigDecimal returnPrice) {
//        CheckoutDetailsDao.cancelReturn(checkoutId, productId, returnQuantity, returnPrice);
//    }

    public List<Map<String, Object>> getProductReturnRates() {
        try (Session session = sessionFactory.openSession()) {
            return checkoutDetailsDao.getProductReturnRates(session);
        }
    }


}