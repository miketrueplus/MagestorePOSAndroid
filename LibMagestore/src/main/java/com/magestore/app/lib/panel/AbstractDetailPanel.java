package com.magestore.app.lib.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.MagestoreView;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractDetailPanel<TModel extends Model>
        extends FrameLayout
        implements MagestoreView<ListController<TModel>> {
    // controller của danh sách
    protected ListController<TModel> mController;

    // Model chính
    protected TModel mItem;

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
        initLayout();
    }

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
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
    }

    /**
     * Bind dữ liệu từ giao diện vào item
     * @return
     */
    public TModel bind2Item() {
        return mItem;
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
}
