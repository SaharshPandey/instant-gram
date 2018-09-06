package com.example.iknownothing.instantgram;

public class Comments {

    public Comments(){

    }

    public Comments(String commenttext,String date,String time,String timestamp,String uid,String username){
        this.commenttext = commenttext;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
        this.uid = uid;
        this.username = username;
    }

    String commenttext,date,time,timestamp,uid,username;

    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
