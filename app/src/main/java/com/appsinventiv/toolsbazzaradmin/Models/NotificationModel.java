package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 01/07/2018.
 */

public class NotificationModel {
    String from,title,message,type;
    long time;

    public NotificationModel(String from, String title, String message, String type, long time) {
        this.from = from;
        this.title = title;
        this.message = message;
        this.type = type;
        this.time = time;
    }

    public NotificationModel() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
