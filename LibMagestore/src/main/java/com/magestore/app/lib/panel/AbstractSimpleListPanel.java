package com.magestore.app.lib.panel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.magestore.app.lib.R;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.MagestoreView;

import java.util.List;

/**
 * Một listview đơn giản
 * Created by Mike on 1/18/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractSimpleListPanel<TModel extends Model>
        extends ListView
        implements MagestoreView<ListController<TModel>> {
    // ID Layout của rowview
    int mListLayout;

    // Task điều khiển danh sách trong list
    protected ListController<TModel> mController;

    // Model chứa data danh sách
    protected List<TModel> mList;

    public AbstractSimpleListPanel(Context context) {
        super(context);
        initLayout();
    }

    public AbstractSimpleListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        mListLayout = a.getResourceId(R.styleable.magestore_view_layout_row, -1);
        a.recycle();
        initLayout();
    }

    public AbstractSimpleListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        mListLayout = a.getResourceId(R.styleable.magestore_view_layout_row, -1);
        a.recycle();
        initLayout();
    }

    @Override
    public void setController(ListController<TModel> controller) {
        mController = controller;
        initLayout();
    }

    @Override
    public ListController<TModel> getController() {
        return mController;
    }

    public void initLayout() {


    }

    public void initModel() {
    }

    public void initValue() {
    }

    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        if (mList != null) {
            this.setAdapter(new AbstractSimpleListPanel<TModel>.SimpleListAdapter(getContext(), mListLayout, mList));
//            mRecycleView.setAdapter(new AbstractListPanel<TModel>.ListRecyclerViewAdapter(mList));
        }
    }

    /**
     * Map mỗi item tương ứng với view trên danh sách
     *
     * @param view
     * @param item
     */
    protected void bindItem(View view, TModel item, int position) {

    }

    /**
     * Hiển thị tiến trình
     *
     * @param show
     */
    public void showProgress(boolean show) {

    }

    public void setListLayout(int listLayout) {
        mListLayout = listLayout;
    }

    /**
     * Hiển thị thông báo lỗi
     *
     * @param strMsg
     */
    public void showErrorMsg(String strMsg) {
        new AlertDialog.Builder(getContext())
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showErrorMsg(Exception exp) {
        new AlertDialog.Builder(getContext())
                .setMessage(exp.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class SimpleListAdapter extends ArrayAdapter<TModel> {
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


        @Override
        public View getView(final int position, final View convertView,
                            final ViewGroup parent) {
            int viewType = getItemViewType(position);
            // Xác định item được chọn trong danh sách
            final TModel item = mList.get(position);

            // first check to see if the view is null. if so, we have to inflate it.
            // to inflate it basically means to render, or show, the view.
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(mListLayout, null);
            }
//
//            // map dữ liệu từ model sang item trong danh sách
            bindItem(v, item, position);
//
//            // Sự kiện click trên view
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Thông báo sự kiện khi đã chọn 1 item trên danh sách
//                    if (mController != null)
//                        mController.bindItem(item);
//                }
//            });

            // return view
            return v;
        }
    }
}