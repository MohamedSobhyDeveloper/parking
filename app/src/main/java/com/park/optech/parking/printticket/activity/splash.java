package com.park.optech.parking.printticket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.park.optech.parking.R;
import com.park.optech.parking.model.callFirebaseModel;
import com.park.optech.parking.sharedpref.MySharedPref;

public class splash extends AppCompatActivity {
    String login ;
    protected Context context;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    String file_upload_url="";
    String restful_api_url="";
    String printer_soap_api_url="";
    String base_soap_api_url="";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
//        login = MySharedPref.getData(context, "code", null);
//
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
////        callFirebaseModel callFirebaseModel = new callFirebaseModel("1", "2","3","4");
//
////        mDatabase.child("parkingApi").child("openpark").setValue(callFirebaseModel);
//
//        mDatabase.child("parkingApi").child("openpark").addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user value
//                        callFirebaseModel callFirebaseModel = dataSnapshot.getValue(callFirebaseModel.class);
//
//                        // [START_EXCLUDE]
//                        if (callFirebaseModel != null) {
//                            file_upload_url = callFirebaseModel.file_upload_url;
//                           restful_api_url = callFirebaseModel.restful_api_url;
//                           printer_soap_api_url = callFirebaseModel.printer_soap_api_url;
//                            base_soap_api_url = callFirebaseModel.base_soap_api_url;
//                        MySharedPref.saveData(splash.this,"file_upload_url",file_upload_url);
//                        MySharedPref.saveData(splash.this,"restful_api_url",restful_api_url);
//                        MySharedPref.saveData(splash.this,"printer_soap_api_url",printer_soap_api_url);
//                        MySharedPref.saveData(splash.this,"base_soap_api_url",base_soap_api_url);
//
//
//
//                        }
//
//                        // [END_EXCLUDE]
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // [START_EXCLUDE]
//                        // [END_EXCLUDE]
//                    }
//                });

        new Handler().postDelayed(() -> {
//                    if (login == null) {
//                        System.out.println("------------NEW Login-----------------");
//                        Intent intent = new Intent(splash.this, mobile.class);
//                        startActivity(intent);
//                        finish();
//
//                    } else {
            System.out.println("------------OLD Login-----------------");
            Intent intent = new Intent(splash.this, PrintTicketActivity.class);
            startActivity(intent);
            finish();
//                                   }

        },500);



    }


}
