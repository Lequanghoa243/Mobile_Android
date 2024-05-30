package com.example.mobileproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.Pages.CourseDetail;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.WishlistRequest;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;
import com.example.mobileproject.utils.SharedPreferencesManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private static final String TAG = "CourseAdapter";
    private Context context;
    private List<Course> courseList;
    private List<String> wishlist;
    private ApiInterface apiInterface;
    private SharedPreferencesManager sharedPreferencesManager;

    public CourseAdapter(Context context, List<Course> courseList, List<String> wishlist) {
        this.context = context;
        this.courseList = courseList;
        this.wishlist = wishlist;
        this.apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        if (course != null) {
            holder.courseName.setText(course.getTitle());
            holder.totalLesson.setText(String.valueOf(course.getNumberofLesson()));
            holder.courseDuration.setText(course.getLearningTime());
            holder.courseDes.setText(course.getDescription());
            holder.courseRating.setText(String.valueOf(course.getTotalrating()));

            if (course.getImages() != null && !course.getImages().isEmpty()) {
                String imageUrl = course.getImages().get(0).getUrl();
                Glide.with(context).load(imageUrl).into(holder.courseImage);
            } else {
                Log.e(TAG, "No images found for course: " + course.getTitle());
            }

            // Change the icon color based on wishlist status
            if (wishlist.contains(course.getId())) {
                holder.wishlistIcon.setImageResource(R.drawable.heart_fill);
            } else {
                holder.wishlistIcon.setImageResource(R.drawable.icon_heart);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent i = new Intent(context, CourseDetail.class);
                i.putExtra("COURSE_ID", course.getId());
                context.startActivity(i);
            });

            holder.wishlistIcon.setOnClickListener(v -> {
                String userId = sharedPreferencesManager.getUserId();
                if (userId != null) {
                    toggleWishlist(userId, course.getId(), holder.wishlistIcon);
                } else {
                    Toast.makeText(context, "You have to log in fist", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Course is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    private void toggleWishlist(String userId, String courseId, ImageView wishlistIcon) {
        WishlistRequest wishlistRequest = new WishlistRequest(userId, courseId);
        apiInterface.addToWishList(wishlistRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (wishlist.contains(courseId)) {
                        wishlist.remove(courseId);
                        wishlistIcon.setImageResource(R.drawable.icon_heart);
                        Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        wishlist.add(courseId);
                        wishlistIcon.setImageResource(R.drawable.heart_fill);
                        Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to update wishlist: " + response.message());
                    Toast.makeText(context, "Failed to update wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Error updating wishlist: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        ImageView courseImage, wishlistIcon;
        TextView courseName, totalLesson, courseDes, courseDuration, courseRating;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.course_image);
            wishlistIcon = itemView.findViewById(R.id.wishlist_icon);
            courseName = itemView.findViewById(R.id.course_title);
            courseDuration = itemView.findViewById(R.id.course_duration);
            totalLesson = itemView.findViewById(R.id.course_total_lesson);
            courseDes = itemView.findViewById(R.id.course_des);
            courseRating = itemView.findViewById(R.id.course_rating);
        }
    }
}
