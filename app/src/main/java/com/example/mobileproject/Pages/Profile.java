package com.example.mobileproject.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    SharedPreferencesManager sharedPreferencesManager;
    TextView usernameTextView, emailTextView;
    LinearLayout loggedInView, loggedOutView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.useremail);
        loggedInView = findViewById(R.id.logged_in_view);
        loggedOutView = findViewById(R.id.logged_out_view);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.user);

        bottomNavigationView.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.findViewById(R.id.my_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add your action here

                Intent intent = new Intent(Profile.this, MyCourse.class);
                startActivity(intent);

            }
        });

        bottomNavigationView.findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add your action here
                Intent intent = new Intent(Profile.this, Category.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.findViewById(R.id.user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Profile.class);
                startActivity(intent);
            }
        });

        LinearLayout faqLayout = findViewById(R.id.certificate);
        faqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Faq.class);
                startActivity(intent);
            }
        });



        checkLoginStatus();
    }

    private void checkLoginStatus() {
        String firstName = sharedPreferencesManager.getFirstName();
        String lastName = sharedPreferencesManager.getLastName();
        String email = sharedPreferencesManager.getEmail();

        if (firstName != null && lastName != null && email != null) {
            loggedInView.setVisibility(View.VISIBLE);
            loggedOutView.setVisibility(View.GONE);
            loadUserData();
        } else {
            loggedInView.setVisibility(View.GONE);
            loggedOutView.setVisibility(View.VISIBLE);
        }
    }

    private void loadUserData() {
        String firstName = sharedPreferencesManager.getFirstName();
        String lastName = sharedPreferencesManager.getLastName();
        String email = sharedPreferencesManager.getEmail();

        if (firstName != null && lastName != null) {
            usernameTextView.setText(firstName + " " + lastName);
        }
        if (email != null) {
            emailTextView.setText(email);
        }
    }

    public void navigateToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void navigateToSignup(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void logout(View view) {
        sharedPreferencesManager.clearUserDetails();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        checkLoginStatus();
    }
}
