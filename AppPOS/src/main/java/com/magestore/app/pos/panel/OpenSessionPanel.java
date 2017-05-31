package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.RegisterShift;
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

public class OpenSessionPanel extends AbstractDetailPanel<RegisterShift> {
    Button bt_open;
    RelativeLayout rl_add_value;
    TextView txt_staff_login;
    EditTextFloat et_float_amount;

    public OpenSessionPanel(Context context) {
        super(context);
    }

    public OpenSessionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenSessionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        et_float_amount = (EditTextFloat) findViewById(R.id.et_float_amount);
        bt_open = (Button) findViewById(R.id.bt_open);
        rl_add_value = (RelativeLayout) findViewById(R.id.rl_add_value);
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
    }

    public void updateFloatAmount(float total){
        et_float_amount.setText(ConfigUtil.formatNumber(total));
    }
}
