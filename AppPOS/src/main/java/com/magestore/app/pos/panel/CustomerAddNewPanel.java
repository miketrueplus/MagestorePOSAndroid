package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CustomerListController;

import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CustomerAddNewPanel extends AbstractDetailPanel<Customer> {
    SimpleSpinner mspinGroupID;
    SimpleSpinner s_spinner_country;
    SimpleSpinner b_spinner_country;
    LinearLayout ll_add_new_customer;
    LinearLayout ll_shipping_address;
    LinearLayout ll_billing_address;
    LinearLayout ll_new_shipping_address;
    LinearLayout ll_new_billing_address;

    public CustomerAddNewPanel(Context context) {
        super(context);
    }

    public CustomerAddNewPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerAddNewPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    }

    @Override
    public void bindItem(Customer item) {
        super.bindItem(item);
        setCustomerGroupDataSet(((CustomerListController) mController).getCustomerGroupList());
        setCountryDataSet(((CustomerListController) mController).getCountry());

        ll_shipping_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_shipping_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
            }
        });

        ll_billing_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_shipping_address.setVisibility(GONE);
            }
        });
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

    public void setCountryDataSet(List<ConfigCountry> countryDataSet) {
        if (countryDataSet != null) {
            s_spinner_country.bind(countryDataSet.toArray(new ConfigCountry[0]));
            b_spinner_country.bind(countryDataSet.toArray(new ConfigCountry[0]));
        }
    }
}
