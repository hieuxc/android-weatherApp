package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    EditText editSearch;
    Button btnSearch, btnChangeActivity;
    TextView txtCity, txtCountry, txtTemp, txtStatus, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=editSearch.getText().toString();
                GetCurrentWeatherData(city);
            }
        });
    }

    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=d42b7179e2061f95601b64af5a5d5a32";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", response);
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