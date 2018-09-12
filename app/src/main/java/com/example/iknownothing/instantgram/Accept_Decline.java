package com.example.iknownothing.instantgram;

public class Accept_Decline {
    String uid;
    String username;
    String fullname;
    String profileImage;

    public Accept_Decline()
    {

    }
    public Accept_Decline(String uid,String username,String fullname,String profileImage)
    {
        this.uid = uid;
        this.fullname=fullname;
        this.username=username;
        this.profileImage=profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
