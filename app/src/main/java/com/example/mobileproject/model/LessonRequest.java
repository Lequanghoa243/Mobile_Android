package com.example.mobileproject.model;

public class LessonRequest {
    private String _id;
    private String id;

    public LessonRequest(String _id, String id) {
        this._id = _id;
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
