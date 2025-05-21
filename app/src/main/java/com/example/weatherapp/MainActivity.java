package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText edt;
    Button btn;
    TextView txt;

    public class Downloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");

                String main = "";
                String description = "";

                String message = "";

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(!main.equals("") && !description.equals("")){

                        message += main + ":" +  description + "\r\n";

                    }

                }
                if (!message.equals("")){
                    txt.setText(message);

                }else{
                    txt.setText("can't find the city");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt = findViewById(R.id.edt);
        btn = findViewById(R.id.btn);
        txt = findViewById(R.id.textResult);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Downloader downloader = new Downloader();

                try {
                    String result = downloader.execute("https://api.openweathermap.org/data/2.5/weather?q="+ edt.getText().toString() +"&appid=46a7ff5e436efc78f1aac010bfe3fb2b").get();
                    Log.i("Json file",result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });



    }
}