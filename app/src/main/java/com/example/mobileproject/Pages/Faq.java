package com.example.mobileproject.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;

public class Faq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setupQuestionToggle(R.id.question_text_1, R.id.answer_text_1);
        setupQuestionToggle(R.id.question_text_2, R.id.answer_text_2);
        setupQuestionToggle(R.id.question_text_3, R.id.answer_text_3);
        setupQuestionToggle(R.id.question_text_4, R.id.answer_text_4);
        setupQuestionToggle(R.id.question_text_5, R.id.answer_text_5);
        setupQuestionToggle(R.id.question_text_6, R.id.answer_text_6);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Faq.this, Profile.class));
            }
        });
    }

    private void setupQuestionToggle(int questionId, int answerId) {
        TextView questionTextView = findViewById(questionId);
        TextView answerTextView = findViewById(answerId);

        questionTextView.setOnClickListener(v -> {
            if (answerTextView.getVisibility() == View.GONE) {
                answerTextView.setVisibility(View.VISIBLE);
            } else {
                answerTextView.setVisibility(View.GONE);
            }
        });
    }
}
