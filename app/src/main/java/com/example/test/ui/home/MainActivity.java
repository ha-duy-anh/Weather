package com.example.test.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.test.model.Weather;
import com.example.test.ui.detail.MainActivity2;
import com.example.test.R;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter programAdapter;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView recyclerView2;
    CustomAdapter2 customAdapter2;
    RecyclerView.LayoutManager layoutManager2;

    Weather weatherData;
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvText);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<RV1Data> rv1DataList = new ArrayList<>();
        rv1DataList.add(new RV1Data(R.drawable.rain, "21℃", "4:00 PM"));
        rv1DataList.add(new RV1Data(R.drawable.night_storm, "20℃", "5:00 PM"));
        rv1DataList.add(new RV1Data(R.drawable.night_storm, "19℃", "6:00 PM"));
        rv1DataList.add(new RV1Data(R.drawable.night_storm, "19℃", "6:00 PM"));
        rv1DataList.add(new RV1Data(R.drawable.rain, "20℃", "8:00 PM"));
        rv1DataList.add(new RV1Data(R.drawable.night_storm, "22℃", "9:00 PM"));
        programAdapter = new CustomAdapter(rv1DataList);
        recyclerView.setAdapter(programAdapter);

        recyclerView2 = findViewById(R.id.rvText2);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        List<RV2Data> rv2DataList = new ArrayList<>();
        rv2DataList.add(new RV2Data(R.drawable.rain3, "Wednesday", "Chance of heavy rain.", "20℃"));
        rv2DataList.add(new RV2Data(R.drawable.rain4, "Thursday", "Chance of heavy rain.", "20℃"));
        rv2DataList.add(new RV2Data(R.drawable.rain4, "Friday", "Chance of heavy rain.", "20℃"));
        rv2DataList.add(new RV2Data(R.drawable.rain3, "Saturday", "Chance of heavy rain.", "20℃"));
        customAdapter2 = new CustomAdapter2(rv2DataList);
        recyclerView2.setAdapter(customAdapter2);

        Spinner spinnerLocation = findViewById(R.id.location_dropdown_menu);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.location, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLocation.setAdapter(adapter);

        new Async().execute();
        textView1 = findViewById(R.id.celsiusDegree);
        textView2 = findViewById(R.id.text); //desc

    }

    public void newScreen(View view) {
        Intent in = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(in);
    }

    private class Async extends AsyncTask <Void, String, Weather> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Weather doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            StringBuilder readTextBuf = new StringBuilder();

            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=ef7d0f574f4f130be5273b3c5e07988d");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    readTextBuf.append(line);
                }

                int code = urlConnection.getResponseCode();
                if (code !=  200) {throw new IOException("Invalid response from server: " + code);}

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                Object reader = parser.parse(readTextBuf.toString());
                String inputWeather = reader.toString();
                weatherData = gson.fromJson(inputWeather, Weather.class);
            } catch (IOException exception1) {
                exception1.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return weatherData;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            textView1.setText(weatherData.getMain().getTemp() + "℉");
            if (weatherData.getWeatherDesc().isEmpty()) {
                System.out.println("Array is empty");
            } else {
                textView2.setText(weatherData.getWeatherDesc().get(0).getDescription());
            }

        }
    }
}