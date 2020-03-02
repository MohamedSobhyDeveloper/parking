package com.park.optech.parking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park.optech.parking.R;
import com.park.optech.parking.model.user_invitation;

import java.util.ArrayList;

/**
 * Created by Mohamed on 06/03/2018.
 */

public class inviteadapter extends BaseAdapter {
    private Context mContext;
    ArrayList<user_invitation> list;
    public inviteadapter(Context c, ArrayList<user_invitation> list) {
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
            grid = inflater.inflate(R.layout.ivitation_list_item, parent, false);

        } else {
            grid = (View) convertView;
        }
        TextView name = (TextView) grid.findViewById(R.id.dname);
        TextView plate = (TextView) grid.findViewById(R.id.platenumber);
        TextView from = (TextView) grid.findViewById(R.id.from_date);
        TextView to = (TextView) grid.findViewById(R.id.to_date);
        TextView s = (TextView) grid.findViewById(R.id.status);
        TextView car=(TextView)grid.findViewById(R.id.cartype);


        name.setText(list.get(position).getName());
        plate.setText(list.get(position).getPlate_No());
        car.setText(list.get(position).getCompany());
        from.setText(list.get(position).getStart_date());
        to.setText(list.get(position).getEnd_date());
        s.setText(list.get(position).getApproved());

        return grid;
    }
}
