package com.example.iknownothing.instantgram;

public class UserPosts {

    public String uid;
    public String postimage;
    public String timestamp;

    public UserPosts()
    {}

    public UserPosts(String uid,String postimage,String timestamp)
    {
        this.uid = uid;
        this.postimage = postimage;
        this.timestamp = timestamp;
    }
}
