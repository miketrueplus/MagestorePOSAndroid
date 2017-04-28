package com.magestore.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.util.ConfigUtil;

/**
 * Created by Mike on 1/19/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class EditTextInteger extends EditText {
    String mValue;
    int mintMinValue;
    int mintMaxValue;
    boolean mblnHaveMinValue;
    boolean mblnHaveMaxValue;

    public EditTextInteger(Context context) {
        super(context);
        initModel();
        initEvent();
    }

    public EditTextInteger(Context context, AttributeSet attrs) {
        super(context, attrs);
        initModel();
        initEvent();
    }

    public EditTextInteger(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initModel();
        initEvent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextInteger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initModel();
        initEvent();
    }

//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        setValue(text.toString());
//    }

    protected void initModel() {
//        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        setKeyListener(DigitsKeyListener.getInstance(ConfigUtil.getFloatDigit()));
        mblnHaveMaxValue = false;
        mblnHaveMinValue = false;
    }
    public String getValue() {
        return ConfigUtil.truncateIntegerDigit(getText().toString());
    }

    public float getValueFloat() {
        return ConfigUtil.parseFloat(super.getText().toString());
    }

    public double getValueDouble() {
        return ConfigUtil.parseDouble(super.getText().toString());
    }

    public int getValueInteger() {
        return ConfigUtil.parseInteger(super.getText().toString());
    }

    /**
     * Tăng giá trị
     * @param addValue
     */
    public void add(int addValue) {
        int value = ConfigUtil.parseInteger(super.getText().toString());
        value += addValue;
        if (mblnHaveMaxValue && value > mintMaxValue) return;
        setText(ConfigUtil.formatNumber(value));
    }

    /**
     * Trừ giá trị
     * @param addValue
     */
    public void substract(int addValue) {
        int value = ConfigUtil.parseInteger(super.getText().toString());
        value -= addValue;
        if (mblnHaveMinValue && value < mintMinValue) return;
        setText(ConfigUtil.formatNumber(value));
    }

    /**
     * Đặt giá trị min
     * @param minValue
     */
    public void setMinValue(int minValue) {
        mintMinValue = minValue;
        mblnHaveMinValue = true;
    }

    /**
     * Đặt giá trị max
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        mintMaxValue = maxValue;
        mblnHaveMaxValue = true;
    }

    /**
     * Chuẩn bị các sự kiện
     */
    protected void initEvent() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // lấy giá trị trong ô text, căn lại giữa max và min
                    int value = ConfigUtil.parseInteger(ConfigUtil.truncateIntegerDigit(getText().toString()));
                    if (mblnHaveMinValue && value < mintMinValue) value = mintMinValue;
                    if (mblnHaveMaxValue && value > mintMaxValue) value = mintMaxValue;
                    setText(ConfigUtil.formatNumber(value));
                    clearFocus();
                } else {
                    // fill giá trị vào, nguyên số để edit, bỏ ký tự tiền
                    selectAll();
                }
            }
        });
    }
}