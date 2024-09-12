package com.MarketMaster.bean.restock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "restocks")
public class RestockBean {
    @Id
    @Column(name = "restock_id")
    private String restockId;



    @Column(name = "restock_total_price")
    private double restockTotalPrice;

    @Column(name = "restock_date")
    private LocalDate restockDate;

    @OneToMany(mappedBy = "restock")
    private List<RestockDetailsBean> restockDetails = new ArrayList<>();

    @Column(name = "employee_id", insertable = false, updatable = false)
    private String employeeId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
        if (this.employee == null) {
            this.employee = new Employee();
            this.employee.setEmployeeId(employeeId);
        }
    }
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

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