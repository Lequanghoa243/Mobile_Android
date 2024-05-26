
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
import com.example.mobileproject.Pages.CourseDetail;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private static final String TAG = "CourseAdapter";
    private Context context;
    List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
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
            holder.courseRating.setText(course.getTotalrating());
            if (course.getImages() != null && !course.getImages().isEmpty()) {
                String imageUrl = course.getImages().get(0).getUrl();
                Glide.with(context).load(imageUrl).into(holder.courseImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, CourseDetail.class);
                        i.putExtra("COURSE_ID", course.getId());
                        context.startActivity(i);
                    }
                });

            } else {
                Log.e(TAG, "No images found for course: " + course.getTitle());
            }
        } else {
            Log.e(TAG, "Course is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        ImageView courseImage;
        TextView courseName, totalLesson, courseDes, courseDuration, courseRating;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.course_image);
            courseName = itemView.findViewById(R.id.course_title);
            courseDuration = itemView.findViewById(R.id.course_duration);
            totalLesson = itemView.findViewById(R.id.course_total_lesson);
            courseDes = itemView.findViewById(R.id.course_des);
            courseRating = itemView.findViewById(R.id.course_rating);
        }
    }
}