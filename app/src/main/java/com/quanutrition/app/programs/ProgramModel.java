package com.quanutrition.app.programs;

public class ProgramModel {
    String id,name,imageLink;
    boolean hasImage;

    public ProgramModel(String id, String name, String imageLink, boolean hasImage) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.hasImage = hasImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
}
