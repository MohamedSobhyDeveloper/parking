package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 03/01/2018.
 */

public class scan_model {

    @SerializedName("Event_ID")
    @Expose
    private String Event_ID;
    @SerializedName("Plate_No")
    @Expose
    private String Plate_No;
    @SerializedName("Trx_Date")
    @Expose
    private String Trx_Date;
    @SerializedName("park_name")
    @Expose
    private String park_name;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("PayAmount")
    @Expose
    private String PayAmount;
    @SerializedName("Location_Sign_name")
    @Expose
    private String Location_Sign_name;

    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("gate")
    @Expose
    private String gate;

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getPark_name() {
        return park_name;
    }

    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getPlate_No() {
        return Plate_No;
    }

    public void setPlate_No(String plate_No) {
        Plate_No = plate_No;
    }

    public String getTrx_Date() {
        return Trx_Date;
    }

    public void setTrx_Date(String trx_Date) {
        Trx_Date = trx_Date;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(String payAmount) {
        PayAmount = payAmount;
    }

    public String getLocation_Sign_name() {
        return Location_Sign_name;
    }

    public void setLocation_Sign_name(String location_Sign_name) {
        Location_Sign_name = location_Sign_name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
