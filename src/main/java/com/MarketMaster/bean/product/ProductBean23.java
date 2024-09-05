package com.MarketMaster.bean.product;

public class ProductBean23 implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String product_id;
	private String product_category;
	private String product_name;
	private int product_price;
	private int product_safeinventory;
	private int Number_of_shelve;
	private int Number_of_inventory;
	private int Number_of_sale;
	private int Number_of_exchange;
	private int Number_of_destruction;
	private int Number_of_remove;

	public String getProduct_id() {
		return product_id;
	}

	public String getProduct_category() {
		return product_category;
	}

	public String getProduct_name() {
		return product_name;
	}

	public int getProduct_price() {
		return product_price;
	}

	public int getProduct_safeinventory() {
		return product_safeinventory;
	}

	public int getNumber_of_shelve() {
		return Number_of_shelve;
	}

	public int getNumber_of_inventory() {
		return Number_of_inventory;
	}

	public int getNumber_of_sale() {
		return Number_of_sale;
	}

	public int getNumber_of_exchange() {
		return Number_of_exchange;
	}

	public int getNumber_of_destruction() {
		return Number_of_destruction;
	}

	public int getNumber_of_remove() {
		return Number_of_remove;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public void setProduct_category(String product_category) {
		this.product_category = product_category;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setProduct_price(int product_price) {
		this.product_price = product_price;
	}

	public void setProduct_safeinventory(int product_safeinventory) {
		this.product_safeinventory = product_safeinventory;
	}

	public void setNumber_of_shelve(int number_of_shelve) {
		Number_of_shelve = number_of_shelve;
	}

	public void setNumber_of_inventory(int number_of_inventory) {
		Number_of_inventory = number_of_inventory;
	}

	public void setNumber_of_sale(int number_of_sale) {
		Number_of_sale = number_of_sale;
	}

	public void setNumber_of_exchange(int number_of_exchange) {
		Number_of_exchange = number_of_exchange;
	}

	public void setNumber_of_destruction(int number_of_destruction) {
		Number_of_destruction = number_of_destruction;
	}

	public void setNumber_of_remove(int number_of_remove) {
		Number_of_remove = number_of_remove;
	}

	public ProductBean23() {
		super();
	}

	public ProductBean23(String product_id) {
		super();
		this.product_id = product_id;
	}

	public ProductBean23(String product_id, String product_category, String product_name, int product_price,
			int product_safeinventory, int number_of_shelve, int number_of_inventory, int number_of_sale,
			int number_of_exchange, int number_of_destruction, int number_of_remove) {
		super();
		this.product_id = product_id;
		this.product_category = product_category;
		this.product_name = product_name;
		this.product_price = product_price;
		this.product_safeinventory = product_safeinventory;
		Number_of_shelve = number_of_shelve;
		Number_of_inventory = number_of_inventory;
		Number_of_sale = number_of_sale;
		Number_of_exchange = number_of_exchange;
		Number_of_destruction = number_of_destruction;
		Number_of_remove = number_of_remove;
	}

	public ProductBean23(String productId, int number_of_shelve, int number_of_inventory) {
		super();
	}

}