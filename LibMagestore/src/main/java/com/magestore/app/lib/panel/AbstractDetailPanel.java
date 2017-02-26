package com.magestore.app.lib.panel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.MagestoreView;

/**
 * Quản lý 1 panel hiển thị thông tin chi tiết
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractDetailPanel<TModel extends Model>
        extends FrameLayout
        implements MagestoreView<ListController<TModel>> {
    // controller của danh sách
    protected ListController<TModel> mController;

    // tham chiếu layout của panel
    protected int mintPanelLayout;

    // Model chính
    protected TModel mItem;

    // view chính của panel
    View mView;

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context) {
        super(context);
        initLayout();
    }

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
        initLayout();
    }

    /**
     * Đọc attributes
     * @param context
     * @param attrs
     */
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, com.magestore.app.lib.R.styleable.magestore_view);
        mintPanelLayout = a.getResourceId(com.magestore.app.lib.R.styleable.magestore_view_layout_panel, -1);
        a.recycle();

        if (mintPanelLayout > -1) setLayoutPanel(mintPanelLayout);
    }

    /**
     * Thiết lập layout cho panel
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

    protected void initLayout() {
    }

    public void initModel() {
    }

    public void initValue() {
    }

    /**
     * Hiển thị tiến trình
     * @param show
     */
    public void showProgress(boolean show) {

    }

    /**
     * Gán danh sách và cập nhật view
     */
    public void bindItem(TModel item) {
        mItem = item;
        notifyDataSetChanged();
    }

    /**
     * Bind dữ liệu từ giao diện vào item
     * @return
     */
    public TModel bind2Item() {
        return mItem;
    }

    /**
     * Bind dữ liệu từ giao diện vào item
     * @param item
     */
    public void bind2Item(TModel item) {

    }

    /**
     * Đặt controller điều khiển
     * @param controller
     */
    public void setController(ListController<TModel> controller) {
        mController = controller;
    }

    /**
     * Trả về controller điều khiển
     * @return
     */
    public ListController<TModel> getController() {
        return mController;
    }

    /**
     * Hiển thị thông báo lỗi
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
                .setMessage(exp.getLocalizedMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void notifyDataSetChanged() {

    }

    @Override
    public void hideAllProgressBar() {

    }
}