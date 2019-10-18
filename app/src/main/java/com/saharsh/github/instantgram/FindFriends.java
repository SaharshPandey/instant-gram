package com.saharsh.github.instantgram;

public class FindFriends {
    public String profileImage,fullname,username;

    public FindFriends()
    {

    }

    public FindFriends(String profileImage, String fullname, String username)
    {

        this.profileImage = profileImage;
        this.fullname = fullname;
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
