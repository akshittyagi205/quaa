package com.quanutrition.app.blogs;

public class TestimonialModel {
    private String id;
    private String title;

    public TestimonialModel(String id, String title, String user, String content, String image, String addedOn) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.content = content;
        this.image = image;
        this.addedOn = addedOn;
    }

    public TestimonialModel(String id, String title, String user, String userImage, String content, String image, String addedOn) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.userImage = userImage;
        this.content = content;
        this.image = image;
        this.addedOn = addedOn;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getAddedOn() {
        return addedOn;
    }

    private String user;
    private String userImage;
    private String content;
    private String image;
    private String addedOn;
}
