package com.park.optech.parking;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.park.optech.parking.sharedpref.MySharedPref;

public class splash extends AppCompatActivity {
    String login ;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        login = MySharedPref.getData(context, "code", null);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (login == null) {
                    System.out.println("------------NEW Login-----------------");
                    Intent intent = new Intent(splash.this, mobile.class);
                    startActivity(intent);
                    finish();

                } else {
                    System.out.println("------------OLD Login-----------------");
                    Intent intent = new Intent(splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2500);
    }


}
