package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.SODetails;

public class NewSOModel {
    String shippingId,shippingName,status,imageUrl;
    int count;

    public NewSOModel(String shippingId, String shippingName, String status, String imageUrl, int count) {
        this.shippingId = shippingId;
        this.shippingName = shippingName;
        this.status = status;
        this.imageUrl = imageUrl;
        this.count = count;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
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
