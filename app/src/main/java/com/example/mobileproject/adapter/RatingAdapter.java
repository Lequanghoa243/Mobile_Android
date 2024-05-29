package com.example.mobileproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.model.Rating;
import com.example.mobileproject.model.User;

import java.util.List;
import java.util.Map;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private Context context;
    private List<Rating> ratingList;
    private Map<String, User> userMap;

    public RatingAdapter(Context context, List<Rating> ratingList, Map<String, User> userMap) {
        this.context = context;
        this.ratingList = ratingList;
        this.userMap = userMap;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = ratingList.get(position);
        holder.comment.setText(rating.getComment());

        User user = userMap.get(rating.getPostedby());
        if (user != null) {
            holder.username.setText(user.getFirstname() + " " + user.getLastname());
        } else {
            holder.username.setText("Unknown User");
        }

        int starRating = (int) Math.round(rating.getStar());
        addStarIcons(holder.starLayout, starRating);
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    private void addStarIcons(ViewGroup starLayout, int starRating) {
        starLayout.removeAllViews(); // Clear existing stars
        for (int i = 0; i < starRating; i++) {
            ImageView starImageView = new ImageView(context);
            starImageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            starImageView.setImageResource(R.drawable.icon_star); // Set star icon drawable
            starLayout.addView(starImageView);
        }
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {

        TextView comment, username;
        ViewGroup starLayout;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.username);
            starLayout = itemView.findViewById(R.id.star_layout); // Assuming you have a LinearLayout for star icons
        }
    }
}
