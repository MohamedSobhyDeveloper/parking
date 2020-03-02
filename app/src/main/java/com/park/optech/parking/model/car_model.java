package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 03/01/2018.
 */

public class car_model {

    @SerializedName("Location_Sign_name")
    @Expose
    private String Location_Sign_name;
    @SerializedName("Latitude")
    @Expose
    private String Latitude;
    @SerializedName("Longitude")
    @Expose
    private String Longitude;


    public String getLocation_Sign_name() {
        return Location_Sign_name;
    }

    public void setLocation_Sign_name(String location_Sign_name) {
        Location_Sign_name = location_Sign_name;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
