package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    LinearLayout llMain;
    EditText editSearch;
    Button btnSearch, btnChangeActivity;
    TextView txtCity, txtCountry, txtTemp, txtStatus, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon;

    String CITY = "Ha noi";
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        GetCurrentWeatherData("Ha noi");

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 18) {
            llMain.setBackgroundResource(R.drawable.day);
        } else {
            llMain.setBackgroundResource(R.drawable.night);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                if (!city.equals("")) {
                    CITY = city;
                }
                GetCurrentWeatherData(CITY);
            }
        });
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("jsonObject", jsonObject.toString());
                startActivity(intent);
            }
        });
    }

    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=d42b7179e2061f95601b64af5a5d5a32";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            txtCity.setText(jsonObject.getString("name") + ",");
                            txtCountry.setText(jsonObject.getJSONObject("sys").getString("country"));

                            JSONObject jsonObjectWeather = jsonObject.getJSONArray("weather").getJSONObject(0);
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/" + icon + "@4x.png").into(imgIcon);
                            txtStatus.setText(jsonObjectWeather.getString("main"));

                            String temp = jsonObject.getJSONObject("main").getString("temp");
                            txtTemp.setText(Double.valueOf(temp).intValue() + "°C");
                            txtHumidity.setText(jsonObject.getJSONObject("main").getString("humidity") + "%");
                            txtWind.setText(jsonObject.getJSONObject("wind").getString("speed") + "m/s");
                            txtCloud.setText(jsonObject.getJSONObject("clouds").getString("all") + "%");

                            Date date = new Date(jsonObject.getLong("dt") * 1000L);
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE yyyy-MM-dd HH:mm:ss");
                            txtDay.setText(simpleDateFormat.format(date));
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
        llMain = (LinearLayout) findViewById(R.id.llMain);
        editSearch = (EditText) findViewById(R.id.editTextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtCity = (TextView) findViewById(R.id.textViewCity);
        txtCountry = (TextView) findViewById(R.id.textViewCountry);
        txtTemp = (TextView) findViewById(R.id.textViewTemp);
        txtStatus = (TextView) findViewById(R.id.textViewStatus);
        txtHumidity = (TextView) findViewById(R.id.textViewHumidity);
        txtCloud = (TextView) findViewById(R.id.textViewCloud);
        txtWind = (TextView) findViewById(R.id.textViewWind);
        txtDay = (TextView) findViewById(R.id.textViewDay);
        imgIcon = (ImageView) findViewById(R.id.imageIcon);
    }
}