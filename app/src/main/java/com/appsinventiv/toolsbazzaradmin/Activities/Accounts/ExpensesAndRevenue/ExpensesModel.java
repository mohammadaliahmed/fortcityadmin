package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

/**
 * Created by AliAh on 03/11/2018.
 */

public class ExpensesModel {
    String key,leftText,rightText,which,time;

    public ExpensesModel(String key, String leftText, String rightText, String which, String time) {
        this.key = key;
        this.leftText = leftText;
        this.rightText = rightText;
        this.which = which;
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public String getWhich() {
        return which;
    }

    public void setWhich(String which) {
        this.which = which;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
