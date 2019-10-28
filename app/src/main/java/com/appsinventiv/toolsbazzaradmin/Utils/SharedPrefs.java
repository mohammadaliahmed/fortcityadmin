package com.appsinventiv.toolsbazzaradmin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.google.gson.Gson;

import java.util.HashMap;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }

    public static void setCommentsCount(HashMap<String, Double> model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("commentsCount", json);
    }

    public static HashMap<String, Double> getCommentsCount() {
        Gson gson = new Gson();
        HashMap<String, Double> map = gson.fromJson(preferenceGetter("commentsCount"), HashMap.class);
        return map;
    }


    public static void setEmployee(Employee model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("employeeModel", json);
    }

    public static Employee getEmployee() {
        Gson gson = new Gson();
        Employee employee = gson.fromJson(preferenceGetter("employeeModel"), Employee.class);
        return employee;
    }

    public static void setCompanyDetails(CompanyDetailsModel model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("setCompanyDetails", json);
    }

    public static CompanyDetailsModel getCompanyDetails() {
        Gson gson = new Gson();
        CompanyDetailsModel employee = gson.fromJson(preferenceGetter("setCompanyDetails"), CompanyDetailsModel.class);
        return employee;
    }


    public static void setRole(String count) {
        preferenceSetter("userRole", count);
    }

    public static String getRole() {
        return preferenceGetter("userRole");
    }

    public static void setOrderCount(String count) {
        preferenceSetter("setOrderCount", count);
    }

    public static String getOrderCount() {
        return preferenceGetter("setOrderCount");
    }

    public static void setSellerProductCount(String count) {
        preferenceSetter("SellerProductCount", count);
    }

    public static String getSellerProductCount() {
        return preferenceGetter("SellerProductCount");
    }

    public static void setChatCount(String count) {
        preferenceSetter("setChatCount", count);
    }

    public static String getChatCount() {
        return preferenceGetter("setChatCount");
    }


    public static void setCartCount(String count) {
        preferenceSetter("cartCount", count);
    }

    public static String getCartCount() {
        return preferenceGetter("cartCount");
    }

    public static void setUsername(String username) {
        preferenceSetter("username", username);
    }

    public static String getUsername() {
        return preferenceGetter("username");
    }


    public static void setFullName(String username) {
        preferenceSetter("name", username);
    }

    public static String getFullName() {
        return preferenceGetter("name");
    }


    public static void setIsLoggedIn(String value) {

        preferenceSetter("isLoggedIn", value);
    }

    public static String getIsLoggedIn() {
        return preferenceGetter("isLoggedIn");
    }


    public static void setFcmKey(String fcmKey) {
        preferenceSetter("fcmKey", fcmKey);
    }

    public static String getFcmKey() {
        return preferenceGetter("fcmKey");
    }


    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }
}
