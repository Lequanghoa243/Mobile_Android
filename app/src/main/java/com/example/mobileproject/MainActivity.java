package com.example.mobileproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.adapter.CategoryAdapter;
import com.example.mobileproject.adapter.CourseAdapter;
import com.example.mobileproject.model.Category;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView categoryRecyclerView, courseRecyclerView;
    CategoryAdapter categoryAdapter;
    CourseAdapter courseAdapter;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started");

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Log.e(TAG, "Main view is not found");
            Toast.makeText(this, "Main view is not found", Toast.LENGTH_SHORT).show();
            return; // Exit onCreate if main view is not found
        }

        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        categoryRecyclerView = findViewById(R.id.category_recycler);
        courseRecyclerView = findViewById(R.id.course_recycler);

        loadCategories();
        loadCourses();
    }

    private void loadCategories() {
        Log.d(TAG, "loadCategories: Started");
        Call<List<Category>> call = apiInterface.getAllCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body();
                    Log.d(TAG, "loadCategories: Categories loaded");
                    getAllCategory(categoryList);
                } else {
                    Log.e(TAG, "Failed to get categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "No response from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCourses() {
        Log.d(TAG, "loadCourses: Started");
        Call<List<Course>> callCourse = apiInterface.getAllCourse();
        callCourse.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courseList = response.body();
                    Log.d(TAG, "loadCourses: Courses loaded - " + courseList.size() + " courses");
                    for (Course course : courseList) {
                        Log.d(TAG, "Course: " + course.getTitle() + ", Lessons: " + course.getNumberofLesson());
                    }
                    getAllCourse(courseList);
                } else {
                    Log.e(TAG, "Failed to get courses: " + response.message());
                    if (response.body() == null) {
                        Log.e(TAG, "Response body is null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "No response from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllCategory(List<Category> categoryList) {
        if (categoryList == null) {
            Log.e(TAG, "Category list is null");
            return;
        }
        Log.d(TAG, "getAllCategory: Populating categories");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }

    private void getAllCourse(List<Course> courseList) {
        if (courseList == null) {
            Log.e(TAG, "Course list is null");
            return;
        }
        Log.d(TAG, "getAllCourse: Populating courses");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        courseRecyclerView.setLayoutManager(layoutManager);
        courseRecyclerView.setHasFixedSize(true);
        courseAdapter = new CourseAdapter(this, courseList);
        courseRecyclerView.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
    }
}