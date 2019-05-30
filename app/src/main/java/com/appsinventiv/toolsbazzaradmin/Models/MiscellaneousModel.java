package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 27/10/2018.
 */

public class MiscellaneousModel {
    private String remarks1, remarks2, remarks3, remarks4, remarks5;
    private float cost1, cost2, cost3, cost4, cost5;
    private long time;
    private float total;
    public MiscellaneousModel(String remarks1, String remarks2, String remarks3, String remarks4,
                              String remarks5, float cost1, float cost2, float cost3, float cost4,
                              float cost5, long time,float total) {
        this.remarks1 = remarks1;
        this.remarks2 = remarks2;
        this.remarks3 = remarks3;
        this.remarks4 = remarks4;
        this.remarks5 = remarks5;
        this.cost1 = cost1;
        this.cost2 = cost2;
        this.cost3 = cost3;
        this.cost4 = cost4;
        this.cost5 = cost5;
        this.time = time;
        this.total=total;
    }

    public MiscellaneousModel() {
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarks3() {
        return remarks3;
    }

    public void setRemarks3(String remarks3) {
        this.remarks3 = remarks3;
    }

    public String getRemarks4() {
        return remarks4;
    }

    public void setRemarks4(String remarks4) {
        this.remarks4 = remarks4;
    }

    public String getRemarks5() {
        return remarks5;
    }

    public void setRemarks5(String remarks5) {
        this.remarks5 = remarks5;
    }

    public float getCost1() {
        return cost1;
    }

    public void setCost1(float cost1) {
        this.cost1 = cost1;
    }

    public float getCost2() {
        return cost2;
    }

    public void setCost2(float cost2) {
        this.cost2 = cost2;
    }

    public float getCost3() {
        return cost3;
    }

    public void setCost3(float cost3) {
        this.cost3 = cost3;
    }

    public float getCost4() {
        return cost4;
    }

    public void setCost4(float cost4) {
        this.cost4 = cost4;
    }

    public float getCost5() {
        return cost5;
    }

    public void setCost5(float cost5) {
        this.cost5 = cost5;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
