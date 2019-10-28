package com.appsinventiv.toolsbazzaradmin.Models;

import java.util.List;

/**
 * Created by AliAh on 10/09/2018.
 */

public class Employee {
    String username, name, email, password, phone, fcmKey,picUrl;
    String role;
    long time;
    int code;
    boolean codeVerified;
    boolean active;
    boolean approved;
    boolean blocked;
    List<String> roles;
    long salary;


    public Employee(String username, String name, String email, String password, String phone, String fcmKey, String role,
                    long time, int code,
                    boolean codeVerified, boolean active,boolean approved,boolean blocked) {
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
        this.blocked=blocked;

    }


    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
