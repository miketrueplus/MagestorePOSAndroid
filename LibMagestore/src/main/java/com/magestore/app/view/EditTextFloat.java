package com.magestore.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.MetaKeyKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.util.ConfigUtil;

/**
 * Created by Mike on 1/19/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class EditTextFloat extends EditText {
    String mValue;

    public EditTextFloat(Context context) {
        super(context);
        initModel();
        initEvent();
    }

    public EditTextFloat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initModel();
        initEvent();
    }

    public EditTextFloat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initModel();
        initEvent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextFloat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initModel();
        initEvent();
    }

//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        setValue(text.toString());
//    }

    protected void initModel() {
        setInputType(InputType.TYPE_CLASS_PHONE);
//        DigitsKeyListener.getInstance("123");
//        setKeyListener(DigitsKeyListener.getInstance(ConfigUtil.getFloatDigit()));
    }

//    public void setValue(double value) {
//        super.setText(ConfigUtil.formatNumber(value));
//    }
//
//    public void setValue(float value) {
//        super.setText(ConfigUtil.formatNumber(value));
//    }
//
//    public void setValue(String value) {
//        super.setText(ConfigUtil.formatFloat(value));
//    }
//

    public void setMinValue(float flMin) {

    }

    public void setMaxValue(float flMax) {

    }

    public String getValue() {
        return ConfigUtil.truncateFloatDigit(getText().toString());
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
     * Chuẩn bị các sự kiện
     */
    protected void initEvent() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // lấy giá trị trong ô text,
//                    getItem().setUnitPrice(ConfigUtil.parseFloatCurrencyFromEdit(DecimalEditText.super.getText().toString().replaceAll("[^" + ConfigUtil.getPriceDigit() + "]", "")));
                    setText(ConfigUtil.formatFloat(ConfigUtil.truncateFloatDigit(getText().toString())));
                    clearFocus();
                } else {
                    // fill giá trị vào, nguyên số để edit, bỏ ký tự tiền
                    selectAll();
                }
            }
        });
    }
}