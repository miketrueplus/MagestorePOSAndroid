package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.magestore.app.lib.adapterview.adapter2pos.AdapterView2Model;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.R;

import java.lang.reflect.InvocationTargetException;

/**
 * Dialog quản lý địa chỉ của customer
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CustomerAddressDetailPanel extends AbstractDetailPanel<CustomerAddress> {
    AdapterView2Model adapterView2Model;

    public CustomerAddressDetailPanel(Context context) {
        super(context);
        init();
    }

    public CustomerAddressDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerAddressDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
    }

    /**
     * Chuẩn bị layout
     */
    protected void initControlLayout() {
        // Load layout view thông tin khách hàng chi tiết
        View v = inflate(getContext(), R.layout.panel_customer_address_detail, null);
        addView(v);
    }

    protected void initControlValue() {
        adapterView2Model = new AdapterView2Model();
        adapterView2Model.setView(this);
        adapterView2Model.setModelClass(PosCustomerAddress.class);
    }

    @Override
    public void bindItem(CustomerAddress item) {
        if (item == null) return;
        adapterView2Model.setModel(item);
        try {
            adapterView2Model.toView();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void getAddress() throws InvocationTargetException, IllegalAccessException {
        adapterView2Model.fromView();
    }
}
