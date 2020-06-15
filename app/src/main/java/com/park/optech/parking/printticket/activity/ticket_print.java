package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.printer.BixolonPrinter;
import com.ganesh.intermecarabic.Arabic864;
import com.park.optech.parking.R;
import com.park.optech.parking.another.BluetoothUtil;
import com.park.optech.parking.model.Ticket_Model;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.models.TicketsModel;
import com.park.optech.parking.printticket.readcard.NdefMessageParser;
import com.park.optech.parking.printticket.readcard.ParsedNdefRecord;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ticket_print extends Activity {


    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;
    @BindView(R.id.backimage)
    ImageButton backimage;
    @BindView(R.id.show1)
    RelativeLayout show1;
    @BindView(R.id.show2)
    RelativeLayout show2;
    @BindView(R.id.parking_header)
    TextView parkingHeader;
    @BindView(R.id.ticket_date)
    TextView ticketDate;
    @BindView(R.id.ticket_member_name)
    TextView ticketMemberName;
    @BindView(R.id.ticket_member_id)
    TextView ticketMemberId;
    @BindView(R.id.ticket_gatenumber)
    TextView ticketGatenumber;
    @BindView(R.id.ticketprice)
    TextView ticketprice;
    @BindView(R.id.ticketnumber)
    TextView ticketnumber;
    @BindView(R.id.printticket)
    Button printticket;
    @BindView(R.id.ticket_popupgreen)
    RelativeLayout ticketPopupgreen;
    @BindView(R.id.blockedmember_number)
    TextView blockedmemberNumber;
    @BindView(R.id.blockedmember_name)
    TextView blockedmemberName;
    @BindView(R.id.cancel_blocked)
    Button cancelBlocked;
    @BindView(R.id.blocked_popup)
    RelativeLayout blockedPopup;
    @BindView(R.id.activity_ticket)
    RelativeLayout activityTicket;



    private List<String> pairedPrinters = new ArrayList<String>();
    private Boolean connectedPrinter = false;
    private static BixolonPrinter bixolonPrinterApi;

    //View layer things
    private Animation rotation = null; //Caching an animation makes the world a better place to be
    private View layoutLoading;
    private View layoutThereArentPairedPrinters;
    private View layoutPrinterReady;
    private TextView debugTextView = null; //A hidden TextView where you can test things
    ProgressDialog pd;

    String type = "";
    ArrayList<String> x = new ArrayList<>();
    String selecteditem;

    ListView l1;
    String[] spinnerArray;
    String[] spinnerArray2;
    HashMap<Integer, String> spinnerMap;
    HashMap<Integer, String> spinnerMapname;
    HashMap<Integer, String> spinnerMap2;

    String userid, username;

    MembersModel membersModel;
    TicketsModel data;
    //

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_print);
        ButterKnife.bind(this);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        backimage.setOnClickListener(view -> {
            Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
        });


        backimage.setOnClickListener(view -> {
            Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);

        });


        if (rotation == null) {
            rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        }

        debugTextView = (TextView) findViewById(R.id.debug);
        layoutLoading = findViewById(R.id.layoutLoading);
        layoutThereArentPairedPrinters = findViewById(R.id.layoutNoExisteImpresora);
        layoutPrinterReady = findViewById(R.id.layoutImpresoraPreparada);
        updateScreenStatus(layoutLoading);

        userid = MySharedPref.getData(ticket_print.this, "userid", null);

        username = MySharedPref.getData(ticket_print.this, "username", null);


