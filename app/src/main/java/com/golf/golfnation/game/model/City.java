package com.golf.golfnation.game.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by NgocNQ on 8/21/2015.
 */
public class City implements Serializable{
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("city_name")
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
