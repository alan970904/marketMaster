package com.MarketMaster.bean.employee;
import java.time.LocalDate;
import java.util.Date;

public class CustomerBean {
	
    private String customerTel;  // 顧客手機
    private String customerName; // 顧客姓名
    private String customerEmail; // 顧客電子郵件
    public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public LocalDate getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(LocalDate localDate) {
		this.dateOfRegistration = localDate;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getLastPurchaseDate() {
		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(Date lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}

	private LocalDate dateOfRegistration; // 註冊日期
    private int totalPoints; // 累積紅利點數
    private String address; // 地址
    private Date lastPurchaseDate; // 最後購買日期

    // Constructors, Getters, and Setters
    public CustomerBean() {}

    public CustomerBean(String customerTel, String customerName, String customerEmail,
                    LocalDate dateOfRegistration, int totalPoints, String address, Date lastPurchaseDate) {
        this.customerTel = customerTel;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.dateOfRegistration = dateOfRegistration;
        this.totalPoints = totalPoints;
        this.address = address;
        this.lastPurchaseDate = lastPurchaseDate;
    }

    // Getters and Setters for each field

}
