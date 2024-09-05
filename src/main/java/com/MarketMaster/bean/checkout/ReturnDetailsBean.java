package com.MarketMaster.bean.checkout;

public class ReturnDetailsBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String returnId;
	private String checkoutId;
	private String productId;
	private String reasonForReturn;
	private String numberOfReturn;
	private String productPrice;
	private String returnPrice;
	
	
	public ReturnDetailsBean() {
		super();
	}
	
	public ReturnDetailsBean(String returnId, String checkoutId, String productId, String reasonForReturn,
			String numberOfReturn, String productPrice, String returnPrice) {
		super();
		this.returnId = returnId;
		this.checkoutId = checkoutId;
		this.productId = productId;
		this.reasonForReturn = reasonForReturn;
		this.numberOfReturn = numberOfReturn;
		this.productPrice = productPrice;
		this.returnPrice = returnPrice;
	}

	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	public String getCheckoutId() {
		return checkoutId;
	}
	public void setCheckoutId(String checkoutId) {
		this.checkoutId = checkoutId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getReasonForReturn() {
		return reasonForReturn;
	}
	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}
	public String getNumberOfReturn() {
		return numberOfReturn;
	}
	public void setNumberOfReturn(String numberOfReturn) {
		this.numberOfReturn = numberOfReturn;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getReturnPrice() {
		return returnPrice;
	}
	public void setReturnPrice(String returnPrice) {
		this.returnPrice = returnPrice;
	}

	@Override
	public String toString() {
		return "ReturnDetailsBean [returnId=" + returnId + ", checkoutId=" + checkoutId + ", productId=" + productId
				+ ", reasonForReturn=" + reasonForReturn + ", numberOfReturn=" + numberOfReturn + ", productPrice="
				+ productPrice + ", returnPrice=" + returnPrice + "]";
	}
	
	
	

}
