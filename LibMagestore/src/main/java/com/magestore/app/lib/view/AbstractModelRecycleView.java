package com.magestore.app.lib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magestore.app.lib.R;
import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.item.GenericModelView;
import com.magestore.app.lib.view.item.ModelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Một listview đơn giản
 * Created by Mike on 1/18/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractModelRecycleView<TModelView extends ModelView, TController extends ListController>
        extends ListRecycleView {

    // tham chiếu layout của mỗi item trong list
    private int mintItemLayout;

    // tham chiếu layout của progress
    private View mProgressView;

    private int mintItemProgressLayout;

    // layout manager của recycle view
    LinearLayoutManager mRecycleViewLayoutManager;

    // có scroll hay k0
    private boolean mNoScroll;

    // số cột trong 1 list
    private int mintSpanCount = 1;

    // tham chiếu phân trang
//    private int mintPageSize = 10;
//    private int mintPageFirst = 1;
//    private int mintPageSizeMax = 500;

    private int mintOrientation = LinearLayoutManager.VERTICAL;

    // Task điều khiển danh sách trong list
    protected TController mController;

    // Model chứa data danh sách
    protected List<TModelView> mList;

    public AbstractModelRecycleView(Context context) {
        super(context);
        initLayout();
    }

    public AbstractModelRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public AbstractModelRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
        initLayout();
    }

    public void setController(TController controller) {
        mController = controller;
        initLayout();
    }

    public void initLayout() {}


    public TController getController() {
        return mController;
    }

    /**
     * Đọc các thuộc tính của layout
     * @param context
     * @param attrs
     */
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        // layout mỗi ô trong list
        mintItemLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        if (mintItemLayout <= -1)
            mintItemLayout = a.getResourceId(R.styleable.magestore_view_layout_row, -1);
        // chiều của list, ngang học dọc
        mintOrientation = a.getInteger(R.styleable.magestore_view_layout_orientation, LinearLayoutManager.VERTICAL);
        // số cột hoặc hàng trong list
        mintSpanCount = a.getInteger(R.styleable.magestore_view_layout_span_count, 1);
        // có scroll hay không
        mNoScroll = a.getBoolean(R.styleable.magestore_view_layout_no_scroll, true);
        a.recycle();

//        if (mintProgresLayout > -1) {
//            mProgressView = findViewById(mintProgresLayout);
//        }

        // tham chiếu layout của recycle view
        if (mintItemLayout > -1) {
            mRecycleViewLayoutManager = new GridLayoutManager(this.getContext(), mintSpanCount, mintOrientation, false);
            setLayoutManager(mRecycleViewLayoutManager);
        }

        // tham chiếu layout của progressbar
//        if (mintProgresLayout > -1) {
//            mProgressView = findViewById(mintProgresLayout);
//        }
    }


    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindListModelView(List<TModelView> list) {
        mList = list;
        if (mList != null) {
            this.setAdapter(new AbstractModelRecycleView<TModelView, TController>.ListRecyclerViewAdapter(mList));
        }
    }

    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindListModel(List<Model> list) {
        ArrayList<TModelView> listModelView = new ArrayList<TModelView>();
        for (Model model : list) listModelView.add(createModelView(model));
        bindListModelView(listModelView);
    }

    /**
     * Khởi tạo model view
     * @param model
     * @return
     */
    public TModelView createModelView(Model model) {
        GenericModelView modelView = new GenericModelView();
        modelView.setModel(model);
        return (TModelView) modelView;
    }

    /**
     * Map mỗi item tương ứng với view trên danh sách
     *
     * @param view
     * @param item
     */
    protected abstract void bindItem(View view, TModelView item, int position);

    /**
     * Xử lý khi 1 item được click trên danh sách
     * @param view
     * @param item
     * @param position
     */
    protected abstract void onClickItem(View view, TModelView item, int position);


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

    public void setListLayout(int listLayout) {
//        mListLayout = listLayout;
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

    public void showErrorMsg(Exception exp) {
        new AlertDialog.Builder(getContext())
                .setMessage(exp.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Thay đổi dữ liệu, refresh lại
     */
    public void notifyDataSetChanged() {
        if (getAdapter() == null) return;
        getAdapter().notifyDataSetChanged();
    }

    /**
     * Adapter map từ danh sách sang recycleview
     */
    public class ListRecyclerViewAdapter
            extends Adapter<AbstractModelRecycleView<TModelView, TController>.ListRecyclerViewAdapter.ListViewHolder> {
//        private int mLayoutRecycleView;

        // đánh dấu vị trí đã chọn
        private int selectedPos = 0;

        // Danh sách của view
        private final List<TModelView> mList;

        public ListRecyclerViewAdapter(List<TModelView> list) {
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
        public AbstractModelRecycleView<TModelView, TController>.ListRecyclerViewAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(mintItemLayout, parent, false);
            return new AbstractModelRecycleView<TModelView, TController>.ListRecyclerViewAdapter.ListViewHolder(view);
        }

        /**
         * Map dataset sang view
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final AbstractModelRecycleView<TModelView, TController>.ListRecyclerViewAdapter.ListViewHolder holder, final int position) {
            // lấy item trên row
            final TModelView item = mList.get(position);

            // hiển thị item
            holder.setItem(item, position);

            holder.mView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem(v, item, position);
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
            int valueReturn = 0;
            if (mList == null) valueReturn = 0;
            else valueReturn = mList.size();
            return valueReturn;
        }

        /**
         * Nắm giữ từng view con trong list
         */
        public class ListViewHolder extends ViewHolder {

            public TModelView mItem;
            public View mView;

            public ListViewHolder(View view) {
                super(view);
                mView = view;
            }

            public void setItem(TModelView item, int position) {
                mItem = item;
//                if (item.getViewState().isStateNormal())
                    bindItem(mView, item, position);
            }
        }
    }
//
//    @Override
//    public void hideAllProgressBar() {
//
//    }
//
//    @Override
//    public void hideErrorMsg(Exception exp) {
//
//    }
//
//    @Override
//    public void showWarning(String strMsg) {
//
//    }
//
//    @Override
//    public void hideWarning() {
//
//    }
//
//    @Override
//    public void showProgressLoadRefresh(boolean show) {
//
//    }
//
//    @Override
//    public void showProgressLoadingMore(boolean show) {
//
//    }
//
//    @Override
//    public void initModel() {
//
//    }
//
//    @Override
//    public void initValue() {
//
//    }
    //    /**
//     * Xuwr
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     */
//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // Nếu k0 tự động thay đổi chiều cao
//        if (!mNoScroll) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            return;
//        }
//
//        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
//                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.height = getMeasuredHeight();
//    }


}