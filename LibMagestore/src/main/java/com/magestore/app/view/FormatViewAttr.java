package com.magestore.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.magestore.app.lib.R;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.util.ConfigUtil;

/**
 * Quản lý mapp từ attr trong layout sang các thuộc tính của view
 * Created by Mike on 1/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class FormatViewAttr {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_CURRENCY = 1;
    public static final int TYPE_QUANTITY = 2;
    public static final int TYPE_TIME = 3;
    public static final int TYPE_DATE = 4;

    String mstrPrefix;
    String mstrPostfix;
    String mchrPrefix;
    int mintDataType;

    /**
     * Khởi tạo với Attr đã được đặt
     * @param context
     * @param attrs
     */
    public FormatViewAttr(Context context, AttributeSet attrs) {
        processAttributeSet(context, attrs);
    }

    /**
     * Truy cập lấy các biến thuộc tính
     * @param context
     * @param attrs
     */
    protected void processAttributeSet(Context context, AttributeSet attrs) {
        // Đọc các thuộc tính attr
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);

        // các thuộc tính với text view
        mstrPrefix = a.getString(R.styleable.magestore_view_prefix);
        mstrPostfix = a.getString(R.styleable.magestore_view_postfix);
        mchrPrefix = a.getString(R.styleable.magestore_view_prefix_char);
        mintDataType = a.getInt(R.styleable.magestore_view_data_type, TYPE_TEXT);

        // Recycle
        a.recycle();
    }

    private CharSequence format(float number) {
        String value = null;
        switch(mintDataType) {
            case TYPE_CURRENCY: // currency
                value = ConfigUtil.formatPrice(number);
                break;
            case TYPE_QUANTITY: // quantity
                value = ConfigUtil.formatQuantity(number);
                break;
            case 3: // time
                break;
            case 4: // date
                break;
            default: // text
        }
        return value.subSequence(0, value.length());
    }

    private CharSequence format(int number) {
        String value = null;
        switch(mintDataType) {
            case TYPE_CURRENCY: // currency
                value = ConfigUtil.formatPrice(number);
                break;
            case TYPE_QUANTITY: // quantity
                value = ConfigUtil.formatQuantity(number);
                break;
            case 3: // time
                break;
            case 4: // date
                break;
            default: // text
        }
        return value.subSequence(0, value.length());
    }

    private CharSequence format(String originalValue) {
        String value = null;
        switch(mintDataType) {
            case TYPE_CURRENCY: // currency
                value = ConfigUtil.formatPrice(originalValue);
                break;
            case TYPE_QUANTITY: // quantity
                value = ConfigUtil.formatQuantity(originalValue);
                break;
            case 3: // time
                break;
            case 4: // date
                break;
            default: // text
        }
        return value.subSequence(0, value.length());
    }

    public CharSequence buildText(String value) {
        return buildText(format(value));
    }

    public CharSequence buildText(float value) {
        return buildText(format(value));
    }

    public CharSequence buildText(int value) {
        return buildText(format(value));
    }

    /**
     * Chuyển đổi text
     * @param text
     * @return
     */
    public CharSequence buildText(CharSequence text) {
        StringBuilder builder = new StringBuilder();

        // Chèn prefix
        if (mstrPrefix != null) builder.append(mstrPrefix);
        if (mchrPrefix != null) builder.append(mchrPrefix);

        // Chèn ký tự ngăn giữa prefix và nội dung chính
        if (mstrPrefix != null || mchrPrefix != null) builder.append(' ');

        // chèn nội dung chính
        builder.append(text);

        // Chèn postfix
        if (mstrPostfix != null) builder.append(mstrPostfix);

        // return
         return builder;
    }
}
