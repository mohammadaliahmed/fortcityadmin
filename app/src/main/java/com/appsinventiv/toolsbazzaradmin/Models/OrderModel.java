package com.appsinventiv.toolsbazzaradmin.Models;

import java.util.ArrayList;

/**
 * Created by AliAh on 20/06/2018.
 */

public class OrderModel {
    String orderId;
    long time;
    Customer customer;
    ArrayList<ProductCountModel> countModelArrayList;
    float totalPrice;
    String instructions;
    String date,chosenTime;
    String orderStatus;
    long invoiceNumber;
    boolean invoiced;
    String trackingNumber;
    String carrier;
    float shippingCharges,deliveryCharges;
    String deliveryBy,receiverName,receiverNameCredit,creditDueDate;
    String orderFor;

    VendorModel vendor;




    public OrderModel() {
    }

    public OrderModel(String orderId, long time, Customer customer, ArrayList<ProductCountModel> countModelArrayList, float totalPrice, String instructions, String date, String chosenTime, String orderStatus, boolean invoiced, long invoiceNumber) {
        this.orderId = orderId;
        this.time = time;
        this.customer = customer;
        this.countModelArrayList = countModelArrayList;
        this.totalPrice = totalPrice;
        this.instructions = instructions;
        this.date = date;
        this.chosenTime = chosenTime;
        this.orderStatus = orderStatus;
        this.invoiced = invoiced;
        this.invoiceNumber = invoiceNumber;
    }



    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }

    public String getOrderFor() {
        return orderFor;
    }

    public void setOrderFor(String orderFor) {
        this.orderFor = orderFor;
    }

    public String getReceiverNameCredit() {
        return receiverNameCredit;
    }

    public void setReceiverNameCredit(String receiverNameCredit) {
        this.receiverNameCredit = receiverNameCredit;
    }

    public String getCreditDueDate() {
        return creditDueDate;
    }

    public void setCreditDueDate(String creditDueDate) {
        this.creditDueDate = creditDueDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDeliveryBy() {
        return deliveryBy;
    }

    public void setDeliveryBy(String deliveryBy) {
        this.deliveryBy = deliveryBy;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Float getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(Float shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public Float getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Float deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public boolean isInvoiced() {
        return invoiced;
    }

    public void setInvoiced(boolean invoiced) {
        this.invoiced = invoiced;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<ProductCountModel> getCountModelArrayList() {
        return countModelArrayList;
    }

    public void setCountModelArrayList(ArrayList<ProductCountModel> countModelArrayList) {
        this.countModelArrayList = countModelArrayList;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}