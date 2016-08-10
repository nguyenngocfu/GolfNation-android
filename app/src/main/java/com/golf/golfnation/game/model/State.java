package com.golf.golfnation.game.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by NgocNQ on 8/21/2015.
 */
public class State implements Serializable{
    @SerializedName("state_id")
    private String stateId;
    @SerializedName("state_name")
    private String stateName;
    @SerializedName("state_code")
    private String stateCode;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
