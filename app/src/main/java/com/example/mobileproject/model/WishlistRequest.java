package com.example.mobileproject.model;

public class WishlistRequest {
    private String _id;
    private String courseId;

    public WishlistRequest(String _id, String courseId) {
        this._id = _id;
        this.courseId = courseId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
