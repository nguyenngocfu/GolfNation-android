package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class GameCourseResponse extends BaseResponse {
    private List<GameCourse> details;

    public List<GameCourse> getDetails() {
        return details;
    }

    public void setDetails(List<GameCourse> details) {
        this.details = details;
    }
}
