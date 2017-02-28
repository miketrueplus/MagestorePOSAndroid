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

import com.magestore.app.lib.R;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.service.ListService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SimpleModelAdapter<TModel extends Model> extends BaseAdapter {

    private Context mContext;
    private TModel[] resultList;

    // tham chiếu id trên layout
    private int mintLayoutDropDown = -1;
    private int mintText1 = -1;
    private int mintText2 = -1;
    private int mintImage = -1;

    // xác định có sử dụng text1, 2 và image không
    boolean mblnUseTxtView1 = true;
    boolean mblnUseTxtView2 = true;
    boolean mblnUseImage = true;

    public SimpleModelAdapter(Context context, TModel... models) {
        resultList = models;
        mContext = context;
    }

    public void setIDModelViewText1(int resID) {
        mintText1 = resID;
    }

    public void setIDModelViewText2(int resID) {
        mintText2 = resID;
    }

    public void setIDModelViewImage(int resID) {
        mintImage = resID;
    }

    public void setLayoutModelViewContent(int layout) {
        mintLayoutDropDown = layout;
    }

    @Override
    public int getCount() {
        return resultList.length;
    }

    @Override
    public TModel getItem(int index) {
        return resultList[index];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mintLayoutDropDown > 0 ? mintLayoutDropDown : R.layout.layout_modelview_default_item_content, parent, false);
            holder = new ViewHolder();
            holder.holdView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // cập nhật dữ liệu từ model lên view
        holder.setItem(convertView, resultList[position], position);

        // trả lại view
        return convertView;
    }

    public class ViewHolder {
        TextView mTxtItem1 = null;
        TextView mTxtItem2 = null;
        ImageView mImageItem = null;

        public void holdView(View convertView) {
            mImageItem = (ImageView) convertView.findViewById(mintImage > 0 ? mintImage : R.id.id_modelview_default_item_image);
            mTxtItem1 = (TextView) convertView.findViewById(mintText1 > 0 ? mintText1 : R.id.id_modelview_default_item_text1);
            mTxtItem2 = (TextView) convertView.findViewById(mintText2 > 0 ? mintText2 : R.id.id_modelview_default_item_text2);
        }

        public void setItem(View v, Model model, int position) {
            // hiển thị nội dung theo layout mặc định
            if (mTxtItem1 != null && mblnUseTxtView1)
                mTxtItem1.setText(model.getDisplayContent());
            if (mTxtItem2 != null && mblnUseTxtView2)
                mTxtItem2.setText(model.getSubDisplayContent());
            if (mImageItem != null && mblnUseImage) {
                mImageItem.setImageBitmap(model.getDisplayBitmap());
                mImageItem.setVisibility(model.getDisplayBitmap() == null ? View.GONE : View.VISIBLE);
            }
//            /**
//             * Cập nhật thông tin chính
//             */
//            if (getItem(position).getDisplayContent() != null && mintText1 > 0) {
//                ((TextView) convertView.findViewById(mintText1)).setVisibility(View.VISIBLE);
//                ((TextView) convertView.findViewById(mintText1)).setText(getItem(position).getDisplayContent());
//            }
//            else {
//                ((TextView) convertView.findViewById(mintText1)).setVisibility(View.GONE);
//            }
//
//            // cập nhật thông tin phụ
//            if (getItem(position).getSubDisplayContent() != null && mintText2 > 0) {
//                ((TextView) convertView.findViewById(mintText2)).setVisibility(View.VISIBLE);
//                ((TextView) convertView.findViewById(mintText2)).setText(getItem(position).getSubDisplayContent());
//            }
//            else {
//                ((TextView) convertView.findViewById(mintText2)).setVisibility(View.GONE);
//            }
//
//            // Hiển thị ảnh nếu có
//            if (getItem(position).getDisplayBitmap() != null && mintImage > 0) {
//                ((ImageView) convertView.findViewById(mintImage)).setVisibility(View.VISIBLE);
//                ((ImageView) convertView.findViewById(mintImage)).setImageBitmap(getItem(position).getDisplayBitmap());
//            } else {
//                ((ImageView) convertView.findViewById(mintImage)).setVisibility(View.GONE);
//            }
        }
    }
}
