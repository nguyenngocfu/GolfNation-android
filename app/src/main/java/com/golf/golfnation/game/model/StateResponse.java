package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class StateResponse extends BaseResponse {
    private List<State> details;

    public List<State> getDetails() {
        return details;
    }

    public void setDetails(List<State> details) {
        this.details = details;
    }
}
