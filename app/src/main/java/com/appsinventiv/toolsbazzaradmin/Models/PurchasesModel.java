package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 06/09/2018.
 */

public class PurchasesModel {
    String id,orderIds;
    long totalQuantity;

    Product product;
    int quantity;
    long time;




    public PurchasesModel(String id, String orderIds, long totalQuantity, Product product, int quantity, long time) {
        this.id = id;
        this.orderIds = orderIds;
        this.totalQuantity = totalQuantity;
        this.product = product;
        this.quantity = quantity;
        this.time = time;
    }

    public PurchasesModel() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
