package com.park.optech.parking.printticket.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.park.optech.parking.R;

public class SearchActivity extends AppCompatActivity {
    Button searchBtn;
    EditText editTextsearch;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBtn=findViewById(R.id.search_button);
        backBtn=findViewById(R.id.backimage);
        editTextsearch=findViewById(R.id.searcEdt);

        clicks();

    }

    private void clicks() {
        backBtn.setOnClickListener(view -> finish());
    }
}
