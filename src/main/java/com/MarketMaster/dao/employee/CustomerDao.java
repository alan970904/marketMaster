package com.MarketMaster.dao.employee;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.MarketMaster.bean.employee.CustomerBean;
import com.MarketMaster.exception.DataAccessException;

public class CustomerDao {
    private SessionFactory sessionFactory;

    public CustomerDao() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public boolean addCustomer(CustomerBean customer) throws DataAccessException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataAccessException("新增會員失敗", e);
        }
    }

    public CustomerBean getCustomer(String customerTel) throws DataAccessException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(CustomerBean.class, customerTel);
        } catch (Exception e) {
            throw new DataAccessException("獲取會員詳情失敗", e);
        }
    }

    public List<CustomerBean> getAllCustomers() throws DataAccessException {
        try (Session session = sessionFactory.openSession()) {
            Query<CustomerBean> query = session.createQuery("from CustomerBean", CustomerBean.class);
            return query.list();
        } catch (Exception e) {
            throw new DataAccessException("獲取所有會員資訊失敗", e);
        }
    }

    public boolean updateCustomer(CustomerBean newCustomer, String originalTel) throws DataAccessException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            
            CustomerBean existingCustomer = session.get(CustomerBean.class, originalTel);
            if (existingCustomer != null) {
                // 如果電話號碼有變更
                if (!originalTel.equals(newCustomer.getCustomerTel())) {
                    // 刪除舊記錄
                    session.delete(existingCustomer);
                    // 插入新記錄
                    session.save(newCustomer);
                } else {
                    // 如果電話號碼沒有變更，直接更新其他欄位
                    existingCustomer.setCustomerName(newCustomer.getCustomerName());
                    existingCustomer.setCustomerEmail(newCustomer.getCustomerEmail());
                    session.update(existingCustomer);
                }
                // 提交事務
                transaction.commit();
                System.out.println("Customer updated successfully");
                return true;
            } else {
                System.out.println("Customer not found with tel: " + originalTel);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new DataAccessException("更新會員失敗: " + e.getMessage(), e);
        }
    }

    public boolean deleteCustomer(String customerTel) throws DataAccessException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomerBean customer = session.get(CustomerBean.class, customerTel);
            if (customer != null) {
                session.delete(customer);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataAccessException("刪除會員失敗", e);
        }
    }
}