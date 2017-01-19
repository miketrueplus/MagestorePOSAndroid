package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.databinding.PanelCustomerAddressDetailBinding;
import com.magestore.app.pos.R;

/**
 * Dialog quản lý địa chỉ của customer
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CustomerAddressDetailPanel extends AbstractDetailPanel<CustomerAddress> {
//    AdapterView2Model adapterView2Model;
    PanelCustomerAddressDetailBinding mBinding;

    public CustomerAddressDetailPanel(Context context) {
        super(context);
//        init();
    }

    public CustomerAddressDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public CustomerAddressDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }


    /**
     * Chuẩn bị layout
     */
    public void initLayout() {
        // Load layout view thông tin khách hàng chi tiết
        View v = inflate(getContext(), R.layout.panel_customer_address_detail, null);
        addView(v);

        // Bind view sang object
        mBinding = DataBindingUtil.bind(v);
    }

    @Override
    public void bindItem(CustomerAddress item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        mBinding.setCustomerAddress(item);
    }

    @Override
    public CustomerAddress bind2Item() {
        CustomerAddress a = mBinding.getCustomerAddress();
        a.setFirstName(mBinding.firstname.getText().toString());
//        try {
//            adapterView2Model.fromView();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        return mItem;
    }
}
