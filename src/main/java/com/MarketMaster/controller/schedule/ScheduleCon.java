package com.MarketMaster.controller.schedule;

import com.MarketMaster.bean.schedule.ScheduleBean;
import com.MarketMaster.dao.schedule.ScheduleDao;
import com.MarketMaster.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.BufferedReader;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/ScheduleCon")
public class ScheduleCon extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
    }
	public ScheduleCon() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();
	    System.out.println("收到的 action：" + action);
		try {
			ScheduleDao scheduleDao = new ScheduleDao(session);
			switch (action) {
			
			case "viewSchedule":
				List<Object[]> scheduleRecords = scheduleDao.getAllSchedule();
				List<String> employeeNames = scheduleDao.getAllEmployeeNames();
				request.setAttribute("scheduleRecords", scheduleRecords);
				request.setAttribute("employeeNames", employeeNames);
				request.getRequestDispatcher("/schedule/Schedule.jsp").forward(request, response);
				break;

			
			case "saveSchedule":
			    System.out.println("開始處理 saveSchedule");
			    try {
			        // 讀取 JSON 數據
			        BufferedReader reader = request.getReader();
			        StringBuilder sb = new StringBuilder();
			        String line;
			        while ((line = reader.readLine()) != null) {
			            sb.append(line);
			        }
			        String jsonString = sb.toString();
			        
			        // 解析 JSON 數據
			        Map<String, Object> scheduleData = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
			        
			        int year = ((Number) scheduleData.get("year")).intValue();
			        int month = ((Number) scheduleData.get("month")).intValue();
			        int day = ((Number) scheduleData.get("day")).intValue();
			        
			        System.out.println("接收到的日期: " + year + "-" + month + "-" + day);
			        
			        Date jobDate = new Date(year - 1900, month - 1, day);
			        
			        @SuppressWarnings("unchecked")
			        List<Map<String, Object>> schedules = (List<Map<String, Object>>) scheduleData.get("schedules");
			        
			        // 添加空值檢查
			        @SuppressWarnings("unchecked")
			        List<String> employeesToAdd = scheduleData.get("employeesToAdd") != null ? 
			            (List<String>) scheduleData.get("employeesToAdd") : new ArrayList<>();
			        @SuppressWarnings("unchecked")
			        List<String> employeesToRemove = scheduleData.get("employeesToRemove") != null ? 
			            (List<String>) scheduleData.get("employeesToRemove") : new ArrayList<>();

			        // 首先刪除該日期的所有現有排程
			        scheduleDao.deleteSchedulesByDate(jobDate);

			        // 處理排程數據
			        for (Map<String, Object> schedule : schedules) {
			            String startTimeStr = (String) schedule.get("startTime");
			            String endTimeStr = (String) schedule.get("endTime");
			            @SuppressWarnings("unchecked")
			            List<String> employees = (List<String>) schedule.get("employees");
			            
			            Time startTime = Time.valueOf(startTimeStr);
			            Time endTime = Time.valueOf(endTimeStr);
			            
			            for (String employeeId : employees) {
			                ScheduleBean scheduleBean = new ScheduleBean();
			                scheduleBean.setEmployeeId(employeeId);
			                scheduleBean.setJobDate(jobDate);
			                scheduleBean.setStartTime(startTime);
			                scheduleBean.setEndTime(endTime);
			                scheduleDao.saveOrUpdateSchedule(scheduleBean);  // 使用 saveOrUpdate 而不是 insert
			            }
			        }
			        
			        // 處理添加員工
			        for (String employeeName : employeesToAdd) {
			            String employeeId = scheduleDao.getEmployeeIdByName(employeeName);
			            if (employeeId != null) {
			                ScheduleBean newSchedule = new ScheduleBean();
			                newSchedule.setEmployeeId(employeeId);
			                newSchedule.setJobDate(jobDate);
			                newSchedule.setStartTime(Time.valueOf("09:00:00"));
			                newSchedule.setEndTime(Time.valueOf("17:00:00"));
			                scheduleDao.saveOrUpdateSchedule(newSchedule);  // 使用 saveOrUpdate 而不是 insert
			            }
			        }
			        
			        // 處理刪除員工
			        for (String employeeName : employeesToRemove) {
			            String employeeId = scheduleDao.getEmployeeIdByName(employeeName);
			            if (employeeId != null) {
			                scheduleDao.deleteSchedule(employeeId, jobDate);
			            }
			        }
			        
			        System.out.println("排程保存完成");
			        response.getWriter().write("Schedule saved successfully");
			    } catch (Exception e) {
			        System.out.println("保存排程時發生錯誤：" + e.getMessage());
			        e.printStackTrace();
			        response.getWriter().write("Error saving schedule: " + e.getMessage());
			    }
			    break;

			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not found");
				break;
			}
		} catch (Exception e) {
			response.getWriter().write("Error saving schedule: " + e.getMessage());
		}
	}

	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}