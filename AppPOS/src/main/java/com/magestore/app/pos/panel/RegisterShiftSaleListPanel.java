package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardRegisterShiftSaleContentBinding;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftSaleListPanel extends AbstractListPanel<SaleSummary> {
    public RegisterShiftSaleListPanel(Context context) {
        super(context);
    }

    public RegisterShiftSaleListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftSaleListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách payment của khách hàng
        View v = inflate(getContext(), R.layout.panel_register_shift_sale_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách đơn hàng
        setLayoutItem(R.layout.card_register_shift_sale_content);

        // Chuẩn bị list danh sách payment
        mRecycleView = (RecyclerView) findViewById(R.id.register_shift_sale_list);
        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void bindItem(View view, SaleSummary item, int position) {
        CardRegisterShiftSaleContentBinding binding = DataBindingUtil.bind(view);
        binding.setRegisterShiftSale(item);
    }
}
