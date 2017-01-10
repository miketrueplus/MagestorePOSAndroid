package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.magestore.app.pos.controller.ListController;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractDetailPanel<TModel> extends FrameLayout {
    TModel mItem;
    ListController<TModel> mController;

    public AbstractDetailPanel(Context context) {
        super(context);
        init();
    }

    public AbstractDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
    }

    protected void initControlLayout() {

    }
    protected void initControlValue() {

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
     * Đặt controller điều khiển
     * @param controller
     */
    public void setController(ListController<TModel> controller) {
        mController = controller;
    }
}
