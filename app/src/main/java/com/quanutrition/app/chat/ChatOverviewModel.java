package com.quanutrition.app.chat;


import com.quanutrition.app.Utils.Tools;

public class ChatOverviewModel implements Comparable {
    private String message,timestamp,sender,name;
    private String read,image,custom_id;
    public ChatOverviewModel(){

    }
    public ChatOverviewModel(String message, String timestamp, String read, String sender, String name, String image, String custom_id) {
        this.read = read;
        this.message = message;
        this.timestamp = timestamp;
        this.sender = sender;
        this.name = name;
        this.image = image;
        this.custom_id = custom_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String isRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(String custom_id) {
        this.custom_id = custom_id;
    }

    @Override
    public int compareTo(Object compareTu) {
        long compareage= Tools.getDateTimeInMilli(((ChatOverviewModel)compareTu).getTimestamp().split(" ")[0],((ChatOverviewModel)compareTu).getTimestamp().split(" ")[1]);
        //For Ascending order
        return (int)(compareage-Tools.getDateTimeInMilli(this.getTimestamp().split(" ")[0],this.getTimestamp().split(" ")[1]));
    }
}
