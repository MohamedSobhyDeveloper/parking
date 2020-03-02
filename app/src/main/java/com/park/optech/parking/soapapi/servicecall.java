package com.park.optech.parking.soapapi;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by mohamed on 22/11/2017.
 */

public class servicecall {
    private static final String TAG = servicecall.class.getSimpleName();

    public static String callWSThreadSoapPrimitive(String strURL, String strSoapAction, SoapObject request) {

        try {
            StringBuffer result;
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE ht = new HttpTransportSE(strURL);
            ht.debug = true;
            ht.call(strSoapAction, envelope);
            int response = (int) envelope.getResponse();

            result = new StringBuffer(String.valueOf(response));
            Log.i(TAG, "result: " + result.toString());
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



}
