package com.example.mobileproject.Pages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.adapter.SearchAdapter;
import com.example.mobileproject.adapter.SearchHistoryAdapter;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.retrofit.ApiInterface;
import com.example.mobileproject.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView, historyRecyclerView;
    private SearchAdapter searchAdapter;
    private SearchHistoryAdapter historyAdapter;
    private ApiInterface apiInterface;
    private List<Course> courseList = new ArrayList<>();
    private EditText searchEditText;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "search_prefs";
    private static final String SEARCH_HISTORY_KEY = "search_history";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_recycler);
        historyRecyclerView = findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCourses(s.toString());
                toggleHistory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        fetchCourses();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadSearchHistory();
    }

    private void fetchCourses() {
        Call<List<Course>> call = apiInterface.getAllCourse();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList = response.body();
                } else {
                    Toast.makeText(Search.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(Search.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterCourses(String query) {
        if (query.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            List<Course> filteredList = new ArrayList<>();
            for (Course course : courseList) {
                if (course.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(course);
                }
            }
            if (searchAdapter == null) {
                searchAdapter = new SearchAdapter(Search.this, filteredList, this::saveSearchQuery);
                recyclerView.setAdapter(searchAdapter);
            } else {
                searchAdapter.updateList(filteredList);
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void saveSearchQuery(String courseTitle) {
        Set<String> searchHistory = sharedPreferences.getStringSet(SEARCH_HISTORY_KEY, new HashSet<>());
        searchHistory.add(courseTitle);
        sharedPreferences.edit().putStringSet(SEARCH_HISTORY_KEY, searchHistory).apply();
        loadSearchHistory();
    }

    private void loadSearchHistory() {
        Set<String> searchHistory = sharedPreferences.getStringSet(SEARCH_HISTORY_KEY, new HashSet<>());
        historyAdapter = new SearchHistoryAdapter(new ArrayList<>(searchHistory));
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void toggleHistory(String query) {
        if (query.isEmpty()) {
            historyRecyclerView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            historyRecyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
