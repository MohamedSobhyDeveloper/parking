package com.park.optech.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park.optech.parking.R;
import com.park.optech.parking.model.user_invitation;
import com.park.optech.parking.model.user_park;

import java.util.ArrayList;

/**
 * Created by Mohamed on 25/03/2018.
 */

public class pnameadapter extends BaseAdapter {
    private Context mContext;
    ArrayList<user_park> list;
    public pnameadapter(Context c, ArrayList<user_park> list) {
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
            grid = inflater.inflate(R.layout.park_row, parent, false);

        } else {
            grid = (View) convertView;
        }

        TextView name = (TextView) grid.findViewById(R.id.parkname);
        name.setText(list.get(position).getName());



        return grid;
    }
}
