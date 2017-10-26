package com.example.nikhil.group22_hw4;

/**
 *
 * File name - TriviaActivity.java
 * Full Name - Nikhil Jonnalagadda
 *
 * **/
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener,ImageAsyncTask.sendImageInterface {


    TextView questionNumberTextView,timerTextView,questionTextView,loadingImageTextView,noImageTextView;
    Button previousButton,nextButton;
    ImageView questionImageView;
    ProgressBar progressBarTriviaActivity;
    ArrayList<Question> questionArrayList;
    int position;
    RadioGroup radioGroup;
    int time = 120;
    Handler handler;
    Runnable runnable;
    ImageAsyncTask imageAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        Intent intent = getIntent();
        if( intent != null) {
            questionArrayList = (ArrayList<Question>) intent.getSerializableExtra("key");
        }

        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionImageView = (ImageView) findViewById(R.id.questionImageView);
        loadingImageTextView = (TextView) findViewById(R.id.loadingImageTextView);
        noImageTextView = (TextView) findViewById(R.id.noImageTextView);

        previousButton = (Button) findViewById(R.id.previousButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        imageAsyncTask = new ImageAsyncTask(TriviaActivity.this);

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        progressBarTriviaActivity = (ProgressBar) findViewById(R.id.progressBarTriviaActivity);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        timerTextView.setText(getString(R.string.time_left)+String.valueOf(msg.obj)+getString(R.string.seconds));
                        break;
                    case 1:
                        Intent intent = new Intent(TriviaActivity.this,StatsActivity.class);
                        intent.putExtra("key",questionArrayList);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                setTime();
            }
        };

        handler.postDelayed(runnable,1000);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("nikhil","checkec id is "+checkedId);

                String answer = questionArrayList.get(position).getChoices().get(checkedId-1);
                questionArrayList.get(position).setGivenAnswer(answer);
            }
        });

        position = 0;
        progressBarTriviaActivity.setVisibility(View.VISIBLE);
        loadingImageTextView.setVisibility(View.VISIBLE);
        previousButton.setClickable(false);
        imageAsyncTask.execute(questionArrayList.get(position).getImageUrl());
        questionNumberTextView.setText(getString(R.string.q)+(position+1));
        questionTextView.setText(questionArrayList.get(position).getQuestionText());
        ArrayList<String> choices = questionArrayList.get(position).getChoices();

        if(radioGroup.getChildCount() > 0) {
            radioGroup.removeAllViews();
        }

        for(int i=0;i<choices.size();i++) {
            RadioButton button = new RadioButton(this);
            button.setText(choices.get(i));
            radioGroup.addView(button);
        }

    }

    public void setTime() {
        Message message = new Message();
        if(time > 0) {
            message.obj = time;
            message.what  = 0;
            handler.sendMessage(message);
            handler.postDelayed(runnable,1000);
            time--;
        } else {

            handler.removeCallbacks(runnable);
            message.obj = time;
            message.what  = 1;
            handler.sendMessage(message);
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.previousButton:
                if(position == 0) {
                    previousButton.setClickable(false);
                    return;
                } else {
                    previousButton.setClickable(true);
                }
                --position;
                break;

            case R.id.nextButton:
                ++position;
                if(position != 0) {
                    previousButton.setClickable(true);
                    previousButton.setOnClickListener(TriviaActivity.this);
                }
                if(position == questionArrayList.size() - 1) {
                    nextButton.setText(getString(R.string.finish));
                } else if (position == questionArrayList.size()) {
                    handler.removeCallbacks(runnable);
                    Intent intent = new Intent(TriviaActivity.this,StatsActivity.class);
                    intent.putExtra("key",questionArrayList);
                    startActivity(intent);
                    finish();
                  //  Toast.makeText(TriviaActivity.this,"finished test",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                break;
        }

        noImageTextView.setVisibility(View.INVISIBLE);
        questionImageView.setVisibility(View.INVISIBLE);
        progressBarTriviaActivity.setVisibility(View.VISIBLE);
        loadingImageTextView.setVisibility(View.VISIBLE);

        if(imageAsyncTask != null) {
            imageAsyncTask.cancel(true);
            imageAsyncTask = null;
        }
        imageAsyncTask = new ImageAsyncTask(TriviaActivity.this);
        imageAsyncTask.execute(questionArrayList.get(position).getImageUrl());

        questionNumberTextView.setText(getString(R.string.q)+(position+1));
        questionTextView.setText(questionArrayList.get(position).getQuestionText());
        ArrayList<String> choices = questionArrayList.get(position).getChoices();

        if(radioGroup.getChildCount() > 0) {
            radioGroup.removeAllViews();
        }
        String checked = questionArrayList.get(position).getGivenAnswer();
        for(int i=0;i<choices.size();i++) {
            RadioButton button = new RadioButton(this);
            button.setText(choices.get(i));
            button.setId(i+1);
            if(checked.equals(choices.get(i))) {
                button.setChecked(true);
            }
            radioGroup.addView(button);
        }
    }

    @Override
    public void sendBitmapImage(Bitmap bitmap) {

        loadingImageTextView.setVisibility(View.INVISIBLE);
        progressBarTriviaActivity.setVisibility(View.INVISIBLE);
        if(bitmap != null) {
            questionImageView.setImageBitmap(bitmap);
            questionImageView.setVisibility(View.VISIBLE);
        } else {
            noImageTextView.setVisibility(View.VISIBLE);
        }

    }
}
