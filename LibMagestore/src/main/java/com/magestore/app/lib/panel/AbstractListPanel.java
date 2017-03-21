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
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magestore.app.lib.R;

import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.view.adapter.DefaultModelView;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.lib.view.listener.EndlessRecyclerOnScrollListener;
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
        extends AbstractPanel<ListController<TModel>>
        implements MagestoreView<ListController<TModel>> {

    // vị trí chọn
    int mintSelectedPos = 0;

    // list search
    int mintSearchAutoCompletePanel;
    private SearchAutoCompletePanel mSearchPanel;

    // Task điều khiển danh sách trong list
//    protected ListController<TModel> mController;

    // Model chứa data danh sách
    protected List<TModel> mList;
    protected List<ModelView> mSaveModelViewList;
    protected List<ModelView> mModelViewList;

    // Các control nắm view
    private RecyclerView mRecycleView;

    // tham chiếu layout của mỗi item trong list
    private int mintItemLayout;

    // layout manager của recycle view
    LinearLayoutManager mRecycleViewLayoutManager;

    // tham chiếu layout của cả danh sách
    private int mintListLayout;

    // có scroll hay không
    private boolean mblnNoScroll;

    // số cột trong 1 list
    private int mintSpanCount = 1;

    // tham chiếu phân trang
    private int mintPageSize;
    private int mintItemMax;
    private boolean haveLazyLoading;

    // progress và thông báo cho các item
    int mintLayoutModelViewProgress;
    int mintLayoutModelViewContent;
    int mintLayoutModelViewMsg;
    int mintLayoutModelViewImage;
    int mintLayoutModelViewText1;
    int mintLayoutModelViewText2;

    private int mintOrientation;

    // scroll litsenr cho lazy loading
    EndlessRecyclerOnScrollListener mScrollListener;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public AbstractListPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public AbstractListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    }

    /**
     * Đọc attributes
     *
     * @param context
     * @param attrs
     */
    @Override
    protected void loadAttrs(Context context, AttributeSet attrs) {
        super.loadAttrs(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);

        // layoyt id của chính panel
//        mintPanelLayout = a.getResourceId(R.styleable.magestore_view_layout_panel, -1);

//        // layout id của progress hiển thị quá trình loading
//        mintIdProgresBar = a.getResourceId(R.styleable.magestore_view_layout_progress, R.id.id_modelview_default_progressbar);
//        mintIdProgresBarTop = a.getResourceId(R.styleable.magestore_view_layout_progress_top, R.id.id_modelview_default_progressbar_top);
//        mintIdProgresBarBottom = a.getResourceId(R.styleable.magestore_view_layout_progress_bottom, R.id.id_modelview_default_progressbar_bottom);

        // layout id của progress hiển thị quá trình loading
//        mintTxtErrMsg = a.getResourceId(R.styleable.magestore_view_layout_msg, R.id.id_modelview_default_textmsg);

        // layout mỗi ô trong list
        mintItemLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        // layout của list
        mintListLayout = a.getResourceId(R.styleable.magestore_view_layout_list, -1);
        // chiều của list, ngang học dọc
        mintOrientation = a.getInteger(R.styleable.magestore_view_layout_orientation, LinearLayoutManager.VERTICAL);
        // số cột hoặc hàng trong list
        mintSpanCount = a.getInteger(R.styleable.magestore_view_layout_span_count, 1);
        // panel search
        mintSearchAutoCompletePanel = a.getResourceId(R.styleable.magestore_view_layout_search, -1);

        // kích thước phân trang, số item tối đa
        mintPageSize = a.getInteger(R.styleable.magestore_view_page_size, -1);
        mintItemMax = a.getInteger(R.styleable.magestore_view_page_size_max, 500);

        // progress bar và các thông báo
        mintLayoutModelViewProgress = a.getResourceId(R.styleable.magestore_view_layout_item_progress, -1);
        mintLayoutModelViewContent = a.getResourceId(R.styleable.magestore_view_layout_item_content, -1);
        mintLayoutModelViewMsg = a.getResourceId(R.styleable.magestore_view_layout_item_msg, -1);

        // có scroll hay không
        mblnNoScroll = a.getBoolean(R.styleable.magestore_view_layout_no_scroll, false);
        a.recycle();
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        // xác định các tham só cho lazy loading
        haveLazyLoading = mintPageSize > 0;
        if (mintPageSize <= 0) mintPageSize = mintItemMax;

        // tham chiếu item của list, layout
        if (mintItemLayout > 0) setLayoutItem(mintItemLayout);

        // tham chiếu layout của recycle view
        initListView();

        // panel search
        if (mintSearchAutoCompletePanel > 0) {
            mSearchPanel = (SearchAutoCompletePanel) findViewById(mintSearchAutoCompletePanel);
        }
    }

    public void initListView() {
        // tham chiếu layout của recycle view
        if (mintListLayout > 0) {
            mRecycleView = (RecyclerView) findViewById(mintListLayout);
            mRecycleViewLayoutManager = new GridLayoutManager(this.getContext(), mintSpanCount, mintOrientation, false);
            mRecycleView.setLayoutManager(mRecycleViewLayoutManager);
            mRecycleView.setAdapter(new AbstractListPanel<TModel>.ListRecyclerViewAdapter());
            mRecycleView.setNestedScrollingEnabled(!mblnNoScroll);
            if (haveLazyLoading) {
                mScrollListener = new EndlessRecyclerOnScrollListener(mRecycleViewLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        // loading dữ liệu
                        mController.doRetrieveMore(current_page);
                    }
                };
                mRecycleView.setOnScrollListener(mScrollListener);
            }
        }
    }

    /**
     * Khởi tạo list layout
     * @param listLayout
     */
    public void initListView(int listLayout) {
        initListView(listLayout, true, false, 1, LinearLayoutManager.VERTICAL);
    }

    /**
     * Khởi tạo list view với số cột xác định
     * @param listLayout
     * @param intSpanCount
     */
    public void initListView(int listLayout,int intSpanCount) {
        initListView(listLayout, true, false, intSpanCount, LinearLayoutManager.VERTICAL);
    }

    /**
     * Khởi tạo list view
     * @param listLayout
     * @param blnNoScroll
     * @param haveLazyLoading
     */
    public void initListView(int listLayout, boolean blnNoScroll, boolean haveLazyLoading) {
        initListView(listLayout, blnNoScroll, haveLazyLoading, 1, LinearLayoutManager.VERTICAL);
    }

    /**
     * Khởi tạo list view
     * @param listLayout
     * @param intSpanCount
     * @param intOrientation
     * @param haveLazyLoading
     * @param blnNoScroll
     */
    public void initListView(int listLayout, boolean blnNoScroll, boolean haveLazyLoading, int intSpanCount, int intOrientation) {
        this.mintListLayout = listLayout;
        this.mintSpanCount = intSpanCount;
        this.mintOrientation = intOrientation;
        this.haveLazyLoading = haveLazyLoading;
        this.mblnNoScroll = blnNoScroll;
        initListView();
    }

    //    protected void initRecycleView(int resID, GridLayoutManager layoutManager) {
