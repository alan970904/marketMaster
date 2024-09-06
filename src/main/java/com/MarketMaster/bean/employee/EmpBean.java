package com.MarketMaster.bean.employee;

import java.time.LocalDate;

public class EmpBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String employeeId;
	private String employeeName;
	private String employeeTel;
	private String employeeIdcard;
	private String employeeEmail;
	private String password;
	private String positionId;
	private LocalDate hiredate;
	private LocalDate resigndate;
	private boolean isFirstLogin;

	public EmpBean() {
	}

	public EmpBean(String employeeId, String employeeName, String employeeTel, String employeeIdcard,
			String employeeEmail, String password, String positionId, LocalDate hiredate, LocalDate resigndate) {
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeTel = employeeTel;
		this.employeeIdcard = employeeIdcard;
		this.employeeEmail = employeeEmail;
		this.password = password;
		this.positionId = positionId;
		this.hiredate = hiredate;
		this.resigndate = resigndate;
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

	public String getEmployeeTel() {
		return employeeTel;
	}

	public void setEmployeeTel(String employeeTel) {
		this.employeeTel = employeeTel;
	}

	public String getEmployeeIdcard() {
		return employeeIdcard;
	}

	public void setEmployeeIdcard(String employeeIdcard) {
		this.employeeIdcard = employeeIdcard;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public LocalDate getHiredate() {
		return hiredate;
	}

	public void setHiredate(LocalDate hiredate) {
		this.hiredate = hiredate;
	}

	public LocalDate getResigndate() {
		return resigndate;
	}

	public void setResigndate(LocalDate resigndate) {
		this.resigndate = resigndate;
	}

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }
}