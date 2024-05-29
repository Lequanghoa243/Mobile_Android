package com.example.mobileproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {

    private List<String> searchHistoryList;

    public SearchHistoryAdapter(List<String> searchHistoryList) {
        this.searchHistoryList = searchHistoryList;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, parent, false);
        return new SearchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        String searchQuery = searchHistoryList.get(position);
        holder.historyItem.setText(searchQuery);
    }

    @Override
    public int getItemCount() {
        return searchHistoryList.size();
    }

    public static class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView historyItem;

        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyItem = itemView.findViewById(R.id.history_item);
        }
    }
}
