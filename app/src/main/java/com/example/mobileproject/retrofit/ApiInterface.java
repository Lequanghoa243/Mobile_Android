package com.example.mobileproject.retrofit;

import com.example.mobileproject.model.Category;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.Lesson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("category")
    Call<List<Category>> getAllCategory();

    @GET("course")
    Call<List<Course>> getAllCourse();
    @GET("course/{id}")
    Call<Course> getCourseById(@Path("id") String courseId);

    @GET("course/{id}/lesson")
    Call<List<Lesson>> getCourseLessons(@Path("id") String courseId);
    @GET("category/{id}/courses")
    Call<List<Course>> getCoursesByCategory(@Path("id") String categoryId);

}

