package com.park.optech.parking.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.park.optech.parking.R;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class mobile extends AppCompatActivity {
    Button btn1,btn2;
    EditText editTextphone,editTextcode;
    private LocationListener locationListener;
    private LocationManager locationManager;
    String p="";
    View view;
    String mob;
    private static final String TAG = "VERIFICATION";
    String result="";
    Context context;
    EditText  editTextGetCarrierNumber;
    CountryCodePicker ccpGetNumber;
    TextView tvValidity;
    ImageView imgValidity;
    //

    //

    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] Permission = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,

    };

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        editTextphone=(EditText)findViewById(R.id.editText_getCarrierNumber);
        btn1=(Button)findViewById(R.id.submit);


          context=this;

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);

        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA
                )
                .subscribe(granted -> {

                    if (granted) { // Always true pre-M
                        // I can control the camera now
                        Log.e("m", "permission");
                    } else {
                        Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
                        finish();
                        // Oups permission denied
                    }
                });






        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AsyncCallWS task=new AsyncCallWS();
                    task.execute();






            }
        });


        assignView();
        registerCarrierEditText();


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    //verify mobile web service
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        String final_result="";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
             String result=verify();
             if (result!=null){
                 final_result=result;

                 System.out.println("id ="+result);
                 System.out.println(editTextphone.getText().toString());
                 MySharedPref.saveData(mobile.this,"mobile",editTextphone.getText().toString());

             }
            //Toast.makeText(verifymobile.this, result, Toast.LENGTH_SHORT).show();
            return null;
        }
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");


        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            if (final_result.equals("")||final_result.equals("null")||final_result.equals(null)) {

                Toast.makeText(mobile.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(mobile.this, verifymobile.class));
                MySharedPref.saveData(mobile.this, "id", final_result);
                finish();

            }


        }
    }
    public String verify() {
        String SOAP_ACTION = serviceurl.URL+"/Verify_Mobile";
        String METHOD_NAME = "Verify_Mobile";
        String NAMESPACE = serviceurl.URL+"/";
        String URL = serviceurl.URL;
        String response="";
        System.out.println("Calll"+SOAP_ACTION);
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("mobile",MySharedPref.getData(mobile.this, "cmobile", null));
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL,50000);
            transport.debug=true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- "+ soapEnvelope.getResponse());

            response=soapEnvelope.getResponse().toString();
            Log.i(TAG, "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }




    private void registerCarrierEditText() {
        ccpGetNumber.registerCarrierNumberEditText(editTextGetCarrierNumber);
        ccpGetNumber.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    imgValidity.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_turned_in_black_24dp));
                    tvValidity.setText("Valid Number");
                    btn1.setVisibility(View.VISIBLE);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editTextGetCarrierNumber.getWindowToken(), 0);
                    if (ccpGetNumber.getSelectedCountryCode().endsWith("0")&&editTextGetCarrierNumber.getText().toString().startsWith("0")){
                         MySharedPref.saveData(mobile.this,"cmobile",ccpGetNumber.getSelectedCountryCode()+editTextGetCarrierNumber.getText().delete(0 , 1));


                    }else {
                        MySharedPref.saveData(mobile.this,"cmobile",ccpGetNumber.getSelectedCountryCode()+editTextGetCarrierNumber.getText().toString());

                    }
                } else {
                    imgValidity.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_late_black_24dp));
                    tvValidity.setText("Invalid Number");
                    btn1.setVisibility(View.GONE);


                }
            }
        });

        // ccpLoadNumber.registerCarrierNumberEditText(editTextLoadCarrierNumber);
    }

    private void assignView() {
        //load number

        //get number
        editTextGetCarrierNumber = (EditText)findViewById(R.id.editText_getCarrierNumber);

        ccpGetNumber = (CountryCodePicker)findViewById(R.id.ccp_getFullNumber);
        tvValidity = (TextView)findViewById(R.id.tv_validity);
        imgValidity = (ImageView)findViewById(R.id.img_validity);


    }




}
