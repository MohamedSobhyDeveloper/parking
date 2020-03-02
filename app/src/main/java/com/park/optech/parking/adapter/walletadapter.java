package com.park.optech.parking.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.park.optech.parking.R;
import com.park.optech.parking.model.User_history;
import com.park.optech.parking.model.wallet_model;

import java.util.ArrayList;



public class walletadapter extends BaseAdapter {
    private Context mContext;
    ArrayList<wallet_model> list;
    public walletadapter(Context c, ArrayList<wallet_model> list) {
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
            grid = inflater.inflate(R.layout.walletrow, parent, false);

        } else {
            grid = (View) convertView;
        }
        TextView date = (TextView) grid.findViewById(R.id.date);
        TextView amount = (TextView) grid.findViewById(R.id.amount);
        TextView balance = (TextView) grid.findViewById(R.id.balance);
        TextView status = (TextView) grid.findViewById(R.id.status);

        date.setText(list.get(position).getTransaction_Date());
        amount.setText(list.get(position).getAmount());
        balance.setText(list.get(position).getBalance());
        status.setText(list.get(position).getStatus());

        return grid;
    }




}
