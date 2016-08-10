package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class CityResponse extends BaseResponse {
    private List<City> details;

    public List<City> getDetails() {
        return details;
    }

    public void setDetails(List<City> details) {
        this.details = details;
    }
}
