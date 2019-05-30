package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 27/10/2018.
 */

public class RentModel {
    private float officeRent,rentACar;
    private long time;
    private float total;

    public RentModel(float officeRent, float rentACar, long time,float total) {
        this.officeRent = officeRent;
        this.rentACar = rentACar;
        this.time = time;
        this.total=total;
    }

    public RentModel() {
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getOfficeRent() {
        return officeRent;
    }

    public void setOfficeRent(float officeRent) {
        this.officeRent = officeRent;
    }

    public float getRentACar() {
        return rentACar;
    }

    public void setRentACar(float rentACar) {
        this.rentACar = rentACar;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
