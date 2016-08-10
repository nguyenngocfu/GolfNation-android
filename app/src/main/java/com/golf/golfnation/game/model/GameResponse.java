package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.user.model.UserDetail;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class GameResponse extends BaseResponse {
    private List<Game> details;

    public List<Game> getDetails() {
        return details;
    }

    public void setDetails(List<Game> details) {
        this.details = details;
    }
}
