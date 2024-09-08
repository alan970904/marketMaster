package com.MarketMaster.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.dao.product.ProductDao;
import com.MarketMaster.util.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DemoInsertProduct")
public class DemoInsertProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();

		ProductDao productDao = new ProductDao(session);
	
		ProductBean product = new ProductBean();
		
		product.setProductId("PMS005");
		product.setProductCategory("肉品海鮮");
		product.setProductName("羊肉");
		product.setProductPrice(200);
		product.setproductSafeInventory(100);
		product.setNumberOfShelve(0);
		product.setNumberOfInventory(0);
		product.setNumberOfSale(0);
		product.setNumberOfExchange(0);
		product.setNumberOfDestruction(0);
		product.setNumberOfRemove(0);
		
		productDao.insertProduct(product);
		
		request.setAttribute("product", product);
		request.getRequestDispatcher("/jsp/ShowInsertProduct.jsp").forward(request, response);



		out.close();
	}

}
