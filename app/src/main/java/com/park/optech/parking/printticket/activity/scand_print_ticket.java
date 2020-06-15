package com.park.optech.parking.printticket.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.park.optech.parking.R;
import com.park.optech.parking.printticket.models.TicketsModel;
import com.park.optech.parking.printticket.sqlite.Database_Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class scand_print_ticket extends AppCompatActivity {
    Button scanBtn,printBtn,searchBtn;
    ImageButton back;
    Button b;
    List<TicketsModel>SyncList;
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


        SyncData();




    }

    private void SyncData() {
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
            String jsonText = jsonArray.toString();
            String jsonTextd = jsonArray.toString();
//        }
    }


    @Override

    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(scand_print_ticket.this,MainActivity.class));
        finish();
    }
}


