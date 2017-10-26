package com.example.nikhil.group22_hw4;


/**
 *
 * File name - StatsActivity.java
 * Full Name - Nikhil Jonnalagadda
 *
 * **/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ListView answersListView = (ListView) findViewById(R.id.answersListView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarStatsActivity);
        TextView percentTextView = (TextView) findViewById(R.id.percentageTextView);

        Button finishButton = (Button) findViewById(R.id.finishButton);
        if(finishButton != null) {
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        ArrayList<Question> arrayList = (ArrayList<Question>) getIntent().getSerializableExtra("key");

        ArrayList<Question> newArrayList = new ArrayList<>();
        int count = 0;
        Iterator<Question> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Question question = iterator.next();
            if (question.getGivenAnswer() != question.getAnswer()) {
                newArrayList.add(question);
                iterator.remove();
            }
        }

        Iterator<Question> iterator2 = arrayList.iterator();
        while (iterator2.hasNext()) {
            count++;
            newArrayList.add(iterator2.next());
        }

        ArrayAdapter answersAdapter = new AnswersAdapter(this,R.id.percentageTextView,newArrayList);
        answersListView.setAdapter(answersAdapter);

        progressBar.setMax(newArrayList.size());
        progressBar.setProgress(count);
        percentTextView.setText(String.valueOf(100*count/newArrayList.size())+getString(R.string.percent));

    }
}
