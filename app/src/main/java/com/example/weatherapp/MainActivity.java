package com.example.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        resultTextView = findViewById(R.id.resultTextView);

    }

    public class Weather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                Log.i("result" , result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this , "Could not find weather", Toast.LENGTH_LONG);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("result" , result);
            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                JSONArray arr = jsonObject.getJSONArray("weather");
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(main != "" && description != ""){
                        message += main+" : "+description+"\r\n";
                    }
                }
                if(message != ""){
                    resultTextView.setText(message);
                } else{
                    Toast.makeText(MainActivity.this , "Could not find weather", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this , "Could not find weather", Toast.LENGTH_LONG);
            } catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public void findWeather(View view){

        Log.i("cityname", cityName.getText().toString());
        Weather task = new Weather();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&appid=c6a892e69e83c17d872b1ac159c88cf8");

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);
    }

}