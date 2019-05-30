package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 27/10/2018.
 */

public class UtilityBillsModel {
    private float electricCityBill, waterBill, internetBill, staffInternetBill, officeTelephoneBill, staffMobileBill, governmentTax;
    private long time;
    private  float total;

    public UtilityBillsModel(float electricCityBill, float waterBill, float internetBill, float staffInternetBill, float officeTelephoneBill, float staffMobileBill,
                             float governmentTax, long time,float total) {
        this.electricCityBill = electricCityBill;
        this.waterBill = waterBill;
        this.internetBill = internetBill;
        this.staffInternetBill = staffInternetBill;
        this.officeTelephoneBill = officeTelephoneBill;
        this.staffMobileBill = staffMobileBill;
        this.governmentTax = governmentTax;
        this.time = time;
        this.total = total;
    }

    public UtilityBillsModel() {
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getElectricCityBill() {
        return electricCityBill;
    }

    public void setElectricCityBill(float electricCityBill) {
        this.electricCityBill = electricCityBill;
    }

    public float getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(float waterBill) {
        this.waterBill = waterBill;
    }

    public float getInternetBill() {
        return internetBill;
    }

    public void setInternetBill(float internetBill) {
        this.internetBill = internetBill;
    }

    public float getStaffInternetBill() {
        return staffInternetBill;
    }

    public void setStaffInternetBill(float staffInternetBill) {
        this.staffInternetBill = staffInternetBill;
    }

    public float getOfficeTelephoneBill() {
        return officeTelephoneBill;
    }

    public void setOfficeTelephoneBill(float officeTelephoneBill) {
        this.officeTelephoneBill = officeTelephoneBill;
    }

    public float getStaffMobileBill() {
        return staffMobileBill;
    }

    public void setStaffMobileBill(float staffMobileBill) {
        this.staffMobileBill = staffMobileBill;
    }

    public float getGovernmentTax() {
        return governmentTax;
    }

    public void setGovernmentTax(float governmentTax) {
        this.governmentTax = governmentTax;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
