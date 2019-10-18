package com.saharsh.github.instantgram;

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

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
