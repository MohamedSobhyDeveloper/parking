package com.park.optech.parking.route;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.park.optech.parking.route.direction;
import com.park.optech.parking.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mohamed on 27/11/2017.
 */

public class drawroute {
    Context context;
    GoogleMap mMap;
    TextView tv1, tv2, tv3;
    public String distance, time;
    ArrayList<LatLng> markerPoints;
    int i =0;

    LatLng currentlatlng;
    Marker marker;
    public drawroute(Context context, GoogleMap mMap) {
        this.context = context;
        this.mMap = mMap;
        this.marker=marker;

    }

    public void setRoutePath(LatLng origin, LatLng dest, TextView tvDuration, TextView tvDuration_Ext, TextView tvDistance) {

        tv1 = tvDuration;
        tv2 = tvDuration_Ext;
        tv3 = tvDistance;
        String url = getDirectionsUrl(origin, dest);
        drawroute.DownloadTask downloadTask = new drawroute.DownloadTask();
        downloadTask.execute(url);
    }

    public void setRoutePath(LatLng origin, LatLng dest) {

        currentlatlng=origin;
        String url = getDirectionsUrl(origin, dest);
        drawroute.DownloadTask downloadTask = new drawroute.DownloadTask();
        downloadTask.execute(url);
    }

    public String getDirectionsUrl(LatLng origin, LatLng dest) {
        if (origin != null) {
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            String sensor = "sensor=false";
            String mode = "mode=driving";
            String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
            return url;
        } else {
            return "";
        }
    }


    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exdownloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            String placeid = null;

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                JSONObject json_place = new JSONObject(data);
                JSONArray placedetails = new JSONArray();
                placedetails.put(json_place.get("geocoded_waypoints"));

                for (int i = 0; i < placedetails.length(); i++) {

                    JSONArray path = placedetails.getJSONArray(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.length(); j++) {
                        JSONObject ponit = path.getJSONObject(j);

                        if (j == 0) {    // Get distance from the list
                            placeid = (String) ponit.get("place_id");

                            continue;
                        } else if (j == 1) { // Get duration from the list
                            String duration = (String) ponit.get("place_id");

                            continue;
                        }
                    }
                }

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                direction parser = new direction();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;

        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    markerPoints = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) {    // Get distance from the list
                            distance = (String) point.get("distance");


//                            setDistance(distance);
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            time = (String) point.get("duration");
//                            System.out.println("********time is *********");
//                            System.out.println(time);
//                            setTime(time);
                            continue;
                        }

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        markerPoints.add(position);

//                        System.out.println("***** marker points *******");
//                        System.out.println(markerPoints);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(markerPoints);
                    lineOptions.width(8);
                    lineOptions.color(context.getResources().getColor(R.color.colorAccent1));


                }

            }


            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null)
                mMap.addPolyline(lineOptions);
            System.out.println("***** marker points *******");
            System.out.println(markerPoints);
//            animateMarker(marker,currentlatlng,true,markerPoints);

            setMarkerPoints(markerPoints);
        }

    }


    public void setDistance(String distance) {
        this.distance = distance;
        tv3.setText(distance);
    }

    public void setTime(String time) {
        this.time = time;
//        System.out.println("*********set time is *********");
//        System.out.println(time);
        tv1.setText(time.split(" ")[0]);
        tv2.setText(time.split(" ")[1]);
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
//        System.out.println("*********get  time is *********");
//        System.out.println(time);
        return time;
    }

    public void setMarkerPoints(ArrayList<LatLng> markerPoints) {
        this.markerPoints = markerPoints;
    }

    public ArrayList<LatLng> getMarkerPoints() {
        return markerPoints;
    }
}
