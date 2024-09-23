package com.MarketMaster.dao.schedule;

import org.hibernate.Session;
import org.hibernate.query.Query;
import com.MarketMaster.bean.schedule.ScheduleBean;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ScheduleDao {

    private Session session;

    public ScheduleDao(Session session) {
        this.session = session;
    }

 // 獲取所有排班記錄
    public List<Object[]> getAllSchedule() {
        String hql = "SELECT s.employeeId, e.employeeName, s.jobDate, "
                   + "function('FORMAT', s.startTime, 'HH:mm'), "  
                   + "function('FORMAT', s.endTime, 'HH:mm') "    
                   + "FROM ScheduleBean s JOIN EmpBean e ON s.employeeId = e.employeeId";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        return query.list();
    }

    // 獲取所有員工姓名
    public List<String> getAllEmployeeNames() {
        String hql = "SELECT DISTINCT e.employeeName FROM EmpBean e";
        Query<String> query = session.createQuery(hql, String.class);
        return query.list();
    }

    // 新增排班記錄
    public ScheduleBean insert(ScheduleBean schedule) {
        if (schedule != null) {
            session.persist(schedule);
            return schedule;
        }
        return null;
    }


    // 根據員工姓名獲取員工ID
    public String getEmployeeIdByName(String employeeName) {
        String hql = "SELECT e.employeeId FROM EmpBean e WHERE e.employeeName = :employeeName";
        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("employeeName", employeeName);
        return query.uniqueResult();
    }

  //刪除
    public int deleteSchedule(String employeeId, Date jobDate, Time startTime, Time endTime) {
        String hql = "DELETE FROM ScheduleBean s WHERE s.employeeId = :employeeId AND s.jobDate = :jobDate "
                   + "AND s.startTime = :startTime AND s.endTime = :endTime";
        Query query = session.createQuery(hql);
        query.setParameter("employeeId", employeeId);
        query.setParameter("jobDate", jobDate);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        
        int result = query.executeUpdate();
        return result;
    }

    // 根據日期和時間段獲取排班中的員工 ID 列表
    public List<String> getEmployeeIdsBySchedule(Date jobDate, Time startTime, Time endTime) {
        String hql = "SELECT s.employeeId FROM ScheduleBean s WHERE s.jobDate = :jobDate "
                   + "AND (CAST(s.startTime AS string) <= :endTime AND CAST(s.endTime AS string) >= :startTime)";
        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("jobDate", jobDate);
        query.setParameter("startTime", startTime.toString());
        query.setParameter("endTime", endTime.toString());
        return query.getResultList();
    }

    // 根據日期和時間段刪除排班
    public void deleteSchedules(Date jobDate, Time startTime, Time endTime) {
        String hql = "DELETE FROM ScheduleBean s WHERE s.jobDate = :jobDate "
                   + "AND (s.startTime <= :endTime AND s.endTime >= :startTime)";
        Query query = session.createQuery(hql);
        query.setParameter("jobDate", jobDate);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.executeUpdate();
    }


    // 根據日期和時間段新增或更新排班
    public void saveOrUpdateSchedule(List<ScheduleBean> newSchedules, Date jobDate, Time startTime, Time endTime) {
        // 先刪除符合條件的排班
        deleteSchedules(jobDate, startTime, endTime);

        // 然後新增新的排班
        for (ScheduleBean schedule : newSchedules) {
            if (schedule != null) {
                session.saveOrUpdate(schedule);
            }
        }
    }

}