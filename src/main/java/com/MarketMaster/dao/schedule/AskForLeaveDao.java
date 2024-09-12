package com.MarketMaster.dao.schedule;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.MarketMaster.bean.schedule.AskForLeaveBean;

public class AskForLeaveDao {

	private Session session;

	public AskForLeaveDao(Session session) {
		this.session = session;
	}

	// 生成下一個請假編號
	public String generateNextLeaveId() {
		String sql = "SELECT MAX(CAST(SUBSTRING(a.leaveId, 2, LENGTH(a.leaveId) - 1) AS INTEGER)) FROM AskForLeaveBean a";
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer maxId = query.uniqueResult();

		if (maxId != null) {
			return "L" + String.format("%05d", maxId + 1);
		}
		return "L00001";
	}

	// 根據員工編號獲取員工姓名
	public String getEmployeeNameById(String employeeId) {
		if (employeeId == null || employeeId.trim().isEmpty()) {
			return null;
		}

		String hql = "SELECT e.employeeName FROM EmpBean e WHERE e.employeeId = :employeeId";
		Query<String> query = session.createQuery(hql, String.class);
		query.setParameter("employeeId", employeeId);
		return query.uniqueResult();
	}

	// 新增請假記錄
	public void addLeaveRecordById(AskForLeaveBean leave) {
		session.persist(leave);
	}

	// 更新請假記錄
	public void updateLeaveRecord(AskForLeaveBean leave) {
		session.merge(leave);
	}

	// 根據員工編號獲取請假記錄
	public List<AskForLeaveBean> getLeaveRecordsById(String employeeId) {
		String hql = "SELECT a FROM AskForLeaveBean a JOIN FETCH a.empBean e WHERE a.employeeId = :employeeId";
		Query<AskForLeaveBean> query = session.createQuery(hql, AskForLeaveBean.class);
		query.setParameter("employeeId", employeeId);
		return query.list();
	}

	// 根據請假編號獲取請假記錄
	public List<AskForLeaveBean> getLeaveRecordsByLeaveId(String leaveId) {
		String hql = "SELECT a FROM AskForLeaveBean a JOIN FETCH a.empBean e WHERE a.leaveId = :leaveId";
		Query<AskForLeaveBean> query = session.createQuery(hql, AskForLeaveBean.class);
		query.setParameter("leaveId", leaveId);
		return query.list();
	}

	// 獲取所有請假記錄
	public List<AskForLeaveBean> getLeaveRecords() {
		String hql = "SELECT a FROM AskForLeaveBean a JOIN FETCH a.empBean";
		Query<AskForLeaveBean> query = session.createQuery(hql, AskForLeaveBean.class);
		return query.list();
	}

	// 刪除請假記錄
	public void deleteLeaveRecord(String leaveId) {
		AskForLeaveBean leave = session.get(AskForLeaveBean.class, leaveId);
		if (leave != null) {
			session.remove(leave);
		}
	}

	//
	public boolean updateLeaveRecord(String leaveIdUpdate, String employeeId, String employeeNameUpdate,
			String startDatetimeUpdate, String endDatetimeUpdate, String leaveCategoryUpdate,
			String reasonOfLeaveUpdate) {
		return false;
	}
	
	
}
