package com.MarketMaster.controller.schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.MarketMaster.dao.schedule.AskForLeaveDao;
import com.MarketMaster.bean.schedule.AskForLeaveBean;
import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.util.HibernateUtil;

@WebServlet("/AskForLeaveCon")
public class AskForLeaveCon extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AskForLeaveCon() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();

		try {
			AskForLeaveDao dao = new AskForLeaveDao(session);

			switch (action) {
			case "searchLeaveRecords":
				String employeeId = request.getParameter("employee_id");
				List<AskForLeaveBean> leaveRecords = dao.getLeaveRecordsById(employeeId);
				request.setAttribute("leaveRecords", leaveRecords);
				request.getRequestDispatcher("schedule/getLeaveById.jsp").forward(request, response);
				break;

			case "searchByLeaveId":
				String leaveId3 = request.getParameter("leave_id");
				List<AskForLeaveBean> leaveRecords3 = dao.getLeaveRecordsByLeaveId(leaveId3);

				if (leaveRecords3 != null && !leaveRecords3.isEmpty()) {
					AskForLeaveBean leaveRecord = leaveRecords3.get(0);

					LocalDateTime startDatetime = leaveRecord.getStartDatetime();
					LocalDateTime endDatetime = leaveRecord.getEndDatetime();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String startDatetimeFormatted = startDatetime.format(formatter);
					String endDatetimeFormatted = endDatetime.format(formatter);

					request.setAttribute("startDatetimeFormatted", startDatetimeFormatted);
					request.setAttribute("endDatetimeFormatted", endDatetimeFormatted);

					request.setAttribute("leaveRecords", leaveRecords3);
					request.getRequestDispatcher("schedule/updateLeaveById.jsp").forward(request, response);
				} else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "找不到指定 ID 的請假記錄。");
				}
				break;

			case "allLeaveRecords":
				List<AskForLeaveBean> allLeaveRecords = dao.getLeaveRecords();
				request.setAttribute("leaveRecords", allLeaveRecords);
				request.getRequestDispatcher("schedule/getAllLeaveRecords.jsp").forward(request, response);
				break;

			case "delete":
				String leaveId = request.getParameter("leave_id");
				String employeeId1 = request.getParameter("employee_id");
				dao.deleteLeaveRecord(leaveId);
				List<AskForLeaveBean> LeaveRecords = dao.getLeaveRecordsById(employeeId1);
				request.setAttribute("leaveRecords", LeaveRecords);
				request.getRequestDispatcher("schedule/getLeaveById.jsp").forward(request, response);
				break;

			case "createForm":
				String employeeId2 = request.getParameter("employee_id");
				String employeeName = dao.getEmployeeNameById(employeeId2);
				String leaveId1 = dao.generateNextLeaveId();

				request.setAttribute("leave_id", leaveId1);
				request.setAttribute("employee_id", employeeId2);
				request.setAttribute("employee_name", employeeName);

				request.getRequestDispatcher("schedule/addLeaveRecordById.jsp").forward(request, response);
				break;

			case "add":
				String leaveId2 = request.getParameter("leave_id");
				String employeeId4 = request.getParameter("employee_id");
				String startDatetimeStr = request.getParameter("start_datetime");
				String endDatetimeStr = request.getParameter("end_datetime");
				String leaveCategory = request.getParameter("leave_category");
				String reasonOfLeave = request.getParameter("reason_of_leave");
				String approvedStatus = "未批准"; // 預設為 "未批准"

				LocalDateTime startDatetime = LocalDateTime.parse(startDatetimeStr,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				LocalDateTime endDatetime = LocalDateTime.parse(endDatetimeStr,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

				AskForLeaveBean leave = new AskForLeaveBean();
				leave.setLeaveId(leaveId2);
				leave.setEmployeeId(employeeId4);
				leave.setStartDatetime(startDatetime);
				leave.setEndDatetime(endDatetime);
				leave.setLeaveCategory(leaveCategory);
				leave.setReasonOfLeave(reasonOfLeave);
				leave.setApprovedStatus(approvedStatus);

				// 獲取對應的 EmpBean 並設置關聯
				EmpBean empBean = session.get(EmpBean.class, employeeId4);
				leave.setEmpBean(empBean);

				dao.addLeaveRecordById(leave);

				List<AskForLeaveBean> leaveRecords4 = dao.getLeaveRecordsById(employeeId4);
				request.setAttribute("leaveRecords", leaveRecords4);

				request.getRequestDispatcher("schedule/getLeaveById.jsp").forward(request, response);
				break;

			case "updateLeaveRecord":
				String leaveIdUpdate = request.getParameter("leave_id");
				String employeeIdUpdate = request.getParameter("employee_id");
				String startDatetimeStr1 = request.getParameter("start_datetime");
				String endDatetimeStr1 = request.getParameter("end_datetime");
				String leaveCategoryUpdate = request.getParameter("leave_category");
				String reasonOfLeaveUpdate = request.getParameter("reason_of_leave");

				DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime startDatetimeUpdate = LocalDateTime.parse(startDatetimeStr1, formatter1);
				LocalDateTime endDatetimeUpdate = LocalDateTime.parse(endDatetimeStr1, formatter1);

				AskForLeaveBean leave1 = session.get(AskForLeaveBean.class, leaveIdUpdate);
				if (leave1 != null) {
					leave1.setStartDatetime(startDatetimeUpdate);
					leave1.setEndDatetime(endDatetimeUpdate);
					leave1.setLeaveCategory(leaveCategoryUpdate);
					leave1.setReasonOfLeave(reasonOfLeaveUpdate);
					leave1.setApprovedStatus("未批准");

					// 如果員工 ID 有變更，更新關聯
					if (!leave1.getEmployeeId().equals(employeeIdUpdate)) {
						EmpBean newEmpBean = session.get(EmpBean.class, employeeIdUpdate);
						leave1.setEmpBean(newEmpBean);
						leave1.setEmployeeId(employeeIdUpdate);
					}

					dao.updateLeaveRecord(leave1);
				}

				List<AskForLeaveBean> leaveRecords1 = dao.getLeaveRecordsById(employeeIdUpdate);
				request.setAttribute("leaveRecords", leaveRecords1);
				request.getRequestDispatcher("schedule/getLeaveById.jsp").forward(request, response);
				break;

			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的操作。");
				break;
			}

		} 
		finally {
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}