package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 1/11/17.
 */

public class Ticket_Model {

    @SerializedName("PK")
    @Expose
    private String PK;
    @SerializedName("Day")
    @Expose
    private String Day;
    @SerializedName("Month")
    @Expose
    private String Month;
    @SerializedName("Year")
    @Expose
    private String Year;
    @SerializedName("Entry_TIME")
    @Expose
    private String EntryTIME;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("PayAmount")
    @Expose
    private String PayAmount;
    @SerializedName("gate")
    @Expose
    private String gate;

    public String getPK() {
        return PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }



    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(String payAmount) {
        PayAmount = payAmount;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getEntryTIME() {
        return EntryTIME;
    }

    public void setEntryTIME(String entryTIME) {
        EntryTIME = entryTIME;
    }
}
