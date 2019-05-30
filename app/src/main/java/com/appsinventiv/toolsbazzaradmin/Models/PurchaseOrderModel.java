package com.appsinventiv.toolsbazzaradmin.Models;

import java.util.ArrayList;

/**
 * Created by AliAh on 07/09/2018.
 */

public class PurchaseOrderModel {
    String id;
    ArrayList<ProductCountModel> productsList;
    VendorModel vendor;
    long total;
    long time;
    boolean isCancelled;
    String employeeName;
    String poNumber;

    public PurchaseOrderModel(String id, ArrayList<ProductCountModel> productsList, VendorModel vendor,
                              long total, long time, boolean isCancelled,
                              String employeeName,
                              String poNumber
    ) {
        this.id = id;
        this.productsList = productsList;
        this.vendor = vendor;
        this.total = total;
        this.time = time;
        this.isCancelled = isCancelled;
        this.employeeName = employeeName;
        this.poNumber=poNumber;
    }


    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public PurchaseOrderModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ProductCountModel> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<ProductCountModel> productsList) {
        this.productsList = productsList;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
