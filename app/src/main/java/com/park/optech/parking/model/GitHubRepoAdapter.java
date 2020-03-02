package com.park.optech.parking.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.park.optech.parking.R;
import com.park.optech.parking.model.clientrepo;

import java.util.List;

/**
 * Created by mohamed on 20/11/2017.
 */

public class GitHubRepoAdapter extends ArrayAdapter<clientrepo> {
    private Context context;
    private List<clientrepo> values;

    public GitHubRepoAdapter(Context context, List<clientrepo> values) {
        super(context, R.layout.row, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row, parent, false);
        }

       // TextView textView = (TextView) row.findViewById(R.id.list_item_pagination_text);

        clientrepo item = values.get(position);
        String message = item.getBalance();
       // textView.setText(message);

        return row;
    }
}
