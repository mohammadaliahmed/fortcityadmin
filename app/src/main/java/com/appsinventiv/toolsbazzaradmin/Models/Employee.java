package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 10/09/2018.
 */

public class Employee {
    String username, name, email, password, phone, fcmKey,picUrl;
    int role;
    long time;
    int code;
    boolean codeVerified;
    boolean active;
    boolean approved;


    public Employee(String username, String name, String email, String password, String phone, String fcmKey, int role,
                    long time, int code,
                    boolean codeVerified, boolean active,boolean approved) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.fcmKey = fcmKey;
        this.role = role;
        this.time = time;
        this.code = code;
        this.codeVerified = codeVerified;
        this.active = active;
        this.approved=approved;

    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isCodeVerified() {
        return codeVerified;
    }

    public void setCodeVerified(boolean codeVerified) {
        this.codeVerified = codeVerified;
    }

    public Employee() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
