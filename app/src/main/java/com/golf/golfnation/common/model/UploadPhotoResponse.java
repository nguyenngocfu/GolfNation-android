package com.golf.golfnation.common.model;

/**
 * Created by Ngọc Nguyễn on 12/22/2014.
 */
public class UploadPhotoResponse extends BaseResponse {

    public PhotoDetail getDetails() {
        return details;
    }

    public void setDetails(PhotoDetail details) {
        this.details = details;
    }

    private PhotoDetail details;
    public UploadPhotoResponse(String statusCode, String message) {
        super(statusCode, message);
    }

}
