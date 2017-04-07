package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.view.AbstractModelRecycleView;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.lib.view.item.GenericModelView;
import com.magestore.app.lib.view.item.ModelView;
import com.magestore.app.lib.view.item.ViewState;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardCheckoutAddressContentBinding;

import java.util.List;

/**
 * Created by Johan on 2/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddressListPanel extends AbstractModelRecycleView<GenericModelView, CheckoutListController> {
    //    CheckoutListController mCheckoutListController;
    int selectPos = 0;
    RelativeLayout ll_checkout_address;
    TextView txt_default_address;
    RelativeLayout rl_addd_new_address;
    LinearLayout ll_content;

//    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
//        this.mCheckoutListController = mCheckoutListController;
//    }

    public CheckoutAddressListPanel(Context context) {
        super(context);
    }

    public CheckoutAddressListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutAddressListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, GenericModelView item, int position) {
        // Đặt các trường text vào danh sách
        CardCheckoutAddressContentBinding binding = DataBindingUtil.bind(view);
        binding.setCustomerAddress((CustomerAddress) item.getModel());

        ll_checkout_address = (RelativeLayout) view.findViewById(R.id.ll_checkout_address);
        txt_default_address = (TextView) view.findViewById(R.id.txt_default_address);
        rl_addd_new_address = (RelativeLayout) view.findViewById(R.id.rl_add_new_address);
        ll_content = (LinearLayout) view.findViewById(R.id.ll_content);

        if (position == 0) {
            txt_default_address.setText(getContext().getString(R.string.checkout_address_item_default));
        } else {
            txt_default_address.setText("");
        }

        if (item.getViewState().isStateWaitInsert()) {
            txt_default_address.setText("");
            rl_addd_new_address.setVisibility(VISIBLE);
            ll_content.setVisibility(GONE);
        } else {
            rl_addd_new_address.setVisibility(GONE);
            ll_content.setVisibility(VISIBLE);
        }

        if (selectPos == position) {
            ll_checkout_address.setBackgroundResource(R.drawable.checkout_address_border_item_select);
        } else {
            ll_checkout_address.setBackgroundResource(R.drawable.checkout_address_border_item);
        }
    }

    @Override
    protected void onClickItem(View view, GenericModelView item, int position) {
        if (item.getViewState().isStateWaitInsert()) {
            getController().addNewAddress();
            selectPos = position;
        } else {
            getController().changeShippingAddress((CustomerAddress) item.getModel());
            selectPos = position;
        }
    }

    public void scrollToPosition(){
        this.getLayoutManager().scrollToPosition(selectPos);
    }

    @Override
    public void bindListModelView(List<GenericModelView> list) {
        Customer guest_customer = getController().getGuestCheckout();
        Customer customer = getController().getSelectedItem().getCustomer();
        if (getController().checkCustomerID(customer, guest_customer)) {
            GenericModelView modelView = new GenericModelView();
            ViewState viewState = new ViewState();
            viewState.setStateWaitInsert();
            modelView.setViewState(viewState);
            list.add(modelView);
        }
        super.bindListModelView(list);
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    public int getSelectPos() {
        return selectPos;
    }
}
