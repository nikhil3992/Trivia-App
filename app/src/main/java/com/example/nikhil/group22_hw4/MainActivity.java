package com.example.nikhil.group22_hw4;

/**
 *
 * File name - MainActivity.java
 * Full Name - Nikhil Jonnalagadda
 *
 * **/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LoadTriviaAsyncTask.sendQuestionsInterface {


    Button startButton,exitButton;
    ImageView centerImage;
    TextView loadingTriviaTextView,triviaReadyTextView;
    ProgressBar progressBar;
    ArrayList<Question> questionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        centerImage = (ImageView) findViewById(R.id.centerImage);
        loadingTriviaTextView = (TextView) findViewById(R.id.loadingTriviaTextView);
        triviaReadyTextView = (TextView) findViewById(R.id.triviaReadyTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        startButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        new LoadTriviaAsyncTask(MainActivity.this).execute(getResources().getString(R.string.address));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:

                Intent intent = new Intent(MainActivity.this,TriviaActivity.class);
                intent.putExtra("key",questionArrayList);
                startActivity(intent);
                finish();
                break;
            case R.id.exitButton:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void sendQuestions(ArrayList<Question> questions) {

        questionArrayList = questions;
        startButton.setClickable(true);
        startButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        centerImage.setVisibility(View.VISIBLE);
        triviaReadyTextView.setVisibility(View.VISIBLE);
        loadingTriviaTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }
}
