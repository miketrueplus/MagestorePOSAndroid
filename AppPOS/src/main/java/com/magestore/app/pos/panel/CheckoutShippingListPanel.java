package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;


import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.pos.databinding.CardCheckoutShippingContentBinding;

import com.magestore.app.pos.R;
/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutShippingListPanel extends AbstractSimpleListView<ShippingMethod> {
    int selectedPos = -1;
    public CheckoutShippingListPanel(Context context) {
        super(context);
    }

    public CheckoutShippingListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutShippingListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, ShippingMethod item, final int position) {
        CardCheckoutShippingContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setShippingMethod(item);

        // chekc trạng thái radio button
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.rad_selected_method);
        radioButton.setChecked(position == selectedPos);
        radioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = position;
                notifyDataSetChanged();
            }
        });
    }

    public ShippingMethod getSelectedShippingMethod() {
        if (selectedPos < 0) return null;
        return mList.get(selectedPos);
    }
}
