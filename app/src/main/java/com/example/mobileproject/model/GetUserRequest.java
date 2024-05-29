package com.example.mobileproject.model;

public class GetUserRequest {

    private String postedby;

    public GetUserRequest(String postedby) {
        this.postedby = postedby;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

}
