package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.controller.CustomerAddressListController;
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

    /**
     * Khởi tạo
     * @param context
     */
    public CustomerAddressDetailPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     */
    public CustomerAddressDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
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

    /**
     * Map từ dataset model sang các text box
     * @param item
     */
    @Override
    public void bindItem(CustomerAddress item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        if (mBinding == null) mBinding = DataBindingUtil.bind(getView());
        mBinding.setCustomerAddress(item);
    }

    /**
     * Map từ các textbox sang address
     * @return
     */
    @Override
    public CustomerAddress bind2Item() {
        if (mController instanceof CustomerAddressListController) {
            //  khởi tạo customer address

            CustomerAddress address = mController.getListService().create();
            bindItem(address);
            return address;
        }
        return null;
    }

    @Override
    public void bind2Item(CustomerAddress address) {
        address.setFirstName(((EditText) findViewById(R.id.firstname)).getText().toString().trim());
        address.setLastName(((EditText) findViewById(R.id.lastname)).getText().toString().trim());
        address.setStreet1(((EditText) findViewById(R.id.street1)).getText().toString().trim());
        address.setStreet2(((EditText) findViewById(R.id.street2)).getText().toString().trim());
        address.setCompany(((EditText) findViewById(R.id.company)).getText().toString().trim());
        address.setTelephone(((EditText) findViewById(R.id.telephone)).getText().toString().trim());
        address.setCity(((EditText) findViewById(R.id.city)).getText().toString().trim());
        address.setPostCode(((EditText) findViewById(R.id.postcode)).getText().toString().trim());
        address.setCountry(((EditText) findViewById(R.id.postcode)).getText().toString().trim());
        address.setState(((EditText) findViewById(R.id.state)).getText().toString().trim());
        address.setVAT(((EditText) findViewById(R.id.vat_id)).getText().toString().trim());    }
}
