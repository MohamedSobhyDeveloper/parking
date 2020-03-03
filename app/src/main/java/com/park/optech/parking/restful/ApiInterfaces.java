package com.park.optech.parking.restful;

import com.park.optech.parking.restful.ApiUrls;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mohamed on 12/31/2017.
 */

public class ApiInterfaces {

    public interface LoginApi {

        @POST("api.php?")
        Call<String> getApiData(@Query("action") String action, @Query("id") String id, @Query("first_name") String first_name, @Query("last_name") String last_name, @Query("email") String email, @Query("mobile") String mobile, @Query("chr1") String chr1, @Query("chr2") String chr2, @Query("chr3") String chr3, @Query("num1") String num1, @Query("num2") String num2, @Query("num3") String num3, @Query("num4") String num4,@Query("car_brand") String car_brand,@Query("model_no") String model_no);
    }

    public interface inviteapi {

        @POST("api.php?")
        Call<String> getApiData(@Query("action") String action, @Query("id") String id, @Query("name") String name, @Query("mobile") String mobile, @Query("national_id") String national_id, @Query("driver_license") String driver_license,@Query("start_date") String start_date,@Query("end_date") String end_date,@Query("park") String park, @Query("chr1") String chr1, @Query("chr2") String chr2, @Query("chr3") String chr3, @Query("num1") String num1, @Query("num2") String num2, @Query("num3") String num3, @Query("num4") String num4);
    }



}
