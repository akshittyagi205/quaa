package com.quanutrition.app.chat;

import com.quanutrition.app.Utils.Tools;

public class ChatMessageModel implements Comparable {

    private String sender,message,timestamp,imageurl,fileflag;
    private String key;
    public ChatMessageModel(){

    }
    public ChatMessageModel(String sender, String message, String timestamp, String imageurl, String fileflag) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.imageurl = imageurl;
        this.fileflag = fileflag;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getFileflag() {
        return fileflag;
    }

    public void setFileflag(String fileflag) {
        this.fileflag = fileflag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Object compareTu) {
        long compareage= Tools.getDateTimeInMilli(((ChatMessageModel)compareTu).getTimestamp().split(" ")[0],((ChatMessageModel)compareTu).getTimestamp().split(" ")[1]);
        /* For Ascending order*/
        return (int)(Tools.getDateTimeInMilli(this.getTimestamp().split(" ")[0],this.getTimestamp().split(" ")[1])-compareage);
    }
}
