package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 1/11/17.
 */

public class User_history {
    @SerializedName("PK")
    @Expose
    private String PK;
    @SerializedName("Transaction_Date")
    @Expose
    private String transactionDate;
    @SerializedName("Entry_TIME")
    @Expose
    private String entryTIME;
    @SerializedName("Exit_TIME")
    @Expose
    private String exitTIME;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("Fees_Collected")
    @Expose
    private String feesCollected;
    @SerializedName("Parking_name")
    @Expose
    private String parkingName;
    @SerializedName("gate")
    @Expose
    private String gate;

    public String getPK() {
        return PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getEntryTIME() {
        return entryTIME;
    }

    public void setEntryTIME(String entryTIME) {
        this.entryTIME = entryTIME;
    }

    public String getExitTIME() {
        return exitTIME;
    }

    public void setExitTIME(String exitTIME) {
        this.exitTIME = exitTIME;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFeesCollected() {
        return feesCollected;
    }

    public void setFeesCollected(String feesCollected) {
        this.feesCollected = feesCollected;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }
}
