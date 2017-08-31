package com.magestore.app.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.MetaKeyKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.magestore.app.lib.R;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

/**
 * Created by Mike on 1/19/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class EditTextFloat extends EditText {
    String mValue;
    boolean isPriceFormat;
    boolean checkAmount;
    boolean isOrder;
    Activity mActivity;

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


    public void setPriceFormat(boolean priceFormat) {
        isPriceFormat = priceFormat;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    protected void initModel() {
        setInputType(InputType.TYPE_NULL);
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
        if (isPriceFormat) {
            if (super.getText().toString().length() < 4) {
                return ConfigUtil.parseFloat(super.getText().toString());
            } else {
                return convertToPrice(super.getText().toString());
            }
        } else {
            return ConfigUtil.parseFloat(super.getText().toString());
        }
    }

    public double getValueDouble() {
        return ConfigUtil.parseDouble(super.getText().toString());
    }

    public int getValueInteger() {
        return ConfigUtil.parseInteger(super.getText().toString());
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * Chuẩn bị các sự kiện
     */
    protected void initEvent() {
//        setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    // lấy giá trị trong ô text,
////                    getItem().setUnitPrice(ConfigUtil.parseFloatCurrencyFromEdit(DecimalEditText.super.getText().toString().replaceAll("[^" + ConfigUtil.getPriceDigit() + "]", "")));
//                    setText(ConfigUtil.formatFloat(ConfigUtil.truncateFloatDigit(getText().toString())));
//                    clearFocus();
//                } else {
//                    // fill giá trị vào, nguyên số để edit, bỏ ký tự tiền
//                    selectAll();
//                }
//            }
//        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int inType = getInputType();       // Backup the input type
                setCursorVisible(false);
                setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                onTouchEvent(motionEvent);               // Call native handler
                setInputType(inType); // Restore input type
                setSelection(0, getText().length());
                setError(null);
                if (exampleCardPopup == null || !exampleCardPopup.isShowing()) {
                    hideSoftKeyboard();
                    showCustomKeyboard();
                }
                return true; // Consume touch event
            }
        });
    }

    private PopupWindow popupWindow;
    private RelativeLayout rl_number_0, rl_number_1, rl_number_2, rl_number_3, rl_number_4, rl_number_5, rl_number_6, rl_number_7, rl_number_8, rl_number_9, rl_number_delete, rl_number_00;
    private ImageView im_arrow_up, im_arrow_down;
    ExampleCardPopup exampleCardPopup;

    private void showCustomKeyboard() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.layout_popup_keyboard, null);
        im_arrow_up = (ImageView) popupView.findViewById(R.id.im_arrow_up);
        im_arrow_down = (ImageView) popupView.findViewById(R.id.im_arrow_down);
        rl_number_00 = (RelativeLayout) popupView.findViewById(R.id.rl_number_00);
        rl_number_0 = (RelativeLayout) popupView.findViewById(R.id.rl_number_0);
        rl_number_1 = (RelativeLayout) popupView.findViewById(R.id.rl_number_1);
        rl_number_2 = (RelativeLayout) popupView.findViewById(R.id.rl_number_2);
        rl_number_3 = (RelativeLayout) popupView.findViewById(R.id.rl_number_3);
        rl_number_4 = (RelativeLayout) popupView.findViewById(R.id.rl_number_4);
        rl_number_5 = (RelativeLayout) popupView.findViewById(R.id.rl_number_5);
        rl_number_6 = (RelativeLayout) popupView.findViewById(R.id.rl_number_6);
        rl_number_7 = (RelativeLayout) popupView.findViewById(R.id.rl_number_7);
        rl_number_8 = (RelativeLayout) popupView.findViewById(R.id.rl_number_8);
        rl_number_9 = (RelativeLayout) popupView.findViewById(R.id.rl_number_9);
        rl_number_delete = (RelativeLayout) popupView.findViewById(R.id.rl_number_delete);
        rl_number_00.setOnClickListener(onClickNumberKeyboard);
        rl_number_0.setOnClickListener(onClickNumberKeyboard);
        rl_number_1.setOnClickListener(onClickNumberKeyboard);
        rl_number_2.setOnClickListener(onClickNumberKeyboard);
        rl_number_3.setOnClickListener(onClickNumberKeyboard);
        rl_number_4.setOnClickListener(onClickNumberKeyboard);
        rl_number_5.setOnClickListener(onClickNumberKeyboard);
        rl_number_6.setOnClickListener(onClickNumberKeyboard);
        rl_number_7.setOnClickListener(onClickNumberKeyboard);
        rl_number_8.setOnClickListener(onClickNumberKeyboard);
        rl_number_9.setOnClickListener(onClickNumberKeyboard);
        rl_number_delete.setOnClickListener(onClickNumberKeyboard);
        checkAmount = false;
        exampleCardPopup = new ExampleCardPopup(getContext(), popupView);
        exampleCardPopup.setOutsideTouchable(true);
        exampleCardPopup.setFocusable(true);
