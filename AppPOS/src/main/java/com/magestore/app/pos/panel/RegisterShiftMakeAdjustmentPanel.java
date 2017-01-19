package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.databinding.PanelRegisterShiftMakeAdjustmentBinding;

/**
 * Created by Johan on 1/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftMakeAdjustmentPanel extends AbstractDetailPanel<RegisterShift> {
    PanelRegisterShiftMakeAdjustmentBinding mBinding;

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
        // Bind view sang object
        if (getView() != null)
            mBinding = DataBindingUtil.bind(getView());
    }

    @Override
    public void bindItem(RegisterShift item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        if (mBinding == null) mBinding = DataBindingUtil.bind(getView());
        mBinding.setRegisterShift(item);
    }
}
