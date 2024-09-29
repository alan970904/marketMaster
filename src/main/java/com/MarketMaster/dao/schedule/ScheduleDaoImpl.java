package com.MarketMaster.dao.schedule;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.MarketMaster.bean.schedule.ScheduleBean;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
@Transactional
public class ScheduleDaoImpl implements ScheduleDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Object[]> getAllSchedule() {
        String hql = "SELECT s.employeeId, e.employeeName, s.jobDate, "
                   + "function('FORMAT', s.startTime, 'HH:mm'), "  
                   + "function('FORMAT', s.endTime, 'HH:mm') "    
                   + "FROM ScheduleBean s JOIN EmpBean e ON s.employeeId = e.employeeId";
        Query<Object[]> query = getSession().createQuery(hql, Object[].class);
        return query.list();
    }

    public List<String> getAllEmployeeNames() {
        String hql = "SELECT DISTINCT e.employeeName FROM EmpBean e";
        Query<String> query = getSession().createQuery(hql, String.class);
        return query.list();
    }

    public ScheduleBean insert(ScheduleBean schedule) {
        if (schedule != null) {
            getSession().persist(schedule);
            return schedule;
        }
        return null;
    }

    public String getEmployeeIdByName(String employeeName) {
        String hql = "SELECT e.employeeId FROM EmpBean e WHERE e.employeeName = :employeeName";
        Query<String> query = getSession().createQuery(hql, String.class);
        query.setParameter("employeeName", employeeName);
        return query.uniqueResult();
    }

    public int deleteSchedule(String employeeId, Date jobDate, Time startTime, Time endTime) {
        String hql = "DELETE FROM ScheduleBean s WHERE s.employeeId = :employeeId AND s.jobDate = :jobDate "
                   + "AND s.startTime = :startTime AND s.endTime = :endTime";
        Query query = getSession().createQuery(hql);
        query.setParameter("employeeId", employeeId);
        query.setParameter("jobDate", jobDate);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        
        return query.executeUpdate();
    }

    public List<String> getEmployeeIdsBySchedule(Date jobDate, Time startTime, Time endTime) {
        String hql = "SELECT s.employeeId FROM ScheduleBean s WHERE s.jobDate = :jobDate "
                   + "AND (CAST(s.startTime AS string) <= :endTime AND CAST(s.endTime AS string) >= :startTime)";
        Query<String> query = getSession().createQuery(hql, String.class);
        query.setParameter("jobDate", jobDate);
        query.setParameter("startTime", startTime.toString());
        query.setParameter("endTime", endTime.toString());
        return query.getResultList();
    }

    public void deleteSchedules(Date jobDate, Time startTime, Time endTime) {
        String hql = "DELETE FROM ScheduleBean s WHERE s.jobDate = :jobDate "
                   + "AND (s.startTime <= :endTime AND s.endTime >= :startTime)";
        Query query = getSession().createQuery(hql);
        query.setParameter("jobDate", jobDate);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.executeUpdate();
    }

    public void saveOrUpdateSchedule(List<ScheduleBean> newSchedules, Date jobDate, Time startTime, Time endTime) {
        Session session = getSession();
        deleteSchedules(jobDate, startTime, endTime);

        for (ScheduleBean schedule : newSchedules) {
            if (schedule != null) {
                session.saveOrUpdate(schedule);
            }
        }
    }
}