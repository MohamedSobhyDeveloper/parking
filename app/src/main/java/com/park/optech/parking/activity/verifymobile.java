package com.park.optech.parking.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.model.user_profile;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



public class verifymobile extends AppCompatActivity {
    Button btn1;
    EditText editTextmessage;
    String final_id="",m="";
    private static final String TAG = "VERIFICATION";
    user_profile data = new user_profile();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifymobile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editTextmessage= findViewById(R.id.editmessage);
        btn1=(Button)findViewById(R.id.submit);
        final_id = MySharedPref.getData(verifymobile.this, "id", null);
        System.out.println("mmmmmmmmm"+final_id);

        GetProfile task = new GetProfile();
        task.execute();


        btn1.setOnClickListener(view -> {
            String confirm=editTextmessage.getText().toString();
            if (!TextUtils.isEmpty(confirm)){
                AsyncCallWS task1 =new AsyncCallWS();
                task1.execute();


            } else {

                Snackbar.make(view, "Enter Your Confirm Message", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }


    /**
     * need for Android 6 real time permissions
     */


    public String sendOTP() {
        String SOAP_ACTION = serviceurl.URL+"/Verify_Code";
        String METHOD_NAME = "Verify_Code";
        String NAMESPACE = serviceurl.URL+"/";
        String URL = serviceurl.URL;
        String response="";
        System.out.println(""+SOAP_ACTION);
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("user_id",final_id);
            Request.addProperty("recovery_code",editTextmessage.getText().toString());
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            System.out.println("-------id-------- "+ final_id);
            System.out.println("-------code-------- "+ editTextmessage.getText().toString());

            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
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

    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String result1="";
        String fina_result="";
        @Override
        protected Void doInBackground(Void... voids) {
            result1=sendOTP();
            if(result1!=null){
                fina_result=result1;
                System.out.println("-------------result--------------- "+result1);

            }
            return null;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            if (fina_result.equals("0")||fina_result.equals(0)){
                startActivity(new Intent(verifymobile.this, MainActivity.class));
                MySharedPref.saveData(verifymobile.this,"code","44");
                MySharedPref.saveData(verifymobile.this,"car",data.getFirst_name());
                MySharedPref.saveData(verifymobile.this,"parkname","");
                MySharedPref.saveData(verifymobile.this,"gatename","");
                finish();

                // MySharedPref.saveData(verifymobile.this,"car",data.getLast_name());


            }else {
                Toast.makeText(verifymobile.this, "Enter Correct Code", Toast.LENGTH_SHORT).show();
            }

        }
    }





    //get profile
    private class GetProfile extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("-------------AsyncCallWS--------------- ");
            String result=getProfile_Event();
            if (result != null) {
                final_result=result;
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    for (int i = 0; i <= arr.length()-1; i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println(i+"  "+obj);
                        data.setFirst_name(obj.get("first_name")+"");



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }



            return null;
        }

        @Override
        protected void onPreExecute() {

        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Void result) {


            MySharedPref.saveData(verifymobile.this,"car",data.getFirst_name());

            //circleimageview.setImageResource(Integer.parseInt(data.getPhoto()));



        }
    }

    public String getProfile_Event() {
        String SOAP_ACTION = serviceurl.URL + "/Show_User_Profile/";
        String METHOD_NAME = "Show_User_Profile";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = null;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("user_id", final_id);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- "+ soapEnvelope.getResponse());
            System.out.println("-------id-------- "+ final_id);
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }






}
