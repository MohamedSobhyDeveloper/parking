package com.park.optech.parking;

import android.app.Application;
import android.content.Context;

    public class myApplication extends Application {

        private static Context context;

        public void onCreate() {
            super.onCreate();
            myApplication.context = getApplicationContext();
        }

        public static Context getAppContext() {
            return myApplication.context;
        }
    }

