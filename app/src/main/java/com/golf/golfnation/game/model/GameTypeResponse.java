package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class GameTypeResponse extends BaseResponse {
    private List<GameType> details;

    public List<GameType> getDetails() {
        return details;
    }

    public void setDetails(List<GameType> details) {
        this.details = details;
    }
}
