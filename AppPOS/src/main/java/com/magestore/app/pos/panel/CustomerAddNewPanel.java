package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.SearchPlaceAutoCompletePanel;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.lib.view.SearchAutoCompleteTextView;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CustomerListController;
import com.magestore.app.pos.databinding.PanelCustomerAddNewBinding;
import com.magestore.app.pos.util.EditTextUtil;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CustomerAddNewPanel extends AbstractDetailPanel<Customer> {
    SimpleSpinner mspinGroupID;
    SimpleSpinner s_spinner_country, b_spinner_country, s_spinner_state, b_spinner_state, s_shipping_address, s_billing_address;
    LinearLayout ll_add_new_customer, ll_short_shipping_address, ll_short_billing_address;
    LinearLayout ll_shipping_address, ll_billing_address, ll_s_shipping_address, ll_s_billing_address;
    LinearLayout ll_new_shipping_address, ll_last_name, ll_b_state, ll_b_last_name, ll_s_company;
    LinearLayout ll_new_billing_address, ll_s_state, ll_s_last_name, ll_b_company;
    EditText s_state, b_state, s_first_name, b_first_name, s_last_name, b_last_name, s_street2, b_street2, s_vat, b_vat;
    EditText first_name, last_name, email, s_company, b_company, s_phone, b_phone, s_city, b_city, s_zipcode, b_zipcode;
    Switch subscribe;
    CheckBox cb_same_billing_and_shipping;
    ConfigRegion shippingRegion, billingRegion;
    CustomerAddress shippingAddress, billingAddress, c_shippingAddress, c_billingAddress;
    TextView tv_shipping_address, tv_billing_address;
    PanelCustomerAddNewBinding mBinding;
    ImageView btn_shipping_address, btn_billing_address;
    List<CustomerAddress> listAddress;
    SearchPlaceAutoCompletePanel s_place_panel, b_place_panel;
    SearchAutoCompleteTextView s_street1, b_street1;
    CustomerService mCustomerService;

    public CustomerAddNewPanel(Context context) {
        super(context);
    }

    public CustomerAddNewPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerAddNewPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_customer_add_new, null);
        addView(view);

        mspinGroupID = (SimpleSpinner) findViewById(R.id.spinner_group_id);
        ll_add_new_customer = (LinearLayout) findViewById(R.id.ll_add_new_customer);
        ll_shipping_address = (LinearLayout) findViewById(R.id.ll_shipping_address);
        ll_billing_address = (LinearLayout) findViewById(R.id.ll_billing_address);
        ll_new_shipping_address = (LinearLayout) findViewById(R.id.ll_new_shipping_address);
        ll_new_billing_address = (LinearLayout) findViewById(R.id.ll_new_billing_address);
        s_spinner_country = (SimpleSpinner) findViewById(R.id.s_spinner_country);
        b_spinner_country = (SimpleSpinner) findViewById(R.id.b_spinner_country);
        s_spinner_state = (SimpleSpinner) findViewById(R.id.s_spinner_state);
        b_spinner_state = (SimpleSpinner) findViewById(R.id.b_spinner_state);
        s_shipping_address = (SimpleSpinner) findViewById(R.id.s_shipping_address);
        s_billing_address = (SimpleSpinner) findViewById(R.id.s_billing_address);
        ll_s_state = (LinearLayout) findViewById(R.id.ll_s_state);
        ll_b_state = (LinearLayout) findViewById(R.id.ll_b_state);
        ll_s_last_name = (LinearLayout) findViewById(R.id.ll_s_last_name);
        ll_b_last_name = (LinearLayout) findViewById(R.id.ll_b_last_name);
        s_state = (EditText) findViewById(R.id.s_state);
        b_state = (EditText) findViewById(R.id.b_state);
        first_name = (EditText) findViewById(R.id.first_name);
        ll_last_name = (LinearLayout) findViewById(R.id.ll_last_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        subscribe = (Switch) findViewById(R.id.subscribe);
        s_first_name = (EditText) findViewById(R.id.s_first_name);
        b_first_name = (EditText) findViewById(R.id.b_first_name);
        s_last_name = (EditText) findViewById(R.id.s_last_name);
        b_last_name = (EditText) findViewById(R.id.b_last_name);
        ll_s_company = (LinearLayout) findViewById(R.id.ll_s_company);
        ll_b_company = (LinearLayout) findViewById(R.id.ll_b_company);
        s_company = (EditText) findViewById(R.id.s_company);
        b_company = (EditText) findViewById(R.id.b_company);
        s_phone = (EditText) findViewById(R.id.s_phone);
        b_phone = (EditText) findViewById(R.id.b_phone);
        s_place_panel = (SearchPlaceAutoCompletePanel) findViewById(R.id.s_search_place_panel);
        s_street1 = (SearchAutoCompleteTextView) findViewById(R.id.s_street1);
        s_street2 = (EditText) findViewById(R.id.s_street2);
        b_place_panel = (SearchPlaceAutoCompletePanel) findViewById(R.id.b_search_place_panel);
        b_street1 = (SearchAutoCompleteTextView) findViewById(R.id.b_street1);
        b_street2 = (EditText) findViewById(R.id.b_street2);
        s_city = (EditText) findViewById(R.id.s_city);
        b_city = (EditText) findViewById(R.id.b_city);
        s_zipcode = (EditText) findViewById(R.id.s_zipcode);
        b_zipcode = (EditText) findViewById(R.id.b_zipcode);
        s_vat = (EditText) findViewById(R.id.s_vat);
        b_vat = (EditText) findViewById(R.id.b_vat);
        cb_same_billing_and_shipping = (CheckBox) findViewById(R.id.cb_same_billing_and_shipping);
        tv_shipping_address = (TextView) findViewById(R.id.tv_shipping_address);
        tv_billing_address = (TextView) findViewById(R.id.tv_billing_address);
        ll_short_shipping_address = (LinearLayout) findViewById(R.id.ll_short_shipping_address);
        ll_short_billing_address = (LinearLayout) findViewById(R.id.ll_short_billing_address);
        btn_shipping_address = (ImageView) findViewById(R.id.btn_shipping_address);
        btn_billing_address = (ImageView) findViewById(R.id.btn_billing_address);
        ll_s_shipping_address = (LinearLayout) findViewById(R.id.ll_s_shipping_address);
        ll_s_billing_address = (LinearLayout) findViewById(R.id.ll_s_billing_address);
        mBinding = DataBindingUtil.bind(view);

        subscribe.setVisibility(ConfigUtil.isSubscribe() ? VISIBLE : GONE);
        ll_last_name.setVisibility(ConfigUtil.isLastName() ? VISIBLE : GONE);
        ll_s_last_name.setVisibility(ConfigUtil.isLastName() ? VISIBLE : GONE);
        ll_b_last_name.setVisibility(ConfigUtil.isLastName() ? VISIBLE : GONE);
        ll_s_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
        ll_b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
        ll_s_company.setVisibility(ConfigUtil.isCompany() ? VISIBLE : GONE);
        ll_b_company.setVisibility(ConfigUtil.isCompany() ? VISIBLE : GONE);

        s_phone.setHint(ConfigUtil.isRequiedPhone() ? getContext().getString(R.string.phone) : getContext().getString(R.string.customer_phone_optional));
        b_phone.setHint(ConfigUtil.isRequiedPhone() ? getContext().getString(R.string.phone) : getContext().getString(R.string.customer_phone_optional));
        s_street1.setHint(ConfigUtil.isRequiedStreet1() ? getContext().getString(R.string.street) : getContext().getString(R.string.customer_street1_optional));
        b_street1.setHint(ConfigUtil.isRequiedStreet1() ? getContext().getString(R.string.street) : getContext().getString(R.string.customer_street1_optional));
        s_city.setHint(ConfigUtil.isRequiedStreet1() ? getContext().getString(R.string.city) : getContext().getString(R.string.customer_city_optional));
        b_city.setHint(ConfigUtil.isRequiedStreet1() ? getContext().getString(R.string.city) : getContext().getString(R.string.customer_city_optional));
        s_zipcode.setHint(ConfigUtil.isRequiedStreet1() ? getContext().getString(R.string.zip_code) : getContext().getString(R.string.customer_zipcode_optional));
        b_zipcode.setHint(ConfigUtil.isRequiedStreet1() ? getContext().getString(R.string.zip_code) : getContext().getString(R.string.customer_zipcode_optional));
    }

    @Override
    public void initValue() {
        if (ConfigUtil.isPlaceAutoComplete()) {
            s_place_panel.setListController(mController, mCustomerService);
            b_place_panel.setListController(mController, mCustomerService);
        } else {
            s_place_panel.setListController(null, null);
            b_place_panel.setListController(null, null);
        }
    }

    @Override
    public void bindItem(Customer item) {
        super.bindItem(item);

        setCustomerGroupDataSet(((CustomerListController) mController).getCustomerGroupList());
        final Map<String, ConfigCountry> countryDataSet = ((CustomerListController) mController).getCountry();
        setCountryDataSet(countryDataSet);
        subscribe.setVisibility(ConfigUtil.isSubscribe() ? VISIBLE : GONE);
        ll_s_shipping_address.setVisibility(GONE);
        ll_s_billing_address.setVisibility(GONE);

        ll_short_shipping_address.setVisibility(GONE);
        btn_shipping_address.setVisibility(VISIBLE);
        ll_short_billing_address.setVisibility(GONE);
        btn_billing_address.setVisibility(VISIBLE);

        if (item != null) {
            mBinding.setCustomer(item);
            mspinGroupID.setSelection(item.getGroupID());
            subscribe.setVisibility(GONE);
            if (item.getAddress() != null && item.getAddress().size() > 0) {
                ll_short_shipping_address.setVisibility(VISIBLE);
                btn_shipping_address.setVisibility(GONE);
                ll_short_billing_address.setVisibility(VISIBLE);
                btn_billing_address.setVisibility(GONE);

                ll_s_shipping_address.setVisibility(VISIBLE);
                ll_s_billing_address.setVisibility(VISIBLE);

                setAddressDataSet(item.getAddress());
                CustomerAddress billingAddress;
                CustomerAddress shippingAddress;
                if (item.getUseOneAddress()) {
                    billingAddress = item.getAddress().get(0);
                    shippingAddress = item.getAddress().get(0);
                } else {
                    if (item.getAddress().size() > 1) {
                        billingAddress = item.getAddress().get(1);
                        shippingAddress = item.getAddress().get(0);
                    } else {
                        billingAddress = item.getAddress().get(0);
                        shippingAddress = item.getAddress().get(0);
                    }
                }

                bindDataShippingAddress(shippingAddress);
                bindDataBillingAddress(billingAddress);

                s_shipping_address.setSelection(shippingAddress.getID());
                s_billing_address.setSelection(billingAddress.getID());
                c_shippingAddress = shippingAddress;
                c_billingAddress = billingAddress;
            }

        } else {
            mBinding.setCustomer(null);
        }

        actionChangeSpinner(countryDataSet);
    }

    private void actionChangeSpinner(final Map<String, ConfigCountry> countryDataSet) {
        s_spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, ConfigRegion> listRegion = countryDataSet.get(s_spinner_country.getSelection()).getRegions();
                if (listRegion != null && listRegion.size() > 0) {
                    s_state.setVisibility(GONE);
                    s_spinner_state.setVisibility(VISIBLE);
                    ll_s_state.setVisibility(VISIBLE);
                    s_spinner_state.bindModelMap((Map<String, Model>) (Map<?, ?>) listRegion);
                } else {
                    ll_s_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
                    s_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
                    s_spinner_state.setVisibility(GONE);
                }
                updateState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_spinner_country.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isAutoComplete = false;
                return false;
            }
        });

        b_spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, ConfigRegion> listRegion = countryDataSet.get(b_spinner_country.getSelection()).getRegions();
                if (listRegion != null && listRegion.size() > 0) {
                    b_state.setVisibility(GONE);
                    ll_b_state.setVisibility(VISIBLE);
                    b_spinner_state.setVisibility(VISIBLE);
                    b_spinner_state.bindModelMap((Map<String, Model>) (Map<?, ?>) listRegion);
                } else {
                    ll_b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
                    b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
                    b_spinner_state.setVisibility(GONE);
                }
                updateState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, ConfigRegion> listRegion = countryDataSet.get(s_spinner_country.getSelection()).getRegions();
                shippingRegion = listRegion.get(s_spinner_state.getSelection());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        b_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, ConfigRegion> listRegion = countryDataSet.get(b_spinner_country.getSelection()).getRegions();
                billingRegion = listRegion.get(b_spinner_state.getSelection());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_shipping_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c_shippingAddress = getAddressSelect(s_shipping_address);
                showShortShippingAddress();
                bindDataShippingAddress(c_shippingAddress);
                ll_short_shipping_address.setVisibility(c_shippingAddress.getIsStoreAddress() ? GONE : VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_billing_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c_billingAddress = getAddressSelect(s_billing_address);
                showShortBillingAddress();
                bindDataBillingAddress(c_billingAddress);
                ll_short_billing_address.setVisibility(c_billingAddress.getIsStoreAddress() ? GONE : VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private CustomerAddress getAddressSelect(SimpleSpinner s_address) {
        for (CustomerAddress address : listAddress) {
            if (address.getID().equals(s_address.getSelection())) {
                return address;
            }
        }
        return null;
    }

    /**
     * Trả param Customer cho ListPanel để request add new Customer
     *
     * @return Customer
     */
    public Customer returnCustomer() {
        boolean check_subscribe = subscribe.isChecked();
        Customer customer = ((CustomerListController) mController).createNewCustomer();
        List<CustomerAddress> listCustomerAddress = new ArrayList<>();

        if (shippingAddress != null) {
            if (cb_same_billing_and_shipping.isChecked()) {
                shippingAddress.setDefaultShipping("1");
                shippingAddress.setDefaultBilling("1");
                listCustomerAddress.add(shippingAddress);
                customer.setAddressList(listCustomerAddress);
            } else {
                shippingAddress.setDefaultShipping("1");
                shippingAddress.setDefaultBilling(String.valueOf(false));
                if (billingAddress != null) {
                    listCustomerAddress.add(billingAddress);
                }
                listCustomerAddress.add(shippingAddress);

            }
        }
        if (billingAddress != null) {
            if (ConfigUtil.isSameAddress()) {
                if (!cb_same_billing_and_shipping.isChecked()) {
                    listCustomerAddress.add(billingAddress);
                }
            } else {
                listCustomerAddress.add(billingAddress);
            }
        }
        customer.setAddressList(listCustomerAddress);
        customer.setID(email.getText().toString());
        customer.setGroupID(mspinGroupID.getSelection());
        customer.setEmail(email.getText().toString());
        customer.setFirstName(first_name.getText().toString());
        customer.setLastName(last_name.getText().toString());
        customer.setSubscriber(String.valueOf(check_subscribe));
        return customer;
    }

    /**
     * Gán giá trị customer group cho spinner
     *
     * @param customerGroupDataSet
     */
    public void setCustomerGroupDataSet(Map<String, String> customerGroupDataSet) {
        if (customerGroupDataSet != null)
            mspinGroupID.bind(customerGroupDataSet);
    }

    /**
     * Gán giá trị country cho spinner country shipping và billing
     *
     * @param countryDataSet
     */
    public void setCountryDataSet(Map<String, ConfigCountry> countryDataSet) {
        if (countryDataSet != null) {
            s_spinner_country.bindModelMap((Map<String, Model>) (Map<?, ?>) countryDataSet);
            b_spinner_country.bindModelMap((Map<String, Model>) (Map<?, ?>) countryDataSet);
        }
    }

    public void setAddressDataSet(List<CustomerAddress> listAddress) {
        this.listAddress = listAddress;
        s_shipping_address.bind(listAddress.toArray(new CustomerAddress[0]));
        s_billing_address.bind(listAddress.toArray(new CustomerAddress[0]));
    }

    public void updateAddress(int type, List<CustomerAddress> listAddress, CustomerAddress address) {
        this.listAddress = listAddress;
        s_shipping_address.bind(listAddress.toArray(new CustomerAddress[0]));
        s_billing_address.bind(listAddress.toArray(new CustomerAddress[0]));
        if (type == 0) {
            s_shipping_address.setSelection(address.getID());
            s_billing_address.setSelection(c_billingAddress.getID());
        } else if (type == 1) {
            s_shipping_address.setSelection(c_shippingAddress.getID());
            s_billing_address.setSelection(address.getID());
        }
    }

    public CustomerAddress getChangeshippingAddress() {
        return c_shippingAddress;
    }

    public CustomerAddress getChangebillingAddress() {
        return c_billingAddress;
    }

    /**
     * Trả về CheckoutShipping Address
     *
     * @return CustomerAddress
     */
    public CustomerAddress getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Trả về Billing Address
     *
     * @return CustomerAddress
     */
    public CustomerAddress getBillingAddress() {
        return billingAddress;
    }

    public void bindDataShippingAddress(CustomerAddress shippingAddress) {
        tv_shipping_address.setText(shippingAddress.getShortAddress());
        s_first_name.setText(shippingAddress.getFirstName());
        s_last_name.setText(shippingAddress.getLastName());
        s_company.setText(shippingAddress.getCompany());
        s_phone.setText(shippingAddress.getTelephone());
        s_street1.setText(shippingAddress.getStreet1());
        s_street2.setText(shippingAddress.getStreet2());
        s_city.setText(shippingAddress.getCity());
        s_zipcode.setText(shippingAddress.getPostCode());
        s_spinner_country.setSelection(shippingAddress.getCountry());
        if (shippingAddress.getRegion().getRegionID() == 0) {
            s_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
            ll_s_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
            s_spinner_state.setVisibility(GONE);
            s_state.setText(shippingAddress.getRegion().getRegionName());
        } else {
            s_state.setVisibility(GONE);
            s_spinner_state.setVisibility(VISIBLE);
            ll_s_state.setVisibility(VISIBLE);
            s_spinner_state.setSelection(shippingAddress.getRegion().getRegionCode());
        }
    }

    public void bindDataBillingAddress(CustomerAddress billingAddress) {
        tv_billing_address.setText(billingAddress.getShortAddress());
        b_first_name.setText(billingAddress.getFirstName());
        b_last_name.setText(billingAddress.getLastName());
        b_company.setText(billingAddress.getCompany());
        b_phone.setText(billingAddress.getTelephone());
        b_street1.setText(billingAddress.getStreet1());
        b_street2.setText(billingAddress.getStreet2());
        b_city.setText(billingAddress.getCity());
        b_zipcode.setText(billingAddress.getPostCode());
        b_spinner_country.setSelection(billingAddress.getCountry());
        if (billingAddress.getRegion().getRegionID() == 0) {
            b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
            ll_b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
            b_spinner_state.setVisibility(GONE);
            b_state.setText(billingAddress.getRegion().getRegionName());
        } else {
            b_state.setVisibility(GONE);
            b_spinner_state.setVisibility(VISIBLE);
            ll_b_state.setVisibility(VISIBLE);
            b_spinner_state.setSelection(billingAddress.getRegion().getRegionCode());
        }
    }

    public void insertCustomerNameToAddress() {
        String firstName = first_name.getText().toString().trim();
        String sFirstName = s_first_name.getText().toString().trim();
        String bFirstName = b_first_name.getText().toString().trim();
        String lastName = last_name.getText().toString().trim();
        String sLastName = s_last_name.getText().toString().trim();
        String bLastName = b_last_name.getText().toString().trim();
        if (!StringUtil.STRING_EMPTY.equals(firstName) && StringUtil.STRING_EMPTY.equals(bFirstName)) {
            b_first_name.setText(firstName);
        }
        if (!StringUtil.STRING_EMPTY.equals(firstName) && StringUtil.STRING_EMPTY.equals(sFirstName)) {
            s_first_name.setText(firstName);
        }
        if (!StringUtil.STRING_EMPTY.equals(lastName) && StringUtil.STRING_EMPTY.equals(sLastName)) {
            s_last_name.setText(lastName);
        }
        if (!StringUtil.STRING_EMPTY.equals(lastName) && StringUtil.STRING_EMPTY.equals(bLastName)) {
            b_last_name.setText(lastName);
        }
    }

    /**
     * Khởi tạo CheckoutShipping Address
     */
    public void insertShippingAddress() {
        shippingAddress = ((CustomerListController) mController).createCustomerAddress();
        shippingAddress.setFirstName(s_first_name.getText().toString());
        shippingAddress.setLastName(s_last_name.getText().toString());
        shippingAddress.setCompany(s_company.getText().toString());
        shippingAddress.setTelephone(s_phone.getText().toString());
        shippingAddress.setStreet1(s_street1.getText().toString());
        shippingAddress.setStreet2(s_street2.getText().toString());
        shippingAddress.setCity(s_city.getText().toString());
        shippingAddress.setPostCode(s_zipcode.getText().toString());
        shippingAddress.setCountry(s_spinner_country.getSelection());
        Region s_region = ((CustomerListController) mController).createRegion();
        if (s_state.getVisibility() == VISIBLE) {
            s_region.setRegionCode(s_state.getText().toString());
            s_region.setRegionName(s_state.getText().toString());
            s_region.setRegionID(0);
            shippingAddress.setRegionID("0");
            shippingAddress.setRegion(s_region);
        } else {
            try {
                s_region.setRegionID(Integer.parseInt(shippingRegion.getID()));
            } catch (Exception e) {
                s_region.setRegionID(0);
            }
            s_region.setRegionName(shippingRegion.getName());
            s_region.setRegionCode(shippingRegion.getCode());
            shippingAddress.setState(s_spinner_state.getSelection());
            shippingAddress.setRegionID(shippingRegion.getID());
            shippingAddress.setRegion(s_region);
        }
        shippingAddress.setVAT(s_vat.getText().toString());
        if (cb_same_billing_and_shipping.isChecked()) {
            billingAddress = shippingAddress;
            b_first_name.setText(billingAddress.getFirstName());
            b_last_name.setText(billingAddress.getLastName());
            b_company.setText(billingAddress.getCompany());
            b_phone.setText(billingAddress.getTelephone());
            b_street1.setText(billingAddress.getStreet1());
            b_street2.setText(billingAddress.getStreet2());
            b_city.setText(billingAddress.getCity());
            b_zipcode.setText(billingAddress.getPostCode());
            b_spinner_country.setSelection(billingAddress.getCountry());
            if (billingAddress.getRegion().getRegionID() == 0) {
                b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
                b_spinner_state.setVisibility(GONE);
                ll_b_state.setVisibility(ConfigUtil.isEditState() ? VISIBLE : GONE);
                b_state.setText(billingAddress.getRegion().getRegionName());
            } else {
                b_state.setVisibility(GONE);
                ll_b_state.setVisibility(VISIBLE);
                b_spinner_state.setVisibility(VISIBLE);
                b_spinner_state.setSelection(billingAddress.getRegion().getRegionCode());
            }
            b_vat.setText(billingAddress.getVAT());
        }
    }

    /**
     * Khởi tạo Billing Address
     */
    public void insertBillingAddress() {
        billingAddress = ((CustomerListController) mController).createCustomerAddress();
        if (shippingAddress != null) {
            shippingAddress.setDefaultShipping("1");
            shippingAddress.setDefaultBilling(String.valueOf(false));
        }
        billingAddress.setDefaultShipping("1");
        billingAddress.setDefaultBilling(String.valueOf(false));
        billingAddress.setFirstName(b_first_name.getText().toString());
        billingAddress.setLastName(b_last_name.getText().toString());
        billingAddress.setCompany(b_company.getText().toString());
        billingAddress.setTelephone(b_phone.getText().toString());
        billingAddress.setStreet1(b_street1.getText().toString());
        billingAddress.setStreet2(b_street2.getText().toString());
        billingAddress.setCity(b_city.getText().toString());
        billingAddress.setPostCode(b_zipcode.getText().toString());
        billingAddress.setCountry(b_spinner_country.getSelection());
        Region b_region = ((CustomerListController) mController).createRegion();
        if (b_state.getVisibility() == VISIBLE) {
            b_region.setRegionCode(b_state.getText().toString());
            b_region.setRegionName(b_state.getText().toString());
            b_region.setRegionID(0);
            billingAddress.setRegionID("0");
            billingAddress.setRegion(b_region);
        } else {
            try {
                b_region.setRegionID(Integer.parseInt(billingRegion.getID()));
            } catch (Exception e) {
                b_region.setRegionID(0);
            }
            b_region.setRegionName(billingRegion.getName());
            b_region.setRegionCode(billingRegion.getCode());
            billingAddress.setState(b_spinner_state.getSelection());
            billingAddress.setRegionID(billingRegion.getID());
            billingAddress.setRegion(b_region);
        }
        billingAddress.setVAT(b_vat.getText().toString());
    }

    /**
     * Delete CheckoutShipping Address
     */
    public void deleteShippingAddress() {
        shippingAddress = null;
        clearText(s_first_name);
        clearText(s_last_name);
        clearText(s_phone);
        clearText(s_company);
        clearText(s_street1);
        clearText(s_street2);
        clearText(s_city);
        clearText(s_zipcode);
        clearText(s_vat);
        cb_same_billing_and_shipping.setChecked(true);
    }

    /**
     * Delete Billing Address
     */
    public void deleteBillingAddress() {
        billingAddress = null;
        clearText(b_first_name);
        clearText(b_last_name);
        clearText(b_phone);
        clearText(b_company);
        clearText(b_street1);
        clearText(b_street2);
        clearText(b_city);
        clearText(b_zipcode);
        clearText(b_vat);
    }

    /**
     * Hiển thị short content shipping address
     */
    public void showShortShippingAddress() {
        tv_shipping_address.setText(c_shippingAddress.getShortAddress());
    }

    public void showNewShortShippingAddress() {
        tv_shipping_address.setText(shippingAddress.getShortAddress());
    }

    /**
     * Hiển thị short content shipping address
     */
    public void showShortBillingAddress() {
        tv_billing_address.setText(c_billingAddress.getShortAddress());
    }

    public void showNewShortBillingAddress() {
        tv_billing_address.setText(billingAddress.getShortAddress());
    }

    public boolean checkSameBillingAndShipping() {
        return cb_same_billing_and_shipping.isChecked();
    }

    /**
     * Kiểm tra các trường customer
     *
     * @return boolean
     */
    public boolean checkRequiedCustomer() {
        if (ConfigUtil.isRequiedFirstName()) {
            if (!isRequied(first_name)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedLastName()) {
            if (!isRequied(last_name)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedEmail()) {
            if (!isRequied(email) || !isRequiedEmail(email)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra các trường shipping address
     *
     * @return boolean
     */
    public boolean checkRequiedShippingAddress() {
        if (ConfigUtil.isRequiedFirstName()) {
            if (!isRequied(s_first_name)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedLastName()) {
            if (!isRequied(s_last_name)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedPhone()) {
            if (!isRequied(s_phone)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedStreet1()) {
            if (!isRequied(s_street1)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedCity()) {
            if (!isRequied(s_city)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedZipCode()) {
            if (!isRequied(s_zipcode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra các trường billing address
     *
     * @return boolean
     */
    public boolean checkRequiedBillingAddress() {
        if (ConfigUtil.isRequiedFirstName()) {
            if (!isRequied(b_first_name)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedLastName()) {
            if (!isRequied(b_last_name)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedPhone()) {
            if (!isRequied(b_phone)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedStreet1()) {
            if (!isRequied(b_street1)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedCity()) {
            if (!isRequied(b_city)) {
                return false;
            }
        }
        if (ConfigUtil.isRequiedZipCode()) {
            if (!isRequied(b_zipcode)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRequied(EditText editText) {
        return EditTextUtil.checkRequied(getContext(), editText);
    }

    public boolean isRequiedEmail(EditText editText) {
        return EditTextUtil.checkRequiedEmail(getContext(), editText);
    }

    public void clearText(EditText editText) {
        editText.setText("");
    }

    private Region mPlaceRegion = null;
    private boolean isAutoComplete = false;

    public void updatePlaceAutoComplete(CustomerAddress mCustomerAddress) {
        mPlaceRegion = mCustomerAddress.getRegion();
        isAutoComplete = true;
        if (ll_new_shipping_address.getVisibility() == VISIBLE) {
            s_street1.setText(mCustomerAddress.getStreet1());
            s_street2.setText(mCustomerAddress.getStreet2());
            s_city.setText(mCustomerAddress.getCity());
            s_zipcode.setText(mCustomerAddress.getPostCode());
            boolean isSameCountry = false;
            if (s_spinner_country.getSelection().equals(mCustomerAddress.getCountry())) {
                isSameCountry = true;
            }
            s_spinner_country.setSelection(mCustomerAddress.getCountry());
            if (isSameCountry) {
                if (s_state.getVisibility() == VISIBLE) {
                    s_state.setText(mPlaceRegion.getRegionName());
                } else {
                    checkListState();
                }
            }
        } else {
            b_street1.setText(mCustomerAddress.getStreet1());
            b_street2.setText(mCustomerAddress.getStreet2());
            b_city.setText(mCustomerAddress.getCity());
            b_zipcode.setText(mCustomerAddress.getPostCode());
            boolean isSameCountry = false;
            if (b_spinner_country.getSelection().equals(mCustomerAddress.getCountry())) {
                isSameCountry = true;
            }
            b_spinner_country.setSelection(mCustomerAddress.getCountry());
            if (isSameCountry) {
                if (b_state.getVisibility() == VISIBLE) {
                    b_state.setText(mPlaceRegion.getRegionName());
                } else {
                    checkListState();
                }
            }
        }
    }

    private void updateState() {
        if (isAutoComplete) {
            if (mPlaceRegion != null) {
                if (ll_new_shipping_address.getVisibility() == VISIBLE) {
                    if (s_state.getVisibility() == VISIBLE) {
                        s_state.setText(mPlaceRegion.getRegionName());
                    } else {
                        checkListState();
                    }
                } else {
                    if (b_state.getVisibility() == VISIBLE) {
                        b_state.setText(mPlaceRegion.getRegionName());
                    } else {
                        checkListState();
                    }
                }
            }
        }
    }

    private void checkListState() {
        final Map<String, ConfigCountry> countryDataSet = ((CustomerListController) mController).getCountry();
        if (ll_new_shipping_address.getVisibility() == VISIBLE) {
            Map<String, ConfigRegion> listRegion = countryDataSet.get(s_spinner_country.getSelection()).getRegions();
            for (ConfigRegion region : listRegion.values()) {
                if (region.getCode().equals(mPlaceRegion.getRegionCode())) {
                    s_spinner_state.setSelection(region.getID());
                }
            }
        } else {
            Map<String, ConfigRegion> listRegion = countryDataSet.get(b_spinner_country.getSelection()).getRegions();
            for (ConfigRegion region : listRegion.values()) {
                if (region.getCode().equals(mPlaceRegion.getRegionCode())) {
                    b_spinner_state.setSelection(region.getID());
                }
            }
        }
    }
}
