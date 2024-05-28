package com.example.mobileproject.Pages;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CourseRecommendAdapter;
import com.example.mobileproject.adapter.LessonAdapter;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlay extends AppCompatActivity {
    private WebView webView;
    private RecyclerView lessonsRecyclerView;
    private RecyclerView courseRecyclerView;
    private LessonAdapter lessonAdapter;
    private CourseRecommendAdapter courseRecommendAdapter;
    private ApiInterface apiInterface;
    private static final String TAG = "VideoPlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        lessonsRecyclerView = findViewById(R.id.lesson_recycler);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseRecyclerView = findViewById(R.id.course_recycler);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);

        String videoUrl = getIntent().getStringExtra("VIDEO_URL");
        String courseId = getIntent().getStringExtra("COURSE_ID");

        if (videoUrl != null) {
            webView.loadUrl(videoUrl);
        } else {
            Toast.makeText(this, "No video URL found", Toast.LENGTH_SHORT).show();
        }

        if (courseId != null) {
            fetchCourseLessons(courseId, videoUrl);
            loadCourses();
        } else {
            Toast.makeText(this, "No course ID found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCourseLessons(String courseId, String activeVideoUrl) {
        apiInterface.getCourseLessons(courseId).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Lesson> lessons = response.body();
                    displayCourseLessons(lessons, courseId, activeVideoUrl);
                } else {
                    Log.e(TAG, "Failed to get course lessons: " + response.message());
                    Toast.makeText(VideoPlay.this, "Failed to get course lessons", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(VideoPlay.this, "Error fetching course lessons", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseLessons(List<Lesson> lessons, String courseId, String activeVideoUrl) {
        if (lessons != null && !lessons.isEmpty()) {
            lessonAdapter = new LessonAdapter(this, lessons, courseId);
            lessonAdapter.setActiveVideoUrl(activeVideoUrl);
            lessonsRecyclerView.setAdapter(lessonAdapter);
        } else {
            Log.e(TAG, "Lesson list is null or empty");
        }
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
                    displayCourses(courseList);
                } else {
                    Log.e(TAG, "Failed to get courses: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(VideoPlay.this, "No response from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourses(List<Course> courseList) {
        if (courseList != null && !courseList.isEmpty()) {
            courseRecommendAdapter = new CourseRecommendAdapter(this, courseList);
            courseRecyclerView.setAdapter(courseRecommendAdapter);
        } else {
            Log.e(TAG, "Course list is null or empty");
        }
    }
}
