package com.example.nikhil.group22_hw4;

/**
 *
 * File name - LoadTriviaAsyncTask.java
 * Full Name - Nikhil Jonnalagadda
 *
 * **/
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LoadTriviaAsyncTask extends AsyncTask<String,Void,ArrayList<Question>> {

    private sendQuestionsInterface questionsInterface;

    public LoadTriviaAsyncTask(sendQuestionsInterface questions) {
        super();
        this.questionsInterface = questions;

    }


    @Override
    protected ArrayList<Question> doInBackground(String... params) {

        ArrayList<Question> questionArrayList = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine())!= null) {
                stringBuilder.append(line);
            }

            JSONObject rootObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = rootObject.getJSONArray("questions");
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject questionObject = jsonArray.getJSONObject(i);

                Question question = new Question();
                question.setQuestionID(questionObject.getInt("id"));

                if(questionObject.has("image")) {
                    question.setImageUrl(questionObject.getString("image"));
                } else {
                    question.setImageUrl("");
                }
                question.setQuestionText(questionObject.getString("text"));

                JSONObject choicesObject = questionObject.getJSONObject("choices");
                int answer = Integer.parseInt(choicesObject.getString("answer"));

                JSONArray choicesArray = choicesObject.getJSONArray("choice");
                ArrayList<String> choices = new ArrayList<String>();
                for(int j=0;j<choicesArray.length();j++) {
                    choices.add((String) choicesArray.get(j));
                }
                question.setAnswer(choices.get(answer-1));
                question.setChoices(choices);
                question.setGivenAnswer("");
                questionArrayList.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return questionArrayList;
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onCancelled(ArrayList<Question> questions) {
        super.onCancelled(questions);
    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        super.onPostExecute(questions);
        questionsInterface.sendQuestions(questions);
    }

    public interface sendQuestionsInterface {
        void sendQuestions(ArrayList<Question> questions);
    }



}
