package com.MarketMaster.bean.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AskForLeaveBean {
    private String leaveId;
    private String employeeId;
    private String employeeName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String leaveCategory;
    private String reasonOfLeave;
    private String approvedStatus;

    public AskForLeaveBean() {
        super();
    }

    public AskForLeaveBean(String leaveId, String employeeId, String employeeName, LocalDateTime startDatetime, LocalDateTime endDatetime,
            String leaveCategory, String reasonOfLeave, String approvedStatus) {
        super();
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.leaveCategory = leaveCategory;
        this.reasonOfLeave = reasonOfLeave;
        this.approvedStatus = approvedStatus;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getLeaveCategory() {
        return leaveCategory;
    }

    public void setLeaveCategory(String leaveCategory) {
        this.leaveCategory = leaveCategory;
    }

    public String getReasonOfLeave() {
        return reasonOfLeave;
    }

    public void setReasonOfLeave(String reasonOfLeave) {
        this.reasonOfLeave = reasonOfLeave;
    }

    public String getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public String getStartDatetimeFormatted() {
        return startDatetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public String getEndDatetimeFormatted() {
        return endDatetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "AskForLeaveBean [leaveId=" + leaveId + ", employeeId=" + employeeId + ", employeeName="
                + employeeName + ", startDatetime=" + startDatetime + ", endDatetime=" + endDatetime
                + ", leaveCategory=" + leaveCategory + ", reasonOfLeave=" + reasonOfLeave + ", approvedStatus="
                + approvedStatus + "]";
    }
}