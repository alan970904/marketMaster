package com.MarketMaster.dao.schedule;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarketMaster.bean.employee.EmpBean;
import com.MarketMaster.bean.schedule.AskForLeaveBean;

@Service
@Transactional
public class AskForLeaveDao implements AskForLeaveService {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public String generateNextLeaveId() {
		String sql = "SELECT MAX(CAST(SUBSTRING(a.leaveId, 2, LENGTH(a.leaveId) - 1) AS INTEGER)) FROM AskForLeaveBean a";
		Query<Integer> query = getSession().createQuery(sql, Integer.class);
		Integer maxId = query.uniqueResult();

		if (maxId != null) {
			return "L" + String.format("%05d", maxId + 1);
		}
		return "L00001";
	}

	@Override
	public String getEmployeeNameById(String employeeId) {
		if (employeeId == null || employeeId.trim().isEmpty()) {
			return null;
		}

		EmpBean empBean = getSession().get(EmpBean.class, employeeId);
		return empBean != null ? empBean.getEmployeeName() : null;
	}

	@Override
	public void addLeaveRecordById(AskForLeaveBean leave) {
		Session session = getSession();
		EmpBean empBean = session.get(EmpBean.class, leave.getEmployeeId());
		leave.setEmpBean(empBean);
		session.persist(leave);
		session.flush(); // 強制立即執行插入操作
	}

	@Override
	public List<AskForLeaveBean> getLeaveRecordsById(String employeeId) {
		String hql = "SELECT DISTINCT a FROM AskForLeaveBean a LEFT JOIN FETCH a.empBean e WHERE a.employeeId = :employeeId";
		Query<AskForLeaveBean> query = getSession().createQuery(hql, AskForLeaveBean.class);
		query.setParameter("employeeId", employeeId);
		return query.list();
	}

	@Override
	public List<AskForLeaveBean> getLeaveRecordsByLeaveId(String leaveId) {
		String hql = "SELECT a FROM AskForLeaveBean a JOIN FETCH a.empBean e WHERE a.leaveId = :leaveId";
		Query<AskForLeaveBean> query = getSession().createQuery(hql, AskForLeaveBean.class);
		query.setParameter("leaveId", leaveId);
		return query.list();
	}

	@Override
	public List<AskForLeaveBean> getLeaveRecords() {
		String hql = "SELECT a FROM AskForLeaveBean a JOIN FETCH a.empBean";
		Query<AskForLeaveBean> query = getSession().createQuery(hql, AskForLeaveBean.class);
		return query.list();
	}

	@Override
	public void deleteLeaveRecord(String leaveId) {
		AskForLeaveBean leave = getSession().get(AskForLeaveBean.class, leaveId);
		if (leave != null) {
			getSession().remove(leave);
		}
	}

	@Override
	public void updateLeaveRecord(AskForLeaveBean leave) {
		Session session = getSession();
		AskForLeaveBean existingLeave = session.get(AskForLeaveBean.class, leave.getLeaveId());
		if (existingLeave != null) {
			existingLeave.setStartDatetime(leave.getStartDatetime());
			existingLeave.setEndDatetime(leave.getEndDatetime());
			existingLeave.setLeaveCategory(leave.getLeaveCategory());
			existingLeave.setReasonOfLeave(leave.getReasonOfLeave());
			session.update(existingLeave);
		}
	}
}