package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.printer.BixolonPrinter;
import com.ganesh.intermecarabic.Arabic864;
import com.park.optech.parking.R;
import com.park.optech.parking.another.BluetoothUtil;
import com.park.optech.parking.model.Ticket_Model;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ticket_print extends Activity {

    private final int LINE_CHARS = 50; // Bixolon 200II


    private static final long PRINTING_SLEEP_TIME = 300;

    private static final long PRINTING_TIME = 4000;


    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;


    private List<String> pairedPrinters = new ArrayList<String>();
    private Boolean connectedPrinter = false;
    private static BixolonPrinter bixolonPrinterApi;

    //View layer things
    private Animation rotation = null; //Caching an animation makes the world a better place to be
    private View layoutLoading;
    private View layoutThereArentPairedPrinters;
    private View layoutPrinterReady;
    private TextView debugTextView = null; //A hidden TextView where you can test things
    private Button printButton = null; //Guess it :P
    ProgressDialog pd;

    String final_id;
    String type="";
    ArrayList<String> x = new ArrayList<>();
    Context context;
   String selecteditem;

    RelativeLayout show1,show2,show3;
    ImageButton backimage;
    Button back,cancel,getticket;
    ListView l1;
    Spinner spinner;
    String[] spinnerArray;
    String[] spinnerArray2;
    HashMap<Integer,String> spinnerMap;
    HashMap<Integer,String> spinnerMapname;
    HashMap<Integer,String> spinnerMap2;
    Ticket_Model data=new Ticket_Model();
    String parkname;
    TextView t1,t2,t3,t4,t5,t6,t7;
    RelativeLayout ticket;
    Button print_btn;
    Bitmap bPrintmap;
    Bitmap Printmap;


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_print);
        pd = new ProgressDialog(ticket_print.this,R.style.AppCompatAlertDialogStyle);

        final_id = MySharedPref.getData(ticket_print.this, "idadmin", null);


        show1=(RelativeLayout)findViewById(R.id.show1);
        show2=(RelativeLayout)findViewById(R.id.show2);
        ticket=(RelativeLayout)findViewById(R.id.ticket_popupgreen);


        show3=(RelativeLayout)findViewById(R.id.show3);
        spinner=(Spinner)findViewById(R.id.spinner);
        getticket=(Button)findViewById(R.id.getticket);
        print_btn=(Button)findViewById(R.id.print);


        backimage=(ImageButton)findViewById(R.id.backimage);
        cancel=(Button)findViewById(R.id.cancel);
        back=(Button)findViewById(R.id.back);
        l1=(ListView)findViewById(R.id.listview);
        t1=(TextView)findViewById(R.id.date);
        t2=(TextView)findViewById(R.id.time);
        t3=(TextView)findViewById(R.id.gatenumber);
        t4=(TextView)findViewById(R.id.cartype);
        t5=(TextView)findViewById(R.id.ticketprice);
        t6=(TextView)findViewById(R.id.ticketnumber);
        t7=(TextView)findViewById(R.id.parking_header);



