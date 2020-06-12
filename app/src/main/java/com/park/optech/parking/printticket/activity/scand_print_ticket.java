package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.models.UsersModels;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.park.optech.parking.soapapi.serviceurl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class scand_print_ticket extends AppCompatActivity {
    Button scanBtn,printBtn,searchBtn;
    ImageButton back;
    Button b;
    ArrayList<MembersModel> membersList;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scand_print_ticket);
        scanBtn=findViewById(R.id.scan);
        printBtn=findViewById(R.id.print);
        searchBtn=findViewById(R.id.searhBtn);

        back=findViewById(R.id.backimage);
        b=findViewById(R.id.back);


        b.setOnClickListener(view -> finish());



        back.setOnClickListener(view -> {
//                startActivity(new Intent(scand_print_ticket.this,MainActivity.class));
            finish();
        });

        scanBtn.setOnClickListener(view -> {
            startActivity(new Intent(scand_print_ticket.this, ticket_scan.class));
            finish();
        });
        printBtn.setOnClickListener(view -> {
            startActivity(new Intent(scand_print_ticket.this, ticket_print.class));
            finish();
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(scand_print_ticket.this, SearchActivity.class));

            }
        });


        pd = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        pd.setMessage("جارى تحميل البيانات");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        membersList= new ArrayList<>();

        Members_Event members_event=new Members_Event();
        members_event.execute();

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
                    Database_Helper.getInstance(scand_print_ticket.this).insertMember(membersList.get(i));
                }
                Toast.makeText(scand_print_ticket.this, "تم تحميل بيانات الاعضاء بنجاح", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }else {
                Toast.makeText(scand_print_ticket.this, "تاكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
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



    @Override

    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(scand_print_ticket.this,MainActivity.class));
        finish();
    }
}


