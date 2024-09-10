package com.MarketMaster.controller.schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.MarketMaster.dao.schedule.AskForLeaveDao;
import com.MarketMaster.bean.schedule.AskForLeaveBean;

@WebServlet("/AskForLeaveCon")
public class AskForLeaveCon extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AskForLeaveCon() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        AskForLeaveDao dao = new AskForLeaveDao();

        switch (action) {
            case "searchLeaveRecords":
                String employeeId = request.getParameter("employee_id");
                try {
                    List<AskForLeaveBean> leaveRecords = dao.getLeaveRecordsById(employeeId);
                    request.setAttribute("leaveRecords", leaveRecords);
                    request.getRequestDispatcher("jsp/getLeaveById.jsp").forward(request, response);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;
            
            case "searchByLeaveId":
                String leaveId3 = request.getParameter("leave_id");

                try {
                    List<AskForLeaveBean> leaveRecords = dao.getLeaveRecordsByLeaveId(leaveId3);

                    if (leaveRecords != null && !leaveRecords.isEmpty()) {
                        AskForLeaveBean leaveRecord = leaveRecords.get(0);

                        LocalDateTime startDatetime = leaveRecord.getStartDatetime();  
                        LocalDateTime endDatetime = leaveRecord.getEndDatetime();      
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String startDatetimeFormatted = startDatetime.format(formatter);
                        String endDatetimeFormatted = endDatetime.format(formatter);

                        request.setAttribute("startDatetimeFormatted", startDatetimeFormatted);
                        request.setAttribute("endDatetimeFormatted", endDatetimeFormatted);

                        request.setAttribute("leaveRecords", leaveRecords);
                        request.getRequestDispatcher("jsp/updateLeaveById.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No leave record found for the given ID.");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;

            case "allLeaveRecords":
                try {
                    List<AskForLeaveBean> allLeaveRecords = dao.getLeaveRecords();
                    request.setAttribute("leaveRecords", allLeaveRecords);
                    request.getRequestDispatcher("jsp/getAllLeaveRecords.jsp").forward(request, response);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;

            case "delete":
                String leaveId = request.getParameter("leave_id"); 
                String employeeId1 = request.getParameter("employee_id"); 
                try {
                    dao.deleteLeaveRecord(leaveId); 
                    List<AskForLeaveBean> LeaveRecords = dao.getLeaveRecordsById(employeeId1); 
                    request.setAttribute("leaveRecords", LeaveRecords);
                    request.getRequestDispatcher("jsp/getLeaveById.jsp").forward(request, response); 
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;

            case "createForm":
                String employeeId2 = request.getParameter("employee_id");

                try {
                    String employeeName = dao.getEmployeeNameById(employeeId2);

                    String leaveId1 = dao.generateNextLeaveId();

                    request.setAttribute("leave_id", leaveId1);
                    request.setAttribute("employee_id", employeeId2);
                    request.setAttribute("employee_name", employeeName);

                    request.getRequestDispatcher("jsp/addLeaveRecordById.jsp").forward(request, response);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;
            
            case "add":
                String leaveId1 = request.getParameter("leave_id"); 
                String employeeId4 = request.getParameter("employee_id"); 
                String employeeName = request.getParameter("employee_name");
                String startDatetimeStr = request.getParameter("start_datetime");
                String endDatetimeStr = request.getParameter("end_datetime");
                String leaveCategory = request.getParameter("leave_category");
                String reasonOfLeave = request.getParameter("reason_of_leave");
                String approvedStatus = "未批准"; // Default to "Not Approved"

                LocalDateTime startDatetime = LocalDateTime.parse(startDatetimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime endDatetime = LocalDateTime.parse(endDatetimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                AskForLeaveBean leave = new AskForLeaveBean();
                leave.setLeaveId(leaveId1);
                leave.setEmployeeId(employeeId4);
                leave.setEmployeeName(employeeName);
                leave.setStartDatetime(startDatetime);
                leave.setEndDatetime(endDatetime);
                leave.setLeaveCategory(leaveCategory);
                leave.setReasonOfLeave(reasonOfLeave);
                leave.setApprovedStatus(approvedStatus);

                try {
                    dao.addLeaveRecordById(leave);

                    List<AskForLeaveBean> leaveRecords = dao.getLeaveRecordsById(employeeId4);
                    request.setAttribute("leaveRecords", leaveRecords);

                    request.getRequestDispatcher("jsp/getLeaveById.jsp").forward(request, response);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;
            
            case "updateLeaveRecord":
                String leaveIdUpdate = request.getParameter("leave_id");
                String employeeIdUpdate = request.getParameter("employee_id");
                String startDatetimeStr1 = request.getParameter("start_datetime");
                String endDatetimeStr1 = request.getParameter("end_datetime");
                String leaveCategoryUpdate = request.getParameter("leave_category");
                String reasonOfLeaveUpdate = request.getParameter("reason_of_leave");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime startDatetimeUpdate = LocalDateTime.parse(startDatetimeStr1, formatter);
                LocalDateTime endDatetimeUpdate = LocalDateTime.parse(endDatetimeStr1, formatter);

                AskForLeaveBean leave1 = new AskForLeaveBean();
                leave1.setLeaveId(leaveIdUpdate);
                leave1.setEmployeeId(employeeIdUpdate);
                leave1.setStartDatetime(startDatetimeUpdate);
                leave1.setEndDatetime(endDatetimeUpdate);
                leave1.setLeaveCategory(leaveCategoryUpdate);
                leave1.setReasonOfLeave(reasonOfLeaveUpdate);
                leave1.setApprovedStatus("未批准");
                
                try {
                    dao.updateLeaveRecord(leave1);
                    List<AskForLeaveBean> leaveRecords = dao.getLeaveRecordsById(employeeIdUpdate);
                    request.setAttribute("leaveRecords", leaveRecords);
                    request.getRequestDispatcher("jsp/getLeaveById.jsp").forward(request, response);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
                }
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}