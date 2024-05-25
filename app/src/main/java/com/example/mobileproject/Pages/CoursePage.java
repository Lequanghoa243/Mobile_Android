package com.example.mobileproject.Pages;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.LessonAdapter;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursePage extends AppCompatActivity {

    private static final String TAG = "CoursePage";
    private ApiInterface apiInterface;

    private ImageView courseImage;
    private TextView courseTitle, courseDescription;
    private RecyclerView lessonsRecyclerView;
    private LessonAdapter lessonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        // Initialize views
        courseImage = findViewById(R.id.course_image);
        courseTitle = findViewById(R.id.course_title);
        courseDescription = findViewById(R.id.course_des);
        lessonsRecyclerView = findViewById(R.id.lesson_recycler);

        // Initialize Retrofit
        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);

        // Get the course ID from the intent
        String courseId = getIntent().getStringExtra("COURSE_ID");
        if (courseId != null) {
            fetchCourseDetails(courseId);
        } else {
            Toast.makeText(this, "No course ID found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchCourseDetails(String courseId) {
        // Fetch course details
        apiInterface.getCourseById(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Course course = response.body();
                    displayCourseDetails(course);
                    fetchCourseLessons(courseId);
                } else {
                    Log.e(TAG, "Failed to get course details: " + response.message());
                    Toast.makeText(CoursePage.this, "Failed to get course details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(CoursePage.this, "Error fetching course details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseDetails(Course course) {
        courseTitle.setText(course.getTitle());
        courseDescription.setText(course.getDescription());
        if (course.getImages() != null && !course.getImages().isEmpty()) {
            String imageUrl = course.getImages().get(0).getUrl();
            Glide.with(this).load(imageUrl).into(courseImage);
        }
    }

    private void fetchCourseLessons(String courseId) {
        // Fetch course lessons
        apiInterface.getCourseLessons(courseId).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Lesson> lessons = response.body();
                    displayCourseLessons(lessons);
                } else {
                    Log.e(TAG, "Failed to get course lessons: " + response.message());
                    Toast.makeText(CoursePage.this, "Failed to get course lessons", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(CoursePage.this, "Error fetching course lessons", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseLessons(List<Lesson> lessons) {
        lessonAdapter = new LessonAdapter(this, lessons);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lessonsRecyclerView.setAdapter(lessonAdapter);
    }
}