package com.MarketMaster.dao.product;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.util.HibernateUtil;

public class ProductDao {
	private Session session;



	public ProductDao(Session session) {
		this.session = session;
	}

	public ProductBean insertProduct(ProductBean product) {
		if (product != null) {
			session.persist(product);
			return product;
		}
		return null;
	}
	
	
	public ProductBean getOne(String productId) {
		ProductBean product = session.get(ProductBean.class, productId);
		if (product != null) {
			return product;
		}
		return null;
	}
	 public List<ProductBean> getAll() {
	        Query<ProductBean> query = session.createQuery("from ProductBean", ProductBean.class);
	        return query.list();
	    }

	public ProductBean updateProduct(ProductBean product) {
		String productId = product.getProductId();
		String productName = product.getProductName();
		int productPrice = product.getProductPrice();
		int productSafeInventory = product.getproductSafeInventory();
		
		ProductBean productBean = session.get(ProductBean.class, productId);
		if (productBean != null) {
			productBean.setProductName(productName);
			productBean.setproductSafeInventory(productSafeInventory);
			productBean.setProductPrice(productPrice);
			session.merge(productBean);
			return productBean;
		}
		return null;
	}
	
	
	public ProductBean shelveProduct(String productId,int shelveNumber) {
		ProductBean productBean = session.get(ProductBean.class, productId);
		int inventory = productBean.getNumberOfInventory();
		int shelve = productBean.getNumberOfShelve();
		if (productBean != null) {
			productBean.setNumberOfInventory(inventory-shelveNumber);
			productBean.setNumberOfShelve(shelve+shelveNumber);
			session.merge(productBean);
			return productBean;
		}
		return null;
	}
	
	
	public ProductBean removeProduct(String productId) {
		ProductBean productBean = session.get(ProductBean.class, productId);
		if (productBean != null) {
			int inventory = productBean.getNumberOfInventory();
			int shelve = productBean.getNumberOfShelve();
			int remove = productBean.getNumberOfRemove();
			productBean.setNumberOfInventory(0);
			productBean.setNumberOfShelve(0);
			productBean.setNumberOfRemove(remove+shelve+inventory);
			session.merge(productBean);
		}
		return productBean;
	}
	
}
