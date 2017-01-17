package com.magestore.app.pos.adapter;

import android.content.Context;
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
    }

    public SimpleListAdapter(Context context, int resource, int textViewResourceId, List<TModel> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(final int position, final View convertView,
                        final ViewGroup parent) {
//        final View view = this.context.getLayoutInflater().inflate(
//                R.layout.item, null);
        return null;
    }
}
