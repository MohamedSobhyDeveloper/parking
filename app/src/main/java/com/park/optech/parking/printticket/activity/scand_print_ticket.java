package com.park.optech.parking.printticket.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.park.optech.parking.R;


public class scand_print_ticket extends AppCompatActivity {
    Button scanBtn,printBtn,searchBtn;
    ImageButton back;
    Button b;
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




    }






    @Override

    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(scand_print_ticket.this,MainActivity.class));
        finish();
    }
}


