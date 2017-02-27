package com.magestore.app.lib.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.magestore.app.lib.R;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.item.ModelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 2/21/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class GenericAdapter<TModelView extends ModelView> extends BaseAdapter {
    HashMap<Model, TModelView> mMapModelView;
    List<TModelView> mListModelView;
    Context mContext;
    int mintMainLayoutID = -1;

    /**
     * Khởi tạo với context
     * @param context
     */
    public GenericAdapter(Context context) {
        mContext = context;
    }

    /**
     * Khởi tạo với context và layout id
     * @param context
     */
    public GenericAdapter(Context context, int layoutID) {
        mContext = context;
        mintMainLayoutID = layoutID;
    }

    /**
     * Xóa danh sách trong view
     */
    public void clearList() {
        if (mListModelView != null) mListModelView.clear();
    }

    /**
     * Gán 1 list model vào adapter
     * @param models
     */
    public void bindList(List<Model> models) {
        // nếu truyền null, clear list luôn
        if (models == null) {
            clearList();
            return;
        }
        mListModelView = new ArrayList<TModelView>(models.size());
        for (int i = 0; i < mListModelView.size(); i++) {
            mListModelView.set(i, null);
        }
//        TModelView modelView = new TModelView();
        notifyDataSetChanged();
    }

    public abstract TModelView generateModelView(Model model);

    /**
     * Trả về số phần tử
     * @return
     */
    @Override
    public int getCount() {
        return mMapModelView.size();
    }

    /**
     * Trả về model view theo thứ tự tương ứng
     * @param i
     * @return
     */
    @Override
    public TModelView getItem(int i) {
        return mListModelView.get(i);
    }

    /**
     * Trả về ID model view theo thứ tự tương ứng
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ModelView modelView = mListModelView.get(position);

        // load layout cho view nếu chưa được load
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater)  mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Nếu model view không có layout ID thì sử dụng layout ID chung
            if (modelView.getMainLayoutID() > 0)
                row = inflater.inflate(modelView.getMainLayoutID(), parent, false);
            else if (mintMainLayoutID > 0)
                row = inflater.inflate(mintMainLayoutID, parent, false);
            else
                row = inflater.inflate(R.layout.layout_modelview_default_item_main, parent, false);
            modelView.holdView(row);
        }

        // áp dữ liệu từ model vào view nếu load được layout
        if (row == null) return row;
        modelView.bindModel();
        return row;
    }
}
