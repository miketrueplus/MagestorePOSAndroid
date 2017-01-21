package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.databinding.PanelRegisterShiftMakeAdjustmentBinding;

/**
 * Created by Johan on 1/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftMakeAdjustmentPanel extends AbstractDetailPanel<RegisterShift> {
    PanelRegisterShiftMakeAdjustmentBinding mBinding;
    RegisterShift registerShift;
    TextView tv_add;
    TextView tv_remove;
    EditText edt_note;
    EditText edt_amount;
    private static String ADD_MAKE_ADJUSTMENT = "add";
    private static String REMOVE_MAKE_ADJUSTMENT = "remove";
    private String selectMakeAdjustment = ADD_MAKE_ADJUSTMENT;

    public RegisterShiftMakeAdjustmentPanel(Context context) {
        super(context);
    }

    public RegisterShiftMakeAdjustmentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftMakeAdjustmentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_register_shift_make_adjustment, null);
        addView(view);

        tv_add = (TextView) view.findViewById(R.id.add);
        tv_remove = (TextView) view.findViewById(R.id.remove);
        edt_note = (EditText) view.findViewById(R.id.note);
        edt_amount = (EditText) view.findViewById(R.id.amount);
        mBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void bindItem(RegisterShift item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        if (mBinding == null) mBinding = DataBindingUtil.bind(getView());
        mBinding.setRegisterShift(item);
        registerShift = item;

        tv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddMakeAdjusment();
            }
        });

        tv_remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRemoveMakeAdjustment();
            }
        });
    }

    @Override
    public RegisterShift bind2Item() {
        float value = 0;
        try {
            value = Float.parseFloat(edt_amount.getText().toString().trim());
        } catch (Exception e) {
            value = 0;
        }

        String note = "";
        if (!TextUtils.isEmpty(edt_note.getText().toString().trim())) {
            note = edt_note.getText().toString().trim();
        }

        // TODO: thiếu setCreateAt() lấy theo h máy hiệnt tại
        CashTransaction cashTransaction = ((RegisterShiftListController) mController).createCashTransaction();
        cashTransaction.setType(selectMakeAdjustment);
        cashTransaction.setValue(value);
        cashTransaction.setNote(note);
        registerShift.setParamCash(cashTransaction);
        return registerShift;
    }

    private void selectAddMakeAdjusment() {
        selectMakeAdjustment = ADD_MAKE_ADJUSTMENT;
        tv_add.setTextColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_text_select));
        tv_add.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_bg_select));
        tv_remove.setTextColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_text_not_select));
        tv_remove.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_bg_not_select));
    }

    private void selectRemoveMakeAdjustment() {
        selectMakeAdjustment = REMOVE_MAKE_ADJUSTMENT;
        tv_add.setTextColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_text_not_select));
        tv_add.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_bg_not_select));
        tv_remove.setTextColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_text_select));
        tv_remove.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.register_shift_dialog_make_adjustment_bg_select));
    }
}
