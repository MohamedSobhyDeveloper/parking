package com.park.optech.parking.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.park.optech.parking.activity.MainActivity;
import com.park.optech.parking.R;
import com.park.optech.parking.adapter.ListAdapter;
import com.park.optech.parking.model.User_history;
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


public class history extends Fragment{
    Context context;
    ListView mDrawerList;
    ArrayList<User_history> dataList= new ArrayList<>();
    Button cancel_btn,share_btn;
    String final_id = "";
    RelativeLayout show,show1,show2;
    ProgressDialog pd;
    User_history data = null;
    HashMap<Integer,String> spinnerMap;
    HashMap<Integer,String> spinnerMap1;
    HashMap<Integer,String> spinnerMap2;
    HashMap<Integer,String> spinnerMap3;
    HashMap<Integer,String> spinnerMap4;
    HashMap<Integer,String> spinnerMap5;
    HashMap<Integer,String> spinnerMap6;
    ListAdapter adapter;


    EditText pname,gname,date,time,duration,debit;

    ImageButton backimage;

    String[] spinnerArray;

    public history() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        context = getActivity();

        pd = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pd.setMessage("Loading history...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        final_id = MySharedPref.getData(getActivity(), "id", null);
        mDrawerList = (ListView) view.findViewById(R.id.listview);
        show=(RelativeLayout)view.findViewById(R.id.showlist);
        show1=(RelativeLayout)view.findViewById(R.id.show);
        show2=(RelativeLayout)view.findViewById(R.id.main);


        pname=(EditText)view.findViewById(R.id.parkname);
        gname=(EditText)view.findViewById(R.id.gatename);
        date=(EditText)view.findViewById(R.id.date);
        time=(EditText)view.findViewById(R.id.time);
        duration=(EditText)view.findViewById(R.id.duration);
        debit=(EditText)view.findViewById(R.id.debit);
        backimage=(ImageButton)view.findViewById(R.id.backimage);


        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show2.setVisibility(View.VISIBLE);
                show1.setVisibility(View.GONE);

            }
        });




        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date1 = spinnerMap.get(adapterView.getPositionForView(view));
                String time1= spinnerMap1.get(adapterView.getPositionForView(view));
                String time2= spinnerMap2.get(adapterView.getPositionForView(view));
                String debit1= spinnerMap6.get(adapterView.getPositionForView(view));
                String duration1= spinnerMap3.get(adapterView.getPositionForView(view));
                String park= spinnerMap4.get(adapterView.getPositionForView(view));
                String gate= spinnerMap5.get(adapterView.getPositionForView(view));


                show1.setVisibility(View.VISIBLE);
                show2.setVisibility(View.GONE);
                pname.setText(park);
                gname.setText(gate);
                date.setText(date1);
                time.setText(time1+" To "+time2);
                debit.setText(debit1);
                duration.setText(duration1);


            }
        });

        cancel_btn = (Button) view.findViewById(R.id.cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        User_HistoryEvent task = new User_HistoryEvent();
        task.execute();

        return view;
    }

    //history
    private class User_HistoryEvent extends AsyncTask<Void, Void, Void> {
        String final_result = null;
        JSONArray arr = null;

        @Override
        protected Void doInBackground(Void... voids) {

            String result = getUser_History();
            if (result != null) {
                final_result = result;
                System.out.println("--------------result-------------- " + final_result);
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                    System.out.println("------------------Size---------------- " + arr.length());
                    spinnerMap = new HashMap<Integer, String>();
                    spinnerMap1 = new HashMap<Integer, String>();
                    spinnerMap2 = new HashMap<Integer, String>();
                    spinnerMap3 = new HashMap<Integer, String>();
                    spinnerMap4 = new HashMap<Integer, String>();
                    spinnerMap5 = new HashMap<Integer, String>();
                    spinnerMap6 = new HashMap<Integer, String>();
                    for (int i = 0; i <= arr.length() - 1; i++) {

                        data = new User_history();
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("------------obj------------- " + obj);
                        data.setPK(obj.getString("PK"));
                        data.setTransactionDate(obj.getString("Transaction_Date"));
                        data.setParkingName(obj.getString("Parking_name"));
                        data.setEntryTIME(obj.getString("Entry_TIME"));
                        data.setExitTIME(obj.getString("Exit_TIME"));
                        data.setDuration(obj.getString("Duration"));
                        data.setFeesCollected(obj.getString("Fees_Collected"));
                        data.setGate(obj.getString("gate"));

                        Log.e("sdata >> ", "" + obj.getString("Transaction_Date"));

                        //
                        spinnerMap.put(i,obj.getString("Transaction_Date"));
                        spinnerMap1.put(i,obj.getString("Entry_TIME"));
                        spinnerMap2.put(i,obj.getString("Exit_TIME"));
                        spinnerMap3.put(i,obj.getString("Duration"));
                        spinnerMap4.put(i,obj.getString("Parking_name"));
                        spinnerMap5.put(i,obj.getString("gate"));
                        spinnerMap6.put(i,obj.getString("Fees_Collected"));

                        //

                        dataList.add(data);

                        Log.e("size >> ", "" + dataList.size());

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
                pd.dismiss();
                if (final_result.equals("") || final_result.equals(null) || final_result.equals("[[]]")) {
                    show.setVisibility(View.VISIBLE);

                } else {
                    adapter = new ListAdapter(context, dataList);
                    mDrawerList.setAdapter(adapter);
                }
            }else {
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pd.dismiss();
               // startActivity(new Intent(getActivity(),MainActivity.class));
            }


        }
    }

    public String getUser_History() {
        String SOAP_ACTION = serviceurl.URL + "/User_History/";
        String METHOD_NAME = "User_History";
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




    //end



}
