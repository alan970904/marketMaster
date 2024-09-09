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

@WebServlet("/DemoAllProduct")
public class DemoAllProduct extends HttpServlet {
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
//		response.setContentType("text/html;charset=UTF-8");
//		PrintWriter out = response.getWriter();

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();

		ProductDao productDao = new ProductDao(session);
		List<ProductBean> products = productDao.selectAll();
		
		request.setAttribute("products", products);
		request.getRequestDispatcher("/jsp/GetPagesProducts.jsp").forward(request, response);



//		out.close();
	}

}
