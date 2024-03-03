package com.zmaryalaiali.wheater;

public class WeatherModel {

    private String time;
    private String tempreture;
    private String icon;
    private String windSpeed;

    public WeatherModel(String time, String tempreture, String icon, String windSpeed) {
        this.time = time;
        this.tempreture = tempreture;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTempreture() {
        return tempreture;
    }

    public void setTempreture(String tempreture) {
        this.tempreture = tempreture;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
