package com.magestore.app.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.pos.R;

import java.util.List;

/**
 * Created by baonguyen on 26/04/2017.
 */

public class CategoryAdapter extends BaseAdapter {
    List<Category> datas;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            convertView = lInflater.inflate(R.layout.item_list_category, null);

            viewHolder = new ViewHolder();
            viewHolder.holdView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setTextColor(Color.BLACK);
        viewHolder.setItem(getItem(position));
        return convertView;
    }

    public void setDatas(List<Category> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Category getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            convertView = lInflater.inflate(R.layout.item_list_category, null);

            viewHolder = new ViewHolder();
            viewHolder.holdView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setTextColor(Color.BLACK);
        viewHolder.setItem(getItem(position));
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        ImageView imgDropdownId;

        public void holdView(View view) {
            //find view
            tvTitle = (TextView) view.findViewById(R.id.text_category);
            imgDropdownId = (ImageView) view.findViewById(R.id.icon_category);
        }

        public void setItem(Category category) {
            switch (category.getLevel()) {
                case 2:
                    tvTitle.setPadding(10, 0, 0, 0);
                    imgDropdownId.setVisibility(category.getChildren().size() == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 3:
                    tvTitle.setPadding(40, 0, 0, 0);
                    imgDropdownId.setVisibility(category.getChildren().size() == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 4:
                    tvTitle.setPadding(70, 0, 0, 0);
                    imgDropdownId.setVisibility(View.GONE);
                    break;
                default:
                    tvTitle.setPadding(0, 0, 0, 0);
                    imgDropdownId.setVisibility(View.VISIBLE);
            }
            tvTitle.setText(category.getName());
        }

    }

}
