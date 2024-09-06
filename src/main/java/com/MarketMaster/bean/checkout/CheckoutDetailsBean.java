package com.MarketMaster.bean.checkout;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "CHECKOUT_DETAILS")
public class CheckoutDetailsBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private String checkoutId;
	
	@Column(name = "NUMBER_OF_CHECKOUT")
    private String productId;
	
	@Column(name = "PRODUCT_PRICE")
    private int numberOfCheckout;
	
	@Column(name = "CHECKOUT_PRICE")
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
