package com.MarketMaster.viewModel;

import java.time.LocalDate;

//EmployeeViewModel 類：用於在表現層展示豐富的員工信息
public class EmployeeViewModel{
	private String employeeId;
    private String employeeName;
    private String employeeTel;
    private String employeeIdcard;
    private String employeeEmail;
    private String positionName;  // 職位名稱替代職位編號
    private String salaryLevel;   
    private LocalDate hiredate;
    private LocalDate resigndate;
    
    public EmployeeViewModel() {}

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

    public String getPositionName() {
    	return positionName;
    	}
    
    public void setPositionName(String positionName) {
    	this.positionName = positionName;
    	}

    public String getSalaryLevel() {
    	return salaryLevel;
    	}
    
    public void setSalaryLevel(String salaryLevel) {
    	this.salaryLevel = salaryLevel;
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
}