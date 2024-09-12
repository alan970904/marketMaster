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

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/ScheduleCon2")
public class ScheduleCon2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ScheduleCon2() {
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
			        // 使用 request.getParameter 獲取參數
			        String yearStr = request.getParameter("year");
			        String monthStr = request.getParameter("month");
			        String dayStr = request.getParameter("day");

			        System.out.println("年份參數: " + yearStr);
			        System.out.println("月份參數: " + monthStr);
			        System.out.println("日期參數: " + dayStr);

			        // 如果任何一個參數為 null，拋出異常
			        if (yearStr == null || monthStr == null || dayStr == null) {
			            throw new IllegalArgumentException("日期參數不能為 null");
			        }

			        // 確保字符串轉換為整數
			        int year = Integer.parseInt(yearStr);
			        int month = Integer.parseInt(monthStr);
			        int day = Integer.parseInt(dayStr);

			        System.out.println("解析後的日期: " + year + "-" + month + "-" + day);

			        Date jobDate1 = new Date(year - 1900, month - 1, day);
			        System.out.println("創建的 Date: " + jobDate1);

			        Map<String, List<String>> scheduleMap = new HashMap<>();
			        Map<String, String> timeMap = new HashMap<>();

			        Map<String, String[]> parameterMap = request.getParameterMap();
			        System.out.println("參數 Map 大小: " + parameterMap.size());

			        for (String key : parameterMap.keySet()) {
			            String[] values = parameterMap.get(key);
			            if (values != null && values.length > 0) {
			                System.out.println("參數: " + key + ", 值: " + Arrays.toString(values));
			                if (key.startsWith("startTime_")) {
			                    String index = key.substring("startTime_".length());
			                    timeMap.put("start_" + index, values[0]);
			                } else if (key.startsWith("endTime_")) {
			                    String index = key.substring("endTime_".length());
			                    timeMap.put("end_" + index, values[0]);
			                } else if (key.startsWith("employees_")) {
			                    String index = key.substring("employees_".length());
			                    scheduleMap.put(index, new ArrayList<>(Arrays.asList(values)));
			                }
			            } else {
			                System.out.println("參數: " + key + " 的值為 null 或空");
			            }
			        }

			        // 打印所有的參數名稱和值
			        System.out.println("Received parameters:");
			        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			            String paramName = entry.getKey();
			            String[] paramValues = entry.getValue();
			            System.out.print(paramName + ": ");
			            for (String value : paramValues) {
			                System.out.print(value + " ");
			            }
			            System.out.println();
			        }

			        String[] employeesToAddArray = request.getParameter("employeesToAdd").split(",");
			        String[] employeesToRemoveArray = request.getParameter("employeesToRemove").split(",");

			        // 使用 stream 去除空字串
			        List<String> employeesToAdd = Arrays.stream(employeesToAddArray).filter(s -> !s.trim().isEmpty())
			                .collect(Collectors.toList());
			        List<String> employeesToRemove = Arrays.stream(employeesToRemoveArray).filter(s -> !s.trim().isEmpty())
			                .collect(Collectors.toList());

			        // 新增員工
			        for (String index : scheduleMap.keySet()) {
			            String startTimeString2 = timeMap.get("start_" + index);
			            String endTimeString1 = timeMap.get("end_" + index);
			            List<String> employees = scheduleMap.get(index);

			            Time startTime1 = Time.valueOf(startTimeString2);
			            Time endTime1 = Time.valueOf(endTimeString1);

			            for (String employeeId1 : employees) {
			                if (employeesToAdd.contains(employeeId1.trim())) {
			                    ScheduleBean newSchedule1 = new ScheduleBean();
			                    newSchedule1.setEmployeeId(employeeId1.trim());
			                    newSchedule1.setJobDate(jobDate1);
			                    newSchedule1.setStartTime(startTime1);
			                    newSchedule1.setEndTime(endTime1);
			                    scheduleDao.insert(newSchedule1);
			                    System.out.println("已新增排程：" + newSchedule1);
			                }
			            }
			        }

			        // 刪除員工
			        for (String employeeId1 : employeesToRemove) {
			            scheduleDao.deleteSchedule(employeeId1.trim(), jobDate1);
			            System.out.println("已刪除排程：員工 ID " + employeeId1.trim() + "，日期 " + jobDate1);
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