package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 27/10/2018.
 */

public class TransportationModel {
    private  float officeTransportation, staffTransportation, officeFuel, staffFuel, shipping, delivery;
    private long time;
    private float total;

    public TransportationModel(float officeTransportation, float staffTransportation, float officeFuel, float staffFuel, float shipping, float delivery, long time
    ,float total
    ) {
        this.officeTransportation = officeTransportation;
        this.staffTransportation = staffTransportation;
        this.officeFuel = officeFuel;
        this.staffFuel = staffFuel;
        this.shipping = shipping;
        this.delivery = delivery;
        this.time = time;
        this.total = total;
    }

    public TransportationModel() {
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getOfficeTransportation() {
        return officeTransportation;
    }

    public void setOfficeTransportation(float officeTransportation) {
        this.officeTransportation = officeTransportation;
    }

    public float getStaffTransportation() {
        return staffTransportation;
    }

    public void setStaffTransportation(float staffTransportation) {
        this.staffTransportation = staffTransportation;
    }

    public float getOfficeFuel() {
        return officeFuel;
    }

    public void setOfficeFuel(float officeFuel) {
        this.officeFuel = officeFuel;
    }

    public float getStaffFuel() {
        return staffFuel;
    }

    public void setStaffFuel(float staffFuel) {
        this.staffFuel = staffFuel;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public float getDelivery() {
        return delivery;
    }

    public void setDelivery(float delivery) {
        this.delivery = delivery;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
