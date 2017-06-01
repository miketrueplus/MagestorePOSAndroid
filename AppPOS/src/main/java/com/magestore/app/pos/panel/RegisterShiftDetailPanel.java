package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftCashListController;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.controller.RegisterShiftSaleListController;
import com.magestore.app.pos.databinding.PanelRegisterShiftDetailBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftDetailPanel extends AbstractDetailPanel<RegisterShift> {
    View v;
    PanelRegisterShiftDetailBinding mBinding;
    RegisterShiftSaleListPanel mRegisterShiftSaleListPanel;
    RegisterShiftSaleListController mRegisterShiftSaleListController;
    RegisterShiftCashListPanel mRegisterShiftCashListPanel;
    RegisterShiftCashListController mRegisterShiftCashListController;
    TextView tv_staff_name;
    TextView tv_location;

    public RegisterShiftDetailPanel(Context context) {
        super(context);
    }

    public RegisterShiftDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        // Load layout view danh sách register shift
        v = inflate(getContext(), R.layout.panel_register_shift_detail, null);
        addView(v);
        mBinding = DataBindingUtil.bind(v);

        tv_staff_name = (TextView) v.findViewById(R.id.tv_location);
        tv_location = (TextView) v.findViewById(R.id.tv_location);

        // chuẩn bị panel view danh sách payment
        mRegisterShiftSaleListPanel = (RegisterShiftSaleListPanel) findViewById(R.id.register_shift_sales);

        // chuẩn bị panel view danh sách cash transaction
        mRegisterShiftCashListPanel = (RegisterShiftCashListPanel) findViewById(R.id.register_shift_cash);

    }

    @Override
    public void initModel() {
        // Lấy lại customer service từ controller của panel
        Controller controller = getController();

        // Controller Payment
        mRegisterShiftSaleListController = new RegisterShiftSaleListController();
        mRegisterShiftSaleListController.setView(mRegisterShiftSaleListPanel);
        mRegisterShiftSaleListController.setMagestoreContext(controller.getMagestoreContext());

        mRegisterShiftCashListController = new RegisterShiftCashListController();
        mRegisterShiftCashListController.setView(mRegisterShiftCashListPanel);
        mRegisterShiftCashListController.setMagestoreContext(controller.getMagestoreContext());

        if (controller instanceof RegisterShiftListController) {
            mRegisterShiftSaleListController.setRegisterShiftService(((RegisterShiftListController) controller).getRegisterShiftService());
            mRegisterShiftCashListController.setRegisterShiftService(((RegisterShiftListController) controller).getRegisterShiftService());
        }
    }

    @Override
    public void bindItem(final RegisterShift item) {
        super.bindItem(item);
        mBinding.setRegisterShift(item);
        tv_staff_name.setText(item.getStaffName());
        // TODO: để tạm location staff
        tv_location.setText(ConfigUtil.getStaff().getStaffLocation().getLocationAddress());
        mRegisterShiftSaleListController.doSelectRegisterShift(item);
        mRegisterShiftCashListController.doSelectRegisterShift(item);
        mRegisterShiftCashListPanel.setRegisterShift(item);

        ((Button) v.findViewById(R.id.make_adjustment)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMakeAdjustment(item);
            }
        });
    }

    private void showMakeAdjustment(RegisterShift item) {
        final RegisterShiftMakeAdjustmentPanel panelMakeAdjustment = new RegisterShiftMakeAdjustmentPanel(getContext());
        panelMakeAdjustment.bindItem(item);
        panelMakeAdjustment.setController(mController);
        MagestoreDialog dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.register_shift_dialog_make_adjustment_title), panelMakeAdjustment);
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterShiftListController) mController).doInputMakeAdjustment(panelMakeAdjustment.bind2Item());
            }
        });
    }
}
