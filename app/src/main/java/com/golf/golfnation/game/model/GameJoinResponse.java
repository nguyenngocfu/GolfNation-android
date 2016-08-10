package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class GameJoinResponse extends BaseResponse {
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
