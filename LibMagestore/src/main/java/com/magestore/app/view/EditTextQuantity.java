package com.magestore.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.util.ConfigUtil;

/**
 * Created by Mike on 1/19/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class EditTextQuantity extends EditText {
    String mValue;
    float mintMinValue;
    float mintMaxValue;
    boolean mblnHaveMinValue;
    boolean mblnHaveMaxValue;

    public EditTextQuantity(Context context) {
        super(context);
        initModel();
        initEvent();
    }

    public EditTextQuantity(Context context, AttributeSet attrs) {
        super(context, attrs);
        initModel();
        initEvent();
    }

    public EditTextQuantity(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initModel();
        initEvent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextQuantity(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
     * Tăng giá trị
     * @param addValue
     */
    public void add(float addValue) {
        float value = ConfigUtil.parseFloat(super.getText().toString());
        value += addValue;
        if (mblnHaveMaxValue && value > mintMaxValue) return;
        setText(ConfigUtil.formatQuantity(value));
    }

    /**
     * Trừ giá trị
     * @param addValue
     */
    public void substract(float addValue) {
        float value = ConfigUtil.parseFloat(super.getText().toString());
        value -= addValue;
        if (mblnHaveMinValue && value < mintMinValue) return;
        setText(ConfigUtil.formatQuantity(value));
    }

    /**
     * Đặt giá trị min
     * @param minValue
     */
    public void setMinValue(float minValue) {
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
                    float value = ConfigUtil.parseFloat(ConfigUtil.truncateFloatDigit(getText().toString()));
                    if (mblnHaveMinValue && value < mintMinValue) value = mintMinValue;
                    if (mblnHaveMaxValue && value > mintMaxValue) value = mintMaxValue;
                    setText(ConfigUtil.formatQuantity(value));
                    clearFocus();
                } else {
                    // fill giá trị vào, nguyên số để edit, bỏ ký tự tiền
                    selectAll();
                }
            }
        });
    }
}