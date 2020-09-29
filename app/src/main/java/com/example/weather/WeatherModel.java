package com.example.weather;

public class WeatherModel {
    public String lowTemp;
    public String highTemp;
    public String description;

    public WeatherModel(String LowTemp, String HighTemp, String Description){
        lowTemp = LowTemp;
        highTemp = HighTemp;
        description = Description;
    }

}
