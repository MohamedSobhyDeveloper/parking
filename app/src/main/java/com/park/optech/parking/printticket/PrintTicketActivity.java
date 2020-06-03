package com.park.optech.parking.printticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.park.optech.parking.R;
import com.park.optech.parking.fragment.MyTicket;
import com.park.optech.parking.fragment.printticket;
import com.park.optech.parking.fragment.wallet;

public class PrintTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ticket);

        printticket myTicket = new printticket();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, myTicket);
        fragmentTransaction.commit();
    }
}
