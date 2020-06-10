package com.park.optech.parking.activity;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.model.scan_model;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ticketactivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private static final int REQUEST_GET_ACCOUNT = 112;
    private static final int PERMISSION_REQUEST_CODE = 200;    //camera permission is needed.
    TextView msg;
    String final_id = null, final_code = null, final_status = null;
    TextView t1,t2,t3,t4,t5,t6;
    scan_model data=new scan_model();
    RelativeLayout ticket;
    ImageButton backimage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketactivity);
        final_id = MySharedPref.getData(ticketactivity.this, "id", null);


        mScannerView = (ZBarScannerView) findViewById(R.id.scannerview);
        ticket=(RelativeLayout)findViewById(R.id.ticket_popup);

        // Programmatically initialize the scanner view
        Button scan_btn = (Button) findViewById(R.id.scanbtn);
        Button cancel=(Button)findViewById(R.id.cancel_btn);
        t1=(TextView)findViewById(R.id.parkname);
        t2=(TextView)findViewById(R.id.username);
        t3=(TextView)findViewById(R.id.carnumber);
        t4=(TextView)findViewById(R.id.brand);
        t5=(TextView)findViewById(R.id.model);
        t6=(TextView)findViewById(R.id.date);
        backimage=(ImageButton)findViewById(R.id.backimage);

        mScannerView.setResultHandler(ticketactivity.this);
        mScannerView.startCamera();


cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        ticket.setVisibility(View.GONE);
        onBackPressed();
        MySharedPref.saveData(ticketactivity.this,"car","mb");
        startActivity(new Intent(ticketactivity.this, MainActivity.class));





    }
});
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                startActivity(new Intent(ticketactivity.this,MainActivity.class));

            }
        });

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScannerView.setResultHandler(ticketactivity.this);
                mScannerView.startCamera();
            }
        });




    }

    @Override
    public void handleResult(Result result) {
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName());
        final_code = result.getContents();
        scan_event task=new scan_event();
        task.execute();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //start web service
    //soap service
    private class scan_event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;
        String m;

        @Override
        protected Void doInBackground(Void... voids) {
//            System.out.println("-------------AsyncCallWS--------------- ");
            String result = scan_ticket();
            if (result != null) {
                final_result = result;
                System.out.println("--------------result-------------- " + result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());

                    for (int i = 0; i <= arr.length()-1; i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("--------" + i + "-------- " + obj);
                        data.setEvent_ID(obj.get("Event_ID")+"");
                        data.setPlate_No(obj.get("Plate_No")+"");
                        data.setTrx_Date(obj.get("Trx_Date")+"");
                        data.setPark_name(obj.get("parking_name")+"");
                        data.setUser(obj.get("user")+"");
                        data.setBrand(obj.get("brand")+"");
                        data.setModel(obj.get("model")+"");
                        data.setGate(obj.get("gate")+"");







                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("--------" + e);


                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result.equals(2)||final_result.equals("2")) {
                Toast.makeText(ticketactivity.this, "This Is Exit Gate", Toast.LENGTH_SHORT).show();
            } else {

                if (final_result.equals(-1) || final_result.equals("-1") || final_result.equals("") || final_result.equals("[[]]") || final_result.equals(null) || final_result.equals("null")) {
                    Toast.makeText(ticketactivity.this, "Your Balance Not Enough", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(ticketactivity.this, "success", Toast.LENGTH_SHORT).show();
                    System.out.println("mm " + data.getPark_name());
                    ticket.setVisibility(View.VISIBLE);
                    t1.setText(data.getPark_name());
                    t2.setText(data.getUser());
                    t3.setText(data.getPlate_No());
                    t4.setText(data.getBrand());
                    t5.setText(data.getModel());
                    t6.setText(data.getTrx_Date());
                    MySharedPref.saveData(ticketactivity.this,"parkname",data.getPark_name());
                    MySharedPref.saveData(ticketactivity.this,"gatename",data.getGate());

                }


            }
        }

        }

    public String scan_ticket() {
        String SOAP_ACTION = serviceurl.URL + "/Entry_Event/";
        String METHOD_NAME = "Entry_Event";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("id", final_id);
            Request.addProperty("scanned_parking_sign_id", final_code);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- " + soapEnvelope.getResponse());
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }



}
