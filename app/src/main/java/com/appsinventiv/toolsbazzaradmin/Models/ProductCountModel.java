package com.appsinventiv.toolsbazzaradmin.Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AliAh on 20/06/2018.
 */

public class ProductCountModel {
    Product product;
    int quantity;
    long time;
    private HashMap<String,String> orderId;

    int quantityPurchased;
    int outOfStock;
    float newCostPrice=-1;

    float purchaseTotal;
    boolean purchased;
    String size;
    String color;
    int isSelected=0;

    public ProductCountModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCountModel model = (ProductCountModel) o;
        return product != null ? product.equals(model.product) : model.product == null;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;

        return result;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public int getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public int getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(int outOfStock) {
        this.outOfStock = outOfStock;
    }

    public float getNewCostPrice() {
        return newCostPrice;
    }

    public void setNewCostPrice(float newCostPrice) {
        this.newCostPrice = newCostPrice;
    }

    public float getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(float purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public ProductCountModel(Product product, int quantity, long time, HashMap<String, String> orderId) {
        this.product = product;
        this.quantity = quantity;
        this.time = time;
        this.orderId= orderId;
    }

    public HashMap<String, String> getOrderId() {
        return orderId;
    }

    public void setOrderId(HashMap<String, String> orderId) {
        this.orderId = orderId;
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
