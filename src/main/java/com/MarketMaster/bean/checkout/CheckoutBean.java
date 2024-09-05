package com.MarketMaster.bean.checkout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckoutBean {
	private String checkoutId;
	private String customerTel;
	private String employeeId;
	private int checkoutTotalPrice;
	private Date checkoutDate;
	private int bonusPoints;
	private Date pointsDueDate;
	private List<CheckoutDetailsBean> checkoutDetails;

	// Constructors, Getters, and Setters

	public CheckoutBean() {
		super();
	}

	public CheckoutBean(String checkoutId, String customerTel, String employeeId, int checkoutTotalPrice,
			Date checkoutDate, int bonusPoints, Date pointsDueDate, List<CheckoutDetailsBean> checkoutDetails) {
		super();
		this.checkoutId = checkoutId;
		this.customerTel = customerTel;
		this.employeeId = employeeId;
		this.checkoutTotalPrice = checkoutTotalPrice;
		this.checkoutDate = checkoutDate;
		this.bonusPoints = bonusPoints;
		this.pointsDueDate = pointsDueDate;
		this.checkoutDetails = checkoutDetails;
	}

	public String getCheckoutId() {
		return checkoutId;
	}

	public void setCheckoutId(String checkoutId) {
		this.checkoutId = checkoutId;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getCheckoutTotalPrice() {
		return checkoutTotalPrice;
	}

	public void setCheckoutTotalPrice(int checkoutTotalPrice) {
		this.checkoutTotalPrice = checkoutTotalPrice;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public Date getPointsDueDate() {
		return pointsDueDate;
	}

	public void setPointsDueDate(Date pointsDueDate) {
		this.pointsDueDate = pointsDueDate;
	}

	public List<CheckoutDetailsBean> getCheckoutDetails() {
		return checkoutDetails;
	}

	public void setCheckoutDetails(List<CheckoutDetailsBean> checkoutDetails) {
		this.checkoutDetails = checkoutDetails;
	}

	public void addCheckoutDetail(CheckoutDetailsBean detail) {
		if (checkoutDetails == null) {
			checkoutDetails = new ArrayList<>();
		}
		checkoutDetails.add(detail);
	}
}