//        readMemeber();

        clicks();


    }

    private void clicks() {
        cancelBlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockedPopup.setVisibility(View.GONE);
            }
        });
    }


    private void printticket() {


        String text = "";
        text += "سارية حتى نهاية اليوم";
        text += "\n";
        text += "يرجى الاحتفاظ بالتذكرة طول فترة الزيارة";
        text += "\n";
        text += "تذكرة رقم: ";
        text += data.getTrx_no();

        String strTextToPrint = "";
        strTextToPrint += "\n";
        strTextToPrint += "التاريخ: ";
        strTextToPrint += data.getTimestamp();
        strTextToPrint += "\n";
        strTextToPrint += "اسم العضو: ";
        strTextToPrint += membersModel.getName();
        strTextToPrint += "\n\n";
        strTextToPrint += "رقم العضوية: ";
        strTextToPrint += membersModel.getMembership_no();
        strTextToPrint += "\n";
        strTextToPrint += "بوابة رقم: ";
        strTextToPrint += data.getCameraNo();
        strTextToPrint += "\n";
        strTextToPrint += "قيمة التذكرة: ";
        strTextToPrint += data.getPayAmount() + " جنية";
        strTextToPrint += "\n\n";
        strTextToPrint += "نادى الصيد المصرى";


        Arabic864 arabic864 = new Arabic864();
        byte[] arabicTXT = arabic864.Convert(strTextToPrint, false);
        byte[] arabicTXT2 = arabic864.Convert(text, false);

        //bixolonPrinterApi.printText(arabicTXT,1,0,10,false);
        bixolonPrinterApi.printBitmap(arabicTXT, 1, 0, 35, false);

        //Bitmap bPrintmap = printbitmap(strTextToPrint);
        //  bixolonPrinterApi.printBitmap(bPrintmap,BixolonPrinter.ALIGNMENT_CENTER, 0,	50, false);
        bixolonPrinterApi.printQrCode(data.getTrx_no(), BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.QR_CODE_MODEL2, 8, false);
        // Bitmap Printmap = printbitmap(text);
        // bixolonPrinterApi.printBitmap(Printmap,BixolonPrinter.ALIGNMENT_CENTER, 0,	50, false);
        bixolonPrinterApi.printBitmap(arabicTXT2, 1, 0, 35, false);

        bixolonPrinterApi.lineFeed(4, false);

        pd.dismiss();


    }




