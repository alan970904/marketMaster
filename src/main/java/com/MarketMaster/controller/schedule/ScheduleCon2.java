package com.MarketMaster.controller.schedule;

import com.MarketMaster.bean.schedule.ScheduleBean;
import com.MarketMaster.dao.schedule.ScheduleDao;
import com.MarketMaster.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
		System.out.println("收到的 action：" + action);

		if (action == null || action.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Error: Action parameter is missing");
			return;
		}

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();

		try {
			ScheduleDao scheduleDao = new ScheduleDao(session);
			switch (action) {
			case "saveSchedule":
				saveSchedule(request, response, scheduleDao);
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not found: " + action);
				break;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error processing request: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveSchedule(HttpServletRequest request, HttpServletResponse response, ScheduleDao scheduleDao)
			throws IOException {
		System.out.println("開始處理 saveSchedule");
		try {
			// 解析日期參數
			String yearStr = request.getParameter("year");
			String monthStr = request.getParameter("month");
			String dayStr = request.getParameter("day");

			System.out.println("年份參數: " + yearStr);
			System.out.println("月份參數: " + monthStr);
			System.out.println("日期參數: " + dayStr);

			if (yearStr == null || monthStr == null || dayStr == null) {
				throw new IllegalArgumentException("日期參數不能為 null");
			}

			// 創建 LocalDate 對象
			LocalDate jobDate = LocalDate.of(Integer.parseInt(yearStr), Integer.parseInt(monthStr),
					Integer.parseInt(dayStr));
			System.out.println("創建的 Date: " + jobDate);

			// 初始化參數 Map
			Map<String, List<String>> scheduleMap = new HashMap<>();
			Map<String, String> startTimeMap = new HashMap<>();
			Map<String, String> endTimeMap = new HashMap<>();

			// 讀取請求的參數
			Map<String, String[]> parameterMap = request.getParameterMap();
			System.out.println("參數 Map 大小: " + parameterMap.size());

			// 解析參數並存儲到相應的 Map 中
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				String key = entry.getKey();
				String[] values = entry.getValue();
				if (values != null && values.length > 0) {
					System.out.println("參數: " + key + ", 值: " + Arrays.toString(values));
					if (key.startsWith("startTime_")) {
						String index = key.substring("startTime_".length());
						// 去除可能的尾部[]字符
						index = index.replaceAll("\\[\\]", "");
						startTimeMap.put(index, values[0]);
					} else if (key.startsWith("endTime_")) {
						String index = key.substring("endTime_".length());
						// 去除可能的尾部[]字符
						index = index.replaceAll("\\[\\]", "");
						endTimeMap.put(index, values[0]);
					} else if (key.startsWith("employees_")) {
						String index = key.substring("employees_".length());
						// 去除可能的尾部[]字符
						index = index.replaceAll("\\[\\]", "");
						scheduleMap.put(index, new ArrayList<>(Arrays.asList(values)));
					}
				} else {
					System.out.println("參數: " + key + " 的值為 null 或空");
				}
			}

			// 處理每個時間段和對應的員工
			for (String index : scheduleMap.keySet()) {
			    String startTimeString = startTimeMap.get(index);
			    String endTimeString = endTimeMap.get(index);
			    List<String> employees = scheduleMap.get(index);

			    System.out.println("處理時間段索引: " + index);
			    System.out.println("startTimeMap 內容: " + startTimeMap);
			    System.out.println("endTimeMap 內容: " + endTimeMap);

			    System.out.println("處理時間段: 開始 = " + startTimeString + ", 結束 = " + endTimeString);

			    // 確保時間格式正確
			    LocalTime startTime = parseTime(startTimeString);
			    LocalTime endTime = parseTime(endTimeString);

			    if (startTime == null || endTime == null) {
			        System.out.println("警告：無效的時間格式，跳過此時間段");
			        continue;
			    }

			    // 轉換為 SQL 的 Date 和 Time 格式
			    Date sqlDate = Date.valueOf(jobDate);
			    Time sqlStartTime = Time.valueOf(startTime);
			    Time sqlEndTime = Time.valueOf(endTime);

			    for (String employeeName : employees) {
			        String employeeId = scheduleDao.getEmployeeIdByName(employeeName.trim());
			        if (employeeId != null) {
			            ScheduleBean newSchedule = new ScheduleBean();
			            newSchedule.setEmployeeId(employeeId);
			            newSchedule.setJobDate(sqlDate);
			            newSchedule.setStartTime(sqlStartTime);
			            newSchedule.setEndTime(sqlEndTime);
			            
			            // 檢查是否已存在相同的排程
			            if (scheduleDao.scheduleExists(employeeId, sqlDate)) {
			                scheduleDao.deleteSchedule(employeeId, sqlDate);
			                System.out.println("刪除舊排程：員工 ID = " + employeeId + ", 日期 = " + jobDate);
			            }
			            scheduleDao.insert(newSchedule);
			            System.out.println("已新增排程：" + newSchedule);
			        } else {
			            System.out.println("未找到員工: " + employeeName);
			        }
			    }
			}

			System.out.println("排程保存完成");
			response.getWriter().write("Schedule saved successfully");
		} catch (Exception e) {
			System.out.println("保存排程時發生錯誤：" + e.getMessage());
			e.printStackTrace();
			response.getWriter().write("Error saving schedule: " + e.getMessage());
		}
	}

	// 解析時間字串為 LocalTime
	private LocalTime parseTime(String timeString) {
		try {
			return LocalTime.parse(timeString);
		} catch (Exception e) {
			System.out.println("警告：時間字串為 null 或空");
			return null;
		}
	}
	
	



	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}