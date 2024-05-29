package com.example.mobileproject.model;

public class RatingRequest {
    private String _id;
    private int star;
    private String courseId;
    private String comment;

    // Constructor
    public RatingRequest(String _id, int star, String courseId, String comment) {
        this._id = _id;
        this.star = star;
        this.courseId = courseId;
        this.comment = comment;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
