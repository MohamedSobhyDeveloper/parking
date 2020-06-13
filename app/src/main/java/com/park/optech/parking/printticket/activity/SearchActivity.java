package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.park.optech.parking.R;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.sqlite.Database_Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    Button searchBtn;
    EditText editTextsearch;
    ImageButton backBtn;

    TextView member_name_tv, membership_no_tv, start_date_tv, end_date_tv, validation_tv;
    CardView member_card;
    @BindView(R.id.layout_ticket)
    LinearLayout layoutTicket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchBtn = findViewById(R.id.search_button);
        backBtn = findViewById(R.id.backimage);
        editTextsearch = findViewById(R.id.searcEdt);
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
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(editTextsearch.getText().toString())) {

                    MembersModel membersModel = Database_Helper.getInstance(SearchActivity.this).getmember(editTextsearch.getText().toString());
                    if (membersModel.getMembership_no() != null) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(membersModel.getEnd_date());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate)) {

                            layoutTicket.setBackgroundColor(getResources().getColor(R.color.red));
                            validation_tv.setText("العضوية منتهية");

                        } else {
                            layoutTicket.setBackgroundColor(getResources().getColor(R.color.m2));
                            validation_tv.setText("العضوية سارية");

                        }


                        Log.e("Data", "" + membersModel.getEnd_date());
                        member_name_tv.setText(membersModel.getName());
                        membership_no_tv.setText(membersModel.getMembership_no());
                        start_date_tv.setText(membersModel.getStart_date());
                        end_date_tv.setText(membersModel.getEnd_date());
                        member_card.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SearchActivity.this, "خطا فى رقم العضوية", Toast.LENGTH_SHORT).show();
                        Log.e("Data", "null");
                        member_card.setVisibility(View.GONE);

                    }

                } else {
                    Toast.makeText(SearchActivity.this, "ادخل رقم العضوية", Toast.LENGTH_SHORT).show();
                    member_card.setVisibility(View.GONE);

                }

            }
        });

    }

    private void clicks() {

    }
}
