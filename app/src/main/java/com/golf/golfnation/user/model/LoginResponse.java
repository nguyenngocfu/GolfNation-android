package com.golf.golfnation.user.model;

import com.golf.golfnation.common.model.BaseResponse;

/**
 * Created by NgocNQ on 7/13/2015.
 */
public class LoginResponse extends BaseResponse {
    private UserDetail details;

    public UserDetail getDetails() {
        return details;
    }

    public void setDetails(UserDetail details) {
        this.details = details;
    }
}