//        // Removes default background.
        exampleCardPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (isOrder) {
            im_arrow_up.setVisibility(GONE);
            im_arrow_down.setVisibility(VISIBLE);
            exampleCardPopup.showOnAnchor(this, RelativePopupWindow.VerticalPosition.ABOVE, RelativePopupWindow.HorizontalPosition.CENTER);
        } else {
            im_arrow_up.setVisibility(VISIBLE);
            im_arrow_down.setVisibility(GONE);
            exampleCardPopup.showOnAnchor(this, RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.CENTER);
        }
//        popupWindow = new PopupWindow(
//                popupView,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        // Removes default background.
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        im_arrow_down.setVisibility(GONE);
//        popupWindow.showAsDropDown(this);
//
        exampleCardPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!StringUtil.isNullOrEmpty(getText().toString())) {
                    setSelection(getText().toString().length());
                }
            }
        });
    }

    View.OnClickListener onClickNumberKeyboard = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (!checkAmount) {
                setText(isPriceFormat ? ConfigUtil.formatPrice("0.00") : "0.00");
            }
            if (id == R.id.rl_number_00) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_00));
            } else if (id == R.id.rl_number_0) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_0));
            } else if (id == R.id.rl_number_1) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_1));
            } else if (id == R.id.rl_number_2) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_2));
            } else if (id == R.id.rl_number_3) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_3));
            } else if (id == R.id.rl_number_4) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_4));
            } else if (id == R.id.rl_number_5) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_5));
            } else if (id == R.id.rl_number_6) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_6));
            } else if (id == R.id.rl_number_7) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_7));
            } else if (id == R.id.rl_number_8) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_8));
            } else if (id == R.id.rl_number_9) {
                actionCharacterKeyboard(getContext().getString(R.string.number_keyboard_9));
            } else if (id == R.id.rl_number_delete) {
                if (!StringUtil.isNullOrEmpty(getText().toString())) {
                    String text_value = getText().toString();
                    String value = StringUtil.removeAllSymbol(text_value);
                    String text = value.substring(0, value.length() - 1);
                    setText(isPriceFormat ? ConfigUtil.formatPrice(convertToPrice(text)) : ConfigUtil.formatNumber(convertToPrice(text)));
                    checkAmount = true;
                }
            }
        }
    };

    private void actionCharacterKeyboard(String charater) {
        String text_value = getText().toString();
        String value = StringUtil.removeAllSymbol(text_value);
        String text = value + charater;
        setText(isPriceFormat ? ConfigUtil.formatPrice(convertToPrice(text)) : ConfigUtil.formatNumber(convertToPrice(text)));
        checkAmount = true;
    }

    private float convertToPrice(String amount) {
        if (StringUtil.isNullOrEmpty(amount)) {
            amount = "0.00";
        }
        amount = StringUtil.removeAllSymbol(amount);
        String decima_symbol = ConfigUtil.getConfigPriceFormat().getDecimalSymbol();
        String text_f = amount.substring(0, amount.length() - 2);
        String text_s = amount.substring(amount.length() - 2, amount.length());
        return ConfigUtil.parseFloat(text_f + decima_symbol + text_s);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    public void dismisPopup(){
        if(exampleCardPopup != null){
            exampleCardPopup.dismiss();
        }
    }
}