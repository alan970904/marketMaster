package com.MarketMaster.controller.schedule;

import com.MarketMaster.dao.schedule.ScheduleDao;
import com.MarketMaster.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/ScheduleHome")
public class ScheduleHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ScheduleHome() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			ScheduleDao scheduleDao = new ScheduleDao(session);
			List<Object[]> scheduleRecords = scheduleDao.getAllSchedule();
			List<String> employeeNames = scheduleDao.getAllEmployeeNames();

			request.setAttribute("scheduleRecords", scheduleRecords);
			request.setAttribute("employeeNames", employeeNames);
			request.getRequestDispatcher("/schedule/Schedule.jsp").forward(request, response);
		}

		finally {

		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
