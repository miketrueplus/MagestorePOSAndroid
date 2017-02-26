package com.magestore.app.lib.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.R;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SearchAutoCompleteAdapter<TModel extends Model> extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<TModel> resultList = new ArrayList<TModel>();
    private ListService<TModel> mService;
    private int mintLayoutDropDown = -1;
    private int mintText1 = -1;
    private int mintText2 = -1;
    private int mintImage = -1;

    public SearchAutoCompleteAdapter(Context context, ListService<TModel> service) {
        mContext = context;
        mService = service;
        mintLayoutDropDown = R.layout.layout_modelview_default_item_content;
        mintText1 = R.id.id_modelview_default_item_text1;
        mintText2 = R.id.id_modelview_default_item_text2;
        mintImage = R.id.id_modelview_default_item_image;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public TModel getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mintLayoutDropDown, parent, false);
        }

        /**
         * Cập nhật thông tin chính
         */
        if (getItem(position).getDisplayContent() != null && mintText1 > 0) {
            ((TextView) convertView.findViewById(mintText1)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(mintText1)).setText(getItem(position).getDisplayContent());
        }
        else {
            ((TextView) convertView.findViewById(mintText1)).setVisibility(View.GONE);
        }

        // cập nhật thông tin phụ
        if (getItem(position).getSubDisplayContent() != null && mintText2 > 0) {
            ((TextView) convertView.findViewById(mintText2)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(mintText2)).setText(getItem(position).getSubDisplayContent());
        }
        else {
            ((TextView) convertView.findViewById(mintText2)).setVisibility(View.GONE);
        }

        // Hiển thị ảnh nếu có
        if (getItem(position).getDisplayBitmap() != null && mintImage > 0) {
            ((ImageView) convertView.findViewById(mintImage)).setVisibility(View.VISIBLE);
            ((ImageView) convertView.findViewById(mintImage)).setImageBitmap(getItem(position).getDisplayBitmap());
        } else {
            ((ImageView) convertView.findViewById(mintImage)).setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<TModel> models = search(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = models;
                    filterResults.count = models.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<TModel>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<TModel> search(Context context, String search) {
        try {
            return mService.retrieve(search, 1, MAX_RESULTS);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<TModel>();
    }
}
