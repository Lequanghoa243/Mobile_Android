package com.example.mobileproject.Common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileproject.R;

public class Splash_screen extends AppCompatActivity {
    private static int Splash_timer = 4000;
    ImageView backgroundImage;
    TextView createdby;
    Animation  sideAnim,bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backgroundImage = findViewById(R.id.splashimg);
        createdby = findViewById(R.id.created_by);
        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        backgroundImage.setAnimation(sideAnim);
        createdby.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_screen.this, Login.class);
                startActivity(intent);
                finish();
            }
        },Splash_timer);

    }
}