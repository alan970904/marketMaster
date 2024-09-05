package com.MarketMaster.bean.checkout;

public class CheckoutDetailsBean  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String checkoutId;
    private String productId;
    private int numberOfCheckout;
    private int productPrice;
    private int checkoutPrice;
    
	public CheckoutDetailsBean() {
		super();
	}

	public CheckoutDetailsBean(String checkoutId, String productId, int numberOfCheckout, int productPrice,
			int checkoutPrice) {
		super();
		this.checkoutId = checkoutId;
		this.productId = productId;
		this.numberOfCheckout = numberOfCheckout;
		this.productPrice = productPrice;
		this.checkoutPrice = checkoutPrice;
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

	public int getNumberOfCheckout() {
		return numberOfCheckout;
	}

	public void setNumberOfCheckout(int numberOfCheckout) {
		this.numberOfCheckout = numberOfCheckout;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getCheckoutPrice() {
		return checkoutPrice;
	}

	public void setCheckoutPrice(int checkoutPrice) {
		this.checkoutPrice = checkoutPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "CheckoutDetailsBean [checkoutId=" + checkoutId + ", productId=" + productId + ", numberOfCheckout="
				+ numberOfCheckout + ", productPrice=" + productPrice + ", checkoutPrice=" + checkoutPrice + "]";
	}
    
	
	
}
