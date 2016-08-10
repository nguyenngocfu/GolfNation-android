package com.golf.golfnation.billing.model;

import com.golf.golfnation.common.model.BaseResponse;
import com.golf.golfnation.game.model.GameCourse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class HistoryResponse extends BaseResponse {
    private List<HistoryRecord> details;

    public List<HistoryRecord> getDetails() {
        return details;
    }

    public void setDetails(List<HistoryRecord> details) {
        this.details = details;
    }
}