//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (nfcAdapter != null) {
//            if (!nfcAdapter.isEnabled())
//                showWirelessSettings();
//
//            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
//        }
//    }



    //connect with printer
    private void updateScreenStatus(View viewToShow) {
        if (viewToShow == layoutLoading) {
            layoutLoading.setVisibility(View.VISIBLE);
            layoutThereArentPairedPrinters.setVisibility(View.GONE);
            layoutPrinterReady.setVisibility(View.GONE);
            iconLoadingStart();
        } else if (viewToShow == layoutThereArentPairedPrinters) {
            layoutLoading.setVisibility(View.GONE);
            layoutThereArentPairedPrinters.setVisibility(View.VISIBLE);
            layoutPrinterReady.setVisibility(View.GONE);
            iconLoadingStop();
        } else if (viewToShow == layoutPrinterReady) {
            show2.setVisibility(View.GONE);
            show1.setVisibility(View.VISIBLE);
            iconLoadingStop();
        }

        updatePrintButtonState();
    }

    PairWithPrinterTask task = null;

    @Override
    protected void onResume() {
        super.onResume();


        bixolonPrinterApi = new BixolonPrinter(this, handler, null);

        task = new PairWithPrinterTask();
        task.execute();

        updatePrintButtonState();
        BluetoothUtil.startBluetooth();

//        if (nfcAdapter != null) {
//            if (!nfcAdapter.isEnabled())
//                showWirelessSettings();
//
//            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void updatePrintButtonState() {


    }


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // Log.i("Handler", msg.what + " " + msg.arg1 + " " + msg.arg2);

            switch (msg.what) {

                case BixolonPrinter.MESSAGE_STATE_CHANGE:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_STATE_CHANGE");
                    switch (msg.arg1) {
                        case BixolonPrinter.STATE_CONNECTED:
                            updateScreenStatus(layoutPrinterReady);
                            Log.i("Handler", "BixolonPrinter.STATE_CONNECTED");
                            connectedPrinter = true;
                            updateScreenStatus(layoutPrinterReady);
                            break;

                        case BixolonPrinter.STATE_CONNECTING:
                            updateScreenStatus(layoutLoading);
                            Log.i("Handler", "BixolonPrinter.STATE_CONNECTING");
                            connectedPrinter = false;
                            break;

                        case BixolonPrinter.STATE_NONE:
                            updateScreenStatus(layoutLoading);
                            Log.i("Handler", "BixolonPrinter.STATE_NONE");
                            connectedPrinter = false;

                            break;
                    }
                    break;

                case BixolonPrinter.MESSAGE_WRITE:
                    switch (msg.arg1) {
                        case BixolonPrinter.PROCESS_SET_SINGLE_BYTE_FONT:
                            Log.i("Handler", "BixolonPrinter.PROCESS_SET_SINGLE_BYTE_FONT");
                            break;

                        case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:
                            Log.i("Handler", "BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT");
                            break;

                        case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:
                            Log.i("Handler", "BixolonPrinter.PROCESS_DEFINE_NV_IMAGE");
                            break;

                        case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:
                            Log.i("Handler", "BixolonPrinter.PROCESS_REMOVE_NV_IMAGE");
                            break;

                        case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:
                            Log.i("Handler", "BixolonPrinter.PROCESS_UPDATE_FIRMWARE");
                            break;
                    }
                    break;

                case BixolonPrinter.MESSAGE_READ:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_READ");
                    break;

                case BixolonPrinter.MESSAGE_DEVICE_NAME:
                    debugTextView.setText(msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME));
                    Log.i("Handler", "BixolonPrinter.MESSAGE_DEVICE_NAME - " + msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME));
                    break;

                case BixolonPrinter.MESSAGE_TOAST:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_TOAST - " + msg.getData().getString("toast"));
                    // Toast.makeText(getApplicationContext(), msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
                    break;

                // The list of paired printers
                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET");
                    if (msg.obj == null) {
                        updateScreenStatus(layoutThereArentPairedPrinters);
                    } else {
                        Set<BluetoothDevice> pairedDevices = (Set<BluetoothDevice>) msg.obj;
                        for (BluetoothDevice device : pairedDevices) {
                            if (!pairedPrinters.contains(device.getAddress())) {
                                pairedPrinters.add(device.getAddress());
                            }
                            if (pairedPrinters.size() == 1) {
                                bixolonPrinterApi.connect(pairedPrinters.get(0));
                            }
                        }
                    }
                    break;

                case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_PRINT_COMPLETE");
                    break;

                case BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP");
                    break;

                case MESSAGE_START_WORK:
                    Log.i("Handler", "MESSAGE_START_WORK");
                    break;

                case MESSAGE_END_WORK:
                    Log.i("Handler", "MESSAGE_END_WORK");
                    break;

                case BixolonPrinter.MESSAGE_USB_DEVICE_SET:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_USB_DEVICE_SET");
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        // DialogManager.showUsbDialog(MainActivity.this,
                        // (Set<UsbDevice>) msg.obj, mUsbReceiver);
                    }
                    break;

                case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:
                    Log.i("Handler", "BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET");
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "No connectable device", Toast.LENGTH_SHORT).show();
                    }
                    // DialogManager.showNetworkDialog(PrintingActivity.this, (Set<String>) msg.obj);
                    break;
            }
        }
    };

    private class PairWithPrinterTask extends AsyncTask<Void, Void, Void> {

        boolean running = true;

        public PairWithPrinterTask() {

        }

        public void stop() {
            running = false;
        }

        @Override
        protected Void doInBackground(Void... params) {


            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    // Stuff that updates the UI
                    if (connectedPrinter == null || connectedPrinter == false) {
                        publishProgress();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        int action = 0;

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (action < 20) {
                bixolonPrinterApi.findBluetoothPrinters();
                action++;
            } else {
                bixolonPrinterApi.disconnect();
                action = 0;
            }
        }
    }


    boolean animated = false;

    public void iconLoadingStart() {
        View loading = findViewById(R.id.loading);
        if (loading != null && !animated) {
            loading.startAnimation(rotation);
            loading.setVisibility(View.VISIBLE);
        }

        if (loading == null) {
            setProgressBarIndeterminateVisibility(Boolean.TRUE);
        }
        animated = true;
    }

    public void iconLoadingStop() {
        setProgressBarIndeterminateVisibility(Boolean.FALSE);

        View loading = findViewById(R.id.loading);
        if (loading != null) {
            loading.clearAnimation();
            loading.setVisibility(View.INVISIBLE);
        }
        animated = false;
    }






















    //start web services


//    private class park_Eventlist extends AsyncTask<Void, Void, Void> {
//        String final_result = null;
//        JSONArray arr = null;
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            String result = get_parklist();
//            if (result != null) {
//                final_result = result;
//
//                System.out.println("--------------result-------------- " + final_result);
//                try {
//                    arr = new JSONArray(result);
//                    arr = arr.getJSONArray(0);
//                    System.out.println("------------------Size---------------- " + arr.length());
//
//                    //  for (int i = 0; i <= arr.length() - 1; i++) {
//                    //  data1 = new user_park();
//                    //  JSONObject obj = arr.getJSONObject(i);
//                    //   System.out.println("------------obj------------- " + obj);
//                    //    mylist.add(obj.getString("name"));
//                    //    mylist.add(obj.getString("pk"));
//                    //  data1.setPk(obj.getString("pk"));
//
//                    spinnerArray = new String[arr.length()];
//                    spinnerMap = new HashMap<Integer, String>();
//                    spinnerMapname = new HashMap<Integer, String>();
//
//                    for (int x = 0; x <= arr.length() - 1; x++) {
//                        JSONObject obj = arr.getJSONObject(x);
//                        spinnerMap.put(x, obj.getString("pk"));
//                        spinnerMapname.put(x, obj.getString("name"));
//                        spinnerArray[x] = obj.getString("name");
//                    }
//
//
//                    //data1.setName(obj.getString("name"));
//                    // parklist.add(data1);
//
//                    // Log.e("size >> ", "" + parklist.size());
//
//                    // }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    System.out.println("ex" + e);
//                }
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            if (final_result != null) {
//
//                if (final_result.equals("[[]]") || final_result.equals("") || final_result.equals(null)) {
//                    Toast.makeText(ticket_print.this, "Somethimg go wrong", Toast.LENGTH_SHORT).show();
//                    ;
//
//                } else {
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ticket_print.this, android.R.layout.simple_spinner_item, spinnerArray);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                }
//
//
//            } else {
//                Toast.makeText(ticket_print.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//    }

