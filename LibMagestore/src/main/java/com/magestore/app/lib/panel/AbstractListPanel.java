package com.magestore.app.lib.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;

import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.magestore.app.lib.R;

import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.MagestoreView;

import java.util.List;

/**
 * Panel tổng quát cho tất cả các dạng list hiển thị danh sách dựa trên RecycleView
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractListPanel<TModel extends Model>
        extends FrameLayout
        implements MagestoreView<ListController<TModel>> {
    // Task điều khiển danh sách trong list
    protected ListController<TModel> mController;

    // Model chứa data danh sách
    protected List<TModel> mList;

    // Các control nắm view
    protected RecyclerView mRecycleView;
//    protected int mLayoutRecycleView;

    // tham chiếu của cả panel
    private int mintPanelLayout;

    // tham chiếu layout của mỗi item trong list
    private int mintItemLayout;

    private int mintProgresLayout;

    // tham chiếu layout của progress
    private View mProgressView;

    // tham chiếu layout của cả danh sách
    private int mintListLayout;

    private int mintSpanCount = 1;

    private int mintOrientation = LinearLayoutManager.VERTICAL;

    // tham chiếu view của layout
    View mView;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public AbstractListPanel(Context context) {
        super(context);
        initLayout();
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public AbstractListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
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
        loadAttrs(context, attrs);
        initLayout();
    }

    /**
     * Đọc attributes
     *
     * @param context
     * @param attrs
     */
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        mintPanelLayout = a.getResourceId(R.styleable.magestore_view_layout_panel, -1);
        mintProgresLayout = a.getResourceId(R.styleable.magestore_view_layout_progress, -1);
        mintItemLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        mintListLayout = a.getResourceId(R.styleable.magestore_view_layout_list, -1);
        mintOrientation = a.getInteger(R.styleable.magestore_view_layout_orientation, LinearLayoutManager.VERTICAL);
        mintSpanCount = a.getInteger(R.styleable.magestore_view_layout_span_count, 1);
        a.recycle();

        // tham chiêu file layout của panel
        if (mintPanelLayout > -1) setLayoutPanel(mintPanelLayout);

        // tham chiếu item của list, layout
        if (mintItemLayout > -1) setLayoutItem(mintItemLayout);

        // tham chiếu layout của recycle view
        if (mintListLayout > -1) {
            mRecycleView = (RecyclerView) findViewById(mintListLayout);
            mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), mintSpanCount, mintOrientation, false));
        }

        // tham chiếu layout của progressbar
        if (mintProgresLayout > -1) {
            mProgressView = findViewById(mintProgresLayout);
        }
    }

    public int getListLayout() {
        return mintListLayout;
    }

    /**
     * Thiết lập layout cho panel
     *
     * @param layoutPanel
     */
    public void setLayoutPanel(int layoutPanel) {
        mintPanelLayout = layoutPanel;
        mView = inflate(getContext(), mintPanelLayout, null);
        addView(mView);
    }

    protected View getView() {
        return mView;
    }

    public void initLayout() {

    }

    public void initModel() {
    }

    public void initValue() {
    }

    protected void initRecycleView(int resID, GridLayoutManager layoutManager) {
        // View chưa danh sách các mặt hàng trong đơn
        mRecycleView = (RecyclerView) findViewById(resID);
        if (mRecycleView != null)
            mRecycleView.setLayoutManager(layoutManager);
    }

    /**
     * Đặt item layout quản lý item trong danh sách
     *
     * @param layout
     */
    public void setLayoutItem(int layout) {
        mintItemLayout = layout;
    }

    /**
     * Hiển thị tiến trình
     *
     * @param show
     */
    public void showProgress(final boolean show) {
        if (mProgressView == null) return;
// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Đặt controller điều khiển
     *
     * @param controller
     */
    public void setController(ListController<TModel> controller) {
        mController = controller;
    }

    /**
     * Trả lại controller
     */
    public ListController<TModel> getController() {
        return mController;
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
    protected abstract void bindItem(View view, TModel item, int position);


    /**
     * Adapter map từ danh sách sang recycleview
     */
    public class ListRecyclerViewAdapter
            extends RecyclerView.Adapter<AbstractListPanel<TModel>.ListRecyclerViewAdapter.ListViewHolder> {
//        private int mLayoutRecycleView;

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
                    .inflate(mintItemLayout, parent, false);
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
            holder.setItem(item, position);

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
                        mController.bindItem(item);
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

            public void setItem(TModel item, int position) {
                mItem = item;
                bindItem(mView, item, position);
            }
        }
    }

    /**
     * @param item
     */
    public void showDeleteItem(TModel item) {

    }

    public void showUpdateItem(TModel item) {

    }

    /**
     * Có sự thay đổi số liệu, cập nhật lại giao diện
     */
    public void notifyDataSetChanged() {
        RecyclerView.Adapter adapter = mRecycleView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
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
        exp.printStackTrace();
        new AlertDialog.Builder(getContext())
                .setMessage(exp.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}