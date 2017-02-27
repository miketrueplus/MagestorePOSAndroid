package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardRegisterShiftCashContentBinding;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftCashListPanel extends AbstractListPanel<CashTransaction> {
    View v;
    RegisterShift registerShift;

    public RegisterShiftCashListPanel(Context context) {
        super(context);
    }

    public RegisterShiftCashListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftCashListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách cash transaction của khách hàng
        v = inflate(getContext(), R.layout.panel_register_shift_cash_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách cash transaction
        setLayoutItem(R.layout.card_register_shift_cash_content);

        // Chuẩn bị list danh sách cash transaction
//        mRecycleView = (RecyclerView) findViewById(R.id.register_shift_cash_list);
//        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void bindItem(View view, CashTransaction item, int position) {
        CardRegisterShiftCashContentBinding binding = DataBindingUtil.bind(view);
        binding.setRegisterShift(registerShift);
        binding.setRegisterShiftCash(item);
    }

    public void setRegisterShift(RegisterShift registerShift) {
        this.registerShift = registerShift;
    }
}
