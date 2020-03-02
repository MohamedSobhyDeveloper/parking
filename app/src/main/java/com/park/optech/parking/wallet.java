package com.park.optech.parking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.park.optech.parking.adapter.walletadapter;
import com.park.optech.parking.model.User_history;
import com.park.optech.parking.model.scan_model;
import com.park.optech.parking.model.wallet_model;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;
import com.park.optech.parking.util.IabHelper;
import com.park.optech.parking.util.IabResult;
import com.park.optech.parking.util.Inventory;
import com.park.optech.parking.util.Purchase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static com.park.optech.parking.service.MyApplication.TAG;


public class wallet extends Fragment {
        Spinner spinner,spinner2;
        private static final String[] method = {"Google Play","PayPal"};
    private static final String[] method2 = {"PC 25","","PC 50","","PC 75","","PC 100"};

    TextView t1;
        scan_model data=new scan_model();
        String final_id="";
        Context context;
        Button b2,b3,chargebtn;
        RelativeLayout show,show2;
        EditText from,to,amount1;
        ArrayList<wallet_model> dataList1= new ArrayList<>();
        walletadapter adapter1;
    ListView mDrawerList1;


    IabHelper mHelper;

    public static final int PAYPAL_REQUEST_CODE=7171;

    String selectedItem="";
    String selectedItem2="";


    private static final String LOG_TAG = "iabv3";
    ImageButton back;

    // PRODUCT & SUBSCRIPTION IDS

    private static final String LICENSE_KEY =null;
    // PUT YOUR MERCHANT KEY HERE;
    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    private static final String MERCHANT_ID="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzD0yFaHIK+GElZ5eppQb0CR5YvvoPESqdJezU6YN3nWeYzCQ8WnE5P8L0LZnvGEYrYJM0pWPsBHqH0NPrW/xs/zcVmASeTiY49BalrhSeKTJJ6g7qmMWy6E4pVcRaur03uy3f3TbMhNachvMMACwuYRIcKpu9TMSjGSL+hCUNs2SctCArQVYyuMDaoqXJ8y5s6FWiIuHlSUtnptbd8RXdDXCSYRIkWcwYs53iHdYn5VfUWsiETTlA6nJym84giHGBxVaGH/uqxYh1uTVctRC3ehADhPNVDVYTeMvVRY5U/nTjKcpeepVYt0rZe+Ddw+43mNc8UryT8cbieAreUagvwIDAQAB";

    private BillingProcessor bp;
    private boolean readyToPurchase = false;



    public wallet() {
        // Required empty public constructor
    }


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);
        context=getActivity();
        adapter1 = new walletadapter(context, dataList1);

        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner2=(Spinner)view.findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, method);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, method2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);



        final_id = MySharedPref.getData(context, "id", null);
        b2=(Button)view.findViewById(R.id.charge);
        b3=(Button)view.findViewById(R.id.exit);
        back=(ImageButton)view.findViewById(R.id.backimage);
        t1=(TextView)view.findViewById(R.id.balance);
        from=(EditText)view.findViewById(R.id.from_date);
        to=(EditText)view.findViewById(R.id.to_date);
        chargebtn=(Button)view.findViewById(R.id.charge);
        amount1=(EditText)view.findViewById(R.id.amount);
        show=(RelativeLayout)view.findViewById(R.id.ticket_popup);
        show2=(RelativeLayout)view.findViewById(R.id.ticket_popup2);
        mDrawerList1 = (ListView) view.findViewById(R.id.listview1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });


        //charge wallet
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = adapterView.getItemAtPosition(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem2 = adapterView.getItemAtPosition(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        chargebtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                if (selectedItem2.equals("")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Choose amount", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    //Toast.makeText(getActivity(), "Choose parking Coins", Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Choose Payment Method", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    // Toast.makeText(getActivity(), "Choose Payment Method", Toast.LENGTH_SHORT).show();

                }else if (selectedItem.equals("PayPal")){
                    // Toast.makeText(getActivity(), "PayPal Not Valid Yet", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(view, "PayPal Not Valid Yet", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }else if (selectedItem.equals("")&& selectedItem2.equals("")){
                    Toast.makeText(getActivity(), "Choose Payment Method And Coins", Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("Google Play")&& selectedItem2.equals("PC 25")){
                    if (!readyToPurchase) {
                        //showToast("Billing not initialized.");
                        Snackbar snackbar = Snackbar
                                .make(view, "Billing not initialized. Try again", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        return;
                    }
                    bp.purchase(getActivity(),"openparktech");


                }else if (selectedItem.equals("Google Play")&& selectedItem2.equals("PC 50")){
                    if (!readyToPurchase) {
                        //showToast("Billing not initialized.");
                        Snackbar snackbar = Snackbar
                                .make(view, "Billing not initialized.Try again", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        return;
                    }
                    bp.purchase(getActivity(),"openparktech2");



                }else if (selectedItem.equals("Google Play")&& selectedItem2.equals("PC 75")){
                    if (!readyToPurchase) {
                        showToast("Billing not initialized.Try again");
                        return;
                    }
                    bp.purchase(getActivity(),"openparktech3");


                }else if (selectedItem.equals("Google Play")&& selectedItem2.equals("PC 100")){
                    if (!readyToPurchase) {
                        // showToast("Billing not initialized.");
                        Snackbar snackbar = Snackbar
                                .make(view, "Billing not initialized.Try again", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        return;
                    }
                    bp.purchase(getActivity(),"openparktech4");


                }



        }
        });





//charge wallet product




        //end

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.setVisibility(View.GONE);
                show2.setVisibility(View.VISIBLE);
                from.setText("From Date");
                to.setText("To Date");
                dataList1.clear();
                adapter1.notifyDataSetChanged();

            }
        });


        balance_event task=new balance_event();
        task.execute();



        if(!BillingProcessor.isIabServiceAvailable(getActivity())) {
            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(getActivity(), LICENSE_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
               // showToast("You Already purcahsed this product,Now Charge your wallet");


                //  updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                showToast("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
               // showToast("onBillingInitialized");
                readyToPurchase = true;
                //   updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
               // showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                // updateTextViews();
            }
        });



        //end spinner

        return view;
    }
    //start webservice report

    private class wallet_repoet extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            /*
            *
            * [] json array
            * {key : value} json object
            *
            * */

