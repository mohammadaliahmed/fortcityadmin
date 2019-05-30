package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 11/09/2018.
 */

public class BannerPicsModel {
    String id,url,category;
    int position;

    public BannerPicsModel(String id, String url, String category, int position) {
        this.id = id;
        this.url = url;
        this.category = category;
        this.position = position;
    }

    public BannerPicsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
