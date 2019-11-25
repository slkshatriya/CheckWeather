package com.one4all.checkweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
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
    View focusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        requestQueue = Volley.newRequestQueue(this);
        Log.d(TAG, "onCreate: passed");
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.KEYCODE_ENTER){
                    checking();
                    return true;

                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                checking();

            }
        });
    }
    public void checking(){
        if (!editText.getText().toString().isEmpty()) {
            Intent intent = new Intent(MainActivity.this, DetailWeather.class);
            intent.putExtra("cityName", editText.getText().toString());
            startActivity(intent);
            editText.setText("");
        }else {
            editText.setError("City name is Empty");
            editText.setFocusable(true);
        }

    }

    public void findView(){
//        cityName = findViewById(R.id.city_name);
//        description = findViewById(R.id.description);
//        cityTemp = findViewById(R.id.city_temp);
//        humidity = findViewById(R.id.humidity);
//        minTemp = findViewById(R.id.min_temp);
//        maxTemp = findViewById(R.id.max_temp);
        button = findViewById(R.id.button);
        editText =findViewById(R.id.editText);
    }
    public void jsonParse(){
        String cit = editText.getText().toString();


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
                    JSONObject jsonObject = response.getJSONObject("main");
                    String ctemp = jsonObject.getString("temp");
                    Log.d(TAG, "onResponse: "+ctemp);
                    cityTemp.setText(ctemp+"K");
                    String humi =jsonObject.getString("humidity");

                    Log.d(TAG, "onResponse: "+humi);
                    humidity.setText(humi);
                    String tM = jsonObject.getString("temp_min");
                    Log.d(TAG, "onResponse: "+tM);
                    minTemp.setText(tM+"K");
                    String tMax = jsonObject.getString("temp_max");
                    Log.d(TAG, "onResponse: "+tMax);
                    maxTemp.setText(tMax+"K");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: passed");
                error.printStackTrace();
                Log.d(TAG, "onErrorResponse: "+error);

            }
        });
        requestQueue.add(jsonObjectRequest);


    }


}
