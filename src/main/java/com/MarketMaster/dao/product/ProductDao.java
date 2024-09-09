package com.MarketMaster.dao.product;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.MarketMaster.bean.product.ProductBean;

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
	
	 public List<ProductBean> selectAll() {
	        
	        Query<ProductBean> query = session.createQuery("from ProductBean", ProductBean.class);
	        return query.list();
	    }

	public ProductBean updateProduct(String productId) {
		ProductBean productBean = session.get(ProductBean.class, productId);
		if (productBean != null) {
			
		}
		return null;

	}
	public ProductBean updateBeanProduct(String productId) {
		ProductBean productBean = session.get(ProductBean.class, productId);
		
		
		
		return null;
	}
	
	
	public ProductBean shelveProduct(String productId,int shelveNumber) {
		ProductBean productBean = session.get(ProductBean.class, productId);
		int inventory = productBean.getNumberOfInventory();
		int shelve = productBean.getNumberOfShelve();
		if (productBean != null) {
			productBean.setNumberOfInventory(inventory-shelveNumber);
			productBean.setNumberOfShelve(shelve+shelveNumber);
			session.persist(productBean);
		}
		return productBean;
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
			session.persist(productBean);
		}
		return productBean;
	}
}
