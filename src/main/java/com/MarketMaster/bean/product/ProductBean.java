package com.MarketMaster.bean.product;

public class ProductBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String productId;
	private String productCategory;
	private String productName;
	private int productPrice;
	private int productSafeInventory;
	private int numberOfShelve;
	private int numberOfInventory;
	private int numberOfSale;
	private int numberOfExchange;
	private int numberOfDestruction;
	private int numberOfRemove;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public String getProductName() {
		return productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public int getproductSafeInventory() {
		return productSafeInventory;
	}

	public int getNumberOfShelve() {
		return numberOfShelve;
	}

	public int getNumberOfInventory() {
		return numberOfInventory;
	}

	public int getNumberOfSale() {
		return numberOfSale;
	}

	public int getNumberOfExchange() {
		return numberOfExchange;
	}

	public int getNumberOfDestruction() {
		return numberOfDestruction;
	}

	public int getNumberOfRemove() {
		return numberOfRemove;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public void setproductSafeInventory(int productSafeInventory) {
		this.productSafeInventory = productSafeInventory;
	}

	public void setNumberOfShelve(int numberOfShelve) {
		this.numberOfShelve = numberOfShelve;
	}

	public void setNumberOfInventory(int numberOfInventory) {
		this.numberOfInventory = numberOfInventory;
	}

	public void setNumberOfSale(int numberOfSale) {
		this.numberOfSale = numberOfSale;
	}

	public void setNumberOfExchange(int numberOfExchange) {
		this.numberOfExchange = numberOfExchange;
	}

	public void setNumberOfDestruction(int numberOfDestruction) {
		this.numberOfDestruction = numberOfDestruction;
	}

	public void setNumberOfRemove(int numberOfRemove) {
		this.numberOfRemove = numberOfRemove;
	}

	public ProductBean(String productId, String productCategory, String productName, int productPrice,
			int productSafeInventory, int numberOfShelve, int numberOfInventory, int numberOfSale, int numberOfExchange,
			int numberOfDestruction, int numberOfRemove) {
		super();
		this.productId = productId;
		this.productCategory = productCategory;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productSafeInventory = productSafeInventory;
		this.numberOfShelve = numberOfShelve;
		this.numberOfInventory = numberOfInventory;
		this.numberOfSale = numberOfSale;
		this.numberOfExchange = numberOfExchange;
		this.numberOfDestruction = numberOfDestruction;
		this.numberOfRemove = numberOfRemove;
	}

	public ProductBean(String productId, int numberOfShelve, int numberOfInventory) {
		super();
		this.productId = productId;
		this.numberOfShelve = numberOfShelve;
		this.numberOfInventory = numberOfInventory;
	}

	public ProductBean(String productName) {
		super();
		this.productName = productName;
	}

	public ProductBean() {
		super();
	}
	
}