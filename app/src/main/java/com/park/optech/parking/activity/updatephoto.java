package com.park.optech.parking.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.park.optech.parking.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;


public class updatephoto extends AppCompatActivity {
    EditText t1;
    Button b1;
    PayPalConfiguration m_configration;
    String paypalclient="AQzwV7AmBbrqS4Esni5obrlo6KJmY2Yvdwr__9JjvJ3S2Eeu9arLOK61A4VuqtiZelyZK4E6qGkD-409";
    Intent m_service;
    int request_code=999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatephoto);
        t1=(EditText)findViewById(R.id.editAmount);
        b1=(Button)findViewById(R.id.btnPaypal);
        m_configration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(paypalclient);

        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configration);
        startService(m_service);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(1), "USD", "Pay For park", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(updatephoto.this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configration); //send the same configuration for restart resiliency
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, request_code);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       super.onActivityResult(requestCode,resultCode,data);
       if (requestCode==request_code){
           if (resultCode==Activity.RESULT_OK){
               PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
               if (confirmation!=null){
                   String state=confirmation.getProofOfPayment().getState();
                   if (state.equals("approved")){
                       t1.setText("success");

                   }else {

                       t1.setText("error");
                   }


               }else {
                   t1.setText("null");


               }




           }

       }
    }

}
