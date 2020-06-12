package com.park.optech.parking.printticket.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public  class UsersModels {

    @Expose
    @SerializedName("password")
    private String password;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("pk")
    private String pk;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = "123456";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }



}
