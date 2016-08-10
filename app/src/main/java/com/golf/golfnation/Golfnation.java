package com.golf.golfnation;

import android.app.Application;

import com.golf.golfnation.user.model.UserDetail;

/**
 * Created by NgocNQ on 7/13/2015.
 */
public class Golfnation extends Application {
    private UserDetail userDetail;

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }
}
