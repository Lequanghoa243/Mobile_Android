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

public class CourseRecommendAdapter extends RecyclerView.Adapter<CourseRecommendAdapter.CourseViewHolder> {

    private static final String TAG = "CourseAdapter";
    private Context context;
    List<Course> courseList;

    public CourseRecommendAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_recommended_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        if (course != null) {
            holder.courseName.setText(course.getTitle());
            holder.courseRating.setText(String.valueOf(course.getTotalrating())); // Ensure this is a string
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
        TextView courseName, totalLesson, courseDuration, courseRating;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.course_image);
            courseName = itemView.findViewById(R.id.course_title);
            courseRating = itemView.findViewById(R.id.course_rating);
        }
    }
}