l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pd.setMessage("Printing Ticket...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        type = spinnerMap2.get(adapterView.getPositionForView(view));
        // Toast.makeText(ticket_print.this, type, Toast.LENGTH_SHORT).show();

        ticket_event task=new ticket_event();
        task.execute();
    }
});


        park_Eventlist task=new park_Eventlist();
        task.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  selectedItem = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(makeinvitation.this, selectedItem, Toast.LENGTH_SHORT).show();
                // x=adapterView.getSelectedItemPosition()+1;
                selecteditem = spinnerMap.get(spinner.getSelectedItemPosition());
                parkname=spinnerMapname.get(spinner.getSelectedItemPosition());
                MySharedPref.saveData(ticket_print.this,"ptname",parkname);

                //Toast.makeText(makeinvitation.this, x, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(makeinvitation.this,x , Toast.LENGTH_SHORT).show();
                // Toast.makeText(makeinvitation.this, x+"", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Loading Ticket...");
                pd.show();
                pd.setCanceledOnTouchOutside(false);

                park_ticket task=new park_ticket();
                task.execute();

            }
        });



      backimage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                      .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
              System.exit(0);
          }
      });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);

            }
        });
        if (rotation == null) {
            rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        }

        debugTextView = (TextView) findViewById(R.id.debug);

        layoutLoading = findViewById(R.id.layoutLoading);
        layoutThereArentPairedPrinters = findViewById(R.id.layoutNoExisteImpresora);
        layoutPrinterReady = findViewById(R.id.layoutImpresoraPreparada);
        updateScreenStatus(layoutLoading);









    }


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
            show3.setVisibility(View.VISIBLE);
            iconLoadingStop();
        }

        updatePrintButtonState();
    }

    PairWithPrinterTask task = null;

    @Override
    protected void onResume() {
        super.onResume();


        bixolonPrinterApi = new BixolonPrinter(this, handler,null);

        task = new PairWithPrinterTask();
        task.execute();

        updatePrintButtonState();
        BluetoothUtil.startBluetooth();

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


    private class park_Eventlist extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = get_parklist();
            if (result != null) {
                final_result = result;

                System.out.println("--------------result-------------- " + final_result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());

                    //  for (int i = 0; i <= arr.length() - 1; i++) {
                    //  data1 = new user_park();
                    //  JSONObject obj = arr.getJSONObject(i);
                    //   System.out.println("------------obj------------- " + obj);
                    //    mylist.add(obj.getString("name"));
                    //    mylist.add(obj.getString("pk"));
                    //  data1.setPk(obj.getString("pk"));

                    spinnerArray = new String[arr.length()];
                    spinnerMap = new HashMap<Integer, String>();
                    spinnerMapname = new HashMap<Integer, String>();

                    for (int x = 0; x <= arr.length() - 1; x++)
                    {
                        JSONObject obj = arr.getJSONObject(x);
                        spinnerMap.put(x,obj.getString("pk"));
                        spinnerMapname.put(x,obj.getString("name"));
                        spinnerArray[x] = obj.getString("name");
                    }


                    //data1.setName(obj.getString("name"));
                    // parklist.add(data1);

                    // Log.e("size >> ", "" + parklist.size());

                    // }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ex"+e);
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

                if (final_result.equals("[[]]") || final_result.equals("") || final_result.equals(null)) {
                    Toast.makeText(ticket_print.this, "Somethimg go wrong", Toast.LENGTH_SHORT).show();
                    ;

                } else {
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(ticket_print.this,android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }




            }else {
                Toast.makeText(ticket_print.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public String get_parklist() {
        String SOAP_ACTION = serviceurl.URL + "/View_Bark/";
        String METHOD_NAME = "View_Bark";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = null;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
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
//            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }



//get ticket type


    private class park_ticket extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = get_ticket();
            if (result != null) {
                final_result = result;
                try {
                    arr = new JSONArray(result);
                   // arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());

                    //  for (int i = 0; i <= arr.length() - 1; i++) {
                    //  data1 = new user_park();
                    //  JSONObject obj = arr.getJSONObject(i);
                    //   System.out.println("------------obj------------- " + obj);
                    //    mylist.add(obj.getString("name"));
                    //    mylist.add(obj.getString("pk"));
                    //  data1.setPk(obj.getString("pk"));

                    spinnerArray2 = new String[arr.length()];
                    spinnerMap2 = new HashMap<Integer, String>();
                    for (int x = 0; x <= arr.length() - 1; x++)
                    {
                        JSONObject obj = arr.getJSONObject(x);
                        spinnerMap2.put(x,obj.getString("pk"));
                        spinnerArray2[x] = obj.getString("name");
                    }


                    //data1.setName(obj.getString("name"));
                    // parklist.add(data1);

                    // Log.e("size >> ", "" + parklist.size());

                    // }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ex"+e);
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

                if (final_result.equals("[[]]") || final_result.equals("") || final_result.equals(null)) {
                    Toast.makeText(ticket_print.this, "Somethimg go wrong", Toast.LENGTH_SHORT).show();
                    ;

                }else {
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(ticket_print.this,android.R.layout.simple_list_item_1, spinnerArray2);
                    l1.setAdapter(adapter);
                    show3.setVisibility(View.GONE);
                    pd.dismiss();

                }




            }else {
                Toast.makeText(ticket_print.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public String get_ticket() {
        String SOAP_ACTION = serviceurl.URL2 + "/retrieve_all_tickets/";
        String METHOD_NAME = "retrieve_all_tickets";
        String NAMESPACE = serviceurl.URL2 + "/";
        String URL = serviceurl.URL2;
        String response = null;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("company",selecteditem);
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
//            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }


//print ticket

    private class ticket_event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;
        boolean running = true;


        @Override
        protected Void doInBackground(Void... voids) {
//            System.out.println("-------------AsyncCallWS--------------- ");

            String result = get_ticketprint();
            if (result != null) {
                final_result=result;
                System.out.println("--------------result-------------- " + result);
                try {
                    arr = new JSONArray(result);
                    //arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    System.out.println(final_id);
                    for (int i = 0; i <= arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

                        data.setPK(obj.get("PK")+"");
                        data.setGate(obj.get("gate")+"");
                        data.setName(obj.get("name")+"");
                        data.setPayAmount(obj.get("PayAmount")+"");
                        data.setPaid(obj.get("paid")+"");
                        data.setDay(obj.get("Day")+"");
                        data.setMonth(obj.get("Month")+"");
                        data.setYear(obj.get("Year")+"");
                        data.setEntryTIME(obj.get("Entry_Time")+"");



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }

            return null;
        }




        @Override
        protected void onPreExecute() {


        }
        @Override
        protected void onPostExecute(Void result) {
            if (final_result.equals("")||final_result.equals("[]")||final_result.equals(null)) {
                Toast.makeText(ticket_print.this, "SomeThing go Wrong", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }else {
                System.out.println(data.getPK());
                //ticket.setVisibility(View.VISIBLE);
                //t1.setText(data.getDay()+"/"+data.getMonth()+"/"+ data.getYear());
                //t2.setText(data.getEntryTIME());
                //t3.setText(data.getGate());
                //t4.setText(data.getName());
                //t5.setText(data.getPayAmount()+"جنية ");
                //t6.setText(data.getPK()+"تذكرة رقم ");
                //t7.setText(MySharedPref.getData(ticket_print.this,"ptname",null));
                printticket();





            }
        }
    }


    public String get_ticketprint() {
        String SOAP_ACTION = serviceurl.URL2 + "/get_ticket/";
        String METHOD_NAME = "get_ticket";
        String NAMESPACE = serviceurl.URL2 + "/";
        String URL = serviceurl.URL2;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("ticket_type_id",type);
            Request.addProperty("user_id",final_id);
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






    //end



    private void printticket() {



        String text="";
        text+="ادارة نظم المعلومات";
        text+="\n";
        text+="فى حالة فقد التذكرة يتم دفع غرامة 20 جنية";
        text+="\n";
        text+="تذكرة رقم: ";
        text+=data.getPK();

        String strTextToPrint="";
        strTextToPrint+="\n";
        strTextToPrint+="التاريخ: ";
        strTextToPrint+=data.getDay()+"/"+data.getMonth()+"/"+data.getYear();
        strTextToPrint+="\n";
        strTextToPrint+="ساعة الدخول: ";
        strTextToPrint+=data.getEntryTIME();
        strTextToPrint+="\n\n";
        strTextToPrint+="بوابة رقم: ";
        strTextToPrint+=data.getGate();
        strTextToPrint+="\n";
        strTextToPrint+="نوع المركبة: ";
        strTextToPrint+=data.getName();
        strTextToPrint+="\n";
        strTextToPrint+="قيمة التذكرة: ";
        strTextToPrint+=data.getPayAmount()+" جنية";
        strTextToPrint+="\n\n";
        strTextToPrint+="مركز مصر للمعارض الدولية";



        Arabic864 arabic864 = new Arabic864();
        byte[] arabicTXT = arabic864.Convert(strTextToPrint, false);
        byte[] arabicTXT2 = arabic864.Convert(text, false);

        //bixolonPrinterApi.printText(arabicTXT,1,0,10,false);
        bixolonPrinterApi.printBitmap(arabicTXT,1,0,35,false);

        //Bitmap bPrintmap = printbitmap(strTextToPrint);
      //  bixolonPrinterApi.printBitmap(bPrintmap,BixolonPrinter.ALIGNMENT_CENTER, 0,	50, false);
        bixolonPrinterApi.printQrCode(data.getPK(), BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.QR_CODE_MODEL2, 8, false);
       // Bitmap Printmap = printbitmap(text);
        // bixolonPrinterApi.printBitmap(Printmap,BixolonPrinter.ALIGNMENT_CENTER, 0,	50, false);
        bixolonPrinterApi.printBitmap(arabicTXT2,1,0,35,false);

        bixolonPrinterApi.lineFeed(4, false);

        pd.dismiss();



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ticket_print.this, scand_print_ticket.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }
}
