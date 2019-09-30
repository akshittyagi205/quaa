package com.quanutrition.app.general;

public class ReferModel {
    private String name,image,joined,status;

    public ReferModel(String name, String image, String joined, String status) {
        this.name = name;
        this.image = image;
        this.joined = joined;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
