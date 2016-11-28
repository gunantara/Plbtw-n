package com.kelc.plbtw_n.plbtw_n;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WELL'S-COMP on 28 Nov 2016.
 */
public class SpinnerDataAdapter extends BaseAdapter{
    private Activity activity;
    private List<String> item;
    private LayoutInflater inflater;

    public SpinnerDataAdapter(Activity activity, List<String> item) {
        this.activity = activity;
        this.item = item;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=inflater.inflate(R.layout.spinner_custom_rows,null);

        TextView category=(TextView)row.findViewById(R.id.spinner_custom_row);
        category.setText(item.get(position));

        return row;
    }
}
