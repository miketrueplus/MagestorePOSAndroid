package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;

import java.util.List;

/**
 * Created by Mike on 2/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutDetailPanel extends AbstractDetailPanel<Checkout> {
    PaymentMethodListPanel mPaymentMethodListPanel;
    CheckoutPaymentListPanel mCheckoutPaymentListPanel;
    CheckoutShippingListPanel mCheckoutShippingListPanel;
    CheckoutAddPaymentPanel mCheckoutAddPaymentPanel;
    CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel;
    TextView txt_grand_total, txt_remain_title, txt_remain_value, txt_payment_creditcard, tv_delivery_date_time;
    RelativeLayout rl_content_payment_method, sales_background_loading, rl_remove_payment_credit_card, rl_delivery;
    MagestoreDialog dialog;
    Switch create_ship, create_invoice;
    ImageButton im_back;
    LinearLayout ll_checkout_add_payment, ll_shipping_address, ll_payment_credit_card;
    SimpleSpinner sp_shipping_method;
    Switch cb_pick_at_store;
    EditText et_checkout_note;

    public CheckoutDetailPanel(Context context) {
        super(context);
    }

    public CheckoutDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        txt_grand_total = (TextView) findViewById(R.id.txt_grand_total);
        im_back = (ImageButton) findViewById(R.id.im_back);

        cb_pick_at_store = (Switch) findViewById(R.id.cb_pick_at_store);
        ll_shipping_address = (LinearLayout) findViewById(R.id.ll_shipping_address);
        rl_content_payment_method = (RelativeLayout) findViewById(R.id.rl_content_payment_method);
        txt_remain_title = (TextView) findViewById(R.id.txt_remain_title);
        txt_remain_value = (TextView) findViewById(R.id.txt_remain_value);
        create_ship = (Switch) findViewById(R.id.create_ship);
        create_invoice = (Switch) findViewById(R.id.create_invoice);
        ll_checkout_add_payment = (LinearLayout) findViewById(R.id.ll_checkout_add_payment);
        sales_background_loading = (RelativeLayout) findViewById(R.id.sales_background_loading);
        sp_shipping_method = (SimpleSpinner) findViewById(R.id.sp_shipping_method);
        et_checkout_note = (EditText) findViewById(R.id.et_checkout_note);
        // credit card
        ll_payment_credit_card = (LinearLayout) findViewById(R.id.ll_payment_credit_card);
        txt_payment_creditcard = (TextView) findViewById(R.id.txt_payment_creditcard);
        rl_remove_payment_credit_card = (RelativeLayout) findViewById(R.id.rl_remove_payment_credit_card);

        // delivery
        rl_delivery = (RelativeLayout) findViewById(R.id.rl_delivery);
        tv_delivery_date_time = (TextView) findViewById(R.id.tv_delivery_date_time);

        im_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBack();
            }
        });

        // đặt sự kiện click nút add payment
        ll_checkout_add_payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddPayment();
            }
        });

        sp_shipping_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getShippingMethod();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cb_pick_at_store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (!check) {
                    ll_shipping_address.setVisibility(GONE);
                } else {
                    ll_shipping_address.setVisibility(VISIBLE);
                }
                ((CheckoutListController) getController()).changePickAtStoreAndReloadShipping();
            }
        });

        rl_remove_payment_credit_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckoutListController) getController()).onRemovePaymentCreditCard();
            }
        });

        rl_delivery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final CheckoutDeliveryPanel mCheckoutDeliveryPanel = new CheckoutDeliveryPanel(getContext());
                mCheckoutDeliveryPanel.initValue();
                final MagestoreDialog dialog = DialogUtil.dialog(getContext(), "", mCheckoutDeliveryPanel);
                dialog.setGoneDialogTitle(true);
                dialog.show();

                Button btn_time_now = (Button) dialog.findViewById(R.id.btn_time_now);
                Button btn_done = (Button) dialog.findViewById(R.id.btn_done);

                btn_time_now.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_delivery_date_time.setText(mCheckoutDeliveryPanel.showDataNow());
                        ((CheckoutListController) getController()).getSelectedItem().setDeliveryDate(mCheckoutDeliveryPanel.bindDateTimeNow());
                        dialog.dismiss();
                    }
                });

                btn_done.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_delivery_date_time.setText(mCheckoutDeliveryPanel.showData());
                        ((CheckoutListController) getController()).getSelectedItem().setDeliveryDate(mCheckoutDeliveryPanel.bindDateTime());
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void bindItem(Checkout item) {
        if (item == null) {
            return;
        }
        super.bindItem(item);
    }

    void onClickBack() {
        ((CheckoutListController) getController()).onBackTohome();
    }

    /**
     * Xử lý khi click thêm mới payment
     */
    void onClickAddPayment() {
        if (!((CheckoutListController) getController()).checkListPaymentDialog()) {
            String message = getContext().getString(R.string.sales_no_payment_method);
            // Tạo dialog và hiển thị
            com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
            return;
        }
        if (dialog == null) {
            dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.sales_add_payment), mCheckoutAddPaymentPanel);
            dialog.setGoneButtonSave(true);
        }
        dialog.show();
    }

    public void showPaymentMethod() {
        rl_content_payment_method.setVisibility(VISIBLE);
    }

    public String isCreateShip() {
        String strCreateShip = "0";
        if (create_ship.isChecked()) {
            strCreateShip = "1";
        }
        return strCreateShip;
    }

    public String isCreateInvoice() {
        String strCreateInvoice = "0";
        if (create_invoice.isChecked()) {
            strCreateInvoice = "1";
        }
        return strCreateInvoice;
    }

    public void isEnableButtonAddPayment(boolean enable) {
        ll_checkout_add_payment.setVisibility(enable ? VISIBLE : GONE);
    }

    public void isEnableCreateInvoice(boolean enable) {
        create_invoice.setVisibility(enable ? VISIBLE : GONE);
    }

    public void isCheckCreateInvoice(boolean isCheck) {
        create_invoice.setChecked(isCheck ? true : false);
    }

    public void isCheckCreateShip(boolean isCheck) {
        create_ship.setChecked(isCheck ? true : false);
    }

    public void isShowLoadingDetail(boolean isShow) {
        sales_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showPickAtStore(boolean isShow) {
        cb_pick_at_store.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showNotifiAddItems() {
        String message = getContext().getString(R.string.checkout_add_items);

        // Tạo dialog và hiển thị
        com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
    }

    public void bindTotalPrice(float totalPrice) {
        String total = ConfigUtil.formatPrice(totalPrice);
        txt_grand_total.setText(total);
        txt_remain_value.setText(total);
    }

    public void updateMoneyTotal(boolean type, float totalPrice) {
        String total = ConfigUtil.formatPrice(totalPrice);
        if (type) {
            txt_remain_title.setText(getContext().getString(R.string.sales_expected_change));
        } else {
            txt_remain_title.setText(getContext().getString(R.string.sales_remain_money));
        }
        txt_remain_value.setText(total);
    }

    public void setShippingDataSet(List<CheckoutShipping> listShipping) {
        sp_shipping_method.bind(listShipping.toArray(new CheckoutShipping[0]));
    }

    public void getShippingMethod() {
        ((CheckoutListController) getController()).resetPositionAddress();
        String code = sp_shipping_method.getSelection();
        isShowLoadingDetail(true);
        ((CheckoutListController) getController()).doInputSaveShipping(code);
    }

    public void selectDefaultShippingMethod(CheckoutShipping shipping) {
        isShowLoadingDetail(true);
        if (shipping != null) {
            String code = shipping.getCode();
            sp_shipping_method.setSelection(code);
            ((CheckoutListController) getController()).doInputSaveShipping(code);
        } else {
            ((CheckoutListController) getController()).doInputSaveShipping("");
        }
    }

    public String getNote() {
        return et_checkout_note.getText().toString();
    }

    public boolean getPickAtStore() {
        return cb_pick_at_store.isChecked();
    }

    public void setPickAtStoreDefault() {
        cb_pick_at_store.setChecked(false);
    }

    public void showNotifiSelectPayment() {
        String message = getContext().getString(R.string.sales_show_notifi_select_payment);

        // Tạo dialog và hiển thị
        com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
    }

    /**
     * Tham chiếu sang panel payment method list
     */
    public void setPaymentMethodListPanel(PaymentMethodListPanel panel) {
        mPaymentMethodListPanel = panel;
    }

    public void setCheckoutPaymentListPanel(CheckoutPaymentListPanel mCheckoutPaymentListPanel) {
        this.mCheckoutPaymentListPanel = mCheckoutPaymentListPanel;
    }

    public void setCheckoutShippingListPanel(CheckoutShippingListPanel mCheckoutShippingListPanel) {
        this.mCheckoutShippingListPanel = mCheckoutShippingListPanel;
    }

    public void setCheckoutAddPaymentPanel(CheckoutAddPaymentPanel mCheckoutAddPaymentPanel) {
        this.mCheckoutAddPaymentPanel = mCheckoutAddPaymentPanel;
    }

    public void setCheckoutPaymentCreditCardPanel(CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel) {
        this.mCheckoutPaymentCreditCardPanel = mCheckoutPaymentCreditCardPanel;
    }

    public void showPanelCheckoutPayment() {
        mPaymentMethodListPanel.setVisibility(GONE);
        mCheckoutPaymentListPanel.setVisibility(VISIBLE);
    }

    public void showPanelPaymentMethod() {
        mPaymentMethodListPanel.setVisibility(VISIBLE);
        mCheckoutPaymentListPanel.setVisibility(GONE);
    }

    public void showPanelCheckoutPaymentCreditCard(boolean isShow) {
        mPaymentMethodListPanel.setVisibility(isShow ? GONE : VISIBLE);
        ll_payment_credit_card.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void updateTitlePaymentCreditCard(String method) {
        txt_payment_creditcard.setText(method);
    }

    public void dismissDialogAddPayment() {
        dialog.dismiss();
    }
}
