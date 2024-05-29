package com.example.mobileproject.retrofit;

import com.example.mobileproject.model.Category;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.EnrollRequest;
import com.example.mobileproject.model.GetUserRequest;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.LoginRequest;
import com.example.mobileproject.model.LoginResponse;
import com.example.mobileproject.model.RatingRequest;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.UserRequest;
import com.example.mobileproject.model.WishlistRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("category")
    Call<List<Category>> getAllCategory();
    @GET("user")
    Call<List<User>> getAllUser();


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

    @POST("course/enrollcourse")
    Call<User> enrollCourse(@Body EnrollRequest enrollRequest);


    @POST("user/get-user")
    Call<User> getAUser(@Body GetUserRequest getUserRequest);

    @PUT("course/wishlist")
    Call<User> addToWishList(@Body WishlistRequest wishlistRequest);

    @PUT("/course/rating")
    Call<Course> rateCourse(@Body RatingRequest ratingRequest);

}
