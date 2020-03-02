package com.park.optech.parking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.park.optech.parking.another.Utility;
import com.park.optech.parking.model.user_profile;
import com.park.optech.parking.restful.ApiMethods;
import com.park.optech.parking.service.AndroidMultiPartEntity;
import com.park.optech.parking.service.Config;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

public class profile extends Fragment {


    public profile() {
        // Required empty public constructor
    }


    Context context;
    private int REQUEST_CAMERA = 1010, SELECT_FILE = 1010;
    CircleImageView circleimageview;

    private Button cancel;
    private Button save_btn1;

    private String userChoosenTask;
    EditText fname,email,brand,modell,phone,plate;
    EditText fname1,email1,brand1,modell1,n11,n21,n31,n41,c11,c21,c31,phone1;

    String nameimage="";
    private final int requestCode = 20;
    File file;
    Uri uri;
    ByteArrayBody bab;
    Intent CamIntent, GalIntent, CropIntent;
    String final_imagepath = null, final_id = null;
    user_profile data = new user_profile();

    private ProgressBar progressBar;
    ProgressDialog pd;


    static final int PICK_IMAGE_REQUEST = 1;
    String filePath;
    long totalSize = 0;
    String str="ب";
    String base64;
    RelativeLayout r1,r2;
    ImageButton imageButton;
    FloatingActionButton fab;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final_id = MySharedPref.getData(getActivity(), "id", null);
        String car = MySharedPref.getData(getActivity(), "car", null);


         fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
            }
        });

        r1=(RelativeLayout)view.findViewById(R.id.showinvitation);
        r2=(RelativeLayout)view.findViewById(R.id.showinvitation1);

          if (car.equals("null")||car.equals("")){
              r2.setVisibility(View.VISIBLE);
              r1.setVisibility(View.GONE);
          }else {
              r1.setVisibility(View.VISIBLE);
              r2.setVisibility(View.GONE);

          }

        pd = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pd.setMessage("Loading Profile...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);



        //update_profile task=new update_profile();
        //task.execute();


        byte[] data = str.getBytes(StandardCharsets.UTF_8);
         base64 = Base64.encodeToString(data, Base64.DEFAULT);

        GetProfile task = new GetProfile();
        task.execute();


        context = getActivity().getBaseContext();
        cancel=(Button)view.findViewById(R.id.cancel);
        fname=(EditText)view.findViewById(R.id.firstname);
        email=(EditText)view.findViewById(R.id.email);
        brand=(EditText)view.findViewById(R.id.brandname);
        modell=(EditText)view.findViewById(R.id.modelname);
        plate=(EditText)view.findViewById(R.id.platenumber);


        phone=(EditText)view.findViewById(R.id.phoneNumber);
        circleimageview = (CircleImageView) view.findViewById(R.id.circleImageView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.b), PorterDuff.Mode.SRC_IN);
        //new njdhd
        save_btn1 = (Button) view.findViewById(R.id.save_btn1);
        fname1=(EditText)view.findViewById(R.id.firstname2);
        email1=(EditText)view.findViewById(R.id.email1);
        brand1=(EditText)view.findViewById(R.id.brandname1);
        modell1=(EditText)view.findViewById(R.id.modelname1);
        n11=(EditText)view.findViewById(R.id.n11);
        n21=(EditText)view.findViewById(R.id.n21);
        n31=(EditText)view.findViewById(R.id.n31);
        n41=(EditText)view.findViewById(R.id.n41);
        c11=(EditText)view.findViewById(R.id.c11);
        c21=(EditText)view.findViewById(R.id.c21);
        c31=(EditText)view.findViewById(R.id.c31);
        phone1=(EditText)view.findViewById(R.id.phoneNumber1);
        imageButton=(ImageButton) view.findViewById(R.id.backimage);


        //end

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);

            }
        });



        save_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(fname1.getText().toString())
                        &&!TextUtils.isEmpty(email1.getText().toString())
                        &&!TextUtils.isEmpty(phone1.getText().toString())


                        ){
                    //  update_profile task=new update_profile();
                    // task.execute();
                    ApiMethods.loginUser(getContext(), "Update_Profile",final_id ,fname1.getText().toString(),"",email1.getText().toString(),phone1.getText().toString(),c11.getText().toString(),c21.getText().toString(),c31.getText().toString(),n11.getText().toString(),n21.getText().toString(),n31.getText().toString(),n41.getText().toString(),brand1.getText().toString(),modell1.getText().toString());


                }else if (TextUtils.isEmpty(fname1.getText().toString())) {
                    fname1.setError("enter name");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();


                }else if (TextUtils.isEmpty(email1.getText().toString())) {
                    email1.setError("enter email");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();


                }else if (!TextUtils.isEmpty(fname1.getText().toString())&&TextUtils.isEmpty(email1.getText().toString())) {
                    email1.setError("enter email");
                    Snackbar.make(view, "Please Complete Data", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();


                }


            }
        });
        fname1.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                fname1.setError(null);

            }
        });
        email1.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {

                email1.setError(null);

            }
        });



        circleimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageBrowse();

            }
        });


        //mohamed sobhy

        c11.addTextChangedListener(new TextWatcher()

        {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (s.length() == 1)
                {
                    c21.requestFocus();
                    c21.setEnabled(true);
                    c21.setText("");
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
        c21.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    c31.requestFocus();
                    c31.setEnabled(true);
                    c31.setText("");
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
        c31.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n11.requestFocus();
                    n11.setEnabled(true);
                    n11.setText("");
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
        n11.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n21.requestFocus();
                    n21.setEnabled(true);
                    n21.setText("");
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
        n21.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n31.requestFocus();
                    n31.setEnabled(true);
                    n31.setText("");
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
        n31.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n41.requestFocus();
                    n41.setEnabled(true);
                    n41.setText("");


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
        n41.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() == 1)
                {
                    n11.setEnabled(false);
                    n21.setEnabled(false);
                    n31.setEnabled(false);
                    n41.setEnabled(false);
                    c21.setEnabled(false);
                    c31.setEnabled(false);
                    phone1.requestFocus();




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


        return view;
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                Uri picUri = data.getData();

                filePath = getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);

                circleimageview.setImageURI(picUri);
                UploadFileToServer task=new UploadFileToServer();
                task.execute();

            }

        }



    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    //rkjeh
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        String final_result=null;
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
        }

        @Override
        protected String doInBackground(Void... params) {
            String x=uploadFile();

            System.out.println("moooo"+x);
            if (x!=null){
                final_result=x;
            }


            // return uploadFile();
            return null;
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);


                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("pk", new StringBody(final_id));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                System.out.println("errr"+e.toString());
            } catch (IOException e) {
                responseString = e.toString();
                System.out.println("errr"+e.toString());

            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            if (final_result.equals("after")){
                Toast.makeText(getActivity(), "profile photo updated successfuly", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

            // showing the server response in an alert dialog
            // showAlert(result);


            super.onPostExecute(result);
        }

    }




    //update profile




    //get profile
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
                        data.setFirst_name(obj.get("first_name")+"");
                        data.setLast_name(obj.get("last_name")+"");
                        data.setEmail(obj.get("email")+"");
                        data.setUsername(obj.get("username")+"");
                        data.setPhoto(obj.get("photo")+"");
                        data.setCharacter1(obj.get("character1")+"");
                        data.setCharacter2(obj.get("character2")+"");
                        data.setCharacter3(obj.get("character3")+"");
                        data.setNumber1(obj.get("number1")+"");
                        data.setNumber2(obj.get("number2")+"");
                        data.setNumber3(obj.get("number3")+"");
                        data.setNumber4(obj.get("number4")+"");
                        data.setBrand(obj.get("brand")+"");
                        data.setModel(obj.get("model")+"");
                        data.setPhone(obj.get("phone")+"");


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

            System.out.println("mo"+data.getEmail());
            System.out.println("mo"+data.getPhone());
            if (final_result != null) {
                pd.dismiss();

                if (data.getLast_name().equals("null")) {
                    email.setText("");
                    brand.setText("");
                    modell.setText("");
                    phone.setText(data.getPhone());
                    phone1.setText(data.getPhone());

                    fname.setText("");

                    if (data.getPhoto().equals("null")) {
                        Picasso.get().load(R.drawable.hh).into(circleimageview);


                    } else {
                        Picasso.get().load(data.getPhoto()).into(circleimageview);



                    }
                } else {
                    email.setText(data.getEmail());
                    brand.setText(data.getBrand());
                    modell.setText(data.getModel());
                    phone.setText(data.getPhone());
                    fname.setText(data.getFirst_name());
                    plate.setText(data.getNumber4()+" "+data.getNumber3()+" "+data.getNumber2()+" "+data.getNumber1()+"--"+data.getCharacter3()+" "+data.getCharacter2()+" "+data.getCharacter1());
                    //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                    email1.setText(data.getEmail());
                    brand1.setText(data.getBrand());
                    modell1.setText(data.getModel());
                    phone1.setText(data.getPhone());
                    fname1.setText(data.getFirst_name());
                    c11.setText(data.getCharacter1());
                    c21.setText(data.getCharacter2());
                    c31.setText(data.getCharacter3());
                    n11.setText(data.getNumber1());
                    n21.setText(data.getNumber2());
                    n31.setText(data.getNumber3());
                    n41.setText(data.getNumber4());

                    if (data.getPhoto().equals("null")) {
                        Picasso.get().load(R.drawable.hh).into(circleimageview);


                    } else {
                        Picasso.get().load(data.getPhoto()).into(circleimageview);



                    }
                    pd.dismiss();
                }
                //circleimageview.setImageResource(Integer.parseInt(data.getPhoto()));
            }else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                pd.dismiss();
               // startActivity(new Intent(getActivity(),MainActivity.class));

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
            Request.addProperty("user_id", final_id);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- "+ soapEnvelope.getResponse());
            System.out.println("-------id-------- "+ final_id);
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }







}
