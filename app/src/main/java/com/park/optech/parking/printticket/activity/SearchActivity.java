package com.park.optech.parking.printticket.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.park.optech.parking.R;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.readcard.NdefMessageParser;
import com.park.optech.parking.printticket.readcard.ParsedNdefRecord;
import com.park.optech.parking.printticket.sqlite.Database_Helper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    ImageButton backBtn;

    TextView member_name_tv, membership_no_tv, start_date_tv, end_date_tv, validation_tv;
    CardView member_card;
    @BindView(R.id.layout_ticket)
    LinearLayout layoutTicket;
    ImageView snapshot;


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

        backBtn = findViewById(R.id.backimage);
        member_name_tv = findViewById(R.id.mem_name);
        membership_no_tv = findViewById(R.id.member_id);
        start_date_tv = findViewById(R.id.start_date);
        end_date_tv = findViewById(R.id.end_date);
        validation_tv = findViewById(R.id.validation);
        member_card = findViewById(R.id.member_card);
        snapshot = findViewById(R.id.snapshot);
        member_card.setVisibility(View.GONE);





        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
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
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            displayMsgs(msgs);
        }
    }

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

            MembersModel membersModel = Database_Helper.getInstance(SearchActivity.this).getmember(memberId);
            if (membersModel.getTag_id() != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date enddate = null;
                try {
                    enddate = sdf.parse(membersModel.getEnd_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (new Date().after(enddate)) {

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
                if (membersModel.getSnapshot()!=null&&!membersModel.getSnapshot().equals("")){
                    Picasso.with(getApplicationContext()).load(membersModel.getSnapshot()).into(snapshot);

                }else {
                    snapshot.setVisibility(View.GONE);
                }
                member_card.setVisibility(View.VISIBLE);
                if (dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
            } else {

                showWelcomDialog();

//                Toast.makeText(SearchActivity.this, "خطا فى رقم العضوية", Toast.LENGTH_SHORT).show();
                Log.e("Data", "null");
                member_card.setVisibility(View.GONE);

            }

        } else {
            Toast.makeText(SearchActivity.this, "ادخل رقم العضوية", Toast.LENGTH_SHORT).show();
            member_card.setVisibility(View.GONE);

        }




//        text.setText(memberId);
    }

    public void showWelcomDialog()
    {
        TextView timer;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.welcom_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        timer =dialog.findViewById(R.id.timer);
        timer.setText(" كارت غير معلوم ");

        Button btn_Cancel = dialog.findViewById(R.id.Cancel);
        btn_Cancel.setEnabled(true);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //endregion



}