//        // View chưa danh sách các mặt hàng trong đơn
//        mRecycleView = (RecyclerView) findViewById(resID);
//        if (mRecycleView != null)
//            mRecycleView.setLayoutManager(layoutManager);
//    }

    /**
     * Đặt item layout quản lý item trong danh sách
     *
     * @param layout
     */
    public void setLayoutItem(int layout) {
        mintItemLayout = layout;
    }

    /**
     * Hiển thị progress bar chung cả panel khi loading dữ liệu lần đầu
     *
     * @param show
     */
    @Override
    public void showProgress(final boolean show) {
        // nếu danh sách đã có số liệu, không show progress nữa
        if (mRecycleView != null && (mRecycleView.getAdapter() != null) && mRecycleView.getAdapter().getItemCount() > 0)
            super.showProgress(false);
        else super.showProgress(show);
    }

    /**
     * Đặt controller điều khiển
     *
     * @param controller
     */
    public void setController(ListController<TModel> controller) {
        super.setController(controller);
        if (mSearchPanel != null) mSearchPanel.setListController(controller);
    }

    /**
     * Clear danh sách
     */
    public void clearList() {
        // null tham chiếu list
        if (mRecycleView == null) return;

        // reset lại controll listener
        if (mScrollListener != null) mScrollListener.resetCurrentPage();
        mModelViewList = null;

        // bind list trống
//        bindList(new ArrayList<TModel>());

        // cập nhật giao diện
        notifyDataSetChanged();

        // disable lazy loading
        enableLazyLoading(false);
    }

    /**
     * Điền kết quả tìm kiếm vào list
     *
     * @param list
     */
    public void publishSearchList(List<TModel> list) {
        if (mRecycleView == null) return;
        if (list == null) return;

        // khóa lazy loading
        lockLazyLoading(true);

        // lưu lại kết quả danh sách chính để back
        if (mSaveModelViewList == null) mSaveModelViewList = mModelViewList;

        // clear danh sách cũ
        mModelViewList = null;
        notifyDataSetChanged();

        // áp danh sách tìm kiếm được vào
        bindList(list);
    }

    /**
     * Dừng tìm kiếm và quay về với kết quả ban đầu
     */
    public void closeSearchList() {
        if (mRecycleView == null) return;
        if (mSaveModelViewList == null) return;

        // khôi phục lại danh sách trước khi tìm kiếm
        mModelViewList = mSaveModelViewList;

        // mở lại lazyloading
        lockLazyLoading(false);

        // update màn hình
        notifyDataSetChanged();
    }

    /**
     * Chèn danh sách vào cuối list
     *
     * @param list
     */
    public void insertListAtLast(List<TModel> list) {
        if (mRecycleView == null) return;
        if (list == null) return;

        // đánh dấu khoảng cách thêm
        int startRange = mRecycleView.getAdapter().getItemCount();

        // khởi tạo model view
        if (mModelViewList == null) mModelViewList = new ArrayList<ModelView>();
        for (TModel model : list) {
            ModelView modelView = new DefaultModelView();
            modelView.setModel(model);
            modelView.getViewState().setStateNormal();
            mModelViewList.add(modelView);
        }

        // update giao diện trên view
        if (startRange == 0) mRecycleView.getAdapter().notifyDataSetChanged();
        mRecycleView.getAdapter().notifyItemRangeInserted(startRange, list.size());
    }

    /**
     * Chèn danh sách vào cuối list
     *
     * @param list
     */
    public void insertListAtLast(TModel... list) {
        if (mRecycleView == null) return;
        if (list == null) return;

        // đánh dấu khoảng cách thêm
        int startRange = mRecycleView.getAdapter().getItemCount();

        // khởi tạo model view
        if (mModelViewList == null) mModelViewList = new ArrayList<ModelView>();
        for (TModel model : list) {
            ModelView modelView = new DefaultModelView();
            modelView.setModel(model);
            modelView.getViewState().setStateNormal();
            mModelViewList.add(modelView);
        }

        // update giao diện trên view
        if (startRange == 0) mRecycleView.getAdapter().notifyDataSetChanged();
        mRecycleView.getAdapter().notifyItemRangeInserted(startRange, list.length);
    }

    /**
     * Chèn danh sách vào đầu list
     *
     * @param list
     */
    public void insertListAtFirst(List<TModel> list) {
        if (mRecycleView == null) return;
        if (list == null) return;

        // đánh dấu khoảng cách thêm
        int startRange = mRecycleView.getAdapter().getItemCount();

        // khởi tạo model view
        if (mModelViewList == null) mModelViewList = new ArrayList<ModelView>();
        int i = 0;
        for (TModel model : list) {
            ModelView modelView = new DefaultModelView();
            modelView.setModel(model);
            modelView.getViewState().setStateNormal();
            mModelViewList.add(i++, modelView);
        }

        // update giao diện trên view
        if (startRange == 0) mRecycleView.getAdapter().notifyDataSetChanged();
        mRecycleView.getAdapter().notifyItemRangeInserted(0, list.size());
        mRecycleView.scrollToPosition(0);

    }

    /**
     * Chèn danh sách vào đầu list
     *
     * @param list
     */
    public void insertListAtFirst(TModel... list) {
        if (mRecycleView == null) return;
        if (list == null) return;

        // đánh dấu khoảng cách thêm
        int startRange = mRecycleView.getAdapter().getItemCount();

        // khởi tạo model view
        if (mModelViewList == null) mModelViewList = new ArrayList<ModelView>();
        int i = 0;
        for (TModel model : list) {
            ModelView modelView = new DefaultModelView();
            modelView.setModel(model);
            modelView.getViewState().setStateNormal();
            mModelViewList.add(i++, modelView);
        }

        // update giao diện trên view
        if (startRange == 0) mRecycleView.getAdapter().notifyDataSetChanged();
        mRecycleView.getAdapter().notifyItemRangeInserted(0, list.length);
        mRecycleView.scrollToPosition(0);

    }

    /**
     * Xóa trên danh sách hiển thị
     *
     * @param list
     * @return true nếu tìm thấy danh sách
     */
    public boolean deleteList(TModel... list) {
        if (mRecycleView == null) return false;
        if (list == null) return false;
        if (mModelViewList == null || mModelViewList.size() <= 0) return false;

        boolean blnFoundModel = false;
        for (TModel model : list) {
            for (int i = mModelViewList.size() - 1; i >= 0; i--) {
                ModelView modelView = mModelViewList.get(i);
                if (modelView != null && modelView.getModel() == model) {
                    mModelViewList.remove(i);
                    mRecycleView.getAdapter().notifyItemRemoved(i);
                    blnFoundModel = true;
                }
            }
        }
        return blnFoundModel;
    }

    /**
     * Cập nhật và thay thế model trong view
     *
     * @param oldModel
     * @param newModel
     * @return
     */
    public boolean replaceModel(TModel oldModel, TModel newModel) {
        if (mRecycleView == null) return false;
        if (oldModel == null) return false;
        if (mModelViewList == null || mModelViewList.size() <= 0) return false;

        // tìm model tương ứng
        boolean blnFoundModel = false;
        for (int i = 0; i < mModelViewList.size(); i++) {
            if (mModelViewList.get(i).getModel() == oldModel) {
                mModelViewList.get(i).setModel(newModel);
                mRecycleView.getAdapter().notifyItemChanged(i);
                blnFoundModel = true;
            }
        }
        return blnFoundModel;
    }

    /**
     * Cập nhật 1 loạt model, đưa lên đầu danh sách
     *
     * @param oldModel
     * @param newModel
     */
    public boolean replaceModelToFirst(TModel oldModel, TModel newModel) {
        if (mRecycleView == null) return false;
        if (mModelViewList == null || mModelViewList.size() <= 0) return false;

        // tìm các ô chứa model để cập nhật
        boolean blnFoundModel = false;
        for (int i = 0; i < mModelViewList.size(); i++) {
            if (mModelViewList.get(i).getModel() == oldModel) {
                if (i > 0) {
                    ModelView modelView = mModelViewList.get(i);
                    modelView.setModel(newModel);
                    mModelViewList.remove(i);
                    mModelViewList.add(0, modelView);
                }
                mRecycleView.getAdapter().notifyItemMoved(i, 0);
                mRecycleView.getAdapter().notifyItemChanged(0);
                blnFoundModel = true;
            }
        }
        if (blnFoundModel) mRecycleView.scrollToPosition(0);
        return blnFoundModel;
    }

    /**
     * Cập nhật 1 model, đưa lên đầu danh sách. Nếu chưa có thì chèn vào
     * @param oldModel
     * @param newModel
     */
    public void replaceModelToFirstInsertIfNotFound(TModel oldModel, TModel newModel) {
        if (!replaceModelToFirst(oldModel, newModel)) insertListAtFirst(newModel);
    }

    /**
     * Cập nhật nội dung ô chứa Model
     * Nếu chưa có thì insert
     * @param oldModel
     * @param newModel
     */
    public void replaceModelInsertAtLastIfNotFound(TModel oldModel, TModel newModel) {
        // nếu k0 có chèn vào cuối
        if (!replaceModel(oldModel, newModel)) insertListAtLast(newModel);
    }

    /**
     * Cập nhật nội dung ô chứa Model
     * Nếu chưa có thì insert
     * @param oldModel
     * @param newModel
     */
    public void replaceModelInsertAtFistIfNotFound(TModel oldModel, TModel newModel) {
        // nếu k0 có chèn vào cuối
        if (!replaceModel(oldModel, newModel)) insertListAtFirst(newModel);
    }

    /**
     * Cập nhật model trong view
     *
     * @param list
     * @return true nếu có model trong danh sách
     */
    public boolean updateModel(TModel... list) {
        if (mRecycleView == null) return false;
        if (list == null) return false;
        if (mModelViewList == null || mModelViewList.size() <= 0) return false;

        // tìm các ô chứa model để cập nhật
        boolean blnFoundModel = false;
        for (TModel model : list) {
            for (int i = 0; i < mModelViewList.size(); i++) {
                if (mModelViewList.get(i).getModel() == model) {
                    mRecycleView.getAdapter().notifyItemChanged(i);
                    blnFoundModel = true;
                }
            }
        }
        return blnFoundModel;
    }

    /**
     * Cập nhật 1 loạt model, đưa lên đầu danh sách
     *
     * @param list
     */
    public boolean updateModelToFirst(TModel... list) {
        if (mRecycleView == null) return false;
        if (list == null) return false;
        if (mModelViewList == null || mModelViewList.size() <= 0) return false;

        // tìm các ô chứa model để cập nhật
        boolean blnFoundModel = false;
        for (TModel model : list) {
            for (int i = 0; i < mModelViewList.size(); i++) {
                if (mModelViewList.get(i).getModel() == model) {
                    if (i > 0) {
                        ModelView modelView = mModelViewList.get(i);
                        mModelViewList.remove(i);
                        mModelViewList.add(0, modelView);
                    }
                    mRecycleView.getAdapter().notifyItemMoved(i, 0);
                    mRecycleView.getAdapter().notifyItemChanged(0);
                    blnFoundModel = true;
                }
            }
        }
        if (blnFoundModel) mRecycleView.scrollToPosition(0);
        return blnFoundModel;
    }

    /**
     * Cập nhật 1 model, đưa lên đầu danh sách. Nếu chưa có thì chèn vào
     *
     * @param model
     */
    public void updateModelToFirstInsertIfNotFound(TModel model) {
        if (!updateModelToFirst(model)) insertListAtFirst(model);
    }

    /**
     * Cập nhật nội dung ô chứa Model
     * Nếu chưa có thì insert
     */
    public void updateModelInsertAtLastIfNotFound(TModel model) {
        // nếu k0 có chèn vào cuối
        if (!updateModel(model)) insertListAtLast(model);
    }

    /**
     * Cập nhật nội dung ô chứa Model
     * Nếu chưa có thì insert
     */
    public void updateModelInsertAtFistIfNotFound(TModel model) {
        // nếu k0 có chèn vào cuối
        if (!updateModel(model)) insertListAtFirst(model);
    }

    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindList(List<TModel> list) {
        if (mRecycleView == null) return;
        if (list == null) return;

        // tham chiếu data set gốc
        mList = list;

        // khởi tạo model view
        mModelViewList = new ArrayList<ModelView>();
        for (TModel model : list) {
            mModelViewList.add(createModelView(model));
        }

        // nếu danh sách không trống
        if (mModelViewList != null && mModelViewList.size() > 0) {
            hideWarning();

            // đặt lại scroll listener cho lazy loading
            if (mScrollListener != null) mScrollListener.resetCurrentPage();
        } else {
            // hiện thông báo
            showWarning(getContext().getString(R.string.msg_no_data));
        }

        // update giao diện trên view
        mRecycleView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Có sử dụng lazy loading hay không
     *
     * @return
     */
    public boolean haveLazyLoading() {
        return haveLazyLoading;
    }

    /**
     * Kích thước 1 page
     *
     * @return
     */
    public int getPageSize() {
        return mintPageSize;
    }

    /**
     * Số item max trong 1 page
     *
     * @return
     */
    public int getItemMax() {
        return mintItemMax;
    }

    /**
     * Tạm khóa lazyloading trong khi đang load dữ liệu
     *
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
     * Hold ayout view của iten, gán findview id vào các biến
     *
     * @param view
     * @return
     */
    protected RecycleViewItemHolder holdItemView(View view) {
        RecycleViewItemHolder viewHolder = new RecycleViewItemHolder(view);
        viewHolder.holdView(view);
        return viewHolder;
    }

    /**
     * Hiển thị dialog confirm delete
     *
     * @param item
     */
    public void showDeleteItemInput(TModel item) {

    }

    /**
     * Hiển thị dialog insert
     */
    public void showInsertItemInput() {

    }

    /**
     * Hiển thị dialog update
     *
     * @param item
     */
    public void showUpdateItemInput(TModel item) {

    }

    /**
     * Adapter map từ danh sách sang recycleview
     */
    public class ListRecyclerViewAdapter
            extends RecyclerView.Adapter<AbstractListPanel<TModel>.RecycleViewItemHolder> {

        public ListRecyclerViewAdapter() {}

        /**
         * Khởi tạo danh sách
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public AbstractListPanel<TModel>.RecycleViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Nếu không chỉ định layout của item, thì sử dụng layout ID chung
            View view = LayoutInflater.from(parent.getContext()).inflate(mintItemLayout > 0 ? mintItemLayout : R.layout.layout_modelview_default_item_main, parent, false);

            // trả lại view holder
            return holdItemView(view);
        }


        /**
         * Map dataset sang view
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final AbstractListPanel<TModel>.RecycleViewItemHolder holder, final int position) {
            // lấy item trên row
            final ModelView modelView = mModelViewList.get(position);

            // hiển thị item
            holder.mModelView = modelView;
            // xử lý hiển thị progress bar nếu có
            holder.displayProgressBar(modelView);

            // xử lý hiển thị thông báo lỗi nếu có
            holder.displayErrorMsg(modelView);

            // xử lý hiển thị content
            holder.displayContent(modelView);

            // hiển thị dữ liệu
            holder.setItem(modelView, position);
        }

        /**
         * Đếm số bản ghi trong view
         * @return
         */
        @Override
        public int getItemCount() {
            if (mModelViewList == null) return 0;
            return mModelViewList.size();
        }
    }

    /**
     * Nắm giữ từng view con trong list
     */
    public class RecycleViewItemHolder extends RecyclerView.ViewHolder {
        ModelView mModelView;
        // view chính cho toàn bộ holder
        View mLayoutMainView;

        // view cho phần hiện content, (bỏ phần progress bar
        View mLayoutContentView;

        // các item
        TextView mTxtItem1 = null;
        TextView mTxtItem2 = null;
        TextView mTxtItemMsg = null;
        ImageView mImageItem = null;
        boolean mblnUseTxtView1 = true;
        boolean mblnUseTxtView2 = true;
        boolean mblnUseImage = true;

        // progress bar và text msg khi có lỗi hoặc thông báo
        protected ProgressBar mProgressBarItem = null;

        public RecycleViewItemHolder(View view) {
            super(view);
        }

        public ModelView getModelView() {
            return mModelView;
        }

        /**
         * Load layout cho view
         *
         * @param view
         */
        public void holdView(View view) {
            mLayoutMainView = view;
            mTxtItem1 = ((TextView) view.findViewById(mintLayoutModelViewText1 > 0 ? mintLayoutModelViewText1 : R.id.id_modelview_default_item_text1));
            mTxtItem2 = ((TextView) view.findViewById(mintLayoutModelViewText2 > 0 ? mintLayoutModelViewText2 : R.id.id_modelview_default_item_text2));
            mImageItem = ((ImageView) view.findViewById(mintLayoutModelViewImage > 0 ? mintLayoutModelViewImage : R.id.id_modelview_default_item_image));
            mProgressBarItem = ((ProgressBar) view.findViewById(mintLayoutModelViewProgress > 0 ? mintLayoutModelViewProgress : R.id.id_modelview_default_item_progressbar));
            mTxtItemMsg = ((TextView) view.findViewById(mintLayoutModelViewMsg > 0 ? mintLayoutModelViewMsg : R.id.id_modelview_default_item_textmsg));
            mLayoutContentView = view.findViewById(mintLayoutModelViewContent > 0 ? mintLayoutModelViewContent : R.id.id_modelview_default_item_content);
            mLayoutMainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update highlight list đã chọn
                    mRecycleView.getAdapter().notifyItemChanged(mintSelectedPos);
                    mintSelectedPos = getAdapterPosition();
                    mRecycleView.getAdapter().notifyItemChanged(mintSelectedPos);

                    // Thông báo sự kiện khi đã chọn 1 item trên danh sách cho controller
                    if (mController != null) {
                        mController.bindItem((TModel) mModelViewList.get(mintSelectedPos).getModel());
                        mController.doShowDetailPanel(true);
                    }
                }
            });
        }

        /**
         * Gán giá trị từ model từ lên view
         *
         * @param item
         * @param position
         */
        public void setItem(ModelView item, int position) {
            if (item == null) return;

            // hiển thị nội dung theo layout mặc định
            if (mTxtItem1 != null && mblnUseTxtView1)
                mTxtItem1.setText(item.getDisplayContent());
            if (mTxtItem2 != null && mblnUseTxtView2)
                mTxtItem2.setText(item.getSubDisplayContent());
            if (mImageItem != null && mblnUseImage) {
                mImageItem.setImageBitmap(item.getImageBitmap());
                mImageItem.setVisibility(item.getImageBitmap() == null ? View.GONE : View.VISIBLE);
            }

            // fill nội dung khác chi tiết cho panel
            itemView.setSelected(mintSelectedPos == position);

            // get state
            if (item.getViewState().isStateNormal()) {
                if (item.getModel() != null)
                    bindItem(mLayoutMainView, (TModel) item.getModel(), position);
                return;
            }
        }

        /**
         * Hiển thị progress bar
         */
        public void displayProgressBar(final ModelView modelView) {
            // không chỉ định progress bar, bỏ qua luôn
            if (mProgressBarItem == null) return;

            // đối với honey commb
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = (mLayoutMainView != null) ? mLayoutMainView.getResources().getInteger(android.R.integer.config_shortAnimTime) : 17694720;

                // hiển thị progress bar
                mProgressBarItem.setVisibility(modelView.getViewState().isStateLoading() ? View.VISIBLE : View.GONE);
                mProgressBarItem.animate().setDuration(shortAnimTime).alpha(
                        modelView.getViewState().isStateLoading() ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressBarItem.setVisibility(modelView.getViewState().isStateLoading() ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // che dấu
                mProgressBarItem.setVisibility(modelView.getViewState().isStateLoading() ? View.VISIBLE : View.GONE);
            }
        }

        /**
         * Hiển thị thông báo lỗi
         */
        public void displayErrorMsg(ModelView modelView) {
            // nếu chưa có text view cho msg thì bỏ qua
            if (mTxtItemMsg == null) return;

            // hiển thị txt view msg và hiển thị thông báo
            mTxtItemMsg.setVisibility(modelView.getViewState().isStateError() ? View.VISIBLE : View.GONE);
            if (modelView.getViewState().getStrMsg() != null)
                mTxtItemMsg.setText(modelView.getViewState().getStrMsg());
        }

        /**
         * Hiển thị phần content
         */
        public void displayContent(final ModelView modelView) {
            // nếu k0 chỉ định layout content, bỏ qua luôn
            if (mLayoutContentView == null) return;

            // hiển thị animation nếu là honeycomb
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = (mLayoutMainView != null) ? mLayoutMainView.getResources().getInteger(android.R.integer.config_shortAnimTime) : 17694720;

                // che dấu content và thông báo lỗi
                mLayoutContentView.setVisibility(modelView.getViewState().isStateNormal() ? View.VISIBLE : View.GONE);
//                mLayoutContentView.animate().setDuration(shortAnimTime).alpha(
//                        modelView.getViewState().isStateNormal() ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mLayoutContentView.setVisibility(modelView.getViewState().isStateNormal() ? View.VISIBLE : View.GONE);
//                    }
//                });
            } else {
                // hiển thị content
                mLayoutContentView.setVisibility(modelView.getViewState().isStateNormal() ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * Có sự thay đổi số liệu, cập nhật lại giao diện
     */
    public void notifyDataSetChanged() {
        if (mRecycleView == null) return;
        if (mRecycleView.getAdapter() != null)
            mRecycleView.getAdapter().notifyDataSetChanged();
    }
}