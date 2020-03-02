package com.park.optech.parking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohamed on 25/03/2018.
 */

public class user_park {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("pk")
    @Expose
    private String pk;

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
