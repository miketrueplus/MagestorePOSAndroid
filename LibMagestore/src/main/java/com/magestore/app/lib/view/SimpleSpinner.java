package com.magestore.app.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.magestore.app.lib.R;
import com.magestore.app.lib.model.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Spinner đơn giản, cho phép set luôn nội dung dưới dạng chuỗi String hoặc Map
 * Created by Mike on 2/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SimpleSpinner extends Spinner {
    // ID Layout của rowview
    int mListLayout;
    boolean mNoScroll;

    // chứa dữ liệu
    String[] mListKeys;
    ArrayList<String> mListValues;

    public SimpleSpinner(Context context) {
        super(context);
    }

    public SimpleSpinner(Context context, int mode) {
        super(context, mode);
    }

    public SimpleSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public SimpleSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
        initLayout();
    }

    public SimpleSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
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
     * Sử dụng 1 chuỗi string để hiển thị trên spinner
     * @param values
     */
    public void bind(String... values) {
        mListKeys = values;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), mListLayout, values);
        setAdapter(adapter);
    }

    /**
     * Sử dụng một mảng model để hiển thị trên spinner
     * @param models
     */
    public void bind(Model... models) {
        Map<String, String> mapModel = new HashMap<String, String>();
        for (Model model: models)
            mapModel.put(model.getID(), model.getDisplayContent());
        bind(mapModel);
    }

    /**
     * Sử dụng 1 list models để hiển thị trên spinner
     * @param models
     */
    public void bind(List<Model> models) {
        bind(models.toArray(new Model[0]));
    }


    /**
     * Sử dụng 1 map làm
     */
    public void bind(Map<String, String> mapValues) {
        // Lập map key và value
        mListValues = new ArrayList<String>();
        mListKeys = mapValues.keySet().toArray(new String[0]);
        for (String key: mListKeys) mListValues.add(mapValues.get(key));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), mListLayout, mListValues);
        setAdapter(adapter);
    }

    /**
     * Sử dụng 1 map làm
     */
    public void bindModelMap(Map<String, Model> mapValues) {
        // Lập map key và value
        mListValues = new ArrayList<String>();
        mListKeys = mapValues.keySet().toArray(new String[0]);
        for (String key: mListKeys) mListValues.add(mapValues.get(key).getDisplayContent());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), mListLayout, mListValues);
        setAdapter(adapter);
    }

    /**
     * Chỉ định selection
     * @param strKey
     */
    public void setSelection(String strKey) {
        if (mListKeys == null) return;
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

    /**
     * Trả về value
     * @return
     */
    public String getSelectionValue() {
        int index = getSelectedItemPosition();
        return mListValues.get(index);
    }
}
