package com.park.optech.parking.restful;


import com.park.optech.parking.activity.exit_scan;
import com.park.optech.parking.myApplication;
import com.park.optech.parking.service.MyApplication;
import com.park.optech.parking.sharedpref.MySharedPref;

public class ApiUrls {

    public static final String BASE_URL = MySharedPref.getData(myApplication.getAppContext(), "restful_api_url", "");
    public static final String API_URL = "api.php?";

}
