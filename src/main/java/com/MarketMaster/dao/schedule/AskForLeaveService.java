package com.MarketMaster.dao.schedule;

import com.MarketMaster.bean.schedule.AskForLeaveBean;
import java.util.List;

public interface AskForLeaveService {
	String generateNextLeaveId();

	String getEmployeeNameById(String employeeId);

	void addLeaveRecordById(AskForLeaveBean leave);

	void updateLeaveRecord(AskForLeaveBean leave);

	List<AskForLeaveBean> getLeaveRecordsById(String employeeId);

	List<AskForLeaveBean> getLeaveRecordsByLeaveId(String leaveId);

	List<AskForLeaveBean> getLeaveRecords();

	void deleteLeaveRecord(String leaveId);


}