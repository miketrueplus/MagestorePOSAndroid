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
    }

    public CustomerAddressDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerAddressDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Chuẩn bị layout
     */
    public void initLayout() {
        // Bind view sang object
        if (getView() != null)
            mBinding = DataBindingUtil.bind(getView());
    }

    @Override
    public void bindItem(CustomerAddress item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        if (mBinding == null) mBinding = DataBindingUtil.bind(getView());
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
