package com.example.nikhil.group22_hw4;

/**
 *
 * File name - ImageAsyncTask.java
 * Full Name - Nikhil Jonnalagadda
 *
 * **/

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageAsyncTask extends AsyncTask<String,Void,Bitmap> {

    private sendImageInterface sendImageObject;

    public ImageAsyncTask(sendImageInterface sendImageObject) {
        super();
        this.sendImageObject = sendImageObject;

    }

    @Override
    protected Bitmap doInBackground(String... params) {

        if(params[0].isEmpty()) {
            return null;
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        sendImageObject.sendBitmapImage(bitmap);

    }

    public interface sendImageInterface {
        void sendBitmapImage(Bitmap bitmap);
    }
}
