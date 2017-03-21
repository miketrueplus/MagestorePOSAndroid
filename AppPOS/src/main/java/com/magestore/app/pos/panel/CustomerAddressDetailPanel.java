package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.controller.CustomerAddressListController;
import com.magestore.app.pos.databinding.PanelCustomerAddressDetailBinding;
import com.magestore.app.pos.R;
import com.magestore.app.pos.util.EditTextUtil;

import java.util.List;
import java.util.Map;

/**
 * Dialog quản lý địa chỉ của customer
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CustomerAddressDetailPanel extends AbstractDetailPanel<CustomerAddress> {
    //    AdapterView2Model adapterView2Model;
    PanelCustomerAddressDetailBinding mBinding;
    SimpleSpinner s_spinner_country, s_spinner_state;
    EditText state;
    ConfigRegion shippingRegion;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public CustomerAddressDetailPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public CustomerAddressDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
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
    @Override
    public void initLayout() {
        View v = inflate(getContext(), R.layout.panel_customer_address_detail, null);
        addView(v);
        mBinding = DataBindingUtil.bind(v);
        s_spinner_country = (SimpleSpinner) findViewById(R.id.s_spinner_country);
        s_spinner_state = (SimpleSpinner) findViewById(R.id.s_spinner_state);
        state = (EditText) findViewById(R.id.state);
    }

    @Override
    public void initValue() {
        Map<String, ConfigCountry> countryDataSet = ((CustomerAddressListController) getController()).getCountry();
        setCountryDataSet(countryDataSet);
        actionChangeSpinner(countryDataSet);
    }

    /**
     * Map từ dataset model sang các text box
     *
     * @param item
     */
    @Override
    public void bindItem(CustomerAddress item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        mBinding.setCustomerAddress(item);
        if (item.getCountry() != null) {
            s_spinner_country.setSelection(item.getCountry());
        }
        if (item.getState() != null) {
            s_spinner_state.setSelection(item.getState());
        }
    }

    private void actionChangeSpinner(final Map<String, ConfigCountry> countryDataSet) {
        s_spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<ConfigRegion> listRegion = countryDataSet.get(s_spinner_country.getSelection()).getRegions();
                if (listRegion != null && listRegion.size() > 0) {
                    state.setVisibility(GONE);
                    s_spinner_state.setVisibility(VISIBLE);
                    s_spinner_state.bind(listRegion.toArray(new ConfigRegion[0]));
                } else {
                    state.setVisibility(VISIBLE);
                    s_spinner_state.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<ConfigRegion> listRegion = countryDataSet.get(s_spinner_country.getSelection()).getRegions();
                shippingRegion = listRegion.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Gán giá trị country cho spinner country shipping và billing
     *
     * @param countryDataSet
     */
    public void setCountryDataSet(Map<String, ConfigCountry> countryDataSet) {
        if (countryDataSet != null) {
            s_spinner_country.bindModelMap((Map<String, Model>) (Map<?, ?>) countryDataSet);
        }
    }

    /**
     * Map từ các textbox sang address
     *
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
        Region s_region = ((CustomerAddressListController) getController()).createRegion();
        address.setFirstName(((EditText) findViewById(R.id.firstname)).getText().toString().trim());
        address.setLastName(((EditText) findViewById(R.id.lastname)).getText().toString().trim());
        address.setStreet1(((EditText) findViewById(R.id.street1)).getText().toString().trim());
        address.setStreet2(((EditText) findViewById(R.id.street2)).getText().toString().trim());
        address.setCompany(((EditText) findViewById(R.id.company)).getText().toString().trim());
        address.setTelephone(((EditText) findViewById(R.id.telephone)).getText().toString().trim());
        address.setCity(((EditText) findViewById(R.id.city)).getText().toString().trim());
        address.setPostCode(((EditText) findViewById(R.id.postcode)).getText().toString().trim());
        address.setCountry(s_spinner_country.getSelection());
        if (state.getVisibility() == VISIBLE) {
            s_region.setRegionCode(state.getText().toString().trim());
            s_region.setRegionName(state.getText().toString());
            s_region.setRegionID(0);
            address.setRegionID(String.valueOf(0));
            address.setRegion(s_region);
        } else {
            try {
                s_region.setRegionID(Integer.parseInt(shippingRegion.getID()));
            } catch (Exception e) {
                s_region.setRegionID(0);
            }
            s_region.setRegionName(shippingRegion.getName());
            s_region.setRegionCode(shippingRegion.getCode());
            address.setState(s_spinner_state.getSelection());
            address.setRegionID(shippingRegion.getID());
            address.setRegion(s_region);
        }
        address.setVAT(((EditText) findViewById(R.id.vat_id)).getText().toString().trim());
    }

    public boolean checkRequiedAddress() {
        if (!isRequied(((EditText) findViewById(R.id.firstname)))) {
            return false;
        }
        if (!isRequied(((EditText) findViewById(R.id.lastname)))) {
            return false;
        }
        if (!isRequied(((EditText) findViewById(R.id.telephone)))) {
            return false;
        }
        if (!isRequied(((EditText) findViewById(R.id.street1)))) {
            return false;
        }
        if (!isRequied(((EditText) findViewById(R.id.city)))) {
            return false;
        }
        if (!isRequied(((EditText) findViewById(R.id.postcode)))) {
            return false;
        }
        return true;
    }

    public boolean isRequied(EditText editText) {
        return EditTextUtil.checkRequied(getContext(), editText);
    }
}
