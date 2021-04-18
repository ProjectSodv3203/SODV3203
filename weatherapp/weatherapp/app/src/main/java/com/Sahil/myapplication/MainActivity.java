package com.Sahil.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText City,Country;
    TextView Result;
    private   final String url="http://api.openweathermap.org/data/2.5/weather";
    private  final String appid="9a91bca98fa62daa026e617f8bd0d2af";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        City=findViewById(R.id.entercity);
        Country= findViewById(R.id.entercountry);
        Result = findViewById(R.id.result);

    }

    public void getdetails(View view) {
        String CompleteUrl="";
        String Cityd = City.getText().toString().trim();
        String countryd = Country.getText().toString().trim();
        if(Cityd.equals("")){
            Result.setText("Please Enter City Name");
        }
        else{
            if(!countryd.equals("")){
               CompleteUrl=url+"?q="+Cityd+","+countryd+"&appid="+appid;
            }else{
                CompleteUrl=url+"?q="+Cityd+"&appid="+appid;

            }
            StringRequest SR = new StringRequest(Request.Method.POST, CompleteUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response",response);
                    String Output ="";

                    try {
                        JSONObject  Json = new JSONObject(response);
                        JSONArray jsonArray = Json.getJSONArray("weather");
                        JSONObject JObjectweather = jsonArray.getJSONObject(0);
                        String detail = JObjectweather.getString("description");
                        JSONObject jobject = Json.getJSONObject("main");
                        double  temp = jobject.getDouble("temp")-273.15;
                        double feelslike = jobject.getDouble("feels_like")-273.15;
                        float pressure = jobject.getInt("pressure");
                        int humidity = jobject.getInt("humidity");
                        JSONObject jsonwind = Json.getJSONObject("wind");
                        String wind = jsonwind.getString("speed");
                        JSONObject jsonclouds = Json.getJSONObject("clouds");
                        String clouds = jsonclouds.getString("all");
                        JSONObject jsonsys = Json.getJSONObject("sys");
                        String Countryname = jsonsys.getString("country");
                        String cityname = Json.getString("name");
                        Result.setTextColor(Color.rgb(70,50,12));
                        Output=Output+"Current Weather Detail Of"+" "+Cityd +" "+countryd+" "
                                +"\n Temp:"+df.format(temp)+"°C"+"\n Feels Like:"+df.format(feelslike)
                                +"°C"+"\n Humidity:"+humidity+"%"
                                +"\n Description:"+detail+"\n Wind Speed"+wind+"M/s"+
                                "\n Cloudiness"+pressure+"hPa";
                        Result.setText(Output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(SR);

        }


    }
}