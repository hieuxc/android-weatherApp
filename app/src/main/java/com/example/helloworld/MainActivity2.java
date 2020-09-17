package com.example.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    LinearLayout llMain2;
    TextView txtCity, txtCountry;
    ListView lv;
    ImageView imgIcon;
    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArrayList;
    JSONObject jsonCurrentWeather;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Mapping();

        try {
            jsonCurrentWeather = new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("jsonObject")));
            txtCity.setText(jsonCurrentWeather.getString("name") + ",");
            txtCountry.setText(jsonCurrentWeather.getJSONObject("sys").getString("country"));

            JSONObject jsonCoord = jsonCurrentWeather.getJSONObject("coord");
            Get7DayData(jsonCoord.getString("lat"), jsonCoord.getString("lon"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 18) {
            llMain2.setBackgroundResource(R.drawable.day);
        } else {
            llMain2.setBackgroundResource(R.drawable.night);
        }
    }

    public void Get7DayData(String lat, String lon) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon
                + "&exclude=minutely,hourly&units=metric&lang=vi&appid=d42b7179e2061f95601b64af5a5d5a32";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("daily");
                            for (int i = 1; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);

                                Date day = new Date(jsonObjectList.getLong("dt") * 1000L);
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("EEEE");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDay = new SimpleDateFormat("dd/M");
                                String DayOfWeek = formatDayOfWeek.format(day);
                                String Day = formatDay.format(day);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                Integer min = Double.valueOf(jsonObjectTemp.getString("min")).intValue();
                                Integer max = Double.valueOf(jsonObjectTemp.getString("max")).intValue();
                                String Temp = min + "° ~ " + max + "°";

                                JSONObject jsonObjectWeather = jsonObjectList.getJSONArray("weather").getJSONObject(0);
                                String Status = jsonObjectWeather.getString("description");
                                String ImageIcon = jsonObjectWeather.getString("icon");

                                weatherArrayList.add(new Weather(DayOfWeek, Day, ImageIcon, Temp, Status));
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    private void Mapping() {
        llMain2 = (LinearLayout) findViewById(R.id.llMain2);
        txtCity = (TextView) findViewById(R.id.textViewCity);
        txtCountry = (TextView) findViewById(R.id.textViewCountry);
        lv = (ListView) findViewById(R.id.listView);
        weatherArrayList = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(MainActivity2.this, weatherArrayList);
        lv.setAdapter(customAdapter);
    }
}