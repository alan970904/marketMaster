package com.MarketMaster.bean.checkout;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "return_products")
public class ReturnProductBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "return_id")
    private String returnId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "return_total_price")
    private Integer returnTotalPrice;

    @Column(name = "return_date")
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    public ReturnProductBean() {
        super();
    }

    public ReturnProductBean(String returnId, String employeeId, Integer returnTotalPrice, Date returnDate) {
        super();
        this.returnId = returnId;
        this.employeeId = employeeId;
        this.returnTotalPrice = returnTotalPrice;
        this.returnDate = returnDate;
    }

    // Getters and setters
    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getReturnTotalPrice() {
        return returnTotalPrice;
    }

    public void setReturnTotalPrice(Integer returnTotalPrice) {
        this.returnTotalPrice = returnTotalPrice;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "ReturnProductBean [returnId=" + returnId + ", employeeId=" + employeeId + ", returnTotalPrice="
                + returnTotalPrice + ", returnDate=" + returnDate + "]";
    }
}