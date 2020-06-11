package com.park.optech.parking.soapapi;

import com.park.optech.parking.myApplication;
import com.park.optech.parking.sharedpref.MySharedPref;

/**
 * Created by mohamed on 22/11/2017.
 */

public class serviceurl {
    public final static String URL = MySharedPref.getData(myApplication.getAppContext(), "base_soap_api_url", "");

    public final static String URL2= "http://www.parking.open-park.com/soap_server.php";


}
