package com.example.mobileproject.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.mobileproject.model.GetUserRequest;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.Rating;
import com.example.mobileproject.model.RatingRequest;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.UserRequest;
import com.example.mobileproject.model.WishlistRequest;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;
import com.example.mobileproject.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetail extends AppCompatActivity {

    private static final String TAG = "CoursePage";
    private ApiInterface apiInterface;
    private SharedPreferencesManager sharedPreferencesManager;

    private ImageView courseImage, addToWishList;

    private TextView courseTitle, courseDescription, courseDuration, courseLesson, courseRating;
    private RecyclerView lessonsRecyclerView, ratingRecycleView;
    private LessonAdapter lessonAdapter;
    private RatingAdapter ratingAdapter;
    private Button enrollButton;
    private LinearLayout enrollLayout;
    private LinearLayout commentSectionLayout;
    private EditText commentEditText;
    private Button postCommentButton;
    private int selectedStarRating = 0;
    private Map<String, User> userMap = new HashMap<>();

    private boolean isInWishlist = false; // Flag to track wishlist status

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
        commentSectionLayout = findViewById(R.id.comment_section_layout);
        commentEditText = findViewById(R.id.comment_edit_text);
        postCommentButton = findViewById(R.id.post_comment_button);
        addToWishList = findViewById(R.id.wishList);

        // Initialize Retrofit
        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        fetchAllUsers();

        // Get the course ID from the intent
        String courseId = getIntent().getStringExtra("COURSE_ID");
        if (courseId != null) {
            fetchCourseDetails(courseId);
            checkWishlistStatus(courseId); // Check wishlist status when activity is created
        } else {
            Toast.makeText(this, "No course ID found", Toast.LENGTH_SHORT).show();
            finish();
        }

        checkEnrollmentStatus(courseId);

        enrollButton.setOnClickListener(v -> {
            String userId = sharedPreferencesManager.getUserId();
            if (userId == null) {
                Intent intent = new Intent(CourseDetail.this, Login.class);
                startActivity(intent);
            } else {
                enrollInCourse(userId, courseId);
            }
        });

        postCommentButton.setOnClickListener(v -> {
            String userId = sharedPreferencesManager.getUserId();
            String comment = commentEditText.getText().toString();
            if (userId != null && !comment.isEmpty() && selectedStarRating > 0) {
                // Make a request to post the comment
                postComment(userId, courseId, comment, selectedStarRating);
            } else {
                Toast.makeText(CourseDetail.this, "Please enter a comment and select a star rating", Toast.LENGTH_SHORT).show();
            }
        });

        addToWishList.setOnClickListener(v -> {
            String userId = sharedPreferencesManager.getUserId();
            if (userId != null && courseId != null) {
                toggleWishlist(userId, courseId);
            } else {
                Toast.makeText(CourseDetail.this, "User ID or Course ID is missing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllUsers() {
        apiInterface.getAllUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    for (User user : users) {
                        userMap.put(user.getId(), user);
                    }
                } else {
                    Log.e(TAG, "Failed to fetch users: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void postComment(String userId, String courseId, String comment, int starRating) {
        // Create the RatingRequest object
        RatingRequest ratingRequest = new RatingRequest(userId, starRating, courseId, comment);

        // Make the API call
        apiInterface.rateCourse(ratingRequest).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CourseDetail.this, "Comment posted", Toast.LENGTH_SHORT).show();
                    commentEditText.setText(""); // Clear the comment EditText
                    fetchCourseDetails(courseId);
                    selectedStarRating = 0; // Reset the star rating
                    resetStars(); // Reset the star UI

                } else {
                    Toast.makeText(CourseDetail.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Toast.makeText(CourseDetail.this, "Error posting comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetStars() {
        ImageView star1 = findViewById(R.id.star1);
        ImageView star2 = findViewById(R.id.star2);
        ImageView star3 = findViewById(R.id.star3);
        ImageView star4 = findViewById(R.id.star4);
        ImageView star5 = findViewById(R.id.star5);

        // Reset all stars to unselected
        star1.setImageResource(R.drawable.star_out_line);
        star2.setImageResource(R.drawable.star_out_line);
        star3.setImageResource(R.drawable.star_out_line);
        star4.setImageResource(R.drawable.star_out_line);
        star5.setImageResource(R.drawable.star_out_line);
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
            ratingAdapter = new RatingAdapter(this, ratings, userMap);
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
                    displayCourseLessons(lessons, courseId);
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

    public void selectStar(View view) {
        // Reset all stars to unselected
        ImageView star1 = findViewById(R.id.star1);
        ImageView star2 = findViewById(R.id.star2);
        ImageView star3 = findViewById(R.id.star3);
        ImageView star4 = findViewById(R.id.star4);
        ImageView star5 = findViewById(R.id.star5);
        star1.setImageResource(R.drawable.star_out_line);
        star2.setImageResource(R.drawable.star_out_line);
        star3.setImageResource(R.drawable.star_out_line);
        star4.setImageResource(R.drawable.star_out_line);
        star5.setImageResource(R.drawable.star_out_line);

        // Set selected stars
        int tag = Integer.parseInt(view.getTag().toString());
        selectedStarRating = tag;
        if (tag >= 1) star1.setImageResource(R.drawable.icon_star);
        if (tag >= 2) star2.setImageResource(R.drawable.icon_star);
        if (tag >= 3) star3.setImageResource(R.drawable.icon_star);
        if (tag >= 4) star4.setImageResource(R.drawable.icon_star);
        if (tag >= 5) star5.setImageResource(R.drawable.icon_star);
    }

    private void displayCourseLessons(List<Lesson> lessons, String courseId) {
        if (lessons != null && !lessons.isEmpty()) {
            lessonAdapter = new LessonAdapter(this, lessons, courseId);
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
            commentSectionLayout.setVisibility(View.GONE); // Hide comment section
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
                        commentSectionLayout.setVisibility(View.VISIBLE); // Show comment section
                    } else {
                        enrollLayout.setVisibility(View.VISIBLE);
                        commentSectionLayout.setVisibility(View.GONE); // Hide comment section
                    }
                } else {
                    enrollLayout.setVisibility(View.VISIBLE);
                    commentSectionLayout.setVisibility(View.GONE); // Hide comment section
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                enrollLayout.setVisibility(View.VISIBLE);
                commentSectionLayout.setVisibility(View.GONE); // Hide comment section
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
                    recreate();
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

    private void checkWishlistStatus(String courseId) {
        String userId = sharedPreferencesManager.getUserId();
        GetUserRequest getUserRequest = new GetUserRequest(userId);
        if (userId != null) {
            apiInterface.getAUser(getUserRequest).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        List<String> wishlist = user.getWishlist();
                        if (wishlist.contains(courseId)) {
                            isInWishlist = true;
                            addToWishList.setImageResource(R.drawable.heart_fill);
                        } else {
                            isInWishlist = false;
                            addToWishList.setImageResource(R.drawable.icon_heart);
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch wishlist: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Error: " + t.getMessage());
                }
            });
        }
    }

    private void toggleWishlist(String userId, String courseId) {
        WishlistRequest wishlistRequest = new WishlistRequest(userId, courseId);
        apiInterface.addToWishList(wishlistRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isInWishlist = !isInWishlist; // Toggle the wishlist state
                    if (isInWishlist) {
                        addToWishList.setImageResource(R.drawable.heart_fill);
                        Toast.makeText(CourseDetail.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        addToWishList.setImageResource(R.drawable.icon_heart);
                        Toast.makeText(CourseDetail.this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to update wishlist: " + response.message());
                    Toast.makeText(CourseDetail.this, "Failed to update wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CourseDetail.this, "Error updating wishlist: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
