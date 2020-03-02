package com.park.optech.parking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.park.optech.parking.adapter.parkadapter;
import com.park.optech.parking.adapter.pnameadapter;
import com.park.optech.parking.model.User_history;
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

public class makeinvitation extends AppCompatActivity  {
    Button b1,b2;
    EditText c1,c2,c3,n1,n2,n3,n4,natid,license,from,to,name,pname,from1,to1;
    Spinner spinner,s;
    String selectedItem="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int fYear, fMonth, fDay, fHour, fMinute;
    String final_id=null;
   // user_invitation data = new user_invitation();
    private static final int REQUEST_CODE = 1;
    ImageButton imageButton,imageButton2;
    ArrayList<user_invitation> dataList= new ArrayList<>();
    ArrayList<user_park> parklist= new ArrayList<>();
    parkadapter adapter;
    pnameadapter a;
    EditText  editTextGetCarrierNumber;
    CountryCodePicker ccpGetNumber;
    TextView tvValidity;
    ImageView imgValidity;
    Button ask_btn,back_btn;
    RelativeLayout show1,show2;
    String value="";
    ArrayList<String> mylist = new ArrayList<String>();
    ImageView i1,i2,i11,i22;
    String  x;
    user_park data1 = null;
    String[] spinnerArray;
    HashMap<Integer,String> spinnerMap;
    String[] spinnerArray1;
    HashMap<Integer,String> spinnerMap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeinvitation);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final_id = MySharedPref.getData(makeinvitation.this, "id", null);
        MySharedPref.saveData(makeinvitation.this,"cmobile","");

        assignView();
        registerCarrierEditText();
        show1=(RelativeLayout)findViewById(R.id.showinvitation);
        show2=(RelativeLayout)findViewById(R.id.showinvitation1);
        imageButton2=(ImageButton)findViewById(R.id.ask);
        ask_btn=(Button)findViewById(R.id.askbtn);
        back_btn=(Button)findViewById(R.id.cancelbtn);
        b1=(Button)findViewById(R.id.addcar);
        b2=(Button)findViewById(R.id.cancel);
        c1=(EditText)findViewById(R.id.c1);
        c2=(EditText)findViewById(R.id.c2);
        c3=(EditText)findViewById(R.id.c3);
        n1=(EditText)findViewById(R.id.n1);
        n2=(EditText)findViewById(R.id.n2);
        n3=(EditText)findViewById(R.id.n3);
        n4=(EditText)findViewById(R.id.n4);
        natid=(EditText)findViewById(R.id.nid);
        license=(EditText)findViewById(R.id.driverlicense);
        from=(EditText)findViewById(R.id.from_date);
        to=(EditText)findViewById(R.id.to_date);
        i1=(ImageView) findViewById(R.id.image1);
        i2=(ImageView) findViewById(R.id.image2);
        i11=(ImageView) findViewById(R.id.image11);
        i22=(ImageView) findViewById(R.id.image22);
        from1=(EditText) findViewById(R.id.from_date1);
        to1=(EditText) findViewById(R.id.to_date1);
        name=(EditText)findViewById(R.id.drivername);
       // pname=(EditText)findViewById(R.id.parkname);
        imageButton=(ImageButton) findViewById(R.id.contact_btn);
        spinner=(Spinner)findViewById(R.id.spinner1);
        s=(Spinner)findViewById(R.id.spinner2);


        park_Event task=new park_Event();
        task.execute();

        park_Eventlist task2=new park_Eventlist();
        task2.execute();






        ask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(from1.getText().toString())&&!TextUtils.isEmpty(to1.getText().toString())) {
                    member_Event task=new member_Event();
                    task.execute();
                }else {
                    Toast.makeText(makeinvitation.this, "Please complete data", Toast.LENGTH_SHORT).show();

                }






            }
        });


       imageButton2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               show1.setVisibility(View.VISIBLE);
               show2.setVisibility(View.GONE);

           }
       });

       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               show1.setVisibility(View.GONE);
               show2.setVisibility(View.VISIBLE);
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


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(makeinvitation.this,MainActivity.class));
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(name.getText().toString())&&!TextUtils.isEmpty(editTextGetCarrierNumber.getText().toString())&&!TextUtils.isEmpty(from.getText().toString())
                        &&!TextUtils.isEmpty(to.getText().toString())


                        ){

                    ApiMethods.inviteUser(makeinvitation.this, "Invite_Guest",final_id ,name.getText().toString(),MySharedPref.getData(makeinvitation.this, "cmobile", null),natid.getText().toString(),license.getText().toString(),from.getText().toString(),to.getText().toString(),x,c1.getText().toString(),c2.getText().toString(),c3.getText().toString(),n1.getText().toString(),n2.getText().toString(),n3.getText().toString(),n4.getText().toString());

                    //  update_profile task=new update_profile();
                    // task.execute();
                  //  ApiMethods.loginUser(makeinvitation.this, updateprofile.this, "Update_Profile",final_id ,fname.getText().toString(),lname.getText().toString(),email.getText().toString(),phone.getText().toString(),c1.getText().toString(),c2.getText().toString(),c3.getText().toString(),n1.getText().toString(),n2.getText().toString(),n3.getText().toString(),n4.getText().toString(),brand.getText().toString(),modell.getText().toString());


                }else {
                    Toast.makeText(makeinvitation.this, "Please Complete data", Toast.LENGTH_SHORT).show();
                    from.setBackgroundColor(R.color.red);
                    to.setBackgroundColor(R.color.red);
                    name.setBackgroundColor(R.color.red);


                }

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(makeinvitation.this,
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
                                TimePickerDialog timePickerDialog = new TimePickerDialog(makeinvitation.this,
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(makeinvitation.this,
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
                                TimePickerDialog timePickerDialog = new TimePickerDialog(makeinvitation.this,
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(makeinvitation.this,
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(makeinvitation.this,
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






    }



    //get park name

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
                    Toast.makeText(makeinvitation.this, "Your are not member", Toast.LENGTH_SHORT).show();
                b1.setEnabled(false);
                b2.setEnabled(false);

                } else {
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(makeinvitation.this,android.R.layout.simple_spinner_item, spinnerArray1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }else {
                Toast.makeText(makeinvitation.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
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

                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editTextGetCarrierNumber.getWindowToken(), 0);
                    if (ccpGetNumber.getSelectedCountryCode().endsWith("0")&&editTextGetCarrierNumber.getText().toString().startsWith("0")){
                        MySharedPref.saveData(makeinvitation.this,"cmobile",ccpGetNumber.getSelectedCountryCode()+editTextGetCarrierNumber.getText().delete(0 , 1));


                    }else {
                        MySharedPref.saveData(makeinvitation.this,"cmobile",ccpGetNumber.getSelectedCountryCode()+editTextGetCarrierNumber.getText().toString());

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

    private void assignView() {
        //load number

        //get number
        editTextGetCarrierNumber = (EditText)findViewById(R.id.editText_getCarrierNumber);

        ccpGetNumber = (CountryCodePicker)findViewById(R.id.ccp_getFullNumber);
        tvValidity = (TextView)findViewById(R.id.tv_validity);
        imgValidity = (ImageView)findViewById(R.id.img_validity);




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
                        Toast.makeText(makeinvitation.this, "Somethimg go wrong", Toast.LENGTH_SHORT).show();
                 ;

                } else {
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(makeinvitation.this,android.R.layout.simple_spinner_item, spinnerArray);
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
                Toast.makeText(makeinvitation.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(makeinvitation.this, "Request send Successfully", Toast.LENGTH_SHORT).show();
                    from1.setText("");
                    to1.setText("");


                } else {
                    Toast.makeText(makeinvitation.this, "Some Thing go Wrong", Toast.LENGTH_SHORT).show();

                }
            }else {
                Toast.makeText(makeinvitation.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
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
