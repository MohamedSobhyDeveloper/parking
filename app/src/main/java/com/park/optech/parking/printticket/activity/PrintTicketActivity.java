package com.park.optech.parking.printticket.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.adapter.inviteadapter;
import com.park.optech.parking.model.user_invitation;
import com.park.optech.parking.printticket.fragment.printticket;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.models.UsersModels;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.park.optech.parking.soapapi.serviceurl;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class PrintTicketActivity extends AppCompatActivity {
    ArrayList<UsersModels> usersList;
    ArrayList<MembersModel> membersList;

    ProgressDialog pd;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ticket);
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

        pd = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        pd.setMessage("جارى تحميل البيانات");
        pd.setCanceledOnTouchOutside(false);

        usersList= new ArrayList<>();
        membersList= new ArrayList<>();

        LoadUsers();


        printticket myTicket = new printticket();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, myTicket);
        fragmentTransaction.commit();
    }

    private void LoadUsers() {
        pd.show();
        Users_Event users_event=new Users_Event();
        users_event.execute();

    }


    private class Users_Event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = getUsersList();
            if (result != null) {
                final_result = result;

                System.out.println("--------------result-------------- " + final_result);
                try {
                    arr = new JSONArray(result);
//                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    UsersModels data = null;
                    for (int i = 0; i <= arr.length() - 1; i++) {

                        data = new UsersModels();
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("------------obj------------- " + obj);
                        data.setEmail(obj.getString("email"));
                        data.setName(obj.getString("name"));
                        data.setPassword(obj.getString("password"));
                        data.setPk(obj.getString("pk"));

                        usersList.add(data);

//                        Log.e("size >> ", "" + dataList1.size());

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ex"+e);
                }

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            //  adapter1.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result!=null) {
//                Database_Helper database_helper=new Database_Helper(PrintTicketActivity.this);

                for (int i=0;i<usersList.size();i++){
//                    database_helper.insertUser(usersList.get(i));
                    Database_Helper.getInstance(PrintTicketActivity.this).insertUser(usersList.get(i));
                }
                Members_Event members_event=new Members_Event();
                members_event.execute();
//                Toast.makeText(PrintTicketActivity.this, "تم تحميل بيانات المستخدمين بنجاح", Toast.LENGTH_SHORT).show();

//                pd.dismiss();
            }else {
//                Toast.makeText(PrintTicketActivity.this, "تاكد من اتصالك بالانترنت سوف تعم", Toast.LENGTH_SHORT).show();
              pd.dismiss();
            }

        }
    }

    public String getUsersList() {
        String SOAP_ACTION = serviceurl.URL2 + "/retrieve_all_users/";
        String METHOD_NAME = "retrieve_all_users";
        String NAMESPACE = serviceurl.URL2 + "/";
        String URL = serviceurl.URL2;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("company", "2");
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
//            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }



    @SuppressLint("StaticFieldLeak")
    private class Members_Event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = getMembdersList();

            if (result != null) {
                final_result = result;

                System.out.println("--------------result-------------- " + final_result);
                try {
                    arr = new JSONArray(result);
//                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    MembersModel data = null;
                    for (int i = 0; i <= arr.length() - 1; i++) {

                        data = new MembersModel();
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("------------obj------------- " + obj);
                        data.setPk(obj.getString("pk"));
                        data.setName(obj.getString("name"));
                        data.setSsn(obj.getString("ssn"));
                        data.setStart_date(obj.getString("start_date"));
                        data.setEnd_date(obj.getString("end_date"));
                        data.setImg_path(obj.getString("img_path"));
                        data.setMembership_no(obj.getString("membership_no"));

                        membersList.add(data);

//                        Log.e("size >> ", "" + dataList1.size());

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ex"+e);
                }

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            //  adapter1.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result!=null) {
//                Database_Helper database_helper=new Database_Helper(PrintTicketActivity.this);
                for (int i=0;i<membersList.size();i++){
//                    database_helper.insertUser(usersList.get(i));
                    Database_Helper.getInstance(PrintTicketActivity.this).insertMember(membersList.get(i));
                    Log.e("INser id", "" + Database_Helper.getInstance(PrintTicketActivity.this).insertMember(membersList.get(i)));
                }
                Toast.makeText(PrintTicketActivity.this, "تم تحميل البيانات  بنجاح", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }else {
//                Toast.makeText(PrintTicketActivity.this, "تاكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

        }
    }


    public String getMembdersList() {
        String SOAP_ACTION = serviceurl.URL2 + "/retrieve_all_members/";
        String METHOD_NAME = "retrieve_all_members";
        String NAMESPACE = serviceurl.URL2 + "/";
        String URL = serviceurl.URL2;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("company", "2");
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
//            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }




}
