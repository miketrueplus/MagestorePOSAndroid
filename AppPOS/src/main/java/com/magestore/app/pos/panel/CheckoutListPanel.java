package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.R;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelCheckoutListBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CheckoutListPanel extends AbstractListPanel<Checkout> {
    private PanelCheckoutListBinding mBinding;
    MagestoreDialog dialog;
    Checkout mCheckout;
    MagestoreContext mMagestoreContext;
    CustomerService mCustomerService;

    public CheckoutListPanel(Context context) {
        super(context);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    public void setMagestoreContext(MagestoreContext mMagestoreContext) {
        this.mMagestoreContext = mMagestoreContext;
    }

    @Override
    protected void bindItem(View view, Checkout item, int position) {
        mCheckout = item;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        mBinding = DataBindingUtil.bind(getView());

        ((Button) findViewById(R.id.btn_sales_order_checkout)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().doShowDetailPanel(true);
            }
        });

    }

    /**
     * Cập nhật bảng giá tổng
     */
    public void updateTotalPrice(Checkout checkout) {
        mBinding.setCheckout(checkout);
    }

    public void showPopUpAddCustomer() {
        CheckoutAddCustomerPanel mCheckoutAddCustomerPanel = new CheckoutAddCustomerPanel(getContext());
        mCheckoutAddCustomerPanel.setMagestoreContext(mMagestoreContext);
        mCheckoutAddCustomerPanel.setCustomerService(mCustomerService);
        mCheckoutAddCustomerPanel.bindItem(mCheckout);
        mCheckoutAddCustomerPanel.setController(mController);
        mCheckoutAddCustomerPanel.initModel();
        mCheckoutAddCustomerPanel.initValue();

        dialog = DialogUtil.dialog(getContext(), "", mCheckoutAddCustomerPanel);
        dialog.setGoneButtonSave(true);
        dialog.show();
    }
}
