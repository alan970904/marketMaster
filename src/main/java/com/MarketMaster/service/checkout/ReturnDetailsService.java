package com.MarketMaster.service.checkout;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.MarketMaster.util.HibernateUtil;
import com.MarketMaster.bean.checkout.ReturnDetailsBean;
import com.MarketMaster.dao.checkout.ReturnDetailsDao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnDetailsService {
    private static final Logger logger = Logger.getLogger(ReturnDetailsService.class.getName());
    private ReturnDetailsDao returnDetailsDao = new ReturnDetailsDao();
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public ReturnDetailsBean getReturnDetails(String returnId, String checkoutId, String productId) {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.getOne(session, returnId, checkoutId, productId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting return details", e);
            return null;
        }
    }

    public List<ReturnDetailsBean> getReturnDetailsByReturnId(String returnId) {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.getPart(session, returnId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting return details by return ID", e);
            return null;
        }
    }

    public List<ReturnDetailsBean> getAllReturnDetails() {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.getAll(session);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting all return details", e);
            return null;
        }
    }

    public void addReturnDetails(ReturnDetailsBean returnDetails) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            returnDetailsDao.insert(session, returnDetails);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error adding return details", e);
        }
    }

    public void updateReturnDetails(ReturnDetailsBean returnDetails) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            returnDetailsDao.update(session, returnDetails);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error updating return details", e);
        }
    }

    public void deleteReturnDetails(String returnId, String checkoutId, String productId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            returnDetailsDao.delete(session, returnId, checkoutId, productId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error deleting return details", e);
        }
    }

    public List<ReturnDetailsBean> searchReturnDetailsByProductId(String productId) {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.searchByProductId(session, productId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error searching return details by product ID", e);
            return null;
        }
    }

    public List<Map<String, Object>> getReturnComparisonReport() {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.getReturnComparisonReport(session);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting return comparison report", e);
            return null;
        }
    }

    public List<Map<String, Object>> getReturnStatistics(Date startDate, Date endDate) {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.getReturnStatistics(session, startDate, endDate);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting return statistics", e);
            return null;
        }
    }

    public List<Map<String, Object>> getMostCommonReturnReasons(int limit) {
        try (Session session = sessionFactory.openSession()) {
            return returnDetailsDao.getMostCommonReturnReasons(session, limit);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting most common return reasons", e);
            return null;
        }
    }

    public void updateInventoryAfterReturn(String productId, int returnQuantity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            returnDetailsDao.updateInventoryAfterReturn(session, productId, returnQuantity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error updating inventory after return", e);
        }
    }
}