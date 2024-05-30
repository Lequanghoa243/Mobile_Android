package com.example.mobileproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Pages.CourseDetail;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Course;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private List<Course> courseList;
    private OnCourseClickListener onCourseClickListener;

    public interface OnCourseClickListener {
        void onCourseClick(String courseTitle);
    }

    public SearchAdapter(Context context, List<Course> courseList, OnCourseClickListener onCourseClickListener) {
        this.context = context;
        this.courseList = courseList;
        this.onCourseClickListener = onCourseClickListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseTitle.setText(course.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetail.class);
            intent.putExtra("COURSE_ID", course.getId());
            context.startActivity(intent);
            onCourseClickListener.onCourseClick(course.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    public void updateList(List<Course> filteredList) {
        this.courseList = filteredList;
        notifyDataSetChanged();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
        }
    }
}
