package com.park.optech.parking;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.library.PulseView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.park.optech.parking.route.drawroute;
import com.park.optech.parking.sharedpref.MySharedPref;
import com.park.optech.parking.soapapi.serviceurl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import static com.park.optech.parking.mapspark.GeoCodingLocationLatlng.getCurrentLocation;
import static com.park.optech.parking.sharedpref.MySharedPref.saveData;


public class parkingfind extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    MapView mapView;
    double latitudevalue = 29.867486;
    double longitudevalue = 31.315431;
    //LocationManager locationManager;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleApiClient googleApiClient;
    LatLng currentlatlng;
    Marker drivermarker;
    LatLng destLocation = null;
    String final_id ="";
    ArrayList<String> drivergate = null;
    ArrayList<String> driverTitle = null;
    ArrayList<String> driveravlspace = null;
    ArrayList<String> driverttlspace = null;
    ArrayList<String> driverdistance = null;
    ArrayList<String> drivertariff1 = null;
    RelativeLayout ticket_popup,show;
    Button cancel_booking ;
    Button book_btn ;
    TextView parkinglocation ;
    TextView avl_space ;
    TextView ttl_space ;
    TextView gatepark ;
    TextView tariff ;
    TextView close ;

    Context context;
    LatLng final_currentlatlong=null;
    Marker user_marker;
    PulseView p;





    public parkingfind() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parkingfind, container, false);
        context=getActivity();
        final_id = MySharedPref.getData(context, "id", null);
        System.out.println("ff"+final_id);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();


        //current location



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ticket_popup = view.findViewById(R.id.ticket_popup);
        show = view.findViewById(R.id.showlist);
         book_btn = view.findViewById(R.id.book_btn);
         parkinglocation = view.findViewById(R.id.parking_header);
        avl_space = view.findViewById(R.id.avl_space);
         ttl_space = view.findViewById(R.id.ttl_space);
         tariff = view.findViewById(R.id.tariff);
        gatepark = view.findViewById(R.id.gatenumber);
        p=(PulseView)view.findViewById(R.id.pv);
        close=(TextView)view.findViewById(R.id.txtclose);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket_popup.setVisibility(View.GONE);
            }
        });

        p.setVisibility(View.VISIBLE);
        p.startPulse();


        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);



            //end

        }


    }
    @Override
    public void onStart() {
        googleApiClient.connect();

        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
       // mMap.getUiSettings().setZoomControlsEnabled(true);
        //LatLng latLng=new LatLng(latitudevalue,longitudevalue);
       // mMap.addMarker(new MarkerOptions().position(latLng).title("parking").icon(BitmapDescriptorFactory.fromResource(R.drawable.car1)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

    }

    //Function to move the map+
    private void moveMap() {
        //Creating a LatLng Object to store Coordinates

//        System.out.println("************ current location*********");
//        System.err.println(getLocation);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location getLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (getLocation != null) {
            currentlatlng = new LatLng(getLocation.getLatitude(),getLocation.getLongitude());
            final_currentlatlong=currentlatlng;

//            currentlatlng = new LatLng(25.2272,55.2888);
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

            mMap.addMarker(new MarkerOptions()
                    .position(currentlatlng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .title("Current Location")); //Adding a title


            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlatlng));
            mMap.getUiSettings().setZoomControlsEnabled(true);


            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentlatlng).tilt(45).zoom((float) 12.5).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            AsyncCallWS task = new AsyncCallWS();
            task.execute();

//            getparkingLocation();

