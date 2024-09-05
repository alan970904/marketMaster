package com.MarketMaster.bean.restock;

import java.time.LocalDate;

public class RestockBean {
    private String restockId;
    private String employeeId;
    private double restockTotalPrice;
    private LocalDate restockDate;

    public RestockBean() {
    }

    public RestockBean(String restockId, String employeeId, double restockTotalPrice, LocalDate restockDate) {
        this.restockId = restockId;
        this.employeeId = employeeId;
        this.restockTotalPrice = restockTotalPrice;
        this.restockDate = restockDate;
    }

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

    public double getRestockTotalPrice() {
        return restockTotalPrice;
    }

    public void setRestockTotalPrice(double restockTotalPrice) {
        this.restockTotalPrice = restockTotalPrice;
    }

    public LocalDate getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDate restockDate) {
        this.restockDate = restockDate;
    }
}