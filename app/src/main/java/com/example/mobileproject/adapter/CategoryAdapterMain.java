package com.example.mobileproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.model.Category;

import java.util.List;

public class CategoryAdapterMain extends RecyclerView.Adapter<CategoryAdapterMain.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private Category activeCategory;

    public CategoryAdapterMain(Context context, List<Category> categoryList, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view, onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getTitle());

        if (category.equals(activeCategory)) {
            holder.itemView.setBackgroundResource(R.drawable.shapeshadow_active); // Background for active category
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shapeshadow); // Background for inactive category
        }

        holder.itemView.setOnClickListener(v -> {
            activeCategory = category;
            notifyDataSetChanged();
            onCategoryClickListener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_title);
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public void setActiveCategory(Category activeCategory) {
        this.activeCategory = activeCategory;
        notifyDataSetChanged();
    }
}
