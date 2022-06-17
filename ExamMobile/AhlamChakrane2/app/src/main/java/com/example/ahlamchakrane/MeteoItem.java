package com.example.ahlamchakrane;

public class MeteoItem {

    public Double main_temp;
    public Double dt;
    public Double main_feels_like;
    public Double lon;
    public Double lat;
    public Double main_temp_min;
    public Double main_temp_max;
    public Double main_pressure;
    public Double main_humidity;
    public Double wind_speed;

    public MeteoItem(Double main_temp, Double dt, Double main_feels_like, Double main_temp_min
            ,Double main_temp_max
            ,Double main_pressure
            ,Double main_humidity
            ,Double wind_speed
            ,Double lon
            ,Double lat) {
        this.main_temp = main_temp;
        this.dt = dt;
        this.main_feels_like = main_feels_like;
        this.main_temp_min = main_temp_min;
        this.main_temp_max = main_temp_max;
        this.main_pressure = main_pressure;
        this.main_humidity = main_humidity;
        this.wind_speed = wind_speed;
        this.lat = lat;
        this.lon = lon;
    }

    public Double getMain_temp() {
        return main_temp;
    }

    public void setMain_temp(Double main_temp) {
        this.main_temp = main_temp;
    }

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public Double getMain_feels_like() {
        return main_feels_like;
    }

    public void setMain_feels_like(Double main_feels_like) {
        this.main_feels_like = main_feels_like;
    }

    public Double getMain_temp_min() {
        return main_temp_min;
    }

    public void setMain_temp_min(Double main_temp_min) {
        this.main_temp_min = main_temp_min;
    }

    public Double getMain_temp_max() {
        return main_temp_max;
    }

    public void setMain_temp_max(Double main_temp_max) {
        this.main_temp_max = main_temp_max;
    }

    public Double getMain_pressure() {
        return main_pressure;
    }

    public void setMain_pressure(Double main_pressure) {
        this.main_pressure = main_pressure;
    }

    public Double getMain_humidity() {
        return main_humidity;
    }

    public void setMain_humidity(Double main_humidity) {
        this.main_humidity = main_humidity;
    }

    public Double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
