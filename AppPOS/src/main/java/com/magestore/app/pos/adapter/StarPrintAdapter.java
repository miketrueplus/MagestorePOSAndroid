package com.magestore.app.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magestore.app.pos.R;
import com.starmicronics.stario.PortInfo;

import java.util.ArrayList;

/**
 * Created by Johan on 7/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrintAdapter extends BaseAdapter {
    Context context;
    ArrayList<PortInfo> listPort;

    public StarPrintAdapter(Context context, ArrayList<PortInfo> listPort) {
        this.context = context;
        this.listPort = listPort;
    }

    @Override
    public int getCount() {
        return listPort.size();
    }

    @Override
    public Object getItem(int i) {
        return listPort.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.simple_textview_row, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.list_content1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String portName;
        PortInfo discovery = listPort.get(i);
        portName = discovery.getPortName();

        if (discovery.getMacAddress().equals("") == false) {
            portName += "\n - " + discovery.getMacAddress();
            if (discovery.getModelName().equals("") == false) {
                portName += "\n - " + discovery.getModelName();
            }
        }
        viewHolder.textView.setText(portName);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
