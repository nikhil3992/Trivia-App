package com.example.nikhil.group22_hw4;
/**
 *
 * File name - AnswersAdapter.java
 * Full Name - Nikhil Jonnalagadda
 *
 * **/
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AnswersAdapter extends ArrayAdapter<Question> {

    private Context context;
    private ArrayList<Question> list;

    public AnswersAdapter(Context context, int resource, ArrayList<Question> list) {
        super(context, resource);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item,parent,false);
            viewHolder.question = (TextView) convertView.findViewById(R.id.question);
            viewHolder.yourAnswer = (TextView) convertView.findViewById(R.id.yourAnswer);
            viewHolder.correctAnswer = (TextView) convertView.findViewById(R.id.correctAnswer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Question questionObject = list.get(position);
        viewHolder.question.setText(questionObject.getQuestionText());
        viewHolder.yourAnswer.setText(questionObject.getGivenAnswer());
        viewHolder.correctAnswer.setText(questionObject.getAnswer());

        if(questionObject.getGivenAnswer().equals(questionObject.getAnswer())) {
            viewHolder.yourAnswer.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            viewHolder.yourAnswer.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        return convertView;
    }

    private class ViewHolder {
        TextView question,yourAnswer,correctAnswer;
    }
}
