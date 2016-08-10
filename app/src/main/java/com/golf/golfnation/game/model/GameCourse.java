package com.golf.golfnation.game.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by NgocNQ on 8/21/2015.
 */
public class GameCourse implements Serializable{
    @SerializedName("golf_course_id")
    private String courseId;
    @SerializedName("golf_course_name")
    private String courseName;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