//        getDriverLocation(latitude, longitude);
        }else {
            p.finishPulse();
            p.setVisibility(View.GONE);
            Toast.makeText(context, "Please Open Your Gps and try again", Toast.LENGTH_SHORT).show();

        }

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                moveMap();

            }
        },1000);




    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String result = marker.getTitle();
        if (result.equals("Current Location")) {
            Toast.makeText(context, "Current Location", Toast.LENGTH_SHORT).show();

        }else {
//        System.out.println("----------- "+result);
            String arr[] = result.split(",");
//        System.out.println("------------size------------- "+arr.length);
            final String gate1 = arr[0];
            final String ttl_space1 = arr[1];
            final String avl_space1 = arr[2];
            final String tariff1 = arr[3];
            final String locationtitle = arr[4];
            destLocation = marker.getPosition();
            System.out.println("fnjnfjkndjkb  " + destLocation);
            ticket_popup.setVisibility(View.VISIBLE);
            parkinglocation.setText(locationtitle);
            avl_space.setText(avl_space1);
            ttl_space.setText(ttl_space1);
            tariff.setText(tariff1);
            gatepark.setText(gate1);

        }

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData(getActivity(), "parklocation", destLocation.toString());
                Intent i = new Intent(new Intent(getActivity(), parkdirection.class));
                i.putExtra("parklocation", destLocation);
                MySharedPref.saveData(getActivity(),"button","1");

                startActivity(i);
            }
        });



        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    //start call services
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        ArrayList<LatLng> driverLAtlng = null;
        JSONArray arr = null;
        String final_result=null;

        @Override
        protected Void doInBackground(Void... voids) {
//            System.out.println("-------------AsyncCallWS--------------- ");
            String result = getparking();
            if (result != null) {
                final_result=result;

//         Toast.makeText(MainActivity.this,"getparking",Toast.LENGTH_LONG).show();
                try {
                    arr = new JSONArray(result);
                    arr = arr.getJSONArray(0);
                   System.out.println("----------array Siz---------- "+arr.length());

                } catch (JSONException e) {
                    e.printStackTrace();
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
               // p.finishPulse();
             //   p.setVisibility(View.GONE);
                if (final_result.equals("") ||final_result.equals(null) || final_result.equals("[[]]")) {
                   // show.setVisibility(View.VISIBLE);
                } else {
                    try {
                        driverLAtlng = new ArrayList<>();
                        drivergate = new ArrayList<>();
                        driverTitle = new ArrayList<>();
                        driverttlspace = new ArrayList<>();
                        driverdistance = new ArrayList<>();
                        driveravlspace = new ArrayList<>();
                        drivertariff1 = new ArrayList<>();
                        for (int i = 0; i <= arr.length() - 1; i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            System.out.println("--------" + i + "-------- " + obj);
                            driverLAtlng.add(new LatLng(Double.parseDouble(obj.getString("lat")), Double.parseDouble(obj.getString("lng"))));
                            drivergate.add(obj.getString("gate"));
                            driveravlspace.add(obj.getString("free_spots"));
                            driverttlspace.add(obj.getString("total_spots"));
                            drivertariff1.add(obj.getString("tariff"));
                            driverTitle.add(obj.getString("company"));

                        }
//                System.out.println(driverLAtlng + "-------------driverLAtlng.size()------------- " + driverLAtlng.size());
                        updateDriverMarker(driverLAtlng);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
               // show.setVisibility(View.VISIBLE);
                //p.finishPulse();
               // p.setVisibility(View.GONE);
            }
        }
    }

    public String getparking() {
        String SOAP_ACTION = serviceurl.URL + "/Find_Gates/";
        String METHOD_NAME = "Find_Gates";
        String NAMESPACE = serviceurl.URL + "/";
        String URL = serviceurl.URL;
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("id", final_id);
            Request.addProperty("latitude", "" + currentlatlng.latitude);
            Request.addProperty("longitude", "" + currentlatlng.longitude);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.debug = true;
            transport.call(SOAP_ACTION, soapEnvelope);
            System.out.println("-------Response-------- "+ soapEnvelope.getResponse());
            System.out.println("-------id-------- "+final_id);
            response = soapEnvelope.getResponse().toString();
            Log.i("", "Result Verification: " + soapEnvelope.getResponse());
        } catch (Exception ex) {
//        System.out.println("---------Exe------- "+ex);
            ex.printStackTrace();
        }
        return response;
    }

    private void updateDriverMarker(ArrayList<LatLng> driverLAtlng) {

        if (drivermarker != null) {
            drivermarker.remove();
        }
        for (int i = 0; i < driverLAtlng.size(); i++) {
            String locationtitle = "";
            String ttl_space = "";
            String avl_space = "";
            String tarrif = "";
            String gate="";
             gate=drivergate.get(i);
            locationtitle = driverTitle.get(i);
            ttl_space = driverttlspace.get(i);
            avl_space = driveravlspace.get(i);
            tarrif = drivertariff1.get(i);
//            String locationtitle =getAddressFromLocation(driverLAtlng.get(i).latitude,driverLAtlng.get(i).longitude,getApplicationContext());

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.parking);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            drivermarker = mMap.addMarker(new MarkerOptions().
                    position(driverLAtlng.get(i)).
                    title(gate + "," + ttl_space + "," + avl_space + "," + tarrif+","+locationtitle).
                    icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentlatlng).tilt(45).bearing(45).zoom((float) 12.5).build();
             p.finishPulse();
            p.setVisibility(View.GONE);

           // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }






    private class UpdateMyLoction extends AsyncTask<Void, Void, Void> {
        @TargetApi(Build.VERSION_CODES.FROYO)
        @SuppressWarnings({})
        protected Void doInBackground(Void... params) {
            return null;
        }

        protected void onPostExecute(Void paramVoid) {

            LatLng updated_latlong = getCurrentLocation(mMap, googleApiClient,getContext());
//            System.out.println("------------updated_latlong------------- "+updated_latlong);

            if (updated_latlong != null) {
                mMap.clear();
                final_currentlatlong = updated_latlong;

                //   mMap.moveCamera(CameraUpdateFactory.newLatLng(final_currentlatlong));


                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                user_marker=mMap.addMarker(new MarkerOptions()
                        .position(updated_latlong) //setting position
                        .draggable(true) //Making the marker draggable
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title("Current Location"));
                Toast.makeText(context, "mohamed", Toast.LENGTH_SHORT).show();






            }
        }

        protected void onPreExecute() {

        }
    }



}

