package com.golf.golfnation.game.model;

import com.golf.golfnation.common.model.BaseResponse;

import java.util.List;

/**
 * Created by NgocNQ on 7/14/2015.
 */
public class MembersResponse extends BaseResponse {
    private List<Member> details;

    public List<Member> getDetails() {
        return details;
    }

    public void setDetails(List<Member> detail) {
        this.details = detail;
    }
}
