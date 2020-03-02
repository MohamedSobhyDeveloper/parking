package com.park.optech.parking.another;

/**
 * Created by Mohamed on 05/02/2018.
 */

public class Constants {

    public static Constants constants=null;

    public static Constants shared(){
        if (constants==null){
            constants = new Constants();
        }
        return constants;
    }

    public static String endpoint;

    public static String getEndpoint()
    {
        return endpoint;
    }

    public static void setEndpoint(String endpoint) {
        Constants.endpoint = endpoint;
    }


}
