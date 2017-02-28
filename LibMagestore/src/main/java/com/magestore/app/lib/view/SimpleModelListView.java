package com.magestore.app.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.magestore.app.lib.R;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.adapter.SimpleModelAdapter;

import java.util.List;

/**
 * Spinner đơn giản, cho phép set luôn nội dung dưới dạng chuỗi String hoặc Map
 * Created by Mike on 2/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SimpleModelListView extends ListView {
    // có scroll hay không
    boolean mNoScroll;

    // chứa dữ liệu
    Model[] mList;

    public SimpleModelListView(Context context) {
        super(context);
    }



    public SimpleModelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public SimpleModelListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
        initLayout();
    }

    /**
     * Đọc các thuộc tính của layout
     * @param context
     * @param attrs
     */
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        mNoScroll = a.getBoolean(R.styleable.magestore_view_layout_no_scroll, true);
        a.recycle();
    }

    public void initLayout() {
    }

    public void initModel() {
    }

    public void initValue() {
    }

    /**
     * Sử dụng một mảng model để hiển thị trên spinner
     * @param models
     */
    private void bind(Model... models) {
        mList = models;
        SimpleModelAdapter<Model> adapter = new SimpleModelAdapter<>(getContext(), mList);
        setAdapter(adapter);
    }

    /**
     * Sử dụng 1 list models để hiển thị trên spinner
     * @param models
     */
    public void bind(List<Model> models) {
        bind(models.toArray(new Model[0]));
    }

    /**
     * Chỉ định selection
     * @param strKey
     */
    public void setSelection(String strKey) {
        if (mList == null) return;
        for (int i = 0; i < mList.length; i++) {
            if (mList[i] != null && mList[i].getID() != null && mList[i].getID().equals(strKey)) {
                setSelection(i);
                return;
            }
        }
    }

    /**
     * Chỉ định selection
     * @param model
     */
    public void setSelection(Model model) {
        if (mList == null) return;
        for (int i = 0; i < mList.length; i++) {
            if (mList[i] == model) {
                setSelection(i);
                return;
            }
        }
    }

    /**
     * Trả về sellection
     * @return
     */
    public Model getSelection() {
        int index = getSelectedItemPosition();
        return mList[index];
    }

    /**
     * Xuwr
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Nếu k0 tự động thay đổi chiều cao
        if (!mNoScroll) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
