package com.example.mobileproject.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.LessonAdapter;
import com.example.mobileproject.adapter.RatingAdapter;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.EnrollRequest;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.Rating;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.UserRequest;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;
import com.example.mobileproject.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetail extends AppCompatActivity {

    private static final String TAG = "CoursePage";
    private ApiInterface apiInterface;
    private SharedPreferencesManager sharedPreferencesManager;

    private ImageView courseImage;
    private TextView courseTitle, courseDescription, courseDuration, courseLesson, courseRating;
    private RecyclerView lessonsRecyclerView, ratingRecycleView;
    private LessonAdapter lessonAdapter;
    private RatingAdapter ratingAdapter;
    private Button enrollButton;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout enrollLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Initialize SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // Initialize views
        courseDuration = findViewById(R.id.course_duration);
        courseLesson = findViewById(R.id.course_total_lesson);
        courseRating = findViewById(R.id.course_rating);
        courseImage = findViewById(R.id.course_image);
        courseTitle = findViewById(R.id.course_title);
        courseDescription = findViewById(R.id.course_des);
        ratingRecycleView = findViewById(R.id.review_recycler);
        lessonsRecyclerView = findViewById(R.id.lesson_recycler);
        enrollButton = findViewById(R.id.enroll_button);
        enrollLayout = findViewById(R.id.enroll_layout); // Initialize the LinearLayout

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

        checkEnrollmentStatus(courseId);

        enrollButton.setOnClickListener(v -> {
            String userId = sharedPreferencesManager.getUserId();
            if (userId == null) {
                // Redirect to login activity
                Intent intent = new Intent(CourseDetail.this, Login.class);
                startActivity(intent);
            } else {
                // Enroll the user in the course
                enrollInCourse(userId, courseId);
            }
        });
    }

    private void fetchCourseDetails(String courseId) {
        // Fetch course details
        apiInterface.getCourseById(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Course course = response.body();
                    List<Rating> ratingList = course.getRatings();
                    displayRating(ratingList);
                    displayCourseDetails(course);
                    fetchCourseLessons(courseId);
                } else {
                    Log.e(TAG, "Failed to get course details: " + response.message());
                    Toast.makeText(CourseDetail.this, "Failed to get course details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(CourseDetail.this, "Error fetching course details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseDetails(Course course) {
        courseDuration.setText(course.getLearningTime());
        courseRating.setText(String.valueOf(course.getTotalrating())); // Ensure this is a string
        courseLesson.setText(String.valueOf(course.getNumberofLesson())); // Ensure this is a string
        courseTitle.setText(course.getTitle());
        courseDescription.setText(course.getDescription());
        if (course.getImages() != null && !course.getImages().isEmpty()) {
            String imageUrl = course.getImages().get(0).getUrl();
            Glide.with(this).load(imageUrl).into(courseImage);
        }
    }

    private void displayRating(List<Rating> ratings) {
        if (ratings != null && !ratings.isEmpty()) {
            ratingAdapter = new RatingAdapter(this, ratings);
            ratingRecycleView.setLayoutManager(new LinearLayoutManager(this));
            ratingRecycleView.setAdapter(ratingAdapter);
        } else {
            Log.e(TAG, "Rating list is null or empty");
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
                    Toast.makeText(CourseDetail.this, "Failed to get course lessons", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(CourseDetail.this, "Error fetching course lessons", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseLessons(List<Lesson> lessons) {
        if (lessons != null && !lessons.isEmpty()) {
            lessonAdapter = new LessonAdapter(this, lessons);
            lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            lessonsRecyclerView.setAdapter(lessonAdapter);
        } else {
            Log.e(TAG, "Lesson list is null or empty");
        }
    }

    private void checkEnrollmentStatus(String courseId) {
        String userId = sharedPreferencesManager.getUserId();
        if (userId == null) {
            enrollLayout.setVisibility(View.VISIBLE);
            return;
        }

        UserRequest userRequest = new UserRequest(userId);
        apiInterface.getCourseListUser(userRequest).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courseList = response.body();
                    boolean isEnrolled = false;
                    for (Course course : courseList) {
                        if (course.getId().equals(courseId)) {
                            isEnrolled = true;
                            break;
                        }
                    }
                    if (isEnrolled) {
                        enrollLayout.setVisibility(View.GONE);
                    } else {
                        enrollLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    enrollLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                enrollLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void enrollInCourse(String userId, String courseId) {
        EnrollRequest enrollRequest = new EnrollRequest(userId, courseId);
        apiInterface.enrollCourse(enrollRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    enrollLayout.setVisibility(View.GONE);
                    Toast.makeText(CourseDetail.this, "Enrolled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseDetail.this, "Failed to enroll", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to enroll: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CourseDetail.this, "Error enrolling in the course", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error enrolling in the course: " + t.getMessage());
            }
        });
    }


    public void returnToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
