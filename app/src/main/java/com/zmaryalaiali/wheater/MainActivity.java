package com.zmaryalaiali.wheater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerViewHome;
    ProgressBar progressBarHome;
    TextInputLayout textInputLayoutSearch;
    TextView tvCityNameHome, tvTemp,tvWeatherInfo;
    ImageView ivIcon;
    ImageView ivSearch;

    WeatherAdapter weatherAdapter;

    ArrayList<WeatherModel> weatherModelList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewHome = findViewById(R.id.rv_home);
        textInputLayoutSearch = findViewById(R.id.et_cityName_search);
        progressBarHome = findViewById(R.id.progress_home);
        tvCityNameHome = findViewById(R.id.tv_cityName);
        tvTemp = findViewById(R.id.tv_temp);
        ivIcon = findViewById(R.id.iv_weather_icon_home);
        tvWeatherInfo = findViewById(R.id.tv_weather_info_home);
        ivSearch = findViewById(R.id.iv_search);

        weatherModelList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this,weatherModelList);
        recyclerViewHome.setAdapter(weatherAdapter);

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "want location permission ", Toast.LENGTH_SHORT).show();
            isPermission();
            return;
        }
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Toast.makeText(this, location.getProvider(), Toast.LENGTH_SHORT).show();

//        String cityName = getCityName(location.getLongitude(),location.getLatitude());

//        setInfo(cityName);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = textInputLayoutSearch.getEditText().getText().toString().trim();
                if (!city.isEmpty()){
                    weatherModelList.clear();
                    setInfo(city);
                    return;
                }
                Toast.makeText(MainActivity.this, "please provider the city Name", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setInfo(String cityName) {
        progressBarHome.setVisibility(View.GONE);
        String url = "http://api.weatherapi.com/v1/forecast.json?key=6b4893133916453899893135241702 &q=" + cityName +"&days=1&aqi=no&alerts=no";
        tvCityNameHome.setText(cityName);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url ,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject rootObject  = response.getJSONObject("current");
                    String temp = rootObject.getString("temp_c");
                    int isDay = rootObject.getInt("is_day");
//
                    if (isDay == 1 ){
                        // for isDay or Morning
//                        Glide.with(MainActivity.this).load("icon").into(ivIcon);
                        Toast.makeText(MainActivity.this, "day", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // for isNight or evening
                        Toast.makeText(MainActivity.this, "night", Toast.LENGTH_SHORT).show();
//                        Glide.with(MainActivity.this).load("icon").into(ivIcon);

                    }
                    JSONObject jsonObjectCondetion = rootObject.getJSONObject("condition");
                    String textHome = jsonObjectCondetion.getString("text");
                    String iconHome = jsonObjectCondetion.getString("icon");
                    tvWeatherInfo.setText(textHome+"text1");
                    Glide.with(MainActivity.this).load(iconHome).into(ivIcon);
//
                    JSONArray jsonArray  = response.getJSONObject("forecast").getJSONArray("forecastday");
                    JSONObject jsonObjectHour = jsonArray.getJSONObject(0);
//                    Toast.makeText(MainActivity.this, "Hour "+jsonObjectHour.toString(), Toast.LENGTH_LONG).show();
                    JSONArray  hours  = jsonObjectHour.getJSONArray("hour");
                    Toast.makeText(MainActivity.this, hours.toString(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < hours.length(); i++) {
                        JSONObject hour = hours.getJSONObject(i);
                        String time = hour.getString("time");
                        String tempRV = hour.getString("temp_c");
                        String windSpeed = hour.getString("wind_kph");
                        String icon = hour.getJSONObject("condition").getString("icon");
                        WeatherModel model = new WeatherModel(time , tempRV, icon , windSpeed);
                        weatherModelList.add(model);
                    }
                    weatherAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "error  with catch "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: error" +response.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, " Error with this "+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(request);
    }

    private boolean isPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.LOCATION_HARDWARE}, 1);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission is Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "please give me your location ", Toast.LENGTH_SHORT).show();
            isPermission();
        }
    }

    private String getCityName(double langitude, double latitude) {
        String cityName = "not found";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,langitude,10);
            for (Address adr: addresses) {

                if (adr != null){
                    String city = adr.getAdminArea();

                    if (city != null){
                        cityName = city;
                    }
                    else {
                        Toast.makeText(this, "current city not found ..", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        } catch (IOException e) {
            Log.d(TAG, "getCityName: Not Found..");
            Toast.makeText(this, "user City Not Found", Toast.LENGTH_SHORT).show();
        }
        return cityName;
    }
}