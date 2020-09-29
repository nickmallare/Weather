package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




public class WeatherInformation extends AppCompatActivity {

    private RequestQueue requestQueue;
    public List<String> cityWeatherDescriptionList = new ArrayList<>();
    public List<String> currentTempList = new ArrayList<>();
    public List<WeatherModel> cityWeatherList = new ArrayList<>();
    public List<String> lowTempList = new ArrayList<>();
    public List<String> highTempList = new ArrayList<>();
    public List<String> descriptionList = new ArrayList<>();
    public ListView lv;
    public String Day1;
    public String Day2;
    public String Day3;
    public String Day4;
    public String Day5;

    public int viewIDs[] = {R.id.Day1Text, R.id.Day2Text, R.id.Day3Text, R.id.Day4Text, R.id.Day5Text};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_information);
        requestQueue = Volley.newRequestQueue(this);

        String city = getIntent().getStringExtra("CITY");
        TextView cityView = (TextView)findViewById(R.id.Title);
        cityView.setText("5 Day Forecast for "+ city);
        getCityWeather(city);
        setDates();

    }

    public void setDates(){


        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();

        Date day1 = calendar.getTime();
        Day1 = dateFormat.format(day1);
        TextView date1 = (TextView)findViewById(R.id.Day1);
        date1.setText("Todays weather");

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day2 = calendar.getTime();
        Day2 = dateFormat.format(day2);
        TextView date2 = (TextView)findViewById(R.id.Day2);
        date2.setText(Day2);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day3 = calendar.getTime();
        Day3 = dateFormat.format(day3);
        TextView date3 = (TextView)findViewById(R.id.Day3);
        date3.setText(Day3);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day4 = calendar.getTime();
        Day4 = dateFormat.format(day4);
        TextView date4 = (TextView)findViewById(R.id.Day4);
        date4.setText(Day4);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day5 = calendar.getTime();
        Day5 = dateFormat.format(day5);
        TextView date5 = (TextView)findViewById(R.id.Day5);
        date5.setText(Day5);

    }

    public String ConvertToF(String temp){
        double convertedTemp = 0.0;
        String returnVal;
        double tempVal = Double.parseDouble(temp);
        convertedTemp = (tempVal-273.15) * (9.0/5.0) + 32.0;
        convertedTemp = Math.round(convertedTemp * 100.0)/100.0;
        returnVal = Double.toString(convertedTemp);

        return returnVal;
    }


    public void getCityWeather(String cityName) {
        Log.i("GETCITYWEATHER", "TEST");
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=315d138b31b407eb7468a83e1b45d07a";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i("Response: ", response.toString());
                        try {
                            JSONArray theList = response.getJSONArray("list");


                            //iterate through this each day! + 8 to previous index for new day!
                            for(int i = 0; i < 40; i += 8){
                                JSONObject firstElement = theList.getJSONObject(i);
                                //String test = firstElement.getString("temp_min");
                                JSONArray weather = firstElement.getJSONArray("weather");
                                JSONObject theWeather = weather.getJSONObject(0);

                                JSONObject temperature = firstElement.getJSONObject("main");

                                lowTempList.add(ConvertToF(temperature.getString("temp_min")));
                                highTempList.add(ConvertToF(temperature.getString("temp_max")));
                                currentTempList.add(ConvertToF(temperature.getString("temp")));
                                descriptionList.add(theWeather.getString("description"));

                            }
                            for(int i = 0; i <descriptionList.size(); i++){

                                TextView addInfo = (TextView)findViewById(viewIDs[i]);
                                addInfo.append(descriptionList.get(i));
                                addInfo.append("\n");
                                if(i == 0) {
                                    addInfo.append("Current Temperature: " + currentTempList.get(i));
                                    addInfo.append("\n");
                                }
                                addInfo.append("Low of " + lowTempList.get(i) + "  High of " + highTempList.get(i));
                            }

                            ImageView imageView = (ImageView) findViewById(R.id.ImageToday);


                            if (descriptionList.get(0).contains("cloud")) {
                                imageView.setImageResource(R.drawable.cloud_icon);
                            }
                            else if (descriptionList.get(0).contains("rain")) {
                                imageView.setImageResource(R.drawable.rain_icon);
                            }
                            else if (descriptionList.get(0).contains("snow")) {
                                imageView.setImageResource(R.drawable.snow_icon);
                            }
                            else if (descriptionList.get(0).contains("sky") || descriptionList.get(0).contains("sun")) {
                                imageView.setImageResource(R.drawable.sun_icon);
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.i("Response: ", error.toString());

            }

        });
        requestQueue.add(jsonObjectRequest);

    }

    public void setWeatherInformation(){
        TextView day1 = findViewById(R.id.Day1Text);
        day1.setText(descriptionList.get(1).toString());
    }


    public void BackButtonClicked(View view) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}

