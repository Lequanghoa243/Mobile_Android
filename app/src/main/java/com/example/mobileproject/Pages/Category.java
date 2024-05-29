package com.example.mobileproject.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CategoryAdapterMain;
import com.example.mobileproject.adapter.CourseAdapterCategory;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.GetUserRequest;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.WishlistRequest;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;
import com.example.mobileproject.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category extends AppCompatActivity implements CategoryAdapterMain.OnCategoryClickListener {
    BottomNavigationView bottomNavigationView;
    RecyclerView categoryRecyclerView, courseRecyclerView;
    CourseAdapterCategory courseAdapter;
    CategoryAdapterMain categoryAdapter;
    ApiInterface apiInterface;
    List<Course> courseList;
    List<String> wishlist = new ArrayList<>(); // Initialize wishlist
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        categoryRecyclerView = findViewById(R.id.category_recycler);
        courseRecyclerView = findViewById(R.id.course_of_category_recycler);

        loadCategories();
        loadWishlist(); // Load the wishlist before loading courses

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.category);
        bottomNavigationView.findViewById(R.id.home).setOnClickListener(v -> {
            Intent intent = new Intent(Category.this, MainActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.findViewById(R.id.my_course).setOnClickListener(v -> {
            Intent intent = new Intent(Category.this, MyCourse.class);
            startActivity(intent);
        });

        bottomNavigationView.findViewById(R.id.category).setOnClickListener(v -> {});

        bottomNavigationView.findViewById(R.id.user).setOnClickListener(v -> {
            Intent intent = new Intent(Category.this, Profile.class);
            startActivity(intent);
        });
    }

    private void loadCategories() {
        Call<List<com.example.mobileproject.model.Category>> call = apiInterface.getAllCategory();
        call.enqueue(new Callback<List<com.example.mobileproject.model.Category>>() {
            @Override
            public void onResponse(Call<List<com.example.mobileproject.model.Category>> call, Response<List<com.example.mobileproject.model.Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.mobileproject.model.Category> categoryList = response.body();
                    getAllCategory(categoryList);
                } else {
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call<List<com.example.mobileproject.model.Category>> call, Throwable t) {
                Toast.makeText(Category.this, "No response from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCourses() {
        Call<List<Course>> callCourse = apiInterface.getAllCourse();
        callCourse.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList = response.body();
                    getAllCourse(courseList);
                } else {
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(Category.this, "No response from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWishlist() {
        String userId = sharedPreferencesManager.getUserId();
        if (userId != null) {
            GetUserRequest getUserRequest = new GetUserRequest(userId);
            apiInterface.getAUser(getUserRequest).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        wishlist = user.getWishlist() != null ? user.getWishlist() : new ArrayList<>();
                        loadCourses(); // Load courses after wishlist is loaded
                    } else {
                        // Handle the error
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Handle the failure
                }
            });
        }
    }

    private void getAllCategory(List<com.example.mobileproject.model.Category> categoryList) {
        if (categoryList == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapterMain(this, categoryList, this);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }

    private void getAllCourse(List<Course> courseList) {
        if (courseList == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        courseRecyclerView.setLayoutManager(layoutManager);
        courseRecyclerView.setHasFixedSize(true);
        courseAdapter = new CourseAdapterCategory(this, courseList, wishlist);
        courseRecyclerView.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(com.example.mobileproject.model.Category category) {
        if (courseList == null) {
            return;
        }
        List<Course> filteredCourses = new ArrayList<>();
        for (Course course : courseList) {
            if (course.getCategory().equals(category.getTitle())) {
                filteredCourses.add(course);
            }
        }
        categoryAdapter.setActiveCategory(category);
        getAllCourse(filteredCourses);
    }

    public void toggleWishlist(String courseId, ImageView wishlistIcon) {
        String userId = sharedPreferencesManager.getUserId();
        WishlistRequest wishlistRequest = new WishlistRequest(userId, courseId);
        apiInterface.addToWishList(wishlistRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (wishlist.contains(courseId)) {
                        wishlist.remove(courseId);
                        wishlistIcon.setImageResource(R.drawable.icon_heart);
                        Toast.makeText(Category.this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        wishlist.add(courseId);
                        wishlistIcon.setImageResource(R.drawable.heart_fill);
                        Toast.makeText(Category.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Category.this, "Failed to update wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Category.this, "Error updating wishlist: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
