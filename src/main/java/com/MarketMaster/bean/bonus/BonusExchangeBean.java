package com.MarketMaster.bean.bonus;
import java.util.Date;

public class BonusExchangeBean {
    private String exchangeId;
    private String customerTel;
    private String productId;
    private int usePoints;
    private int numberOfExchange;
    private Date exchangeDate;

    // Constructors, Getters, and Setters
    public BonusExchangeBean() {}

    public BonusExchangeBean(String exchangeId, String customerTel, String productId,
                         int usePoints, int numberOfExchange, Date exchangeDate) {
        this.exchangeId = exchangeId;
        this.customerTel = customerTel;
        this.productId = productId;
        this.usePoints = usePoints;
        this.numberOfExchange = numberOfExchange;
        this.exchangeDate = exchangeDate;
    }

    // Getters and Setters for each field
    public String getExchangeId() { return exchangeId; }
    public void setExchangeId(String exchangeId) { this.exchangeId = exchangeId; }
    public String getCustomerTel() { return customerTel; }
    public void setCustomerTel(String customerTel) { this.customerTel = customerTel; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getUsePoints() { return usePoints; }
    public void setUsePoints(int usePoints) { this.usePoints = usePoints; }
    public int getNumberOfExchange() { return numberOfExchange; }
    public void setNumberOfExchange(int numberOfExchange) { this.numberOfExchange = numberOfExchange; }
    public Date getExchangeDate() { return exchangeDate; }
    public void setExchangeDate(Date exchangeDate) { this.exchangeDate = exchangeDate; }
}
