package com.park.optech.parking.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.park.optech.parking.R;
import com.park.optech.parking.printticket.ticketactivity;
import com.park.optech.parking.route.drawroute;
import com.park.optech.parking.sharedpref.MySharedPref;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.park.optech.parking.mapspark.GeoCodingLocationLatlng.getCurrentLocation;

public class parkdirection extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    double longitude, latitude;
    LatLng currentlatlng,destinationLatlng;
    Marker user_marker, parking_marker;
    BitmapDrawable bitmapdraw;
    Bitmap smallMarker,b;
    String carsearch;
    LatLng final_currentlatlong=null;
    ArrayList<LatLng> markerPoints;
    int i =0;
    RelativeLayout park;
    Button cancel_booking ;
    Button book_btn ;
    Button ticket ;
    String x="";
    ImageButton backimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkdirection);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        destinationLatlng =getIntent().getExtras().getParcelable("parklocation");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        park=(RelativeLayout)findViewById(R.id.parkpopup);
        cancel_booking =findViewById(R.id.cancel_book_btn);
        book_btn =findViewById(R.id.book_btn);
        ticket =findViewById(R.id.ticketbtn);
        x  = MySharedPref.getData(this, "button", null);
        backimage=(ImageButton)findViewById(R.id.backimage);


        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parkdirection.this, MainActivity.class));
            }
        });



        cancel_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                park.setVisibility(View.GONE);
                if (x.equals("0")) {
                    ticket.setVisibility(View.GONE);
                }else {
                    ticket.setVisibility(View.VISIBLE);
                }
                final Timer timer = new Timer();
                timer.schedule( new TimerTask()
                {
                    public void run() {

                        if(final_currentlatlong!=null){
//                            System.out.println("____________Timer____________");
                            UpdateMyLoction task=new UpdateMyLoction();
                            task.execute();
                        }else {
//                            System.out.println("____________Timer canceled____________");
                            try {
                                timer.cancel();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }, 0, 5000);
            }
        });
        book_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + currentlatlng.latitude + "," +currentlatlng.longitude + "&daddr=" + destinationLatlng.latitude + "," + destinationLatlng.longitude));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);

    }
});
ticket.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(new Intent(parkdirection.this, ticketactivity.class));
        startActivity(i);

    }
});


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }


    private void moveMap() {

        LatLng getLocation = getCurrentLocation(mMap,googleApiClient,getApplicationContext());
//        System.out.println("************ current location*********");
//        System.err.println(getLocation);
        if (getLocation!=null) {
            currentlatlng =getLocation;
            final_currentlatlong=currentlatlng;
//         currentlatlng = new LatLng(25.2272,55.2888);
            Location loc1 = new Location(LocationManager.GPS_PROVIDER);
            loc1.setLatitude(currentlatlng.latitude);
            loc1.setLongitude(currentlatlng.longitude);

            Location loc2 = new Location(LocationManager.GPS_PROVIDER);
            loc2.setLatitude(destinationLatlng.latitude);
            loc2.setLongitude(destinationLatlng.longitude);

            float bearing = loc1.bearingTo(loc2);

            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car);
            b = bitmapdraw.getBitmap();
            smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            mMap.getUiSettings().setZoomControlsEnabled(true);


            user_marker=  mMap.addMarker(new MarkerOptions()
                    .position(currentlatlng)
                    .draggable(true).rotation(bearing).flat(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .title("Current Location"));


            if (destinationLatlng!=null) {
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.parking);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

                parking_marker = mMap.addMarker(new MarkerOptions()
                        .position(destinationLatlng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title("parking Location")); //Adding a title
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlatlng));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentlatlng).bearing(bearing).zoom((float) 12.5).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            drawroute routePath = new drawroute(getApplicationContext(),mMap);
            routePath.setRoutePath(currentlatlng,destinationLatlng);

        }

    }
    @Override
    protected void onStart() {
        googleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }



    /////////////

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    moveMap();
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
        return false;
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





    private class UpdateMyLoction extends AsyncTask<Void, Void, Void> {
        @TargetApi(Build.VERSION_CODES.FROYO)
        @SuppressWarnings({})
        protected Void doInBackground(Void... params) {
            return null;
        }

        protected void onPostExecute(Void paramVoid) {

            LatLng updated_latlong = getCurrentLocation(mMap, googleApiClient, getApplicationContext());
//            System.out.println("------------updated_latlong------------- "+updated_latlong);

            if (updated_latlong != null) {
                mMap.clear();
                final_currentlatlong = updated_latlong;

             //   mMap.moveCamera(CameraUpdateFactory.newLatLng(final_currentlatlong));


                drawroute routePath = new drawroute(getApplicationContext(), mMap);
                routePath.setRoutePath(final_currentlatlong, destinationLatlng);

                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                user_marker=mMap.addMarker(new MarkerOptions()
                        .position(updated_latlong) //setting position
                        .draggable(true) //Making the marker draggable
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title("Current Location"));


                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.parking);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                parking_marker=mMap.addMarker(new MarkerOptions()
                        .position(destinationLatlng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title("parking Location"));

            }
        }

        protected void onPreExecute() {

        }
    }

}
