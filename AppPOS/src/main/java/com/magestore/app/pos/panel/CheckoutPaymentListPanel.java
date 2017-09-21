package com.magestore.app.pos.panel;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardCheckoutPaymentContentBinding;
import com.magestore.app.util.AnimationView;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextFloat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutPaymentListPanel extends AbstractSimpleRecycleView<CheckoutPayment> {
    private final static int CODE_DELETE = -5;
    private final static int CODE_CANCEL = -3;
    private final static int CODE_ADD_00 = 55003;
    private final static int CODE_ADD_10 = 55005;
    private final static int CODE_ADD_20 = 55002;
    private final static int CODE_ADD_50 = 55001;
    private final static int CODE_PLACE_ORDER = 55004;
    RelativeLayout rl_keyboard_add_10, rl_keyboard_add_20, rl_keyboard_add_50, rl_keyboard_add_00,
            rl_keyboard_0, rl_keyboard_1, rl_keyboard_2, rl_keyboard_3, rl_keyboard_4,
            rl_keyboard_5, rl_keyboard_6, rl_keyboard_7, rl_keyboard_8, rl_keyboard_9,
            rl_keyboard_delete, rl_keyboard_hidden, rl_keyboard_place_order;
    CheckoutListController mCheckoutListController;
    Checkout mCheckout;
    List<EditTextFloat> listTextChangeValue;
    HashMap<CheckoutPayment, EditTextFloat> mapTextId;
    KeyboardView mKeyboardView;
    LinearLayout ll_custom_keyboard;
    boolean checkAmount = false;

    public void setLayoutCustomKeyboard(LinearLayout ll_custom_keyboard) {
        this.ll_custom_keyboard = ll_custom_keyboard;
    }

    public void setKeyboardView(KeyboardView mKeyboardView) {
        this.mKeyboardView = mKeyboardView;
    }

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutPaymentListPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        listTextChangeValue = new ArrayList<>();
        mapTextId = new HashMap<>();
    }

    @Override
    protected void bindItem(View view, CheckoutPayment item, final int position) {
        CardCheckoutPaymentContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setCheckoutPayment(item);

        CheckoutPayment checkoutPayment = mList.get(position);

        EditText reference_number = (EditText) view.findViewById(R.id.reference_number);
        reference_number.setText(item.getReferenceNumber());
        actionAddReferenceNumber(reference_number, checkoutPayment);

        rl_keyboard_add_10 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_add_10);
        rl_keyboard_add_20 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_add_20);
        rl_keyboard_add_50 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_add_50);
        rl_keyboard_add_00 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_add_00);
        rl_keyboard_0 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_0);
        rl_keyboard_1 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_1);
        rl_keyboard_2 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_2);
        rl_keyboard_3 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_3);
        rl_keyboard_4 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_4);
        rl_keyboard_5 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_5);
        rl_keyboard_6 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_6);
        rl_keyboard_7 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_7);
        rl_keyboard_8 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_8);
        rl_keyboard_9 = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_9);
        rl_keyboard_delete = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_delete);
        rl_keyboard_hidden = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_hidden);
        rl_keyboard_place_order = (RelativeLayout) ll_custom_keyboard.findViewById(R.id.rl_keyboard_place_order);

        rl_keyboard_add_10.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_add_20.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_add_50.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_add_00.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_0.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_1.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_2.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_3.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_4.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_5.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_6.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_7.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_8.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_9.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_delete.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_hidden.setOnClickListener(mOnKeyBoardClick);
        rl_keyboard_place_order.setOnClickListener(mOnKeyBoardClick);

        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        final EditTextFloat checkout_value = (EditTextFloat) view.findViewById(R.id.checkout_value);

        checkout_value.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    checkAmount = false;
                    checkout_value.setSelection(checkout_value.length());
                    showCustomKeyboard(v);
                } else {
                    checkout_value.setSelection(checkout_value.getText().length());
                    hideCustomKeyboard();
                }
            }
        });

        checkout_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAmount = false;
                showCustomKeyboard(v);
            }
        });

        checkout_value.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                checkout_value.setCursorVisible(false);
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType); // Restore input type
                edittext.setSelection(0, edittext.getText().length());
                return true; // Consume touch event
            }
        });

        mapTextId.put(item, checkout_value);
        checkout_value.setText(ConfigUtil.formatNumber(checkoutPayment.getAmount()));
        actionChangeValueTotal(checkout_value, mCheckout, checkoutPayment);

        TextView txt_suggest_1 = (TextView) view.findViewById(R.id.txt_suggest_payment_1);
        TextView txt_suggest_2 = (TextView) view.findViewById(R.id.txt_suggest_payment_2);
        TextView txt_suggest_3 = (TextView) view.findViewById(R.id.txt_suggest_payment_3);
        TextView txt_suggest_4 = (TextView) view.findViewById(R.id.txt_suggest_payment_4);

        actionSuggestPayment(item, checkout_value, txt_suggest_1, txt_suggest_2, txt_suggest_3, txt_suggest_4);

        RelativeLayout rl_remove_payment = (RelativeLayout) view.findViewById(R.id.rl_remove_payment);
        rl_remove_payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionRemovePayment(position);
            }
        });
    }

    @Override
    protected void onClickItem(View view, CheckoutPayment item, int position) {
//        CheckoutPayment checkoutPayment = mList.get(position);
    }

    public void updateTotal(List<CheckoutPayment> listPayment) {
        float totalValue = 0;

        if (mCheckout != null) {
            float grand_total = mCheckout.getGrandTotal();

            for (CheckoutPayment payment : listPayment) {
                totalValue += payment.getAmount();
            }

            if (totalValue >= grand_total) {
                float money = totalValue - grand_total;
                mCheckout.setExchangeMoney(money);
                mCheckout.setRemainMoney(0);
                mCheckoutListController.updateMoneyTotal(true, money);
                mCheckoutListController.updateMaxAmountStoreCredit(grand_total);
                // disable add payment
                mCheckoutListController.isEnableButtonAddPayment(false);
                mCheckoutListController.isEnableCreateInvoice(true);
                mCheckoutListController.changeTitlePlaceOrder(false);
            } else {
                float money = grand_total - totalValue;
                mCheckout.setRemainMoney(money);
                mCheckout.setExchangeMoney(0);
                mCheckoutListController.updateMoneyTotal(false, money);
                mCheckoutListController.updateMaxAmountStoreCredit(money);
                if (listPayment.size() == 1) {
                    if (listPayment.get(0).isPaylater().equals("1")) {
                        mCheckoutListController.isEnableButtonAddPayment(true);
                    } else {
                        mCheckoutListController.isEnableButtonAddPayment(totalValue > 0 ? true : false);
                    }
                } else {
                    mCheckoutListController.isEnableButtonAddPayment(totalValue > 0 ? true : false);
                }
                mCheckoutListController.isEnableCreateInvoice(false);
                if (mCheckoutListController.getSelectedItem().getStatus() == CheckoutListController.STATUS_CHECKOUT_PROCESSING) {
                    if (money == grand_total) {
                        if (mCheckoutListController.getListChoosePayment() != null && mCheckoutListController.getListChoosePayment().size() > 0) {
                            mCheckoutListController.changeTitlePlaceOrder(true);
                        } else {
                            mCheckoutListController.changeTitlePlaceOrder(false);
                        }
                    } else {
                        mCheckoutListController.changeTitlePlaceOrder(true);
                    }
                }
            }
        }
    }

    private void actionRemovePayment(int position) {
        mapTextId.remove(mList.get(position));
        mList.remove(position);
        float grand_total = mCheckout.getGrandTotal();
        float totalValue = 0;
        float allRowTotal;

        for (EditTextFloat edt_value : mapTextId.values()) {
            allRowTotal = edt_value.getValueFloat();
            totalValue += allRowTotal;
        }

        if (totalValue >= grand_total) {
            float money = totalValue - grand_total;
            mCheckout.setExchangeMoney(money);
            mCheckoutListController.updateMoneyTotal(true, money);
            mCheckoutListController.updateMaxAmountStoreCredit(grand_total);
            // disable add payment
            mCheckoutListController.isEnableButtonAddPayment(false);
            mCheckoutListController.isEnableCreateInvoice(true);
            mCheckoutListController.changeTitlePlaceOrder(false);
        } else {
            float money = grand_total - totalValue;
            mCheckout.setRemainMoney(money);
            mCheckoutListController.updateMoneyTotal(false, money);
            mCheckoutListController.updateMaxAmountStoreCredit(money);
            mCheckoutListController.isEnableButtonAddPayment(totalValue > 0 ? true : false);
            mCheckoutListController.isEnableCreateInvoice(false);
            if (money == grand_total) {
                if (mList.size() > 0) {
                    mCheckoutListController.changeTitlePlaceOrder(true);
                } else {
                    mCheckoutListController.changeTitlePlaceOrder(false);
                }
            } else {
                mCheckoutListController.changeTitlePlaceOrder(true);
            }
        }
        if (checkPaymentStoreCredit()) {
            mCheckoutListController.isShowPluginStoreCredit(false);
        }
        notifyDataSetChanged();
        mCheckoutListController.onRemovePaymentMethod();
    }

    private void actionAddReferenceNumber(final EditText reference_number, final CheckoutPayment checkoutPayment) {
        reference_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String referenceNumber = reference_number.getText().toString().trim();
                checkoutPayment.setReferenceNumber(referenceNumber);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionChangeValueTotal(final EditTextFloat checkout_value, final Checkout mCheckout, final CheckoutPayment checkoutPayment) {
        checkout_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float grand_total = mCheckout.getGrandTotal();
                float totalValue = 0;
                float currentValue;
                float allRowTotal;
                float totalNotExchange = 0;

                currentValue = checkout_value.getValueFloat();

                for (EditTextFloat edt_value : mapTextId.values()) {
                    edt_value.setEnabled(true);
                    allRowTotal = edt_value.getValueFloat();
                    totalValue += allRowTotal;
                }

                if (totalValue > grand_total) {
                    for (EditTextFloat edt_value : mapTextId.values()) {
                        if (edt_value == checkout_value) {
                            checkout_value.setEnabled(true);
                        } else {
                            edt_value.setEnabled(false);
                            allRowTotal = edt_value.getValueFloat();
                            totalNotExchange += allRowTotal;
                        }
                    }

                    float money = totalValue - grand_total;
                    mCheckout.setExchangeMoney(money);
                    mCheckoutListController.updateMoneyTotal(true, money);

                    float remain_money = grand_total - totalNotExchange;
                    checkoutPayment.setAmount(currentValue);
                    checkoutPayment.setBaseAmount(currentValue);
                    checkoutPayment.setRealAmount(remain_money);
                    checkoutPayment.setBaseRealAmount(remain_money);
                    // disable add payment
                    mCheckoutListController.isEnableButtonAddPayment(false);
                    mCheckoutListController.isEnableCreateInvoice(true);
                    mCheckoutListController.changeTitlePlaceOrder(false);
                } else {
                    float money = grand_total - totalValue;
                    mCheckout.setRemainMoney(money);
                    mCheckoutListController.updateMoneyTotal(false, money);
                    checkoutPayment.setAmount(currentValue);
                    checkoutPayment.setBaseAmount(currentValue);
                    checkoutPayment.setRealAmount(currentValue);
                    checkoutPayment.setBaseRealAmount(currentValue);
                    if (totalValue == grand_total) {
                        mCheckoutListController.updateMaxAmountStoreCredit(grand_total);
                        mCheckoutListController.isEnableButtonAddPayment(false);
                        mCheckoutListController.isEnableCreateInvoice(true);
                        mCheckoutListController.changeTitlePlaceOrder(false);
                    } else {
                        mCheckoutListController.updateMaxAmountStoreCredit(money);
                        mCheckoutListController.isEnableButtonAddPayment(true);
                        mCheckoutListController.isEnableCreateInvoice(false);
                        mCheckoutListController.changeTitlePlaceOrder(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionSuggestPayment(CheckoutPayment checkoutPayment, final EditTextFloat checkout_value, TextView txt_suggest_1, TextView txt_suggest_2, TextView txt_suggest_3, TextView txt_suggest_4) {
        final float amount = checkoutPayment.getRealAmount();

        txt_suggest_1.setText(ConfigUtil.formatNumber(amount));

        int value_2 = ((((int) (amount / 10)) + 1) * 10);
        txt_suggest_2.setText(ConfigUtil.formatNumber(value_2));

        int value_3 = (int) ((((int) (amount / 50)) + 1) * 50);
        if (value_3 == value_2) {
            value_3 = value_3 + (int) ((((int) (amount / 50)) + 2) * 50);
        }
        txt_suggest_3.setText(ConfigUtil.formatNumber(value_3));

        int value_4 = (int) ((((int) (amount / 100)) + 1) * 100);
        if (value_4 == value_3 || value_4 == value_2) {
            value_4 = (int) ((((int) (amount / 100)) + 2) * 100);
        }
        txt_suggest_4.setText(ConfigUtil.formatNumber(value_4));

        actionClickSuggest(1, txt_suggest_1, amount, 0, checkout_value);

        actionClickSuggest(2, txt_suggest_2, 0, value_2, checkout_value);

        actionClickSuggest(3, txt_suggest_3, 0, value_3, checkout_value);

        actionClickSuggest(4, txt_suggest_4, 0, value_4, checkout_value);
    }

    private void actionClickSuggest(int type, TextView txt_suggest, final float amount, final int value, final EditTextFloat checkout_value) {
        if (type == 1) {
            txt_suggest.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkout_value.setText(ConfigUtil.formatNumber(amount));
                }
            });
        } else if (type == 2 || type == 3 || type == 4) {
            txt_suggest.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkout_value.setText(ConfigUtil.formatNumber(value));
                }
            });
        }
    }

    // plugin store credit
    private boolean checkPaymentStoreCredit() {
        for (CheckoutPayment payment : mList) {
            if (payment.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                return true;
            }
        }
        return false;
    }

    public void removePaymentStoreCredit() {
        int count = 0;
        boolean checkPayment = false;
        if (mList != null && mList.size() > 0) {
            for (CheckoutPayment payment : mList) {
                if (payment.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                    checkPayment = true;
                    break;
                }
                count++;
            }
            if (checkPayment) {
                actionRemovePayment(count);
            }
        }
    }

    public void resetListPayment() {
        mapTextId = new HashMap<>();
    }

    public void setCheckout(Checkout mCheckout) {
        this.mCheckout = mCheckout;
    }

    public void hideCustomKeyboard() {
        AnimationView.collapse(ll_custom_keyboard);
        ll_custom_keyboard.setVisibility(View.GONE);
        ll_custom_keyboard.setEnabled(false);
    }

    public void showCustomKeyboard(View v) {
        AnimationView.expand(ll_custom_keyboard);
        ll_custom_keyboard.setVisibility(View.VISIBLE);
        ll_custom_keyboard.setEnabled(true);
        if (v != null)
            ((InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private OnClickListener mOnKeyBoardClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            View focusCurrent = mCheckoutListController.getMagestoreContext().getActivity().getWindow().getCurrentFocus();
            if (focusCurrent == null || focusCurrent.getClass() != EditTextFloat.class) return;
            EditTextFloat edittext = (EditTextFloat) focusCurrent;
            if (id == R.id.rl_keyboard_delete) {
                if (edittext.getValueFloat() > 0) {
                    String text_value = edittext.getText().toString();
                    String value = StringUtil.removeAllSymbol(text_value);
                    String text = value.substring(0, value.length() - 1);
                    edittext.setText(ConfigUtil.formatNumber(convertToPrice(text)));
                    checkAmount = true;
                }
            } else if (id == R.id.rl_keyboard_hidden) {
                edittext.setSelection(edittext.getText().length());
                hideCustomKeyboard();
                checkAmount = true;
            }
            if (!checkAmount) {
                edittext.setText(ConfigUtil.formatNumber(0.00));
            }
            if (id == R.id.rl_keyboard_place_order) {
                hideCustomKeyboard();
                mCheckoutListController.doInputPlaceOrder();
            } else if (id == R.id.rl_keyboard_add_00) {
                String text_value = edittext.getText().toString();
                String value = StringUtil.removeAllSymbol(text_value);
                String text = value + "00";
                edittext.setText(ConfigUtil.formatNumber(convertToPrice(text)));
                checkAmount = true;
            } else if (id == R.id.rl_keyboard_add_10) {
                float current_amount = edittext.getValueFloat();
                current_amount = current_amount + 10;
                edittext.setText(ConfigUtil.formatNumber(current_amount));
                edittext.setSelection(edittext.length());
                checkAmount = true;
            } else if (id == R.id.rl_keyboard_add_20) {
                float current_amount = edittext.getValueFloat();
                current_amount = current_amount + 20;
                edittext.setText(ConfigUtil.formatNumber(current_amount));
                edittext.setSelection(edittext.length());
                checkAmount = true;
            } else if (id == R.id.rl_keyboard_add_50) {
                float current_amount = edittext.getValueFloat();
                current_amount = current_amount + 50;
                edittext.setText(ConfigUtil.formatNumber(current_amount));
                edittext.setSelection(edittext.length());
                checkAmount = true;
            } else if (id == R.id.rl_keyboard_0) {
                actionCharacterKeyboard("0", edittext);
            } else if (id == R.id.rl_keyboard_1) {
                actionCharacterKeyboard("1", edittext);
            } else if (id == R.id.rl_keyboard_2) {
                actionCharacterKeyboard("2", edittext);
            } else if (id == R.id.rl_keyboard_3) {
                actionCharacterKeyboard("3", edittext);
            } else if (id == R.id.rl_keyboard_4) {
                actionCharacterKeyboard("4", edittext);
            } else if (id == R.id.rl_keyboard_5) {
                actionCharacterKeyboard("5", edittext);
            } else if (id == R.id.rl_keyboard_6) {
                actionCharacterKeyboard("6", edittext);
            } else if (id == R.id.rl_keyboard_7) {
                actionCharacterKeyboard("7", edittext);
            } else if (id == R.id.rl_keyboard_8) {
                actionCharacterKeyboard("8", edittext);
            } else if (id == R.id.rl_keyboard_9) {
                actionCharacterKeyboard("9", edittext);
            }
        }
    };

    private void actionCharacterKeyboard(String charater, EditTextFloat edittext) {
        String text_value = edittext.getText().toString();
        String value = StringUtil.removeAllSymbol(text_value);
        String text = value + charater;
        edittext.setText(ConfigUtil.formatNumber(convertToPrice(text)));
        checkAmount = true;
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // Get the EditText and its Editable
            View focusCurrent = mCheckoutListController.getMagestoreContext().getActivity().getWindow().getCurrentFocus();
            if (focusCurrent == null || focusCurrent.getClass() != EditTextFloat.class) return;
            EditTextFloat edittext = (EditTextFloat) focusCurrent;
            Editable editable = edittext.getText();
            int start = edittext.getSelectionStart();
            // Handle key
            if (primaryCode == CODE_CANCEL) {
                hideCustomKeyboard();
            } else if (primaryCode == CODE_PLACE_ORDER) {
                hideCustomKeyboard();
                mCheckoutListController.doInputPlaceOrder();
            } else if (primaryCode == CODE_DELETE) {
                if (edittext.getValueFloat() > 0) {
                    String text_value = edittext.getText().toString();
                    String value = StringUtil.removeAllSymbol(text_value);
                    String text = value.substring(0, value.length() - 1);
                    edittext.setText(ConfigUtil.formatNumber(convertToPrice(text)));
                }
            } else if (primaryCode == CODE_ADD_00) {
                String text_value = edittext.getText().toString();
                String value = StringUtil.removeAllSymbol(text_value);
                String text = value + "00";
                edittext.setText(ConfigUtil.formatNumber(convertToPrice(text)));
            } else if (primaryCode == CODE_ADD_10) {
                float current_amount = edittext.getValueFloat();
                current_amount = current_amount + 10;
                edittext.setText(ConfigUtil.formatNumber(current_amount));
                edittext.setSelection(edittext.length());
            } else if (primaryCode == CODE_ADD_20) {
                float current_amount = edittext.getValueFloat();
                current_amount = current_amount + 20;
                edittext.setText(ConfigUtil.formatNumber(current_amount));
                edittext.setSelection(edittext.length());
            } else if (primaryCode == CODE_ADD_50) {
                float current_amount = edittext.getValueFloat();
                current_amount = current_amount + 50;
                edittext.setText(ConfigUtil.formatNumber(current_amount));
                edittext.setSelection(edittext.length());
            } else {
                String charater = Character.toString((char) primaryCode);
                String text_value = edittext.getText().toString();
                String value = StringUtil.removeAllSymbol(text_value);
                String text = value + charater;
                edittext.setText(ConfigUtil.formatNumber(convertToPrice(text)));
            }
        }

        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private float convertToPrice(String amount) {
        String decima_symbol = ConfigUtil.getConfigPriceFormat().getDecimalSymbol();
        String text_f = amount.substring(0, amount.length() - 2);
        String text_s = amount.substring(amount.length() - 2, amount.length());
        return ConfigUtil.parseFloat(text_f + decima_symbol + text_s);
    }
}
