package com.MarketMaster.bean.checkout;

public class ReturnProductBean  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String returnId;
    private String employeeId;
    private String returnTotalPrice;
    private String returnDate;

	public ReturnProductBean() {
		super();
	}

	public ReturnProductBean(String returnId, String employeeId, String returnTotalPrice, String returnDate) {
		super();
		this.returnId = returnId;
		this.employeeId = employeeId;
		this.returnTotalPrice = returnTotalPrice;
		this.returnDate = returnDate;
	}

	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getReturnTotalPrice() {
		return returnTotalPrice;
	}
	public void setReturnTotalPrice(String returnTotalPrice) {
		this.returnTotalPrice = returnTotalPrice;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public String toString() {
		return "ReturnProductBean [returnId=" + returnId + ", employeeId=" + employeeId + ", returnTotalPrice="
				+ returnTotalPrice + ", returnDate=" + returnDate + "]";
	}



}
