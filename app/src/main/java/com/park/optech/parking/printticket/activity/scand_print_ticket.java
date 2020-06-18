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
import com.park.optech.parking.printticket.models.TicketsModel;
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
import java.util.List;


public class scand_print_ticket extends AppCompatActivity {
    Button scanBtn,printBtn,searchBtn;
    ImageButton back;
    Button b;
    List<TicketsModel>SyncList;
    ProgressDialog pd;
    String jsonText;

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
            startActivity(new Intent(scand_print_ticket.this, PrintActivity.class));
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


        SyncData();
        syncTickets();



    }

    private void SyncData() {
        pd.show();
        SyncList=new ArrayList<>();
        List<TicketsModel>ticketsModelList= Database_Helper.getInstance(this).getTickets();
        if (ticketsModelList!=null&&ticketsModelList.size()>0){
            for (int i=0;i<ticketsModelList.size();i++){
                if (ticketsModelList.get(i).getSync().equals("0")){
                    SyncList.add(ticketsModelList.get(i));
                }

            }
        }

//        if (SyncList!=null&&SyncList.size()>0){
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = null;
            for (int i=0;i<5;i++){
                 obj = new JSONObject();

                try {
                    obj.put("cameraNo", "1")
                            .put("Timestamp", "2020-6-15")
                            .put("PayTime", "2020-6-15")
                            .put("PayAmount", "20")
                            .put("PayUser", "1")
                            .put("company", "2")
                            .put("paid", "1")
                            .put("trx_no", "23245484874878")
                            .put("members", "2")
                            .put("sync", "1");

                    jsonArray.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
          


// Encodes the JSONArray as a compact JSON string
             jsonText = jsonArray.toString();
            String jsonTextd = jsonArray.toString();
//        }
    }


    private void syncTickets() {
        pd.show();
        Tickets_Event tickets_event=new Tickets_Event();
        tickets_event.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private class Tickets_Event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = syncData();
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

//                        SyncList.add(data);

//                        Log.e("size >> ", "" + dataList1.size());

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ex"+e);
                }

            }
            else
                Log.e("RESULT", "NULL");

            return null;
        }

        @Override
        protected void onPreExecute() {
            //  adapter1.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result.equals("1")) {
//                Database_Helper database_helper=new Database_Helper(PrintTicketActivity.this);
                for (int i=0;i<SyncList.size();i++){
//                    database_helper.insertUser(usersList.get(i));
                    Database_Helper.getInstance(scand_print_ticket.this).updateTicket(SyncList.get(i).getPk());
//                    Log.e("INser id", "" + Database_Helper.getInstance(scand_print_ticket.this).insertMember(membersList.get(i)));
                }
                Toast.makeText(scand_print_ticket.this, "تم تحميل البيانات  بنجاح", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(scand_print_ticket.this, "حدث خطا أثناء التحميل"+ final_result, Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();

        }
    }


    public String syncData() {
        String SOAP_ACTION = serviceurl.URL2 + "/SyncTickets/";
        String METHOD_NAME = "SyncTickets";
        String NAMESPACE = serviceurl.URL2 + "/";
        String URL = serviceurl.URL2;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tickets", jsonText);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            transport.setTimeout(7000);
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


