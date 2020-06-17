package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.park.optech.parking.R;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.models.TicketsModel;
import com.park.optech.parking.printticket.readcard.NdefMessageParser;
import com.park.optech.parking.printticket.readcard.ParsedNdefRecord;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.park.optech.parking.sharedpref.MySharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintActivity extends AppCompatActivity {

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

    String userid, username;

    MembersModel membersModel;
    //

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    @BindView(R.id.blockedmember_startdate)
    TextView blockedmemberStartdate;
    @BindView(R.id.blockedmember_enddate)
    TextView blockedmemberEnddate;


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
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

        userid = MySharedPref.getData(PrintActivity.this, "userid", null);

        username = MySharedPref.getData(PrintActivity.this, "username", null);


        backimage.setOnClickListener(view -> {
            Intent intent = new Intent(PrintActivity.this, scand_print_ticket.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);

        });

        cancelBlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockedPopup.setVisibility(View.GONE);
            }
        });


    }


    //connect with printer


    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PrintActivity.this, scand_print_ticket.class)
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

    @SuppressLint("MissingSuperCall")
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
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
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

        String id1 = separated[3];
        id1 = id1.replace(" ", "");
        id1 = id1.replace("ID(reverseddec)", "");

        String id2 = separated[4];
        id2 = id2.replace(" ", "");
        id2 = id2.replace("Technologies", "");

        String memberId = id1 + id2;

        memberId = memberId.replaceAll("\n", "");


        if (!memberId.equals("")) {

            membersModel = Database_Helper.getInstance(PrintActivity.this).getmember(memberId);

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
                    blockedmemberStartdate.setText(membersModel.getStart_date());
                    blockedmemberEnddate.setText(membersModel.getEnd_date());


                } else {
//                        Date currentTime = Calendar.getInstance().getTime();
                    Date currentTime = Calendar.getInstance().getTime();
//                    @SuppressLint("SimpleDateFormat")
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    String formattedDate = df.format(currentTime);
//                    formattedDate = formattedDate.replace("-", "");
//
//                    TicketsModel ticketsModel = new TicketsModel();
//                    ticketsModel.setCameraNo("1");
//                    ticketsModel.setCompany("2");
//                    ticketsModel.setPaid("1");
//                    ticketsModel.setPayAmount("20");
////                        ticketsModel.setPayTime(currentTime+"");
////                        ticketsModel.setTimestamp(currentTime+"");
//                    ticketsModel.setMembers(membersModel.getMembership_no());
//                    ticketsModel.setPayUser(userid);
//                    ticketsModel.setSync("0");
//
//                    Date currentTime2 = Calendar.getInstance().getTime();
//                    @SuppressLint("SimpleDateFormat")
//                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
//                    String formattedDate2 = df.format(currentTime);
//
//                    ticketsModel.setTimestamp(formattedDate2);
//                    List<TicketsModel> ticketsModelList = new ArrayList<>();
//                    ticketsModelList = Database_Helper.getInstance(PrintActivity.this).getTickets();
//
//
//                    String trxNumber = "20010020" + formattedDate + ticketsModelList.size();
//
//                    ticketsModel.setTrx_no(trxNumber);
//
//                    Database_Helper.getInstance(PrintActivity.this).insertTicket(ticketsModel);
//
//                    Log.e("TRX_NO",trxNumber +"     :   " + String.valueOf(System.currentTimeMillis()));
//
//                    TicketsModel data=Database_Helper.getInstance(PrintActivity.this).getTicket();


                    ticketPopupgreen.setVisibility(View.VISIBLE);
                    blockedPopup.setVisibility(View.GONE);
                    ticketDate.setText(currentTime + "");
                    ticketMemberName.setText(membersModel.getName());
                    ticketMemberId.setText(membersModel.getMembership_no());
                    ticketGatenumber.setText(membersModel.getStart_date() + "");
                    ticketprice.setText(membersModel.getEnd_date());
//                    ticketnumber.setText(data.getTrx_no());

                    String finalMemberId = memberId;
                    printticket.setOnClickListener(view -> {
                        ticketPopupgreen.setVisibility(View.GONE);
                        Intent intent = new Intent(PrintActivity.this, ticket_print.class);
                        intent.putExtra("memberid", finalMemberId);
                        MySharedPref.saveData(PrintActivity.this,"memberid",finalMemberId);
                        startActivity(intent);
                        finish();
                    });

                }


            }


        } else {
            Toast.makeText(PrintActivity.this, "ادخل رقم العضوية", Toast.LENGTH_SHORT).show();

        }


//        text.setText(memberId);
    }

    //endregion


}