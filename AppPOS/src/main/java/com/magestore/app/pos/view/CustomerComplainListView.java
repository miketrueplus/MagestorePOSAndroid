package com.magestore.app.pos.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.panel.AbstractSimpleListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.PanelCustomerComplainListBinding;

/**
 * Created by Mike on 1/18/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerComplainListView extends AbstractSimpleListPanel<Complain> {
    public CustomerComplainListView(Context context) {
        super(context);
    }

    public CustomerComplainListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerComplainListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, Complain item, int position) {
        PanelCustomerComplainListBinding binding = DataBindingUtil.bind(view);
        binding.setComplain(item);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách khách hàng
//        View v = inflate(getContext(), R.layout.panel_customer_complain_list, null);
//        addView(v);
    }
}
