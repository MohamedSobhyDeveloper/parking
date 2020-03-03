package com.park.optech.parking.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.park.optech.parking.R;
import com.park.optech.parking.adapter.inviteadapter;
import com.park.optech.parking.adapter.parkadapter;
import com.park.optech.parking.adapter.pnameadapter;
import com.park.optech.parking.model.user_invitation;
import com.park.optech.parking.model.user_park;
import com.park.optech.parking.restful.ApiMethods;
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
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class MyTicket extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    Button b11,b22;
    ListView mDrawerList;
    String final_id=null;
    Context context;
    ArrayList<user_invitation> dataList1= new ArrayList<>();
    inviteadapter adapter1;
    RelativeLayout show,show1,show2,show3;



    //start new
    Button b1;
    EditText c1,c2,c3,n1,n2,n3,n4,natid,license,name,pname;
    Spinner spinner,s;
    String selectedItem="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int fYear, fMonth, fDay, fHour, fMinute;
    // user_invitation data = new user_invitation();
    private static final int REQUEST_CODE = 1;
    ImageButton imageButton;
    ArrayList<user_invitation> dataList= new ArrayList<>();
    ArrayList<user_park> parklist= new ArrayList<>();
    parkadapter adapter;
    pnameadapter a;
    EditText  editTextGetCarrierNumber;
    CountryCodePicker ccpGetNumber;
    TextView tvValidity,from1,to1,from,to;
    ImageView imgValidity;
    Button ask_btn;
    ImageButton backimage,backimage2;
    String value="";
    ArrayList<String> mylist = new ArrayList<String>();
    ImageView i1,i2,i11,i22;
    String  x;
    user_park data1 = null;
    String[] spinnerArray;
    HashMap<Integer,String> spinnerMap;
    String[] spinnerArray1;
    HashMap<Integer,String> spinnerMap1;
    ProgressDialog pd;





    public MyTicket() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_ticket, container, false);

        context=getActivity();
        final_id = MySharedPref.getData(getActivity(), "id", null);
        show=(RelativeLayout)view.findViewById(R.id.showinvitation);


        mDrawerList = (ListView) view.findViewById(R.id.listview);
        b11=(Button)view.findViewById(R.id.apply);
        b22=(Button)view.findViewById(R.id.get);



        pd = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pd.setMessage("Loading Invitations...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);




        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show1.setVisibility(View.VISIBLE);
                show2.setVisibility(View.GONE);
                show.setVisibility(View.GONE);
                show3.setVisibility(View.GONE);
                pd = new ProgressDialog(getActivity());


            }
        });

        b22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MySharedPref.getData(getActivity(),"key",null).equals("1")){
                show.setVisibility(View.GONE);
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.VISIBLE);
                show3.setVisibility(View.GONE);

            }else {
                    show.setVisibility(View.GONE);
                    show1.setVisibility(View.VISIBLE);
                    show2.setVisibility(View.GONE);
                    show3.setVisibility(View.GONE);
                    pd = new ProgressDialog(getActivity());



                }
            }
        });


        invitation_Event taskk=new invitation_Event();
        taskk.execute();

        park_Event task=new park_Event();
        task.execute();

        park_Eventlist task2=new park_Eventlist();
        task2.execute();


        //start new update

        MySharedPref.saveData(getActivity(),"cmobile","");

        assignView(view);
        registerCarrierEditText();
        show1=(RelativeLayout)view.findViewById(R.id.showinvitation1);
        show2=(RelativeLayout)view.findViewById(R.id.showinvitation2);
        show3=(RelativeLayout)view.findViewById(R.id.showinvitation11);
        ask_btn=(Button)view.findViewById(R.id.askbtn);
        b1=(Button)view.findViewById(R.id.addcar);
        c1=(EditText)view.findViewById(R.id.c1);
        c2=(EditText)view.findViewById(R.id.c2);
        c3=(EditText)view.findViewById(R.id.c3);
        n1=(EditText)view.findViewById(R.id.n1);
        n2=(EditText)view.findViewById(R.id.n2);
        n3=(EditText)view.findViewById(R.id.n3);
        n4=(EditText)view.findViewById(R.id.n4);
        natid=(EditText)view.findViewById(R.id.nid);
        license=(EditText)view.findViewById(R.id.driverlicense);
        from=(TextView) view.findViewById(R.id.from_date);
        to=(TextView) view.findViewById(R.id.to_date);
        i1=(ImageView) view.findViewById(R.id.image1);
        i2=(ImageView) view.findViewById(R.id.image2);
        i11=(ImageView) view.findViewById(R.id.image11);
        i22=(ImageView) view.findViewById(R.id.image22);
        from1=(TextView) view.findViewById(R.id.from_date1);
        to1=(TextView) view.findViewById(R.id.to_date1);
        name=(EditText)view.findViewById(R.id.drivername);
        // pname=(EditText)findViewById(R.id.parkname);
        imageButton=(ImageButton) view.findViewById(R.id.contact_btn);
        spinner=(Spinner)view.findViewById(R.id.spinner1);
        s=(Spinner)view.findViewById(R.id.spinner2);
        backimage=(ImageButton)view.findViewById(R.id.backimage);
        backimage2=(ImageButton)view.findViewById(R.id.backimage1);






        ask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(from1.getText().toString())&&!TextUtils.isEmpty(to1.getText().toString())) {
                  member_Event task=new member_Event();
                    task.execute();
                }else if (TextUtils.isEmpty(from1.getText().toString())){
                    from1.setError("Enter Date");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (TextUtils.isEmpty(to1.getText().toString())){
                    to1.setError("Enter Date");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (TextUtils.isEmpty(from1.getText().toString())&&TextUtils.isEmpty(to1.getText().toString())){
                    from1.setError("Enter Date");
                    to1.setError("Enter Date");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (TextUtils.isEmpty(from1.getText().toString())&&!TextUtils.isEmpty(to1.getText().toString())){
                    from1.setError("Enter Date");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else {
                    to1.setError("Enter Date");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                }



            }
        });


        from1.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                    from1.setError(null);

            }
        });
        to1.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                to1.setError(null);

            }
        });


        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    show1.setVisibility(View.GONE);
                    show.setVisibility(View.VISIBLE);
                  invitation_Event taskk=new invitation_Event();
                  taskk.execute();
                pd.setMessage("Loading Invitations...");
                pd.show();

                from1.setText("");
                    to1.setText("");

            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });


        backimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitation_Event taskk=new invitation_Event();
                taskk.execute();
                show2.setVisibility(View.GONE);
                show.setVisibility(View.VISIBLE);
                pd.setMessage("Loading Invitations...");
                pd.show();



            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(name.getText().toString())&&!TextUtils.isEmpty(editTextGetCarrierNumber.getText().toString())&&!TextUtils.isEmpty(from.getText().toString())
                        &&!TextUtils.isEmpty(to.getText().toString())


                        ){

                    ApiMethods.inviteUser(getActivity(), "Invite_Guest",final_id ,name.getText().toString(),MySharedPref.getData(getActivity(), "cmobile", null),natid.getText().toString(),license.getText().toString(),from.getText().toString(),to.getText().toString(),x,c1.getText().toString(),c2.getText().toString(),c3.getText().toString(),n1.getText().toString(),n2.getText().toString(),n3.getText().toString(),n4.getText().toString());

                    //  update_profile task=new update_profile();
                    // task.execute();
                    //  ApiMethods.loginUser(makeinvitation.this, updateprofile.this, "Update_Profile",final_id ,fname.getText().toString(),lname.getText().toString(),email.getText().toString(),phone.getText().toString(),c1.getText().toString(),c2.getText().toString(),c3.getText().toString(),n1.getText().toString(),n2.getText().toString(),n3.getText().toString(),n4.getText().toString(),brand.getText().toString(),modell.getText().toString());


                }else if (TextUtils.isEmpty(name.getText().toString())){
                    name.setError("Enetr Name");
                }else if (TextUtils.isEmpty(editTextGetCarrierNumber.getText().toString())){
                    editTextGetCarrierNumber.setError("Enter Mobile Number");
                }else if (TextUtils.isEmpty(from.getText().toString())){
                    from.setError("Enter Date");
                }else if (TextUtils.isEmpty(to.getText().toString())){
                    to.setError("Enter Date");
                }else {
                    name.setError("Enetr Name");
                    editTextGetCarrierNumber.setError("Enter Mobile Number");
                    from.setError("Enter Date");
                    to.setError("Enter Date");





                }

            }
        });



        name.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                name.setError(null);

            }
        });
        editTextGetCarrierNumber.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                editTextGetCarrierNumber.setError(null);

            }
        });
        to.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                to.setError(null);

            }
        });
        from.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                from.setError(null);

            }
        });


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  selectedItem = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(makeinvitation.this, selectedItem, Toast.LENGTH_SHORT).show();
                // x=adapterView.getSelectedItemPosition()+1;
                selectedItem = spinnerMap.get(s.getSelectedItemPosition());
                //  Toast.makeText(makeinvitation.this,x , Toast.LENGTH_SHORT).show();
                // Toast.makeText(makeinvitation.this, x+"", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  selectedItem = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(makeinvitation.this, selectedItem, Toast.LENGTH_SHORT).show();
                // x=adapterView.getSelectedItemPosition()+1;
                x = spinnerMap1.get(spinner.getSelectedItemPosition());
                //Toast.makeText(makeinvitation.this, x, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(makeinvitation.this,x , Toast.LENGTH_SHORT).show();
                // Toast.makeText(makeinvitation.this, x+"", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //number car action


        c1.addTextChangedListener(new TextWatcher()

        {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (s.length() == 1)
                {
                    c2.requestFocus();
                    c2.setEnabled(true);

                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        c2.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    c3.requestFocus();
                    c3.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        c3.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n1.requestFocus();
                    n1.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        n1.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n2.requestFocus();
                    n2.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        n2.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n3.requestFocus();
                    n3.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        n3.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n4.requestFocus();
                    n4.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        n4.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    c2.setEnabled(false);
                    c3.setEnabled(false);
                    n1.setEnabled(false);
                    n2.setEnabled(false);
                    n3.setEnabled(false);
                    n4.setEnabled(false);
                    editTextGetCarrierNumber.requestFocus();

                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });



        i11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.AppCompatAlertDialogStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fYear=year;
                                fMonth=monthOfYear+1;
                                fDay=dayOfMonth;
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.AppCompatAlertDialogStyle,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

                                                fHour=hourOfDay;
                                                fMinute=minute;

                                                from.setText(fYear+"-"+fMonth+"-"+fDay+" "+fHour+":"+fMinute+":"+"00");
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);




                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });



        i22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.AppCompatAlertDialogStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fYear=year;
                                fMonth=monthOfYear+1;
                                fDay=dayOfMonth;
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.AppCompatAlertDialogStyle,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

                                                fHour=hourOfDay;
                                                fMinute=minute;

                                                to.setText(fYear+"-"+fMonth+"-"+fDay+" "+fHour+":"+fMinute+":"+"00");


                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);




                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();



            }
        });


        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.AppCompatAlertDialogStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fYear=year;
                                fMonth=monthOfYear+1;
                                fDay=dayOfMonth;
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog

                                from1.setText(fYear+"-"+fMonth+"-"+fDay);

                                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);




                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.AppCompatAlertDialogStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fYear=year;
                                fMonth=monthOfYear+1;
                                fDay=dayOfMonth;
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog

                                to1.setText(fYear+"-"+fMonth+"-"+fDay);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });









        //end


        return view;
    }



    private class invitation_Event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = get_invitation();
            if (result != null) {
                final_result = result;

                System.out.println("--------------result-------------- " + final_result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    user_invitation data = null;
                    for (int i = 0; i <= arr.length() - 1; i++) {

                        data = new user_invitation();
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("------------obj------------- " + obj);
                        data.setPlate_No(obj.getString("Plate_No"));
                        data.setCompany(obj.getString("company"));
                        data.setName(obj.getString("name"));
                        data.setStart_date(obj.getString("start_date"));
                        data.setEnd_date(obj.getString("end_date"));
                        data.setApproved(obj.getString("approved"));



                        dataList1.add(data);

                        Log.e("size >> ", "" + dataList1.size());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ex"+e);
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dataList1.clear();
          //  adapter1.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result!=null) {
                pd.dismiss();

                if (final_result.equals("") ||final_result.equals(null) || final_result.equals("[[]]")) {
                    show3.setVisibility(View.VISIBLE);

                } else{
                    adapter1 = new inviteadapter(context, dataList1);
                    mDrawerList.setAdapter(adapter1);
            }

            }else {
                Toast.makeText(context, "Check Internet Connections", Toast.LENGTH_SHORT).show();
                pd.dismiss();
               // startActivity(new Intent(getActivity(),MainActivity.class));

            }





        }
    }

    public String get_invitation() {
        String SOAP_ACTION = serviceurl.URL + "/Find_Invitations/";
        String METHOD_NAME = "Find_Invitations";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = null;
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
//            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }



    //start new

    private class park_Event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = get_park();
            if (result != null) {
                final_result = result;
                user_invitation data = null;

                System.out.println("--------------result-------------- " + final_result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    spinnerArray1 = new String[arr.length()];
                    spinnerMap1 = new HashMap<Integer, String>();
                    for (int x = 0; x <= arr.length() - 1; x++)
                    {
                        JSONObject obj = arr.getJSONObject(x);
                        spinnerMap1.put(x,obj.getString("pk"));
                        spinnerArray1[x] = obj.getString("name");
                    }
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
                    //Toast.makeText(getActivity(), "Your are not member", Toast.LENGTH_SHORT).show();

                    MySharedPref.saveData(getActivity(),"key","0");


                } else {
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerArray1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    MySharedPref.saveData(getActivity(),"key","1");
                }
            }else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public String get_park() {
        String SOAP_ACTION = serviceurl.URL + "/Parking_Member/";
        String METHOD_NAME = "Parking_Member";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = null;
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
//            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }


    //get number
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = context.getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name1 = cursor.getString(nameColumnIndex);
                editTextGetCarrierNumber.setText(number);
                name.setText(name1);

                System.out.print(number);
            }
        }
    };

    private void registerCarrierEditText() {
        ccpGetNumber.registerCarrierNumberEditText(editTextGetCarrierNumber);
        ccpGetNumber.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    // Toast.makeText(makeinvitation.this, "Correct Number", Toast.LENGTH_SHORT).show();
                    imgValidity.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_turned_in_black_24dp));
                    tvValidity.setText("Valid Number");

                    InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editTextGetCarrierNumber.getWindowToken(), 0);
                    if (ccpGetNumber.getSelectedCountryCode().endsWith("0")&&editTextGetCarrierNumber.getText().toString().startsWith("0")){
                        MySharedPref.saveData(getActivity(),"cmobile",ccpGetNumber.getSelectedCountryCode()+editTextGetCarrierNumber.getText().delete(0 , 1));


                    }else {
                        MySharedPref.saveData(getActivity(),"cmobile",ccpGetNumber.getSelectedCountryCode()+editTextGetCarrierNumber.getText().toString());

                    }
                } else {

                    //  Toast.makeText(makeinvitation.this, "Invalid Correct Number", Toast.LENGTH_SHORT).show();
                    imgValidity.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_late_black_24dp));
                    tvValidity.setText("Invalid Number");


                }
            }
        });

        // ccpLoadNumber.registerCarrierNumberEditText(editTextLoadCarrierNumber);
    }

    private void assignView(View view) {
        //load number
        editTextGetCarrierNumber = (EditText)view.findViewById(R.id.editText_getCarrierNumber);
        ccpGetNumber = (CountryCodePicker)view.findViewById(R.id.ccp_getFullNumber);
        tvValidity = (TextView)view.findViewById(R.id.tv_validity);
        imgValidity = (ImageView)view.findViewById(R.id.img_validity);
        //get number





    }


    //get park list


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
                    for (int x = 0; x <= arr.length() - 1; x++)
                    {
                        JSONObject obj = arr.getJSONObject(x);
                        spinnerMap.put(x,obj.getString("pk"));
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
                    Toast.makeText(getActivity(), "Somethimg go wrong", Toast.LENGTH_SHORT).show();

                } else {
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(adapter);
                }
                //  a = new com.park.tech.parking.adapter.pnameadapter(makeinvitation.this, parklist);
                // s.setAdapter(a);

                // ArrayAdapter<String> adapter = new ArrayAdapter<String>(makeinvitation.this,
                //     android.R.layout.simple_spinner_item, mylist);
                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //  s.setAdapter(adapter);




            }else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
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




    //be_member in garage

    private class member_Event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {


            String result = be_member();
            if (result != null) {
                final_result = result;


            }
            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result!=null) {
                if (final_result.equals("\"Added Successfully\"")) {
                    Toast.makeText(context, "Request send successfully", Toast.LENGTH_SHORT).show();
                    from1.setText("");
                    to1.setText("");


                } else {
                    Toast.makeText(getActivity(), "Some Thing go Wrong", Toast.LENGTH_SHORT).show();

                }
            }else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public String be_member() {

        String SOAP_ACTION = serviceurl.URL + "/be_member/";
        String METHOD_NAME = "be_member";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = null;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("id", final_id);
            Request.addProperty("start", from1.getText().toString());
            Request.addProperty("end", to1.getText().toString());
            Request.addProperty("park",selectedItem);

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



}
