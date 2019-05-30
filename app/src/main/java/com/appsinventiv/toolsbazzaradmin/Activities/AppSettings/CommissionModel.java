package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

public class CommissionModel {
    String id,categoryName;
    int commission;

    public CommissionModel(String id, String categoryName, int commission) {
        this.id = id;
        this.categoryName = categoryName;
        this.commission = commission;
    }

    public CommissionModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }
}
