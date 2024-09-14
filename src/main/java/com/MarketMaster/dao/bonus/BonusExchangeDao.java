package com.MarketMaster.dao.bonus;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.MarketMaster.bean.bonus.BonusExchangeBean;
import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.util.HibernateUtil;

public class BonusExchangeDao {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public List<ProductBean> findExchangeableProducts(int customerPoints) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM ProductBean p WHERE p.productPrice <= :customerPoints AND p.numberOfInventory > 0";
            Query<ProductBean> query = session.createQuery(hql, ProductBean.class);
            query.setParameter("customerPoints", customerPoints);
            return query.list();
        }
    }

    public void save(BonusExchangeBean bonusExchange) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(bonusExchange);
            session.getTransaction().commit();
        }
    }

    // 其他需要的方法...
}