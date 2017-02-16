package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.observe.GenericState;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

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
    Checkout mCheckout;
    TextView tv_shipping_method;
    RelativeLayout rl_title_shipping_method, rl_title_payment_method, rl_content_payment_method;
    ImageView im_shipping_arrow, im_payment_arrow;
    MagestoreDialog dialog;

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

        tv_shipping_method = (TextView) findViewById(R.id.tv_shipping_method);
        rl_title_shipping_method = (RelativeLayout) findViewById(R.id.rl_title_shipping_method);
        rl_title_payment_method = (RelativeLayout) findViewById(R.id.rl_title_payment_method);
        rl_content_payment_method = (RelativeLayout) findViewById(R.id.rl_content_payment_method);
        im_shipping_arrow = (ImageView) findViewById(R.id.im_shipping_arrow);
        im_payment_arrow = (ImageView) findViewById(R.id.im_payment_arrow);

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

        rl_title_shipping_method.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTitleShippingMethod();
            }
        });

        rl_title_payment_method.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTitlePaymentMethod();
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
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.sales_add_payment), mCheckoutAddPaymentPanel);
        dialog.setGoneButtonSave(true);
        dialog.show();
    }

    public void onClickTitleShippingMethod() {
        if (mCheckoutShippingListPanel.getVisibility() == VISIBLE) {
            mCheckoutShippingListPanel.setVisibility(GONE);
            im_shipping_arrow.setRotation(0);
        } else {
            mCheckoutShippingListPanel.setVisibility(VISIBLE);
            im_shipping_arrow.setRotation(180);
        }
    }

    public void onClickTitlePaymentMethod() {
        if (rl_content_payment_method.getVisibility() == VISIBLE) {
            rl_content_payment_method.setVisibility(GONE);
            im_payment_arrow.setRotation(0);
        } else {
            rl_content_payment_method.setVisibility(VISIBLE);
            im_payment_arrow.setRotation(180);
        }
    }

    public void showPaymentMethod(){
        rl_content_payment_method.setVisibility(VISIBLE);
        im_payment_arrow.setRotation(180);
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

    public void setTitleShippingMethod(String titleShippingMethod) {
        tv_shipping_method.setText(getContext().getString(R.string.shipping) + ": " + titleShippingMethod);
    }

    public void dismissDialogAddPayment(){
        dialog.dismiss();
    }
}
