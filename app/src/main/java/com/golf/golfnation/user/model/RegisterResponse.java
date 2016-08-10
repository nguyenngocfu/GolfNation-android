package com.golf.golfnation.user.model;

import com.golf.golfnation.common.model.BaseResponse;

/**
 * Created by NgocNQ on 7/13/2015.
 */
public class RegisterResponse extends BaseResponse {
    private UserDetail detail;

    public UserDetail getDetail() {
        return detail;
    }

    public void setDetail(UserDetail detail) {
        this.detail = detail;
    }
}