//            System.out.println("-------------AsyncCallWS--------------- ");
            String result = View_Report();
            if (result != null) {
                final_result = result;
                System.out.println("--------------result-------------- " + result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    wallet_model data1 = null;
                    for (int i = 0; i <= arr.length() - 1; i++) {

                        data1= new wallet_model();
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("------------obj------------- " + obj);
                        data1.setTransaction_Date(obj.getString("Transaction_Date"));
                        data1.setAmount(obj.getString("amount"));
                        data1.setBalance(obj.getString("balance"));
                        data1.setStatus(obj.getString("status"));

                        dataList1.add(data1);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            dataList1.clear();
            adapter1.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result.equals("") || final_result.equals("[[]]") || final_result.equals(null) || final_result.equals("null")) {
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();

            } else {

                adapter1 = new walletadapter(context, dataList1);
                mDrawerList1.setAdapter(adapter1);

            }
        }
    }




    public String View_Report() {
        String SOAP_ACTION = serviceurl.URL + "/Wallet_Activity_Report/";
        String METHOD_NAME = "Wallet_Activity_Report";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("user_id", final_id);
            Request.addProperty("from_date", from.getText().toString());
            Request.addProperty("to_date", to.getText().toString());
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- " + soapEnvelope.getResponse());
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }













    //start web service
    //soap service
    private class balance_event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;
        String m;

        @Override
        protected Void doInBackground(Void... voids) {
//            System.out.println("-------------AsyncCallWS--------------- ");
            String result = View_Balance();
            if (result != null) {
                final_result = result;
                System.out.println("--------------result-------------- " + result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());

                    for (int i = 0; i <= arr.length()-1; i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("--------" + i + "-------- " + obj);
                        data.setBalance(obj.get("balance")+"");




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("--------" + e);


                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result!=null) {

                if (final_result.equals("") || final_result.equals("[[]]") || final_result.equals(null) || final_result.equals("null")) {
                    Toast.makeText(getActivity(), "No Balance Avilable", Toast.LENGTH_LONG).show();
                    t1.setText("0" + " EGP ");

                } else {

                    // Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    t1.setText(data.getBalance() + " EGP ");


                }
            }else {
                Toast.makeText(context, "Check internet connection", Toast.LENGTH_SHORT).show();
            }


        }

    }

    public String View_Balance() {
        String SOAP_ACTION = serviceurl.URL + "/View_Balance/";
        String METHOD_NAME = "View_Balance";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("user_id", final_id);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- " + soapEnvelope.getResponse());
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }


        //update wallet
        private class charge_wallet extends AsyncTask<Void, Void, Void> {
            String final_result = null;
            JSONArray arr = null;

            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("-------------AsyncCallWS--------------- ");
                try {

                    String result = updatewallet();
                    System.out.println(result);
                    if (result!=null){
                        final_result=result;
                    }


                }catch (Exception ex){
                    System.out.println("Exception "+ex.getMessage());

                }


                return null;
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(Void result) {
                if (final_result.equals("\"Charge Successfully\"")){
                    Toast.makeText(context, "Charge Wallet Successfully", Toast.LENGTH_SHORT).show();
                    balance_event task=new balance_event();
                    task.execute();

                }else {
                    Toast.makeText(context, "Some Thing go Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }



        private String updatewallet(){

            String SOAP_ACTION = serviceurl.URL+"/charge_wallet/";
            String METHOD_NAME = "charge_wallet";
            String NAMESPACE = serviceurl.URL+"/";
            String URL = serviceurl.URL;
            String response="";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("user_id", final_id);
                Request.addProperty("balance", MySharedPref.getData(getActivity(),"amount",null));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.encodingStyle="utf-8";

                soapEnvelope.dotNet = false;
                soapEnvelope.implicitTypes=true;
                soapEnvelope.setAddAdornments(false);
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug=true;
                transport.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                transport.call(SOAP_ACTION, soapEnvelope);
                System.out.println("-------Response-------- "+ soapEnvelope.getResponse());
                response=soapEnvelope.getResponse().toString();
                Log.i("", "Result Verification: " + soapEnvelope.getResponse());
            } catch (Exception ex) {
                System.out.println("---------Exe------- "+ex);
                ex.printStackTrace();
            }
            return response;
        }


        //charge wallet




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);

    }



    @Override
    public void onDestroy(){
        if (bp != null)
            bp.release();
        context.stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }



    @Override
    public void onResume() {
        super.onResume();
        updatePurchase();
    }

    private void updatePurchase() {
        Boolean consumed = bp.consumePurchase("openparktech");
        if (consumed) {
            MySharedPref.saveData(getActivity(), "amount", "25");

            charge_wallet charge_wallet = new charge_wallet();
            charge_wallet.execute();
        }

        Boolean consumed2 = bp.consumePurchase("openparktech2");
        if (consumed2) {
            MySharedPref.saveData(getActivity(), "amount", "50");

            charge_wallet charge_wallet = new charge_wallet();
            charge_wallet.execute();
        }


        Boolean consumed3 = bp.consumePurchase("openparktech3");
        if (consumed3) {
            MySharedPref.saveData(getActivity(), "amount", "75");

            charge_wallet charge_wallet = new charge_wallet();
            charge_wallet.execute();
        }

        Boolean consumed4 = bp.consumePurchase("openparktech4");
        if (consumed4) {
            MySharedPref.saveData(getActivity(), "amount", "100");

            charge_wallet charge_wallet = new charge_wallet();
            charge_wallet.execute();
        }
        /*
        if (bp.isPurchased("openparktech")) {
            Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show();
            Boolean consumed = bp.consumePurchase("openparktech");
            if (consumed) {
                MySharedPref.saveData(getActivity(), "amount", "25");

                charge_wallet charge_wallet = new charge_wallet();
                charge_wallet.execute();
            }else{
                Toast.makeText(context, "12", Toast.LENGTH_SHORT).show();
            }
        }  else if (bp.isPurchased("openparktech2")) {
           // Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show();
            Boolean consumed = bp.consumePurchase("openparktech2");
            if (consumed) {
                MySharedPref.saveData(getActivity(), "amount", "50");
                charge_wallet charge_wallet = new charge_wallet();
                charge_wallet.execute();
            }
        }else if (bp.isPurchased("openparktech3")) {
           // Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show();
            Boolean consumed = bp.consumePurchase("openparktech3");
            if (consumed) {
                MySharedPref.saveData(getActivity(), "amount", "75");

                charge_wallet charge_wallet = new charge_wallet();
                charge_wallet.execute();
            }
        }else if (bp.isPurchased("openparktech4")) {
               //  Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show();
                Boolean consumed = bp.consumePurchase("openparktech4");
                if (consumed) {
                    MySharedPref.saveData(getActivity(), "amount", "100");

                    charge_wallet charge_wallet = new charge_wallet();
                    charge_wallet.execute();
                }

        }else {
            Toast.makeText(context, "sobhy", Toast.LENGTH_SHORT).show();
        }
        */
    }







    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }










}
