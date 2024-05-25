package com.example.mobileproject.retrofit;

import com.example.mobileproject.model.Category;
import com.example.mobileproject.model.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("category")
    Call<List<Category>> getAllCategory();

    @GET("course")
    Call<List<Course>> getAllCourse();

}
