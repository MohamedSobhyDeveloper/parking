package com.park.optech.parking.printticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.fragment.MyTicket;
import com.park.optech.parking.fragment.printticket;
import com.park.optech.parking.fragment.wallet;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class PrintTicketActivity extends AppCompatActivity {

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

        printticket myTicket = new printticket();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, myTicket);
        fragmentTransaction.commit();
    }
}
