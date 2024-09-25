package com.MarketMaster.dao.schedule;

import com.MarketMaster.bean.schedule.ScheduleBean;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface ScheduleDao {
    List<Object[]> getAllSchedule();
    List<String> getAllEmployeeNames();
    ScheduleBean insert(ScheduleBean schedule);
    String getEmployeeIdByName(String employeeName);
    int deleteSchedule(String employeeId, Date jobDate, Time startTime, Time endTime);
    List<String> getEmployeeIdsBySchedule(Date jobDate, Time startTime, Time endTime);
    void deleteSchedules(Date jobDate, Time startTime, Time endTime);
    void saveOrUpdateSchedule(List<ScheduleBean> newSchedules, Date jobDate, Time startTime, Time endTime);
}