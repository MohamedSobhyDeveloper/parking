package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.printer.BixolonPrinter;
import com.ganesh.intermecarabic.Arabic864;
import com.park.optech.parking.R;
import com.park.optech.parking.another.BluetoothUtil;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.models.TicketsModel;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.park.optech.parking.sharedpref.MySharedPref;


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

    String type = "";
    ArrayList<String> x = new ArrayList<>();
    String selecteditem;

    ListView l1;
    String[] spinnerArray;
    String[] spinnerArray2;
    HashMap<Integer, String> spinnerMap;
    HashMap<Integer, String> spinnerMapname;
    HashMap<Integer, String> spinnerMap2;

    String userid, username,memberId;

    MembersModel membersModel;
    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_print);
        ButterKnife.bind(this);


        backimage.setOnClickListener(view -> {
            Intent intent = new Intent(ticket_print.this, PrintActivity.class)
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

        memberId = MySharedPref.getData(ticket_print.this, "memberid", null);



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

                    String text = "";
                    text += "سارية حتى نهاية اليوم";
                    text += "\n";
                    text += "يرجى الاحتفاظ بالتذكرة طول فترة الزيارة";
                    text += "\n";
//        text += ".";
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



                }

            Intent intent = new Intent(ticket_print.this, PrintActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);


        }

















    }



    @Override
    protected void onResume() {
        super.onResume();


        bixolonPrinterApi = new BixolonPrinter(this, handler, null);

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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ticket_print.this, PrintActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }




    //region connect with printer

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
//            show2.setVisibility(View.GONE);
//            show1.setVisibility(View.VISIBLE);
//            iconLoadingStop();
            printticket();
        }

        updatePrintButtonState();
    }

    PairWithPrinterTask task = null;


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


    //endregion



}
