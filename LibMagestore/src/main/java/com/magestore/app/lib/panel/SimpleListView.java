package com.magestore.app.lib.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.magestore.app.lib.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mike on 2/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SimpleListView extends ListView  {
    // ID Layout của rowview
    int mListLayout;
    boolean mNoScroll;

    // chứa dữ liệu
    String[] mListKeys;

    public SimpleListView(Context context) {
        super(context);
    }

    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public SimpleListView(Context context, AttributeSet attrs, int defStyle) {
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
        mListLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        if (mListLayout == -1)
            mListLayout = a.getResourceId(R.styleable.magestore_view_layout_row, -1);
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
     * Xử lý nếu không muốn có scroll
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

    /**
     * Sử dụng 1 chuỗi string để hiển thị trên spinner
     * @param values
     */
    public void bind(String... values) {
        mListKeys =  values;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), mListLayout, values);
        setAdapter(adapter);
    }

    /**
     * Sử dụng 1 map làm
     */
    public void bind(Map<String, String> mapValues) {
        // Lập map key và value
        ArrayList<String> values = new ArrayList<String>();
        mListKeys = mapValues.keySet().toArray(new String[0]);
        for (String key: mListKeys) values.add(mapValues.get(key));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), mListLayout, values);
        setAdapter(adapter);
    }

    /**
     * Chỉ định selection
     * @param strKey
     */
    public void setSelection(String strKey) {
        for (int i = 0; i < mListKeys.length; i++) {
            if (mListKeys[i].equals(strKey)) {
                setSelection(i);
                return;
            }
        }
    }

    /**
     * Trả về sellection
     * @return
     */
    public String getSelection() {
        int index = getSelectedItemPosition();
        return mListKeys[index];
    }
}
