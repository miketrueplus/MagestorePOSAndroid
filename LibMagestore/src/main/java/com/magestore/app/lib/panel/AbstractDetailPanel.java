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
        extends AbstractPanel<ListController<TModel>>
        implements MagestoreView<ListController<TModel>> {
    // Model chính
    private TModel mItem;

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo panel
     * @param context
     */
    public AbstractDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Return item
     * @return
     */
    public TModel getItem() {
        return mItem;
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
     * Bind dữ liệu từ giao diện vào item
     * @param item
     */
    public void bind2Item(TModel item) {

    }
}