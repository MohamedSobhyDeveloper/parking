package com.park.optech.parking.restful;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.park.optech.parking.activity.MainActivity;
import com.park.optech.parking.sharedpref.MySharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ApiMethods {
    static public String sessionKey;
    static public String data;

    public static void loginUser(final Context context,
                                 String action, String id, final String first_name, String last_name, String email, String mobile, String chr1, String chr2, String chr3, String num1, String num2, String num3, String num4, String car_brand , String model_no) {


        Retrofit retrofit = RetrofitSingleton.getInstance(context);
        ApiInterfaces.LoginApi service = retrofit.create(ApiInterfaces.LoginApi.class);

        if (/*NetworkingUtils.isNetworkConnected()*/true) {
            Call<String> call = service.getApiData(action,id,first_name,last_name,email,mobile,chr1,chr2,chr3,num1,num2,num3,num4,car_brand,model_no);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String m=response.body();
                    System.out.println("mbmbmb  "+ m);

                    if (m.equals("{\"status\":\"true\"}")){
                        Toast.makeText(context, "profile updated successfully", Toast.LENGTH_SHORT).show();
                        MySharedPref.saveData(context,"car",first_name);
                        context.startActivity(new Intent(context,MainActivity.class));


                    }else {
                        Toast.makeText(context, "something go wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                    //parseNetworkError(t, context);
                    //loginCallback.loginData(false);
                    Toast.makeText(context, "something go wrong", Toast.LENGTH_SHORT).show();

                }
            });
        } else {

            //Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }
    }


    //invite user
    public static void inviteUser(final Context context,
                                  String action, String id, final String name, String mobile, String national_id, String driver_license, final String start_date, String end_date,String park, String chr1, String chr2, String chr3, String num1, String num2, String num3, String num4) {



        Retrofit retrofit = RetrofitSingleton.getInstance(context);
        ApiInterfaces.inviteapi service = retrofit.create(ApiInterfaces.inviteapi.class);

        if (/*NetworkingUtils.isNetworkConnected()*/true) {
            Call<String> call = service.getApiData(action,id,name,mobile,national_id,driver_license,start_date,end_date,park,chr1,chr2,chr3,num1,num2,num3,num4);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String m=response.body();
                    System.out.println("mbmbmb  "+ m);

                    if (m.equals("{\"status\":\"true\"}")){
                        Toast.makeText(context, "Invitation send successfully", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context,MainActivity.class));


                        //context.startActivity(new Intent(context,MainActivity.class));


                    }else {
                        Toast.makeText(context, "something go wrong", Toast.LENGTH_SHORT).show();
                    }



                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                   // parseNetworkError(t, context);
                    Toast.makeText(context, "something go wrong", Toast.LENGTH_SHORT).show();

                }
            });
        } else {

            //Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }
    }



    //end





}