//    public String get_parklist() {
//        String SOAP_ACTION = serviceurl.URL + "/View_Bark/";
//        String METHOD_NAME = "View_Bark";
//        String NAMESPACE = serviceurl.URL + "/";
//        String URL = serviceurl.URL;
//        String response = null;
//        try {
//            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            soapEnvelope.dotNet = true;
//            soapEnvelope.setOutputSoapObject(Request);
//            HttpTransportSE transport = new HttpTransportSE(URL);
//            transport.debug = true;
//            transport.call(SOAP_ACTION, soapEnvelope);
//            System.out.println("-------Response-------- " + soapEnvelope.getResponse());
//            response = soapEnvelope.getResponse().toString();
//            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
//        } catch (Exception ex) {
////            System.out.println("---------Exe------- "+ex);
//            ex.printStackTrace();
//        }
//        return response;
//    }


//get ticket type


//    private class park_ticket extends AsyncTask<Void, Void, Void> {
//        String final_result = null;
//        JSONArray arr = null;
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            String result = get_ticket();
//            if (result != null) {
//                final_result = result;
//                try {
//                    arr = new JSONArray(result);
//                    // arr = arr.getJSONArray(0);
//                    System.out.println("------------------Size---------------- " + arr.length());
//
//                    //  for (int i = 0; i <= arr.length() - 1; i++) {
//                    //  data1 = new user_park();
//                    //  JSONObject obj = arr.getJSONObject(i);
//                    //   System.out.println("------------obj------------- " + obj);
//                    //    mylist.add(obj.getString("name"));
//                    //    mylist.add(obj.getString("pk"));
//                    //  data1.setPk(obj.getString("pk"));
//
//                    spinnerArray2 = new String[arr.length()];
//                    spinnerMap2 = new HashMap<Integer, String>();
//                    for (int x = 0; x <= arr.length() - 1; x++) {
//                        JSONObject obj = arr.getJSONObject(x);
//                        spinnerMap2.put(x, obj.getString("pk"));
//                        spinnerArray2[x] = obj.getString("name");
//                    }
//
//
//                    //data1.setName(obj.getString("name"));
//                    // parklist.add(data1);
//
//                    // Log.e("size >> ", "" + parklist.size());
//
//                    // }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    System.out.println("ex" + e);
//                }
//
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            if (final_result != null) {
//
//                if (final_result.equals("[[]]") || final_result.equals("") || final_result.equals(null)) {
//                    Toast.makeText(ticket_print.this, "Somethimg go wrong", Toast.LENGTH_SHORT).show();
//                    ;
//
//                } else {
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ticket_print.this, android.R.layout.simple_list_item_1, spinnerArray2);
//                    l1.setAdapter(adapter);
//                    pd.dismiss();
//
//                }
//
//
//            } else {
//                Toast.makeText(ticket_print.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//    }

