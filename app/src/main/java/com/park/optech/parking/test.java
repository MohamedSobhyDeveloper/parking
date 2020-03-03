package com.park.optech.parking;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.park.optech.parking.sharedpref.MySharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class test extends AppCompatActivity {
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        t=(TextView)findViewById(R.id.text);
        try {
            JSONObject jsonObject=new JSONObject(MySharedPref.getData(test.this,"details",null));
            showdetails(jsonObject.getJSONObject("response"),MySharedPref.getData(test.this,"amount",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showdetails(JSONObject response, String amount) {
        try {
            t.setText(response.getString("id")+" "+"$"+amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
