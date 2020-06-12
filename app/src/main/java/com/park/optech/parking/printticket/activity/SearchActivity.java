package com.park.optech.parking.printticket.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.park.optech.parking.printticket.sqlite.Members_Table;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Button searchBtn;
    EditText editTextsearch;
    ImageButton backBtn;

    TextView member_name_tv,membership_no_tv,start_date_tv,end_date_tv,validation_tv;
    CardView member_card;
    Database_Helper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBtn=findViewById(R.id.search_button);
        backBtn=findViewById(R.id.backimage);
        editTextsearch=findViewById(R.id.searcEdt);
        member_name_tv = findViewById(R.id.mem_name);
        membership_no_tv = findViewById(R.id.member_id);
        start_date_tv = findViewById(R.id.start_date);
        end_date_tv = findViewById(R.id.end_date);
        validation_tv = findViewById(R.id.validation);
        member_card = findViewById(R.id.member_card);

        member_card.setVisibility(View.GONE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MembersModel membersModel = Database_Helper.getInstance(SearchActivity.this).getmember(editTextsearch.getText().toString());
                List<Members_Table> membersModels = Database_Helper.getInstance(SearchActivity.this).getMembers();
                Log.e("Data size",""+membersModels.size());
                if (membersModel != null)
                {
                    Log.e("Data",""+membersModel.getEnd_date());
                    member_name_tv.setText(membersModel.getName());
                    membership_no_tv.setText(membersModel.getMembership_no());
                    start_date_tv.setText(membersModel.getStart_date());
                    end_date_tv.setText(membersModel.getEnd_date());
                    member_card.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(SearchActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    Log.e("Data","null");
                }

            }
        });

    }

    private void clicks() {

    }
}