//    public String get_ticket() {
//        String SOAP_ACTION = serviceurl.URL2 + "/retrieve_all_tickets/";
//        String METHOD_NAME = "retrieve_all_tickets";
//        String NAMESPACE = serviceurl.URL2 + "/";
//        String URL = serviceurl.URL2;
//        String response = null;
//        try {
//            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//            Request.addProperty("company", selecteditem);
//            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            soapEnvelope.dotNet = true;
//            soapEnvelope.setOutputSoapObject(Request);
//            HttpTransportSE transport = new HttpTransportSE(URL);
//            transport.debug = true;
//            transport.call(SOAP_ACTION, soapEnvelope);
//            System.out.println("-------Response-------- " + soapEnvelope.getResponse());
//            response = soapEnvelope.getResponse().toString();
//            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
//        } catch (Exception ex) {
////            System.out.println("---------Exe------- "+ex);
//            ex.printStackTrace();
//        }
//        return response;
//    }


//print ticket

//    private class ticket_event extends AsyncTask<Void, Void, Void> {
//        String final_result = null;
//        JSONArray arr = null;
//        boolean running = true;
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            System.out.println("-------------AsyncCallWS--------------- ");
//
//            String result = get_ticketprint();
//            if (result != null) {
//                final_result = result;
//                System.out.println("--------------result-------------- " + result);
//                try {
//                    arr = new JSONArray(result);
//                    //arr = arr.getJSONArray(0);
//                    System.out.println("------------------Size---------------- " + arr.length());
//                    System.out.println(final_id);
//                    for (int i = 0; i <= arr.length(); i++) {
//                        JSONObject obj = arr.getJSONObject(i);
//
//                        data.setPK(obj.get("PK") + "");
//                        data.setGate(obj.get("gate") + "");
//                        data.setName(obj.get("name") + "");
//                        data.setPayAmount(obj.get("PayAmount") + "");
//                        data.setPaid(obj.get("paid") + "");
//                        data.setDay(obj.get("Day") + "");
//                        data.setMonth(obj.get("Month") + "");
//                        data.setYear(obj.get("Year") + "");
//                        data.setEntryTIME(obj.get("Entry_Time") + "");
//
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    System.out.println(e);
//                }
//            }
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//
//
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            if (final_result.equals("") || final_result.equals("[]") || final_result.equals(null)) {
//                Toast.makeText(ticket_print.this, "SomeThing go Wrong", Toast.LENGTH_SHORT).show();
//                pd.dismiss();
//
//            } else {
//                System.out.println(data.getPK());
//                //ticket.setVisibility(View.VISIBLE);
//                //t1.setText(data.getDay()+"/"+data.getMonth()+"/"+ data.getYear());
//                //t2.setText(data.getEntryTIME());
//                //t3.setText(data.getGate());
//                //t4.setText(data.getName());
//                //t5.setText(data.getPayAmount()+"جنية ");
//                //t6.setText(data.getPK()+"تذكرة رقم ");
//                //t7.setText(MySharedPref.getData(ticket_print.this,"ptname",null));
//                printticket();
//
//
//            }
//        }
//    }
//
//
//    public String get_ticketprint() {
//        String SOAP_ACTION = serviceurl.URL2 + "/get_ticket/";
//        String METHOD_NAME = "get_ticket";
//        String NAMESPACE = serviceurl.URL2 + "/";
//        String URL = serviceurl.URL2;
//        String response = "";
//        try {
//            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//            Request.addProperty("ticket_type_id", type);
//            Request.addProperty("user_id", final_id);
//            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            soapEnvelope.dotNet = true;
//            soapEnvelope.setOutputSoapObject(Request);
//            HttpTransportSE transport = new HttpTransportSE(URL);
//            transport.debug = true;
//            transport.call(SOAP_ACTION, soapEnvelope);
//            System.out.println("-------Response-------- " + soapEnvelope.getResponse());
//            response = soapEnvelope.getResponse().toString();
//            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
//        } catch (Exception ex) {
//            System.out.println("---------Exe------- " + ex);
//            ex.printStackTrace();
//        }
//        return response;
//    }


    //end


//

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }




    //region start read card
    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }


    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }


    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }


    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            displayMsgs(msgs);
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        String str = null;
        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            str = record.str();
            builder.append(str).append("\n");
        }

