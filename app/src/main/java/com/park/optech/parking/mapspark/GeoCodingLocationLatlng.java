package com.park.optech.parking.mapspark;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by mohamed on 23/11/17.
 */

public class GeoCodingLocationLatlng
{

    //Getting current location
    public static LatLng getCurrentLocation(GoogleMap mMap, GoogleApiClient googleApiClient, Context context) {
        mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            double latitude,longitude;
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            return new LatLng(latitude,longitude);
        }
        else
            return null;
    }



    public  static  String getAddressFromLocation(double latitude, double longitude, Context context)
    {       Geocoder geocoder;
        List<Address> addresses;
        String pickupadd;
        try {

            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address_city = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            pickupadd = address_city.split(",")[0] ;
        } catch (IOException e) {
            e.printStackTrace();
            return  "";
        }
        return pickupadd;
    }

    public static LatLng getLocationFromAddress(String strAddress, Context context){

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1= new LatLng(location.getLatitude(),location.getLongitude());

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
