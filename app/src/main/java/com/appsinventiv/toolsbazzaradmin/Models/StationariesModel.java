package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 27/10/2018.
 */

public class StationariesModel {
    private float officeStationaries, staffStationaries, advertising, billBooksPrint, packingMaterial, businessCardPrint;
    private long time;
    private float total;

    public StationariesModel(float officeStationaries, float staffStationaries, float advertising, float billBooksPrint, float packingMaterial, float businessCardPrint, long time,
                             float total) {
        this.officeStationaries = officeStationaries;
        this.staffStationaries = staffStationaries;
        this.advertising = advertising;
        this.billBooksPrint = billBooksPrint;
        this.packingMaterial = packingMaterial;
        this.businessCardPrint = businessCardPrint;
        this.time = time;
        this.total = total;
    }

    public StationariesModel() {
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getOfficeStationaries() {
        return officeStationaries;
    }

    public void setOfficeStationaries(float officeStationaries) {
        this.officeStationaries = officeStationaries;
    }

    public float getStaffStationaries() {
        return staffStationaries;
    }

    public void setStaffStationaries(float staffStationaries) {
        this.staffStationaries = staffStationaries;
    }

    public float getAdvertising() {
        return advertising;
    }

    public void setAdvertising(float advertising) {
        this.advertising = advertising;
    }

    public float getBillBooksPrint() {
        return billBooksPrint;
    }

    public void setBillBooksPrint(float billBooksPrint) {
        this.billBooksPrint = billBooksPrint;
    }

    public float getPackingMaterial() {
        return packingMaterial;
    }

    public void setPackingMaterial(float packingMaterial) {
        this.packingMaterial = packingMaterial;
    }

    public float getBusinessCardPrint() {
        return businessCardPrint;
    }

    public void setBusinessCardPrint(float businessCardPrint) {
        this.businessCardPrint = businessCardPrint;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
