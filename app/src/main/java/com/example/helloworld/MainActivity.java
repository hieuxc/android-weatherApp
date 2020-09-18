package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    Button btnChangeActivity;
    ImageButton btnSearch;
    TextView txtCity, txtTemp, txtDes, txtHumidity, txtCloud, txtWind, txtDay, txtTime, txtTempMax,
            txtTempMin, txtSunrise, txtSunset, txtPressure;
    ImageView imgIcon;

    String CITY = "Ha noi";
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        GetCurrentWeatherData(CITY);

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
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=d42b7179e2061f95601b64af5a5d5a32";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtCity.setText(jsonObject.getString("name") + ", " + country);

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatSun = new SimpleDateFormat("hh:mm");
                            Date dateSunrise = new Date(jsonObjectSys.getLong("sunrise") * 1000l);
                            Date dateSunset = new Date(jsonObjectSys.getLong("sunset") * 1000l);
                            txtSunrise.setText(formatSun.format(dateSunrise)+" AM");
                            txtSunset.setText(formatSun.format(dateSunset)+" PM");

                            JSONObject jsonObjectWeather = jsonObject.getJSONArray("weather").getJSONObject(0);
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/" + icon + "@4x.png").into(imgIcon);
                            txtDes.setText(jsonObjectWeather.getString("description"));

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            Log.d("main:  ", jsonObjectMain.toString());

                            int temp = Double.valueOf(jsonObjectMain.getString("temp")).intValue();
                            int tempMax = Double.valueOf(jsonObjectMain.getString("temp_max")).intValue();
                            int tempMin = Double.valueOf(jsonObjectMain.getString("temp_min")).intValue();
                            txtTemp.setText(Integer.toString(temp));
                            txtTempMax.setText(tempMax + "°C");
                            txtTempMin.setText(tempMin + "°C");
                            txtHumidity.setText(jsonObjectMain.getString("humidity") + "%");
                            txtPressure.setText(jsonObjectMain.getString("pressure") + "hPa");

                            txtWind.setText(jsonObject.getJSONObject("wind").getString("speed") + "m/s");
                            txtCloud.setText(jsonObject.getJSONObject("clouds").getString("all") + "%");

                            Date date = new Date(jsonObject.getLong("dt") * 1000L);
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDay = new SimpleDateFormat("EEEE d/M");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
                            txtDay.setText(formatDay.format(date));
                            txtTime.setText(formatTime.format(date));

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
        btnSearch = (ImageButton) findViewById(R.id.buttonSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtCity = (TextView) findViewById(R.id.textViewCity);
        txtTemp = (TextView) findViewById(R.id.textViewTemp);
        txtDes = (TextView) findViewById(R.id.textViewDes);
        txtHumidity = (TextView) findViewById(R.id.textViewHumidity);
        txtCloud = (TextView) findViewById(R.id.textViewCloud);
        txtWind = (TextView) findViewById(R.id.textViewWind);
        txtDay = (TextView) findViewById(R.id.textViewDay);
        txtTempMax = (TextView) findViewById(R.id.textViewTempMax);
        txtTempMin = (TextView) findViewById(R.id.textViewTempMin);
        txtSunrise = (TextView) findViewById(R.id.textViewSunrise);
        txtSunset = (TextView) findViewById(R.id.textViewSunset);
        txtPressure = (TextView) findViewById((R.id.textViewPressure));
        txtTime = (TextView) findViewById(R.id.textViewTime);
        imgIcon = (ImageView) findViewById(R.id.imageViewIcon);
    }
}