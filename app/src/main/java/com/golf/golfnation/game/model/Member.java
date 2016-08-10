package com.golf.golfnation.game.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NgocNQ on 7/16/2015.
 */
public class Member {
    @SerializedName("User_Id")
    private String id;
    private String email;
    @SerializedName("User_Name")
    private String realUserName;
    private String firstname;
    private String lastname;
    private String username;
    @SerializedName("zip_code")
    private String zipcode;
    private String location;
    private String photoName;

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    private int prize;

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    private boolean winner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getRealUserName() {
        return realUserName;
    }

    public void setRealUserName(String realUserName) {
        this.realUserName = realUserName;
    }
}
