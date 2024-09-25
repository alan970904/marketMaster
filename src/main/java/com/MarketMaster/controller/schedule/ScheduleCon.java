package com.MarketMaster.controller.schedule;

import com.MarketMaster.bean.schedule.ScheduleBean;
import com.MarketMaster.dao.schedule.ScheduleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/schedule")
public class ScheduleCon {

    private final ScheduleDao scheduleDao;

    @Autowired
    public ScheduleCon(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @GetMapping
    public String showSchedule(Model model) {
        List<Object[]> scheduleRecords = scheduleDao.getAllSchedule();
        List<String> employeeNames = scheduleDao.getAllEmployeeNames();
        model.addAttribute("scheduleRecords", scheduleRecords);
        model.addAttribute("employeeNames", employeeNames);
        return "schedule/Schedule";
    }

    @PostMapping("/save")
    @ResponseBody
    public Map<String, Object> saveSchedule(@RequestParam Map<String, String> allParams) {
        Map<String, Object> response = new HashMap<>();
        try {
            int year = Integer.parseInt(allParams.get("year"));
            int month = Integer.parseInt(allParams.get("month"));
            int day = Integer.parseInt(allParams.get("day"));

            LocalDate jobDate = LocalDate.of(year, month, day);
            Date sqlDate = Date.valueOf(jobDate);

            List<ScheduleBean> newSchedules = new ArrayList<>();

            for (String key : allParams.keySet()) {
                if (key.startsWith("startTime_")) {
                    String index = key.substring("startTime_".length());
                    String startTimeString = allParams.get("startTime_" + index);
                    String endTimeString = allParams.get("endTime_" + index);
                    String[] employeeNames = allParams.get("employees_" + index + "[]").split(",");

                    Time startTime = Time.valueOf(LocalTime.parse(startTimeString));
                    Time endTime = Time.valueOf(LocalTime.parse(endTimeString));

                    for (String employeeName : employeeNames) {
                        String employeeId = scheduleDao.getEmployeeIdByName(employeeName.trim());
                        if (employeeId != null) {
                            ScheduleBean newSchedule = new ScheduleBean();
                            newSchedule.setEmployeeId(employeeId);
                            newSchedule.setJobDate(sqlDate);
                            newSchedule.setStartTime(startTime);
                            newSchedule.setEndTime(endTime);
                            newSchedules.add(newSchedule);
                        }
                    }
                }
            }

            scheduleDao.saveOrUpdateSchedule(newSchedules, sqlDate, 
                Time.valueOf("00:00:00"), Time.valueOf("23:59:59"));

            response.put("status", "success");
            response.put("message", "排程保存成功");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "保存排程時發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}