package com.magestore.app.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Adapter đơn giản với 1 list ít dữ liệu, hiển thị cho spinner hoặc ListView ngắn
 * Created by Mike on 1/15/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SimpleListAdapter<TModel> extends ArrayAdapter<TModel> {
    // ID Layout của rowview
    int mRowLayout;
    // Danh sách của view
    private List<TModel> mList;

    public SimpleListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SimpleListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SimpleListAdapter(Context context, int resource, TModel[] objects) {
        super(context, resource, objects);
    }

    public SimpleListAdapter(Context context, int resource, int textViewResourceId, TModel[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SimpleListAdapter(Context context, int resource, List<TModel> objects) {
        super(context, resource, objects);
        mList = objects;
    }

    public SimpleListAdapter(Context context, int resource, int textViewResourceId, List<TModel> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void setRowLayout(int rowLayout) {
        mRowLayout = rowLayout;
    }

    @Override
    public View getView(final int position, final View convertView,
                        final ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mRowLayout, null);
        }

        // xác định item trong danh sách
        TModel item = mList.get(position);

        // return view
        return v;
    }

    protected void bindItem(View view, TModel item, int position) {

    }
}
