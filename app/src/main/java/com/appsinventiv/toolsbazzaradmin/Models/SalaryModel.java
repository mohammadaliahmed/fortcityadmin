package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 26/10/2018.
 */

public class SalaryModel {
    private String id;
    private float basicSalary=0,overTime=0,bonus=0,deduction=0,ETFandEPF=0;
    private String reason;
    private long time;
    private String userId;

    private float total;

    public SalaryModel(String id, float basicSalary, float overTime, float bonus, float deduction,
                       String reason, long time, String userId,float ETFandEPF) {
        this.id = id;
        this.basicSalary = basicSalary;
        this.overTime = overTime;
        this.bonus = bonus;
        this.deduction = deduction;
        this.reason = reason;
        this.time = time;

        this.userId = userId;
        this.ETFandEPF=ETFandEPF;
    }

    public float getTotal() {
        total=(basicSalary+overTime+bonus)-(deduction+ETFandEPF);
        return total;
    }

    public float getETFandEPF() {
        return ETFandEPF;
    }

    public void setETFandEPF(float ETFandEPF) {
        this.ETFandEPF = ETFandEPF;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SalaryModel() {
    }

    public float getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public float getOverTime() {
        return overTime;
    }

    public void setOverTime(float overTime) {
        this.overTime = overTime;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public float getDeduction() {
        return deduction;
    }

    public void setDeduction(float deduction) {
        this.deduction = deduction;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
