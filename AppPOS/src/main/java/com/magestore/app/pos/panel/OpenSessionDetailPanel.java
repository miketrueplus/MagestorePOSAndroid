package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.SessionController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OpenSessionDetailPanel extends AbstractDetailPanel<RegisterShift> {
    Button bt_open;
    RelativeLayout rl_add_value;
    TextView txt_staff_login;
    EditTextFloat et_float_amount;
    EditText et_note;

    public OpenSessionDetailPanel(Context context) {
        super(context);
    }

    public OpenSessionDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenSessionDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        et_float_amount = (EditTextFloat) findViewById(R.id.et_float_amount);
        bt_open = (Button) findViewById(R.id.bt_open);
        rl_add_value = (RelativeLayout) findViewById(R.id.rl_add_value);
        et_note = (EditText) findViewById(R.id.et_note);
        txt_staff_login = (TextView) findViewById(R.id.txt_staff_login);
        initValue();
    }

    @Override
    public void initValue() {
        rl_add_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionController) mController).addValue();
            }
        });

        txt_staff_login.setText(getContext().getString(R.string.open_session_login, ConfigUtil.getStaff().getStaffName()));

        bt_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionParam param = ((SessionController) mController).createSessionParam();
                float float_amount = et_float_amount.getValueFloat();
                float base_float_amount = ConfigUtil.convertToBasePrice(float_amount);
                param.setBalance(float_amount);
                param.setBaseBalance(base_float_amount);
                param.setBaseCashAdded(base_float_amount);
                param.setBaseFloatAmount(base_float_amount);
                param.setFloatAmount(float_amount);
                param.setBaseCurrencyCode(ConfigUtil.getBaseCurrencyCode());
                param.setCashAdded(float_amount);
                param.setOpenedNote(et_note.getText().toString());
                param.setShiftCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());
                param.setStaffId(ConfigUtil.getPointOfSales().getStaffId());
                param.setLocationId(ConfigUtil.getPointOfSales().getLocationId());
                param.setOpenedAt(ConfigUtil.getCurrentDateTime());
                param.setCloseAt(ConfigUtil.getCurrentDateTime());
                param.setShiftId(ConfigUtil.getPointOfSales().getID());
                ((SessionController) mController).doInputOpenSession(param);
            }
        });
    }

    public void updateFloatAmount(float total) {
        et_float_amount.setText(ConfigUtil.formatNumber(total));
    }
}
