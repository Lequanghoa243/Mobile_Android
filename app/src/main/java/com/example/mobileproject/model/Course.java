package com.example.mobileproject.model;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Course {
    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isInProgress() {
        return progress > 0 && progress < 100;
    }
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("learningTime")
    @Expose
    private String learningTime;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("lessonlist")
    @Expose
    private List<String> lessonlist;
    @SerializedName("NumberofLesson")
    @Expose
    private Integer numberofLesson;
    @SerializedName("totalrating")
    @Expose
    private String totalrating;
    @SerializedName("ratings")
    @Expose
    private List<Rating> ratings;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("images")
    @Expose
    private List<Image> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(String learningTime) {
        this.learningTime = learningTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getLessonlist() {
        return lessonlist;
    }

    public void setLessonlist(List<String> lessonlist) {
        this.lessonlist = lessonlist;
    }

    public Integer getNumberofLesson() {
        return numberofLesson;
    }

    public void setNumberofLesson(Integer numberofLesson) {
        this.numberofLesson = numberofLesson;
    }

    public String getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(String totalrating) {
        this.totalrating = totalrating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }



}
