package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
    TextView txt_grand_total, txt_remain_title, txt_remain_value;
    RelativeLayout rl_content_payment_method, sales_background_loading;
    MagestoreDialog dialog;
    Switch create_ship, create_invoice;
    ImageButton im_back;
    LinearLayout ll_checkout_add_payment;
    SimpleSpinner sp_shipping_method;
    Switch cb_pick_as_store;
    LinearLayout ll_shipping_address;
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

        cb_pick_as_store = (Switch) findViewById(R.id.cb_pick_as_store);
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

        cb_pick_as_store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    ll_shipping_address.setVisibility(GONE);
                } else {
                    getShippingMethod();
                    ll_shipping_address.setVisibility(VISIBLE);
                }
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
        ((CheckoutListController) getController()).showSalesShipping();
        ((CheckoutListController) getController()).showActionButtonCheckout();
        ((CheckoutListController) getController()).doShowDetailPanel(false);
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
        if (enable) {
            ll_checkout_add_payment.setVisibility(VISIBLE);
        } else {
            ll_checkout_add_payment.setVisibility(GONE);
        }
    }

    public void isEnableCreateInvoice(boolean enable) {
        if (enable) {
            create_invoice.setVisibility(VISIBLE);
        } else {
            create_invoice.setVisibility(GONE);
        }
    }

    public void isShowLoadingDetail(boolean isShow) {
        if (isShow) {
            sales_background_loading.setVisibility(VISIBLE);
        } else {
            sales_background_loading.setVisibility(GONE);
        }
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
        String code = sp_shipping_method.getSelection();
        isShowLoadingDetail(true);
        ((CheckoutListController) getController()).doInputSaveShipping(code);
    }

    public String getNote(){
        return et_checkout_note.getText().toString();
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

    public void showPanelCheckoutPayment() {
        mPaymentMethodListPanel.setVisibility(GONE);
        mCheckoutPaymentListPanel.setVisibility(VISIBLE);
    }

    public void showPanelPaymentMethod() {
        mPaymentMethodListPanel.setVisibility(VISIBLE);
        mCheckoutPaymentListPanel.setVisibility(GONE);
    }

    public void dismissDialogAddPayment() {
        dialog.dismiss();
    }
}
