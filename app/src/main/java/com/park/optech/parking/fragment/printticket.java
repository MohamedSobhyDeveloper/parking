package com.park.optech.parking.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.park.optech.parking.R;
import com.park.optech.parking.activity.MainActivity;
import com.park.optech.parking.activity.scand_print_ticket;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;


public class printticket extends Fragment {
    Button login;
    ImageButton backimage;
    EditText uname,upassword;
    ProgressDialog pd;

    public printticket() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_printticket, container, false);
        login=(Button)view.findViewById(R.id.login);
        backimage=(ImageButton)view.findViewById(R.id.backimage);
        uname=(EditText)view.findViewById(R.id.username);
        upassword=(EditText)view.findViewById(R.id.password);
        pd = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);





        backimage.setOnClickListener(view1 -> Objects.requireNonNull(getActivity()).finish());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(uname.getText().toString())&&!TextUtils.isEmpty(upassword.getText().toString())){
                    pd.setMessage("Login...");
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);

                    AsyncCallWS task=new AsyncCallWS();
                    task.execute();
                }else {
                    Toast.makeText(getActivity(), "Invalid username and password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }



    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        String final_result = null ;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("", "doInBackground");
            String result=get_login();
            if (result != null){
                final_result=result;

            }

            return null;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
            if (final_result.equals("null")||final_result.equals(null)||final_result.equals("")){
                Toast.makeText(getActivity(), "Invalid Correct Email and Password", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }else {
                MySharedPref.saveData(Objects.requireNonNull(getActivity()),"idadmin",final_result);
                pd.dismiss();
                startActivity(new Intent(getActivity(), scand_print_ticket.class));
                getActivity().finish();

            }

        }
    }

    public String get_login() {
        String SOAP_ACTION = serviceurl.URL2+"/login/";
        String METHOD_NAME = "login";
        String NAMESPACE = serviceurl.URL2+"/";
        String URL = serviceurl.URL2;
        String response = "";
        // System.out.println(""+SOAP_ACTION);
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("email", uname.getText().toString());
            Request.addProperty("password", upassword.getText().toString());
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug=true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------result-------- "+ soapEnvelope.getResponse());
            response=soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + response);
        } catch (Exception ex) {
            System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }

}
