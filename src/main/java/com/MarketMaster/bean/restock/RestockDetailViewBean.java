package com.MarketMaster.bean.restock;

import java.math.BigDecimal;
import java.util.Date;

public class RestockDetailViewBean {
    private String restockId;
    private String employeeId;
    private String productId;
    private String productName;
    private String productCategory;
    private int numberOfRestock;
    private BigDecimal restockPrice;
    private Date restockDate;
    private Date productionDate;
    private Date dueDate;

    // Constructors
    public RestockDetailViewBean() {}

    // Getters and Setters
    public String getRestockId() {
        return restockId;
    }

    public void setRestockId(String restockId) {
        this.restockId = restockId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getNumberOfRestock() {
        return numberOfRestock;
    }

    public void setNumberOfRestock(int numberOfRestock) {
        this.numberOfRestock = numberOfRestock;
    }

    public BigDecimal getRestockPrice() {
        return restockPrice;
    }

    public void setRestockPrice(BigDecimal restockPrice) {
        this.restockPrice = restockPrice;
    }

    public Date getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(Date restockDate) {
        this.restockDate = restockDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "RestockDetailViewBean{" +
                "restockId='" + restockId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", numberOfRestock=" + numberOfRestock +
                ", restockPrice=" + restockPrice +
                ", restockDate=" + restockDate +
                ", productionDate=" + productionDate +
                ", dueDate=" + dueDate +
                '}';
    }
}