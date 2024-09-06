package com.MarketMaster.bean.employee;

public class RankLevelBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String positionId;
    private String positionName;
    private int limitsOfAuthority;
    private int activeEmployeeCount;
    private int totalEmployeeCount;

    public RankLevelBean() {
    }

    public RankLevelBean(String positionId, String positionName, int limitsOfAuthority, int activeEmployeeCount, int totalEmployeeCount) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.limitsOfAuthority = limitsOfAuthority;
        this.activeEmployeeCount = activeEmployeeCount;
        this.totalEmployeeCount = totalEmployeeCount;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getLimitsOfAuthority() {
        return limitsOfAuthority;
    }

    public void setLimitsOfAuthority(int limitsOfAuthority) {
        this.limitsOfAuthority = limitsOfAuthority;
    }

    public int getActiveEmployeeCount() {
        return activeEmployeeCount;
    }

    public void setActiveEmployeeCount(int activeEmployeeCount) {
        this.activeEmployeeCount = activeEmployeeCount;
    }

    public int getTotalEmployeeCount() {
        return totalEmployeeCount;
    }

    public void setTotalEmployeeCount(int totalEmployeeCount) {
        this.totalEmployeeCount = totalEmployeeCount;
    }
}