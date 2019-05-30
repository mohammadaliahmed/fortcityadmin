package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 15/10/2018.
 */

public class Temporarymodel {
    String time;
    long purchaseCount;
    float totalCost;
    float totalPurchaseCost;


    public Temporarymodel(String time, long purchaseCount, float totalCost, float totalPurchaseCost) {
        this.time = time;
        this.purchaseCount = purchaseCount;
        this.totalCost = totalCost;
        this.totalPurchaseCost = totalPurchaseCost;
    }

    public float getTotalPurchaseCost() {
        return totalPurchaseCost;
    }

    public void setTotalPurchaseCost(float totalPurchaseCost) {
        this.totalPurchaseCost = totalPurchaseCost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(long purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }
}
