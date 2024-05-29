package com.example.mobileproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.Pages.Category;
import com.example.mobileproject.Pages.CourseDetail;
import com.example.mobileproject.Pages.MyCourse;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Course;

import java.util.List;

public class CourseAdapterCategory extends RecyclerView.Adapter<CourseAdapterCategory.CourseViewHolder> {

    private static final String TAG = "CourseAdapterCategory";
    private Context context;
    List<Course> courseList;
    private List<String> wishlist;

    public CourseAdapterCategory(Context context, List<Course> courseList, List<String> wishlist) {
        this.context = context;
        this.courseList = courseList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_card_tiem, parent, false);
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
                if (context instanceof MyCourse) {
                    ((MyCourse) context).toggleWishlist(course.getId(), holder.wishlistIcon);
                } else if (context instanceof Category) {
                    ((Category) context).toggleWishlist(course.getId(), holder.wishlistIcon);
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
