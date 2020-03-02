package com.park.optech.parking.restful;



import com.park.optech.parking.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {


    private static Retrofit mInstance;
    private static OkHttpClient.Builder okHttpClientBuilder;
    private static HttpLoggingInterceptor loggingInterceptor;


    public static synchronized Retrofit getInstance() {
        if (mInstance == null) {


            okHttpClientBuilder = new OkHttpClient.Builder();  /// I must use OkHttpClient.Builder to add the log interceptor to the request
            loggingInterceptor = new HttpLoggingInterceptor(); /// I must use HttpLoggingInterceptor to could identify log configuration
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); /// add the log level body, header or ... etc

            if(BuildConfig.DEBUG){  // only enable log in depug mode to still secure my requests like password ..
                okHttpClientBuilder.addInterceptor(loggingInterceptor);
            }

            mInstance = new Retrofit.Builder()
                    .baseUrl(ApiUrls.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }
        return mInstance;
    }
}
