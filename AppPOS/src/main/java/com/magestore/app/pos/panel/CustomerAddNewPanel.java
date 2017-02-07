package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;

/**
 * Created by Johan on 2/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CustomerAddNewPanel extends AbstractDetailPanel<Customer> {
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
    }

}
