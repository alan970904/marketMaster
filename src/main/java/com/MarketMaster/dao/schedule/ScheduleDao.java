package com.MarketMaster.dao.schedule;

import org.hibernate.Session;
import org.hibernate.query.Query;
import com.MarketMaster.bean.schedule.ScheduleBean;
import java.sql.Date;
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

    // 檢查特定日期和員工的排班是否存在
    public boolean scheduleExists(String employeeId, Date jobDate) {
        String hql = "SELECT COUNT(*) FROM ScheduleBean s WHERE s.employeeId = :employeeId AND s.jobDate = :jobDate";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("employeeId", employeeId);
        query.setParameter("jobDate", jobDate);
        return query.uniqueResult() > 0;
    }

    // 刪除特定員工和日期的排班
    public void deleteSchedule(String employeeId, Date jobDate) {
        String hql = "DELETE FROM ScheduleBean s WHERE s.employeeId = :employeeId AND s.jobDate = :jobDate";
        Query query = session.createQuery(hql);
        query.setParameter("employeeId", employeeId);
        query.setParameter("jobDate", jobDate);
        query.executeUpdate();
    }
    public void saveOrUpdateSchedule(ScheduleBean scheduleBean) {
        session.saveOrUpdate(scheduleBean);
    }
    // 根據日期刪除排班
    public void deleteSchedulesByDate(Date date) {
        String hql = "DELETE FROM ScheduleBean WHERE jobDate = :date";
        session.createQuery(hql)
               .setParameter("date", date)
               .executeUpdate();
    }
}