package com.example.mobileproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Pages.VideoPlay;
import com.example.mobileproject.R;
import com.example.mobileproject.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private Context context;
    private List<Lesson> lessonList;
    private String courseId;
    private String activeVideoUrl;

    public LessonAdapter(Context context, List<Lesson> lessonList, String courseId) {
        this.context = context;
        this.lessonList = lessonList;
        this.courseId = courseId;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lesson_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.lessonTitle.setText(lesson.getTitle());

        if (lesson.getVideoURL().equals(activeVideoUrl)) {
            holder.itemView.setBackgroundResource(R.drawable.shapeshadow_active); // Background for active video
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shapeshadow); // Background for inactive video
        }

        holder.itemView.setOnClickListener(v -> {
            activeVideoUrl = lesson.getVideoURL();
            notifyDataSetChanged(); // Notify adapter to refresh the list

            Intent intent = new Intent(context, VideoPlay.class);
            intent.putExtra("VIDEO_URL", lesson.getVideoURL());
            intent.putExtra("COURSE_ID", courseId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    public void setActiveVideoUrl(String activeVideoUrl) {
        this.activeVideoUrl = activeVideoUrl;
        notifyDataSetChanged();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitle;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.lesson_title);
        }
    }
}
