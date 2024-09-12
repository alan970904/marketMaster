package com.MarketMaster.bean.restock;

import java.time.LocalDate;

import com.MarketMaster.bean.product.ProductBean;
import jakarta.persistence.*;

@Entity
@Table(name = "restock_details")
public class RestockDetailsBean {
    @Id
    @Column(name = "restock_id")
    private String restockId;  // 主鍵，同時也是外鍵

    @ManyToOne
    @JoinColumn(name = "restock_id", insertable = false, updatable = false)
    private RestockBean restock;

    @Column(name = "product_id")
    private String productId;  // 外鍵

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductBean product;

    @Column(name = "number_of_restock")
    private int numberOfRestock;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "restock_price")
    private int productPrice;

    @Column(name = "restock_total_price")
    private int restockTotalPrice;

    @Column(name = "production_date")
    private LocalDate productionDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "RESTOCK_DATE")
    private LocalDate restockDate;

    // Constructor
    public RestockDetailsBean() {}

    // Getters and Setters
    public String getRestockId() {
        return restockId;
    }

    public void setRestockId(String restockId) {
        this.restockId = restockId;
    }

    public RestockBean getRestock() {
        return restock;
    }

    public void setRestock(RestockBean restock) {
        this.restock = restock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNumberOfRestock() {
        return numberOfRestock;
    }

    public void setNumberOfRestock(int numberOfRestock) {
        this.numberOfRestock = numberOfRestock;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getRestockTotalPrice() {
        return restockTotalPrice;
    }

    public void setRestockTotalPrice(int restockTotalPrice) {
        this.restockTotalPrice = restockTotalPrice;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDate restockDate) {
        this.restockDate = restockDate;
    }

    @Override
    public String toString() {
        return "RestockDetailBean{" +
                "restockId='" + restockId + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", numberOfRestock=" + numberOfRestock +
                ", productPrice=" + productPrice +
                ", restockTotalPrice=" + restockTotalPrice +
                ", productionDate=" + productionDate +
                ", dueDate=" + dueDate +
                ", restockDate=" + restockDate +
                '}';
    }
}