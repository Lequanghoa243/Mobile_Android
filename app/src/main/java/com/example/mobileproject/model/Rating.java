package com.example.mobileproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Rating {

    @SerializedName("star")
    @Expose
    private Double star;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("postedby")
    @Expose
    private String postedby;
    @SerializedName("_id")
    @Expose
    private String id;

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
