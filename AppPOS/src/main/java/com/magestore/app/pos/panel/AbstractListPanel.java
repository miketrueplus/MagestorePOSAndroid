package com.magestore.app.pos.panel;

import android.content.Context;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.AbstractPosListController;
import com.magestore.app.pos.controller.ListController;
import com.magestore.app.util.DialogUtil;

import java.util.List;

/**
 * Panel tổng quát cho tất cả các dạng list hiển thị danh sách dựa trên RecycleView
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractListPanel<TModel> extends FrameLayout {
    // Controller điều khiển danh sách trong list
    ListController<TModel> mController;

    // Model chứa data danh sách
    List<TModel> mList;

    // Các control nắm view
    RecyclerView mRecycleView;
    int mLayoutItem;
    int mLayoutRecycleView;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public AbstractListPanel(Context context) {
        super(context);
        init();
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public AbstractListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public AbstractListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Khởi tạo
     */
    protected void init() {
        initControlLayout();
        initControlValue();
    }

    protected void initControlLayout() {

    }

    protected void initControlValue() {

    }

    /**
     * Đặt recycle view layout quản lý danh sách
     *
     * @param layout
     */
    public void setmLayoutRecycleView(int layout) {
        mLayoutRecycleView = layout;
    }

    /**
     * Đặt item layout quản lý item trong danh sách
     *
     * @param layout
     */
    public void setLayoutItem(int layout) {
        mLayoutItem = layout;
    }

    /**
     * Hiển thị tiến trình
     *
     * @param show
     */
    public void showProgress(boolean show) {

    }

    /**
     * Đặt controller điều khiển
     * @param controller
     */
    public void setController(AbstractPosListController<TModel> controller) {
        mController = controller;
    }

    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        if (mList != null) {
            mRecycleView.setAdapter(new AbstractListPanel<TModel>.ListRecyclerViewAdapter(mList));
        }
    }

    /**
     * Map mỗi item tương ứng với view trên danh sách
     *
     * @param view
     * @param item
     */
    protected abstract void bindItem(View view, TModel item);

    /**
     * Adapter map từ danh sách sang recycleview
     */
    public class ListRecyclerViewAdapter
            extends RecyclerView.Adapter<AbstractListPanel<TModel>.ListRecyclerViewAdapter.ListViewHolder> {
        private int mLayoutRecycleView;

        // đánh dấu vị trí đã chọn
        private int selectedPos = 0;

        // Danh sách của view
        private final List<TModel> mList;

        public ListRecyclerViewAdapter(List<TModel> list) {
            mList = list;
        }

        /**
         * Khởi tạo danh sách
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public AbstractListPanel<TModel>.ListRecyclerViewAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(mLayoutItem, parent, false);
            return new AbstractListPanel<TModel>.ListRecyclerViewAdapter.ListViewHolder(view);
        }

        /**
         * Map dataset sang view
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final AbstractListPanel<TModel>.ListRecyclerViewAdapter.ListViewHolder holder, final int position) {
            final TModel item = mList.get(position);
            holder.setItem(item);

            // highlight vị trí đã chọn
            holder.itemView.setSelected(selectedPos == position);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update highlight list đã chọn
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);

                    // Thông báo sự kiện khi đã chọn 1 item trên danh sách
                    if (mController != null)
                        mController.onSelectItem(item);
                }
            });
        }

        /**
         * Đếm số bản ghi trong view
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return mList.size();
        }

        /**
         * Nắm giữ từng view con trong list
         */
        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TModel mItem;
            public View mView;

            public ListViewHolder(View view) {
                super(view);
                mView = view;
            }

            public void setItem(TModel item) {
                mItem = item;
                bindItem(mView, item);
            }
        }
    }

    /**
     *
     * @param item
     */
    public void showDeleteItem(TModel item) {

    }

    public void showEditItem(TModel item) {

    }
}