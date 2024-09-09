package com.MarketMaster.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

@WebServlet("/ProductsServlet")
public class ProductsServlet extends HttpServlet {
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
	private void processAction(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String action = request.getParameter("action");
		switch (action) {

		case "GetPagesProducts":
			handleGetPagesProducts(request, response);
			break;
		case "InsertProduct":
			handleInsertProduct(request, response);
			break;
		case "ShelveProduct":
			handleShelveProduct(request, response);
			break;
		}
	}

	private void handleGetPagesProducts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();

		ProductDao productDao = new ProductDao(session);
		List<ProductBean> products = productDao.selectAll();

		request.setAttribute("products", products);
		request.getRequestDispatcher("/jsp/GetPagesProducts.jsp").forward(request, response);
	}

	private void handleInsertProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		ProductDao productDao = new ProductDao(session);

		String productId = request.getParameter("productId");
		String productCategory = request.getParameter("productCategory");
		String productName = request.getParameter("productName");
		int productPrice = Integer.parseInt(request.getParameter("productPrice"));
		int productSafeinventory = Integer.parseInt(request.getParameter("productSafeInventory"));
		int numberOfShelve = Integer.parseInt(request.getParameter("numberOfShelve"));
		int numberOfInventory = Integer.parseInt(request.getParameter("numberOfInventory"));
		int numberOfSale = Integer.parseInt(request.getParameter("numberOfSale"));
		int numberOfExchange = Integer.parseInt(request.getParameter("numberOfExchange"));
		int numberOfDestruction = Integer.parseInt(request.getParameter("numberOfDestruction"));
		int numberOfRemove = Integer.parseInt(request.getParameter("numberOfRemove"));

		ProductBean product = new ProductBean();

		product.setProductId(productId);
		product.setProductCategory(productCategory);
		product.setProductName(productName);
		product.setProductPrice(productPrice);
		product.setproductSafeInventory(productSafeinventory);
		product.setNumberOfShelve(numberOfShelve);
		product.setNumberOfInventory(numberOfInventory);
		product.setNumberOfSale(numberOfSale);
		product.setNumberOfExchange(numberOfExchange);
		product.setNumberOfDestruction(numberOfDestruction);
		product.setNumberOfRemove(numberOfRemove);

		productDao.insertProduct(product);
		
		request.setAttribute("product", product);
		request.getRequestDispatcher("/jsp/ShowInsertProduct.jsp").forward(request, response);
	}

	private void handleShelveProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		
		String productId = request.getParameter("productId");
		int numberOfShelve = Integer.parseInt(request.getParameter("numberOfShelve"));

		ProductDao productDao = new ProductDao(session);
		productDao.shelveProduct(productId, numberOfShelve);

        request.getRequestDispatcher("/jsp/GetPagesProducts.jsp").forward(request, response);


	}
}
