package com.park.optech.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.park.optech.parking.R;
import com.park.optech.parking.model.User_history;

import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<User_history> list;

    public ListAdapter(Context c, ArrayList<User_history> list) {
        mContext = c;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            //grid = inflater.inflate(R.layout.grid_single, null);
            //grid = inflater.inflate(R.layout.grid_single, parent, false);
            grid = inflater.inflate(R.layout.history_list_item, parent, false);

        } else {
            grid = (View) convertView;
        }
        //TextView date = (TextView) grid.findViewById(R.id.datetv);
        //TextView from = (TextView) grid.findViewById(R.id.fromtv);
        TextView location = (TextView) grid.findViewById(R.id.locationtv);
       // TextView duration = (TextView) grid.findViewById(R.id.durationtv);
        TextView debit = (TextView) grid.findViewById(R.id.debittv);
        TextView gate = (TextView) grid.findViewById(R.id.gate);


        // date.setText(list.get(position).getTransactionDate());
      //  from.setText(list.get(position).getEntryTIME()+" to "+list.get(position).getExitTIME());
        location.setText(list.get(position).getParkingName());
      //  duration.setText(list.get(position).getDuration());
        debit.setText(list.get(position).getFeesCollected()+" EGP ");
        gate.setText(list.get(position).getGate());


        return grid;
    }

}