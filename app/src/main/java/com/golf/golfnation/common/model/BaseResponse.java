package com.golf.golfnation.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 7/13/2015.
 */
public class BaseResponse {
    @SerializedName("Status")
    private String status;
    @SerializedName("Message")
    private String message;

    public BaseResponse(){}

    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
