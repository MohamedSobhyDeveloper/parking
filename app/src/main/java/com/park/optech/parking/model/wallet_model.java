package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 1/18/2018.
 */

public class wallet_model {
    @SerializedName("PK")
    @Expose
    private String PK;
    @SerializedName("Transaction_Date")
    @Expose
    private String Transaction_Date;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("status")
    @Expose
    private String status;

    public String getPK() {
        return PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    public String getTransaction_Date() {
        return Transaction_Date;
    }

    public void setTransaction_Date(String transaction_Date) {
        Transaction_Date = transaction_Date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
