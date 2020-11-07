package com.park.optech.parking.activity;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.model.user_profile;
import com.park.optech.parking.restful.ApiMethods;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateprofile extends AppCompatActivity  {
    Button b1,b2;
    EditText fname,lname,email,brand,modell,n1,n2,n3,n4,c1,c2,c3,phone;

    user_profile data = new user_profile();
    String final_id=null;
    CircleImageView circleimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        final_id = MySharedPref.getData(updateprofile.this, "id", null);
        circleimageview = (CircleImageView)findViewById(R.id.circleImageView);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        GetProfile task = new GetProfile();
        task.execute();
        b1=(Button)findViewById(R.id.save_btn);
        b2=(Button)findViewById(R.id.cancel);
        fname=(EditText)findViewById(R.id.firstname);
        lname=(EditText)findViewById(R.id.lastname);
        email=(EditText)findViewById(R.id.email);
        brand=(EditText)findViewById(R.id.brandname);
        modell=(EditText)findViewById(R.id.modelname);
        n1=(EditText)findViewById(R.id.n1);
        n2=(EditText)findViewById(R.id.n2);
        n3=(EditText)findViewById(R.id.n3);
        n4=(EditText)findViewById(R.id.n4);
        c1=(EditText)findViewById(R.id.c1);
        c2=(EditText)findViewById(R.id.c2);
        c3=(EditText)findViewById(R.id.c3);
        phone=(EditText)findViewById(R.id.phoneNumber);


b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(updateprofile.this, MainActivity.class));
    }
});
b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(fname.getText().toString())&&!TextUtils.isEmpty(lname.getText().toString())
                &&!TextUtils.isEmpty(email.getText().toString())
                ||!TextUtils.isEmpty(brand.getText().toString())
                ||!TextUtils.isEmpty(modell.getText().toString())
                ||!TextUtils.isEmpty(n1.getText().toString())
                ||!TextUtils.isEmpty(n2.getText().toString())
                ||!TextUtils.isEmpty(n3.getText().toString())
                ||!TextUtils.isEmpty(n4.getText().toString())
                ||!TextUtils.isEmpty(c1.getText().toString())
                ||!TextUtils.isEmpty(c2.getText().toString())
                ||!TextUtils.isEmpty(c3.getText().toString())
                &&!TextUtils.isEmpty(phone.getText().toString())

                ){
          //  update_profile task=new update_profile();
           // task.execute();
            ApiMethods.loginUser(updateprofile.this, "Update_Profile",final_id ,fname.getText().toString(),lname.getText().toString(),email.getText().toString(),phone.getText().toString(),c1.getText().toString(),c2.getText().toString(),c3.getText().toString(),n1.getText().toString(),n2.getText().toString(),n3.getText().toString(),n4.getText().toString(),brand.getText().toString(),modell.getText().toString());


        }else {
            Toast.makeText(updateprofile.this, "Please Complete Profile", Toast.LENGTH_SHORT).show();

        }
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









        //end




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    //update profile
    private class update_profile extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("-------------AsyncCallWS--------------- ");
            String result = updateProfile();
            System.out.println(result);
            if (result!=null){
                final_result=result;
            }


            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
   if (final_result.equals("\"Updated Successfully\"")){
    Toast.makeText(updateprofile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
       MySharedPref.saveData(updateprofile.this,"car",fname.getText().toString());
       startActivity(new Intent(updateprofile.this,MainActivity.class));


   }else {
    Toast.makeText(updateprofile.this, "Invalid Correct data Or Check Internet Connection", Toast.LENGTH_SHORT).show();

}

        }
    }



    private String updateProfile(){

        String SOAP_ACTION = serviceurl.URL+"/update_profile/";
        String METHOD_NAME = "update_profile";
        String NAMESPACE = serviceurl.URL+"/";
        String URL = serviceurl.URL;
        String response="";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("id", final_id);
            Request.addProperty("car_brand", brand.getText().toString());
            Request.addProperty("model_no", modell.getText().toString());
            Request.addProperty("num1", n1.getText().toString());
            Request.addProperty("num2", n2.getText().toString());
            Request.addProperty("num3", n3.getText().toString());
            Request.addProperty("num4", n4.getText().toString());
            Request.addProperty("chr1", c1.getText().toString());
            Request.addProperty("chr2",c2.getText().toString());
            Request.addProperty("chr3", c3.getText().toString());
            Request.addProperty("first_name", fname.getText().toString());
            Request.addProperty("last_name", lname.getText().toString());
            Request.addProperty("email", email.getText().toString());
            Request.addProperty("mobile", phone.getText().toString());
            Request.addProperty("timezone", "");
            Request.addProperty("username", "");


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.encodingStyle="ISO-8859-1,utf-8;q=0.7,*;q=0.7";

            soapEnvelope.dotNet = true;
            soapEnvelope.implicitTypes=true;
            soapEnvelope.setAddAdornments(false);
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL,60000);
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





    //end






    //get profile

    private class GetProfile extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("-------------AsyncCallWS--------------- ");
            String result = getProfile_Event();
            if (result != null) {
                final_result = result;
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    for (int i = 0; i <= arr.length() - 1; i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println(i + "  " + obj);
                        data.setFirst_name(obj.get("first_name") + "");
                        data.setLast_name(obj.get("last_name") + "");
                        data.setEmail(obj.get("email") + "");
                        data.setUsername(obj.get("username") + "");
                        data.setPhoto(obj.get("photo") + "");
                        data.setCharacter1(obj.get("character1") + "");
                        data.setCharacter2(obj.get("character2") + "");
                        data.setCharacter3(obj.get("character3") + "");
                        data.setNumber1(obj.get("number1") + "");
                        data.setNumber2(obj.get("number2") + "");
                        data.setNumber3(obj.get("number3") + "");
                        data.setNumber4(obj.get("number4") + "");
                        data.setBrand(obj.get("brand") + "");
                        data.setModel(obj.get("model") + "");
                        data.setPhone(obj.get("phone") + "");


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

                    if (final_result != null) {

                if (data.getFirst_name().equals("null")) {
                    email.setText("");
                    brand.setText("");
                    modell.setText("");
                    phone.setText(data.getPhone());
                    fname.setText("");
                    lname.setText("");
                    n1.setText("");
                    n2.setText("");
                    n3.setText("");
                    n4.setText("");
                    c1.setText("");
                    c2.setText("");
                    c3.setText("");
                    if (data.getPhoto().equals("null")) {
                        Picasso.with(getApplicationContext()).load(R.drawable.hh).into(circleimageview);


                    } else {
                        Picasso.with(getApplicationContext()).load(data.getPhoto()).into(circleimageview);

                    }

                } else {
                    email.setText(data.getEmail());
                    brand.setText(data.getBrand());
                    modell.setText(data.getModel());
                    phone.setText(data.getPhone());
                    fname.setText(data.getFirst_name());
                    lname.setText(data.getLast_name());
                    n1.setText(data.getNumber1());
                    n2.setText(data.getNumber2());
                    n3.setText(data.getNumber3());
                    n4.setText(data.getNumber4());
                    c1.setText(data.getCharacter1());
                    c2.setText(data.getCharacter2());
                    c3.setText(data.getCharacter3());
                    if (data.getPhoto().equals("null")) {
                        Picasso.with(getApplicationContext()).load(R.drawable.hh).into(circleimageview);

                    } else {
                        Picasso.with(getApplicationContext()).load(data.getPhoto()).into(circleimageview);


                    }
                }

                //circleimageview.setImageResource(Integer.parseInt(data.getPhoto()));


            } else {
                Toast.makeText(updateprofile.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        public String getProfile_Event() {
            String SOAP_ACTION = serviceurl.URL + "/Show_User_Profile/";
            String METHOD_NAME = "Show_User_Profile";
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
                System.out.println("-------id-------- " + final_id);
                response = soapEnvelope.getResponse().toString();
                Log.i("", "Result Verification: " + soapEnvelope.getResponse());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }


    }}
