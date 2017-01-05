package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.magestore.app.lib.adapterview.adapter2pos.AdapterView2Entity;
import com.magestore.app.lib.entity.Address;
import com.magestore.app.pos.R;

/**
 * Dialog quản lý địa chỉ của customer
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerAddressPanel extends FrameLayout {
    Address mAddress;
//    EditText mtxtFirstName;
//    EditText mtxtLastName;
//    EditText mtxtCompany;
//    EditText mtxtPhone;
//    EditText mtxtStreet1;
//    EditText mtxtStreet2;
//    EditText mtxtCity;
//    EditText mtxtZipCode;
//    EditText mtxtCountry;
//    EditText mtxtState;
//    EditText mtxtVAT;

    public CustomerAddressPanel(Context context) {
        super(context);
        init();
    }

    public CustomerAddressPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerAddressPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
        initTask();
    }

    /**
     * Chuẩn bị layout
     */
    private void initControlLayout() {
        // Load layout view thông tin khách hàng chi tiết
        View v = inflate(getContext(), R.layout.panel_customer_address, null);
        addView(v);

        // các edit text control
//        mtxtFirstName = (EditText) findViewById(R.id.firstname);
//        mtxtLastName = (EditText) findViewById(R.id.txt_address_last_name);
//        mtxtCompany = (EditText) findViewById(R.id.txt_address_company);
//        mtxtPhone = (EditText) findViewById(R.id.txt_address_phone);
//        mtxtStreet1 = (EditText) findViewById(R.id.txt_address_street1);
//        mtxtStreet2 = (EditText) findViewById(R.id.txt_address_street2);
//        mtxtCity = (EditText) findViewById(R.id.txt_address_city);
//        mtxtZipCode = (EditText) findViewById(R.id.txt_address_zip_code);
//        mtxtCountry = (EditText) findViewById(R.id.txt_address_country);
//        mtxtState = (EditText) findViewById(R.id.txt_address_state);
//        mtxtVAT = (EditText) findViewById(R.id.txt_address_vat);
    }

    private void initControlValue() {

    }

    private void initTask() {

    }

    public void setAddress(Address address) {
        // Gán address
        mAddress = address;
        if (mAddress == null) return;

        AdapterView2Entity adapterView2Entity = new AdapterView2Entity();
        adapterView2Entity.toView(this, mAddress);

        // đặt value cho các address
//        mtxtFirstName.setText(address.getFirstName());
//        mtxtLastName.setText(address.getLastName());
//        mtxtCompany.setText(address.getCompany());
//        mtxtPhone.setText(address.getTelephone());
//        mtxtStreet1.setText(address.getStreet1());
//        mtxtStreet2.setText(address.getStreet2());
//        mtxtCity.setText(address.getCity());
//        mtxtZipCode.setText(address.getPostCode());
//        mtxtCountry.setText(address.getCountry());
//        mtxtState.setText(address.getRegionCode());
//        mtxtVAT.setText(address.getVAT());
    }

    public Address getAddress() {
        AdapterView2Entity adapterView2Entity = new AdapterView2Entity();
        adapterView2Entity.fromView(this, mAddress);

        if (mAddress == null) return mAddress;
//        mAddress.setFirstName(mtxtFirstName.getText().toString());
//        mAddress.setLastName(mtxtLastName.getText().toString());
//        mAddress.setCompany(mtxtCompany.getText().toString());
//        mAddress.setTelephone(mtxtPhone.getText().toString());
//        mAddress.setStreet1(mtxtStreet1.getText().toString());
//        mAddress.setStreet2(mtxtStreet2.getText().toString());
//        mAddress.setCity(mtxtCity.getText().toString());
//        mAddress.setPostCode(mtxtZipCode.getText().toString());
//        mAddress.setCountry(mtxtCountry.getText().toString());
//        // state
//        mAddress.setVAT(mtxtVAT.getText().toString());
        return mAddress;
    }
}
