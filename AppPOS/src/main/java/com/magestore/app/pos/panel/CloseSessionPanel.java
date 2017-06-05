package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 6/5/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CloseSessionPanel extends AbstractDetailPanel<RegisterShift> {
    OpenSessionListValuePanel close_session_list_panel;
    RelativeLayout rl_add_value;
    EditTextFloat et_r_close_balance;
    TextView tv_open_session_balance;

    public CloseSessionPanel(Context context) {
        super(context);
    }

    public CloseSessionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloseSessionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_close_session, null);
        addView(view);
        rl_add_value = (RelativeLayout) findViewById(R.id.rl_add_value);
        et_r_close_balance = (EditTextFloat) view.findViewById(R.id.et_r_close_balance);
        tv_open_session_balance = (TextView) view.findViewById(R.id.tv_open_session_balance);
        close_session_list_panel = (OpenSessionListValuePanel) view.findViewById(R.id.close_session_list_panel);
        close_session_list_panel.setType(OpenSessionListValuePanel.TYPE_CLOSE_SESSION);
    }

    @Override
    public void initValue() {
        close_session_list_panel.setRegisterShiftListController((RegisterShiftListController) mController);
        ((RegisterShiftListController) mController).setOpenSessionListPanel(close_session_list_panel);

        rl_add_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterShiftListController) getController()).addValue();
            }
        });
    }

    @Override
    public void bindItem(RegisterShift item) {
        super.bindItem(item);
        tv_open_session_balance.setText(ConfigUtil.formatPrice(item.getBaseFloatAmount()));
    }

    public void updateFloatAmount(float total) {
        et_r_close_balance.setText(ConfigUtil.formatNumber(total));
    }
}
