package com.park.optech.parking.service;

import com.park.optech.parking.model.clientrepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mohamed on 20/11/2017.
 */

public interface client {

    @GET("{user}")
    Call<List<clientrepo>> UserList(@Path("user") String balance);

}
