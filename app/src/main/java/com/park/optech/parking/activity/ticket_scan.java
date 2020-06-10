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
import com.park.optech.parking.model.Ticket_Model;
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

public class ticket_scan extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    ImageButton back;
    String ticket_id = null, final_id = null;
    Ticket_Model data=new Ticket_Model();
    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    TextView t11,t22,t33,t44,t55,t66;
    RelativeLayout ticket,view,ticketred,ticket_popup;
    Button scan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_scan);

        mScannerView = (ZBarScannerView) findViewById(R.id.scannerview);
        back = (ImageButton) findViewById(R.id.backimage);
        t1=(TextView)findViewById(R.id.date);
        t2=(TextView)findViewById(R.id.time);
        t3=(TextView)findViewById(R.id.gatenumber);
        t4=(TextView)findViewById(R.id.cartype);
        t5=(TextView)findViewById(R.id.ticketprice);
        t6=(TextView)findViewById(R.id.status);
        t11=(TextView)findViewById(R.id.date1);
        t22=(TextView)findViewById(R.id.time1);
        t33=(TextView)findViewById(R.id.gatenumber1);
        t44=(TextView)findViewById(R.id.cartype1);
        t55=(TextView)findViewById(R.id.ticketprice1);
        t66=(TextView)findViewById(R.id.status1);
        Button ex=(Button)findViewById(R.id.exit);
        Button scant_ticket=(Button)findViewById(R.id.scanticket);
        Button pay_ticket=(Button)findViewById(R.id.payticket);
        ticket=(RelativeLayout)findViewById(R.id.ticket_popupgreen);
        ticketred=(RelativeLayout)findViewById(R.id.ticket_popupred);
        scan=(Button)findViewById(R.id.scanbtn);
        // view=(RelativeLayout)findViewById(R.id.view_popup) ;




        mScannerView.setResultHandler(ticket_scan.this);
        mScannerView.startCamera();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScannerView.setResultHandler(ticket_scan.this);
                mScannerView.startCamera();
            }
        });




        scant_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ticketred.setVisibility(View.GONE);
                // view.setVisibility(View.VISIBLE);

            }
        });

        pay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  pay_event task=new pay_event();
               // task.execute();

                Toast.makeText(ticket_scan.this, "Ticket Paid Successfully", Toast.LENGTH_SHORT).show();


            }
        });



        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticket.setVisibility(View.GONE);
            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ticket_scan.this, scand_print_ticket.class));
                finish();
            }
        });

    }


    @Override
    public void handleResult(Result result) {
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName());
        ticket_id = result.getContents();
        scan_event task=new scan_event();
        task.execute();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }





    //scan ticket
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
                    //arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());

                    for (int i = 0; i <= arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("--------" + i + "-------- " + obj.get("Entry_Time"));


                        data.setPK(obj.get("PK")+"");
                        data.setGate(obj.get("gate")+"");
                        data.setName(obj.get("name")+"");
                        data.setPayAmount(obj.get("PayAmount")+"");
                        data.setPaid(obj.get("paid")+"");
                        data.setDay(obj.get("Day")+"");
                        data.setMonth(obj.get("Month")+"");
                        data.setYear(obj.get("Year")+"");
                        data.setEntryTIME(obj.get("Entry_Time")+"");


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

            if (final_result.equals("")||final_result.equals("[]")||final_result.equals(null)){
                Toast.makeText(ticket_scan.this, "Wrong Ticket", Toast.LENGTH_LONG).show();

            }else {

                if (data.getPaid().equals("1")){
                    // view.setVisibility(View.GONE);
                    ticket.setVisibility(View.VISIBLE);
                    t1.setText(data.getDay()+"/"+data.getMonth()+"/"+ data.getYear());
                    t2.setText(data.getEntryTIME());
                    t3.setText(data.getGate());
                    t4.setText(data.getName());
                    t5.setText(data.getPayAmount()+"جنية");
                    t6.setText(data.getPaid());


                }else if(data.getPaid().equals("0")) {
                    //   view.setVisibility(View.GONE);
                    ticketred.setVisibility(View.VISIBLE);
                    t11.setText(data.getDay()+"/"+data.getMonth()+"/"+ data.getYear());
                    t22.setText(data.getEntryTIME());
                    t33.setText(data.getGate());
                    t44.setText(data.getName());
                    t55.setText(data.getPayAmount()+"جنية");
                    t66.setText(data.getPaid());


                }


            }

        }
    }

    public String scan_ticket() {
        String SOAP_ACTION = serviceurl.URL2 + "/scan_ticket/";
        String METHOD_NAME = "scan_ticket";
        String NAMESPACE = serviceurl.URL2 + "/";
        String URL = serviceurl.URL2;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("ticket_id", ticket_id);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ticket_scan.this, scand_print_ticket.class));
        finish();
    }

    //end


}
