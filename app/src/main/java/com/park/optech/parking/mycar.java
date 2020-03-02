package com.park.optech.parking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.park.optech.parking.another.Constants;
import com.park.optech.parking.model.car_model;
import com.park.optech.parking.model.user_profile;
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


public class mycar extends Fragment {
    Button slocation,findcar,exitgate,b;
    String final_id="";
    String mb1="";
    String mb2="";

    Context context;
    car_model data=new car_model();
    EditText etuserEmail,etlastname,etfirstname;
    user_profile data1=new user_profile();
    CircleImageView circleimageview;
    EditText p,g;
    String pname;



    public mycar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_mycar, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context=getActivity();
        slocation=(Button)view.findViewById(R.id.scan_loc_btn);
        findcar=(Button)view.findViewById(R.id.takeme2car_btn);
        exitgate=(Button)view.findViewById(R.id.open_exit);
        final_id = MySharedPref.getData(context, "id", null);
        mb1 = MySharedPref.getData(context, "parkname", null);
        mb2 = MySharedPref.getData(context, "gatename", null);

        if (mb1.equals("")){
            startActivity(new Intent(getActivity(),MainActivity.class));
            Toast.makeText(context, "You must check in first", Toast.LENGTH_SHORT).show();
        }

        circleimageview = (CircleImageView) view.findViewById(R.id.circleImageView);
        p=(EditText) view.findViewById(R.id.parkname);
        g=(EditText) view.findViewById(R.id.gatename);
        b=(Button)view.findViewById(R.id.cancel);


       p.setText(mb1);
       g.setText(mb2);

        GetProfile task=new GetProfile();
        task.execute();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

        slocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),location_ticket.class));
            }
        });
        exitgate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),exit_scan.class));
            }
        });
        findcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car_event task=new car_event();
                task.execute();
            }
        });



        return view;
    }

    //start web service
    //soap service
    private class car_event extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;
        String m;

        @Override
        protected Void doInBackground(Void... voids) {
//            System.out.println("-------------AsyncCallWS--------------- ");
            String result = Find_Car();
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
                        data.setLocation_Sign_name(obj.get("Location_Sign_name")+"");
                        data.setLatitude(obj.get("Latitude")+"");
                        data.setLongitude(obj.get("Longitude")+"");



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

            if (final_result.equals("")||final_result.equals("[[]]")||final_result.equals(null)||final_result.equals("null")){
                Toast.makeText(getActivity(), "You didn't book any place", Toast.LENGTH_LONG).show();

            }else {

                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                MySharedPref.saveData(getActivity(),"button","0");
                double x= Double.parseDouble(data.getLatitude());
                double y= Double.parseDouble(data.getLongitude());
                System.out.println("mm  "+x);
                LatLng destLocation=new LatLng(x,y);
                System.out.println("mkjk  "+destLocation);
                Intent i = new Intent(new Intent(getActivity(), parkdirection.class));
                i.putExtra("parklocation", destLocation);
                startActivity(i);


            }


        }

    }

    public String Find_Car() {
        String SOAP_ACTION = serviceurl.URL + "/Find_Car/";
        String METHOD_NAME = "Find_Car";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("id", final_id);
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

    private class GetProfile extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("-------------AsyncCallWS--------------- ");
            String result=getProfile_Event();
            if (result != null) {
                final_result=result;
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    for (int i = 0; i <= arr.length()-1; i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println(i+"  "+obj);
                        data1.setUsername(obj.get("username")+"");
                        data1.setEmail(obj.get("email")+"");
                        data1.setPhoto(obj.get("photo")+"");
                        data1.setFirst_name(obj.get("first_name")+"");
                        data1.setLast_name(obj.get("last_name")+"");


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
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Void result) {
            if (final_result != null) {


                MySharedPref.saveData(getActivity(), "car", data1.getFirst_name());

                if (data1.getLast_name().equals("null")) {
                    if (data1.getPhoto().equals("null")) {
                        Picasso.get().load(R.drawable.hh).into(circleimageview);

                    } else {

                        Picasso.get().load(data1.getPhoto()).into(circleimageview);


                    }
                } else {

                    if (data1.getPhoto().equals("null")) {
                        Picasso.get().load(R.drawable.hh).into(circleimageview);

                    } else {
                        Picasso.get().load(data1.getPhoto()).into(circleimageview);


                    }
                }

            }else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
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
            Request.addProperty("id", final_id);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- "+ soapEnvelope.getResponse());
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }



    private class car_event1 extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;
        String m;

        @Override
        protected Void doInBackground(Void... voids) {
//            System.out.println("-------------AsyncCallWS--------------- ");
            String result = Find_Car();
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
                        data.setLocation_Sign_name(obj.get("Location_Sign_name")+"");
                        data.setLatitude(obj.get("Latitude")+"");
                        data.setLongitude(obj.get("Longitude")+"");



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

            if (final_result.equals("")||final_result.equals("[[]]")||final_result.equals(null)||final_result.equals("null")){
              //  Toast.makeText(getActivity(), "You didn't book any place", Toast.LENGTH_LONG).show();

            }else {
                g.setText(data.getLocation_Sign_name());




            }


        }

    }


}
