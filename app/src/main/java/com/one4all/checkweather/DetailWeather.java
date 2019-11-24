package com.one4all.checkweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailWeather extends AppCompatActivity {
    TextView cityName;
    TextView description;
    TextView cityTemp;
    TextView humidity;
    TextView minTemp;
    TextView maxTemp;
    Button button;
    EditText editText;
    RequestQueue requestQueue;
    private static final String TAG = "hello";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);
        requestQueue = Volley.newRequestQueue(this);
        String cityName = getIntent().getStringExtra("cityName");
        getSupportActionBar().setTitle(cityName);
        findView();
        progressBar.setVisibility(View.VISIBLE);
        jsonParse();
    }
    public void findView(){
        cityName = findViewById(R.id.city_name);
        description = findViewById(R.id.description);
        cityTemp = findViewById(R.id.city_temp);
        humidity = findViewById(R.id.humidity);
        minTemp = findViewById(R.id.min_temp);
        maxTemp = findViewById(R.id.max_temp);
        button = findViewById(R.id.button);
        editText =findViewById(R.id.editText);
        progressBar = findViewById(R.id.progressBar);
    }
    public void jsonParse(){
        String cit = getIntent().getStringExtra("cityName");


        Log.d(TAG, "jsonParse: parsed");
        String url ="https://api.openweathermap.org/data/2.5/weather?q="+cit+"&appid=944a827554566cb138283498ca7977d6";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: called"+response.toString());
                String cityN;

                try {
                    cityN = response.getString("name");
                    Log.d(TAG, "onResponse: "+cityN);
                    getSupportActionBar().setTitle(cityN);
                    cityName.setText(cityN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray des = response.getJSONArray("weather");
                    for (int i =0;i < des.length();i++){
                        JSONObject jsonObject = des.getJSONObject(i);
                        String de = jsonObject.getString("description");
                        Log.d(TAG, "onResponse: "+de);
                        description.setText(de);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = response.getJSONObject("main");
                    String ctemp = jsonObject.getString("temp");
                    Log.d(TAG, "onResponse: "+ctemp);
                    double temp = Double.parseDouble(ctemp) -273.15;
                    int t = (int) temp;

                    cityTemp.setText(t +"°C");
                    String humi =jsonObject.getString("humidity");

                    Log.d(TAG, "onResponse: "+humi);
                    humidity.setText(humi);
                    String tM = jsonObject.getString("temp_min");
                    Log.d(TAG, "onResponse: "+tM);
                    double clesinMin = Double.parseDouble(tM)  -273.15;
                    int r = (int) clesinMin;
                    minTemp.setText(r+"°C");
                    String tMax = jsonObject.getString("temp_max");
                    Log.d(TAG, "onResponse: "+tMax);
                    double clesMax = Double.parseDouble(tMax) -273.15;
                    int o = (int) clesMax;
                    maxTemp.setText(o+"°C");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(DetailWeather.this,error.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG, "onErrorResponse: passed");
                error.printStackTrace();
                Log.d(TAG, "onErrorResponse: "+error);

            }
        });
        requestQueue.add(jsonObjectRequest);


    }
}
