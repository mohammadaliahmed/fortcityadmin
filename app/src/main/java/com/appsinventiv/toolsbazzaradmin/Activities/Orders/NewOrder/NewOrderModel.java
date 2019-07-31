package com.appsinventiv.toolsbazzaradmin.Activities.Orders.NewOrder;

public class NewOrderModel {
    String storeId,storeName,status,imageUrl;
    int count;

    public NewOrderModel(String storeId, String storeName, String status, String imageUrl, int count) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.status = status;
        this.imageUrl = imageUrl;
        this.count = count;
    }

    public NewOrderModel() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
