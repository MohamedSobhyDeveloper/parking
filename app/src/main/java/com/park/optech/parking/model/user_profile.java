package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 02/01/2018.
 */

public class user_profile {

    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("time-zone")
    @Expose
    private String timezone;

    @SerializedName("character1")
    @Expose
    private String character1;
    @SerializedName("character2")
    @Expose
    private String character2;
    @SerializedName("character3")
    @Expose
    private String character3;

    @SerializedName("number1")
    @Expose
    private String number1;
    @SerializedName("number2")
    @Expose
    private String number2;
    @SerializedName("number3")
    @Expose
    private String number3;
    @SerializedName("number4")
    @Expose
    private String number4;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCharacter1() {
        return character1;
    }

    public void setCharacter1(String character1) {
        this.character1 = character1;
    }

    public String getCharacter2() {
        return character2;
    }

    public void setCharacter2(String character2) {
        this.character2 = character2;
    }

    public String getCharacter3() {
        return character3;
    }

    public void setCharacter3(String character3) {
        this.character3 = character3;
    }

    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public String getNumber3() {
        return number3;
    }

    public void setNumber3(String number3) {
        this.number3 = number3;
    }

    public String getNumber4() {
        return number4;
    }

    public void setNumber4(String number4) {
        this.number4 = number4;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
