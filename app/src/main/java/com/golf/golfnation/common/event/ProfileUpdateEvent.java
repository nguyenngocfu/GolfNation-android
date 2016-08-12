package com.golf.golfnation.common.event;

/**
 * Created by Bruce on 12/8/2016.
 */
public class ProfileUpdateEvent {
    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    private String photoURL;

    public ProfileUpdateEvent(String photoURL) {
        this.photoURL = photoURL;
    }
}
