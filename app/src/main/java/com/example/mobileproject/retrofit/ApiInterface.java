package com.example.mobileproject.retrofit;

import com.example.mobileproject.model.Category;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.LoginRequest;
import com.example.mobileproject.model.LoginResponse;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.UserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @Headers("Content-Type: application/json")
    @POST("/user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("user/course-list")
    Call<List<Course>>  getCourseListUser(@Body UserRequest userRequest);

    @POST("user/wishlist")
    Call<List<Course>>  getWishListUser(@Body UserRequest userRequest);

}
