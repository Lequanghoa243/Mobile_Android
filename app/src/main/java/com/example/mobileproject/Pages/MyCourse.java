package com.example.mobileproject.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CourseAdapterCategory;
import com.example.mobileproject.adapter.ListAdapter;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.UserRequest;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;
import com.example.mobileproject.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCourse extends AppCompatActivity implements ListAdapter.OnCategoryClickListener {
    private static final String TAG = "MyCourse";

    private RecyclerView categoryRecyclerView, courseRecyclerView;
    private ListAdapter listAdapter;
    private CourseAdapterCategory courseAdapter;
    private BottomNavigationView bottomNavigationView;
    LinearLayout loggedInView, loggedOutView;
    SharedPreferencesManager sharedPreferencesManager;
    ApiInterface apiInterface;
    private List<Course> courseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        initViews();
        setupBottomNavigationView();

        checkLoginStatus();
    }

    private void initViews() {
        loggedInView = findViewById(R.id.logged_in_view);
        loggedOutView = findViewById(R.id.logged_out_view);
        categoryRecyclerView = findViewById(R.id.list_recycler);
        courseRecyclerView = findViewById(R.id.course_recycler);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);

        // Initialize the category list
        List<String> categoryList = Arrays.asList("Saved Courses", "In-progress Courses", "Completed Courses");
        listAdapter = new ListAdapter(this, categoryList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(listAdapter);

        // Setup RecyclerView for courses
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseRecyclerView.setHasFixedSize(true);
        courseAdapter = new CourseAdapterCategory(this, courseList);
        courseRecyclerView.setAdapter(courseAdapter);
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.my_course);
        bottomNavigationView.findViewById(R.id.my_course).setOnClickListener(v -> {
            Intent intent = new Intent(MyCourse.this, MainActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.findViewById(R.id.category).setOnClickListener(v -> {
            Intent intent = new Intent(MyCourse.this, Category.class);
            startActivity(intent);
        });

        bottomNavigationView.findViewById(R.id.user).setOnClickListener(v -> {
            Intent intent = new Intent(MyCourse.this, Profile.class);
            startActivity(intent);
        });
    }

    private void checkLoginStatus() {
        String firstName = sharedPreferencesManager.getFirstName();
        String lastName = sharedPreferencesManager.getLastName();
        String email = sharedPreferencesManager.getEmail();

        if (firstName != null && lastName != null && email != null) {
            loggedInView.setVisibility(View.VISIBLE);
            loggedOutView.setVisibility(View.GONE);
        } else {
            loggedInView.setVisibility(View.GONE);
            loggedOutView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchCourses() {
        String userId = sharedPreferencesManager.getUserId();
        if (userId == null) {
            Log.e(TAG, "User ID is null");
            Toast.makeText(MyCourse.this, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest userRequest = new UserRequest(userId);

        Call<List<Course>> call = apiInterface.getCourseListUser(userRequest);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList.clear();
                    courseList.addAll(response.body());
                    courseAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Courses fetched successfully");
                    Toast.makeText(MyCourse.this, "Courses fetched successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to fetch courses: " + response.message());
                    Toast.makeText(MyCourse.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch courses", t);
                Toast.makeText(MyCourse.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWishlist() {
        String userId = sharedPreferencesManager.getUserId();
        if (userId == null) {
            Log.e(TAG, "User ID is null");
            Toast.makeText(MyCourse.this, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest userRequest = new UserRequest(userId);

        Call<List<Course>> call = apiInterface.getWishListUser(userRequest);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList.clear();
                    courseList.addAll(response.body());
                    courseAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Wishlist fetched successfully");
                    Toast.makeText(MyCourse.this, "Wishlist fetched successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to fetch wishlist: " + response.message());
                    Toast.makeText(MyCourse.this, "Failed to fetch wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch wishlist", t);
                Toast.makeText(MyCourse.this, "Failed to fetch wishlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(String category) {
        if (category.equals("Saved Courses")) {
            fetchWishlist();
        } else if (category.equals("In-progress Courses")) {
            fetchCourses();
        } else {

        }
    }
}
