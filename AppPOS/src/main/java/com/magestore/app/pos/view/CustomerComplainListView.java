package com.magestore.app.pos.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.pos.databinding.PanelCustomerComplainListBinding;

/**
 * Created by Mike on 1/18/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerComplainListView extends AbstractSimpleListView<Complain> {
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
}
