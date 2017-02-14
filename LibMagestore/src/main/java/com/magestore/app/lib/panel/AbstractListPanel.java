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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.magestore.app.lib.R;

import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.EndlessRecyclerOnScrollListener;
import com.magestore.app.lib.view.ListRecycleView;
import com.magestore.app.lib.view.MagestoreView;

import java.util.ArrayList;
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

    // tham chiếu id layout của progress
    private int mintProgresLayout;

    // tham chiếu id của thông báo lỗi
    private int mintTxtErrMsg;

    // tham chiếu layout của progress
    private View mProgressView;

    // tham chiếu layout thông báo lỗi
    private TextView mTxtErrorMsg;

    private int mintItemProgressLayout;

    // layout manager của recycle view
    LinearLayoutManager mRecycleViewLayoutManager;

    // tham chiếu layout của cả danh sách
    private int mintListLayout;

    // số cột trong 1 list
    private int mintSpanCount = 1;

    // tham chiếu phân trang
    private int mintPageSize = -1;
    private int mintItemMax = 500;
    private boolean haveLazyLoading = false;

    private int mintOrientation = LinearLayoutManager.VERTICAL;

    // tham chiếu view của layout
    View mView;

    // loading progress của từng item
    TModel mModelLoadingProgress = null;
    boolean mblnModelLoadingProgress = false;

    // scroll litsenr cho lazy loading
    EndlessRecyclerOnScrollListener mScrollListener;

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

        // layoyt id của chính panel
        mintPanelLayout = a.getResourceId(R.styleable.magestore_view_layout_panel, -1);
        // layout id của progress hiển thị quá trình loading
        mintProgresLayout = a.getResourceId(R.styleable.magestore_view_layout_progress, -1);
        // layout id của progress hiển thị quá trình loading
        mintTxtErrMsg = a.getResourceId(R.styleable.magestore_view_layout_msg, -1);
        // layout mỗi ô trong list
        mintItemLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        // layout của list
        mintListLayout = a.getResourceId(R.styleable.magestore_view_layout_list, -1);
        // chiều của list, ngang học dọc
        mintOrientation = a.getInteger(R.styleable.magestore_view_layout_orientation, LinearLayoutManager.VERTICAL);
        // số cột hoặc hàng trong list
        mintSpanCount = a.getInteger(R.styleable.magestore_view_layout_span_count, 1);

        // kích thước phân trang, số item tối đa
        mintPageSize = a.getInteger(R.styleable.magestore_view_page_size, -1);
        mintItemMax = a.getInteger(R.styleable.magestore_view_page_size_max, 500);

        a.recycle();

        // xác định các tham só cho lazy loading
        haveLazyLoading = mintPageSize > 0;
        if (mintPageSize <= 0) mintPageSize = mintItemMax;

        // tham chiêu file layout của panel
        if (mintPanelLayout > -1) setLayoutPanel(mintPanelLayout);

        // tham chiếu item của list, layout
        if (mintItemLayout > -1) setLayoutItem(mintItemLayout);

        // tham chiếu layout của recycle view
        if (mintListLayout > -1) {
            mRecycleView = (RecyclerView) findViewById(mintListLayout);
            mRecycleViewLayoutManager = new GridLayoutManager(this.getContext(), mintSpanCount, mintOrientation, false);
            mRecycleView.setLayoutManager(mRecycleViewLayoutManager);
            if (haveLazyLoading) {
                mScrollListener = new EndlessRecyclerOnScrollListener(mRecycleViewLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        // loading dữ liệu
                        mController.doRetrieveMore(current_page);
                        // hiện progress loading ở item cuối cùng
                        setItemLoadingProgress(null, true);

                    }
                };
                mRecycleView.setOnScrollListener(mScrollListener);
            }
        }

        // tham chiếu layout của progressbar
        if (mintProgresLayout > -1) {
            mProgressView = findViewById(mintProgresLayout);
        }

        // tham chiếu layout của thông báo lỗi
        if (mintTxtErrMsg > -1) {
            mTxtErrorMsg = (TextView) findViewById(mintTxtErrMsg);
        }
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

        // nếu danh sách đã có số liệu, không show progress nữa
        if ((mRecycleView.getAdapter() != null) && mRecycleView.getAdapter().getItemCount() > 0)
            return;
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
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
//        mController.setPage(mintPageSize, mintItemMax);
    }

    /**
     * Trả lại controller
     */
    public ListController<TModel> getController() {
        return mController;
    }

    /**
     * Clear danh sách
     */
    public void clearList() {
        // null tham chiếu list
        mList = null;
        if (mRecycleView == null) return;

        // tạo adapter với zero phần tử
        mRecycleView.setAdapter(new AbstractListPanel<TModel>.ListRecyclerViewAdapter(new ArrayList<TModel>()));

        // reset lại controll listener
        if (mScrollListener!=null) mScrollListener.resetCurrentPage();

        // cập nhật giao diện
        notifyDataSetChanged();

        // disable lazy loading
        enableLazyLoading(false);
    }

    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindList(List<TModel> list) {
        if (mRecycleView == null) return;

        // đặt tham chiếu cho list
        mList = list;
        if (mList != null) {
            // khởi tạo adapter
            mRecycleView.setAdapter(new AbstractListPanel<TModel>.ListRecyclerViewAdapter(mList));

            // đặt lại scroll listener cho lazy loading
            if (mScrollListener!=null) mScrollListener.resetCurrentPage();
        }

        // notify view thay đổi
        notifyDataSetChanged();
    }

    /**
     * Có sử dụng lazy loading hay không
     * @return
     */
    public boolean haveLazyLoading() {
        return haveLazyLoading;
    }

    public int getPageSize() { return mintPageSize;}
    public int getItemMax() {return mintItemMax;}

    /**
     * Tạm khóa lazyloading trong khi đang load dữ liệu
     * @param lock
     */
    public void lockLazyLoading(boolean lock) {
        if (mScrollListener != null) mScrollListener.lockLazyLoading(lock);
    }

    /**
     * Bật tắt layzyloading
     */
    public void enableLazyLoading(boolean enable) {
        if (mScrollListener != null) mScrollListener.enableLazyLoading(enable);
//        if (mRecycleView == null) return;
//        if (enable)
//            mRecycleView.setOnScrollListener(mScrollListener);
//        else
//            mRecycleView.setOnScrollListener(null);
    }

    /**
     * Map mỗi item tương ứng với view trên danh sách
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
            // kiểm tra bật progress nếu thêm mới 1 item hoặc lazy loading
            if (position >= mList.size()) {
                holder.setItem(null, position);
                return;
            }

            // lấy item trên row
            final TModel item = mList.get(position);

            // hiển thị item
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
            int valueReturn = 0;
            if (mList == null) valueReturn = 0;
            else valueReturn = mList.size();
            if (mblnModelLoadingProgress
                    && (mModelLoadingProgress == null))
//                    && (mScrollListener != null && mScrollListener.isEnableLazyLoading() && !mScrollListener.isLockLazyLoading()))
                valueReturn++;
            return valueReturn;
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
                if (position >= mList.size()) {
                    showItemLoadingProgres(mView, true);
                    return;
                }
                else {
                    showItemLoadingProgres(mView, false);
                }

//                if (item == mModelLoadingProgress) {
//                    showItemLoadingProgres(mView, true);
//                    return;
//
//                }
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
        if (mRecycleView == null) return;
        RecyclerView.Adapter adapter = mRecycleView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

//    /**
//     * Notify khi item được cho vào cuối danh sách
//     * @param list
//     */
//    public void notifyDataSetChangedLastItem(List<TModel> list) {
//        int start = mRecycleView.getAdapter().getItemCount() - 1 - list.size();
//        int range = mList.size() -  start;
//        mRecycleView.getAdapter().notifyItemChanged(mRecycleView.getAdapter().getItemCount() - 1 - list.size());
//        mRecycleView.getAdapter().notifyItemRangeInserted(start, range);
//    }

    /**
     * Notify khi item được cho vào cuối danh sách
     * @param list
     */
    public void notifyDataSetInsertLastItem(List<TModel> list) {
        int start = mRecycleView.getAdapter().getItemCount() - 1 - list.size();
        int range = mList.size() -  start;
        mRecycleView.getAdapter().notifyItemChanged(mRecycleView.getAdapter().getItemCount() - 1 - list.size());
//        mRecycleView.getAdapter().notifyItemRangeInserted(start, range);
    }

    /**
     * Notify khi item đầu tiên thay đổi
     */
    public void notifyDataSetInsertFirstItem() {
        mRecycleView.getAdapter().notifyItemRangeInserted(0, 1);
        mRecycleView.getAdapter().notifyItemChanged(0, 1);
    }

    /**
     * Notify khi item được cho vào cuối danh sách
     * @param list
     */
    public void notifyDataSetInsertFirstItem(List<TModel> list) {
        int start = 0;
        int range = list.size();
        mRecycleView.getAdapter().notifyItemRangeInserted(start, range);
        mRecycleView.getAdapter().notifyItemChanged(start, range);
    }

    /**
     * Thay đổi giao diện khi xóa 1 item
     * @param position
     */
    public void notifyDataSetRemoveItem(int position) {
        mRecycleView.getAdapter().notifyItemRemoved(position);
        mRecycleView.getAdapter().notifyItemRangeChanged(position, mRecycleView.getAdapter().getItemCount());
    }

    /**
     * Thay đổi giao diện khi thay đổi 1 position
     * @param position
     */
    public void notifyDataSetUpdateItem(int position) {
        mRecycleView.getAdapter().notifyItemChanged(position);
    }

    /**
     * Hiển thị thông báo lỗi
     *
     * @param strMsg
     */
    public void showErrorMsg(String strMsg) {
        if (mTxtErrorMsg != null) {
            mTxtErrorMsg.setText(strMsg);
            mTxtErrorMsg.setVisibility(VISIBLE);
        }
        else {
            new AlertDialog.Builder(getContext())
                    .setMessage(strMsg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public void showErrorMsg(Exception exp) {
        exp.printStackTrace();
        showErrorMsg(exp.getMessage());
    }

    /**
     * Bật tắt show loading theo 1 item
     * @param model
     * @param show
     */
    public void setItemLoadingProgress(TModel model, boolean show) {
        mModelLoadingProgress = model;
        mblnModelLoadingProgress = show;
    }

    /**
     * Hiwwne thị quá trình show loading cho item
     * @param v
     * @param show
     */
    public void showItemLoadingProgres(View v, boolean show) {
        if (mRecycleView == null) return;
        if (mRecycleView instanceof ListRecycleView) {
            ((ListRecycleView) mRecycleView).showProgress(v, show);
        }
    }
}