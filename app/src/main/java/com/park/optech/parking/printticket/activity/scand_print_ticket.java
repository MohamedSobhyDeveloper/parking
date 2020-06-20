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
import com.park.optech.parking.printticket.models.TicketsModel;
import com.park.optech.parking.printticket.sqlite.Database_Helper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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


        searchBtn.setOnClickListener(view -> startActivity(new Intent(scand_print_ticket.this, SearchActivity.class)));


        pd = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        pd.setMessage("جارى تحميل البيانات");
        pd.setCanceledOnTouchOutside(false);


        SyncData();



    }

    private void SyncData() {
        SyncList = new ArrayList<>();
        List<TicketsModel> ticketsModelList = Database_Helper.getInstance(this).getTickets();
        if (ticketsModelList != null && ticketsModelList.size() > 0) {
            for (int i = 0; i < ticketsModelList.size(); i++) {
                if (ticketsModelList.get(i).getSync().equals("0")) {
                    SyncList.add(ticketsModelList.get(i));
                }

            }
        }

        if (SyncList != null && SyncList.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = null;
            for (int i = 0; i < SyncList.size(); i++) {
                obj = new JSONObject();

                try {
                    obj.put("cameraNo", SyncList.get(i).getCameraNo())
                            .put("Timestamp", SyncList.get(i).getTimestamp())
                            .put("PayTime", SyncList.get(i).getPayTime())
                            .put("PayAmount", SyncList.get(i).getPayAmount())
                            .put("PayUser", SyncList.get(i).getPayUser())
                            .put("company", SyncList.get(i).getCompany())
                            .put("paid", SyncList.get(i).getPaid())
                            .put("trx_no", SyncList.get(i).getTrx_no())
                            .put("members", SyncList.get(i).getMembers())
                            .put("sync", "1");

                    jsonArray.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            jsonText = jsonArray.toString();

            syncTickets();


        }
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
//                Toast.makeText(scand_print_ticket.this, "حدث خطا أثناء التحميل"+ final_result, Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public  String syncData() {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost("http://parking.open-park.com/SyncTickets.php");

            httpPost.setEntity(new StringEntity(jsonText,"UTF-8"));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }//end of try
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }//end of catch

        // 11. return result
        return result;
    }//end of POST method
    //-----------------------------
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}