//        ParsedNdefRecord record = records.get(2);
//        String str = record.str();
//        str=str.replace(" ","");
//        str=str.replace("ID(dec):","");
//
//        ParsedNdefRecord record2 = records.get(3);
//        String str2 = record2.str();
//        str2=str2.replace(" ","");
//        str2=str2.replace("ID(reversed dec):","");
//
//        String memberId=str+str2;
//        memberId=memberId.replace(" ","");

        String[] separated = str.split(":");

        String id1=separated[3];
        id1=id1.replace(" ","");
        id1=id1.replace("ID(reverseddec)","");

        String id2=separated[4];
        id2=id2.replace(" ","");
        id2=id2.replace("Technologies","");

        String memberId=id1+id2;

        memberId=memberId.replaceAll("\n","");



        if (!memberId.equals("")) {

                membersModel = Database_Helper.getInstance(ticket_print.this).getmember(memberId);

                if (membersModel.getMembership_no() != null) {

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(membersModel.getEnd_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (System.currentTimeMillis() > Objects.requireNonNull(strDate).getTime()) {
                        blockedPopup.setVisibility(View.VISIBLE);
                        ticketPopupgreen.setVisibility(View.GONE);
                        blockedmemberName.setText(membersModel.getName());
                        blockedmemberNumber.setText(membersModel.getMembership_no());

                    } else {
//                        Date currentTime = Calendar.getInstance().getTime();
                        Date currentTime = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(currentTime);
                        formattedDate = formattedDate.replace("-", "");

                        TicketsModel ticketsModel = new TicketsModel();
                        ticketsModel.setCameraNo("1");
                        ticketsModel.setCompany("2");
                        ticketsModel.setPaid("1");
                        ticketsModel.setPayAmount("20");
//                        ticketsModel.setPayTime(currentTime+"");
//                        ticketsModel.setTimestamp(currentTime+"");
                        ticketsModel.setMembers(membersModel.getMembership_no());
                        ticketsModel.setPayUser(userid);
                        ticketsModel.setSync("0");

                        Date currentTime2 = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate2 = df.format(currentTime);

                        ticketsModel.setTimestamp(formattedDate2);
                        List<TicketsModel> ticketsModelList = new ArrayList<>();
                        ticketsModelList = Database_Helper.getInstance(ticket_print.this).getTickets();


                        String trxNumber = "20010020" + formattedDate + ticketsModelList.size();

                        ticketsModel.setTrx_no(trxNumber);

                        Database_Helper.getInstance(ticket_print.this).insertTicket(ticketsModel);

                        Log.e("TRX_NO",trxNumber +"     :   " + String.valueOf(System.currentTimeMillis()));

                        TicketsModel data=Database_Helper.getInstance(ticket_print.this).getTicket();


                        ticketPopupgreen.setVisibility(View.VISIBLE);
                        blockedPopup.setVisibility(View.GONE);
                        ticketDate.setText(data.getTimestamp());
                        ticketMemberName.setText(membersModel.getName());
                        ticketMemberId.setText(membersModel.getMembership_no());
                        ticketGatenumber.setText(data.getCameraNo());
                        ticketprice.setText(data.getPayAmount() + " جنية ");
                        ticketnumber.setText(data.getTrx_no());

                        printticket.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ticketPopupgreen.setVisibility(View.GONE);
                                pd.setMessage("Printing...");
                                pd.show();
                                pd.setCanceledOnTouchOutside(false);
                                printticket();
                            }
                        });

                    }


                }
//                pd.dismiss();


            } else {
                Toast.makeText(ticket_print.this, "ادخل رقم العضوية", Toast.LENGTH_SHORT).show();

            }





//        text.setText(memberId);
    }

    //endregion


}
