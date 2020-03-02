package com.park.optech.parking;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohamed on 14/11/2017.
 */

public class worker extends AsyncTask<String,Void,Void> {
    Context context;
    worker(Context ctx){
        context=ctx;
    }

    @Override
    protected Void doInBackground(String... voids) {
        String type=voids[0];
        String urllogin="http://localhost:8080/client/login.php";
        if (type.equals("login")){
            try {
                URL url=new URL(urllogin);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
