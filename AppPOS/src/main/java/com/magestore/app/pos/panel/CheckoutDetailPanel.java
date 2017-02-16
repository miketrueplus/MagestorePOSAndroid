package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.observe.GenericState;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;

/**
 * Created by Mike on 2/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutDetailPanel extends AbstractDetailPanel<Checkout> {
    PaymentMethodListPanel mPaymentMethodListPanel;
    Checkout mCheckout;
    CheckoutPaymentListPanel mCheckoutPaymentListPanel;

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

        // đặt sự kiện click nút ađ payment
        ((Button) findViewById(R.id.btn_checkout_add_payment)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddPayment();
            }
        });

        ((Button) findViewById(R.id.btn_checkout_place_holder)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlaceHolder();
            }
        });
    }

    @Override
    public void bindItem(Checkout item) {
        if (item == null) {
            return;
        }
        super.bindItem(item);
        mCheckout = item;
    }

    /**
     * Xử lý khi click thêm mới payment
     */
    void onClickAddPayment() {

    }

    /**
     * Xử lý khi thanh toán (Place holder hoặc Partial)
     */
    void onClickPlaceHolder() {

        //TODO: test thử observe
        GenericState<CheckoutListController> state = new GenericState<CheckoutListController>(null, CheckoutListController.STATE_ON_PLACE_ORDER);
        getController().getSubject().setState(state);
        ((CheckoutListController) getController()).onPlaceOrder();

        ((CheckoutListController) getController()).doInputPlaceOrder();
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

    public void showPanelCheckoutPayment() {
        mPaymentMethodListPanel.setVisibility(GONE);
        mCheckoutPaymentListPanel.setVisibility(VISIBLE);
    }

    public void showPanelPaymentMethod() {
        mPaymentMethodListPanel.setVisibility(VISIBLE);
        mCheckoutPaymentListPanel.setVisibility(GONE);
    }
}
