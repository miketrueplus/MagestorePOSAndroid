package com.magestore.app.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.model.staff.StaffPermisson;
import com.magestore.app.pos.R;

import java.util.List;

/**
 * Created by Johan on 7/4/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StaffPermissonAdapter extends BaseAdapter {
    Context context;
    List<StaffPermisson> listPermisson;

    public StaffPermissonAdapter(Context context, List<StaffPermisson> listPermisson) {
        this.context = context;
        this.listPermisson = listPermisson;
    }

    public void setListPermisson(List<StaffPermisson> listPermisson) {
        this.listPermisson = listPermisson;
    }

    @Override
    public int getCount() {
        return listPermisson.size();
    }

    @Override
    public StaffPermisson getItem(int i) {
        return listPermisson.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            convertView = lInflater.inflate(R.layout.card_staff_permisson_content, null);

            viewHolder = new StaffPermissonAdapter.ViewHolder();
            viewHolder.holdView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StaffPermissonAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.setItem(getItem(position));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_staff_name;
        TextView tv_permisson;

        public void holdView(View view) {
            //find view
            tv_staff_name = (TextView) view.findViewById(R.id.tv_staff_name);
            tv_permisson = (TextView) view.findViewById(R.id.tv_permisson);
        }

        public void setItem(StaffPermisson item) {
            tv_staff_name.setText(item.getDisplayName());
            tv_permisson.setText(item.getRole());
        }
    }
}
