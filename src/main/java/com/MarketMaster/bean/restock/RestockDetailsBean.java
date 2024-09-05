package com.MarketMaster.bean.restock;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RestockDetailsBean {
    private String restockId;
    private String employeeId;
    private LocalDate restockDate;
    private String productId;
    private String productName;
    private int numberOfRestock;
    private BigDecimal productPrice;
    private BigDecimal restockTotalPrice;
    private LocalDate productionDate;
    private LocalDate dueDate;

    // Constructor
    public RestockDetailsBean() {}

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

    public LocalDate getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDate restockDate) {
        this.restockDate = restockDate;
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

    public int getNumberOfRestock() {
        return numberOfRestock;
    }

    public void setNumberOfRestock(int numberOfRestock) {
        this.numberOfRestock = numberOfRestock;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getRestockTotalPrice() {
        return restockTotalPrice;
    }

    public void setRestockTotalPrice(BigDecimal restockTotalPrice) {
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

    @Override
    public String toString() {
        return "RestockDetailBean{" +
                "restockId='" + restockId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", restockDate=" + restockDate +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", numberOfRestock=" + numberOfRestock +
                ", productPrice=" + productPrice +
                ", restockTotalPrice=" + restockTotalPrice +
                ", productionDate=" + productionDate +
                ", dueDate=" + dueDate +
                '}';
    }
}