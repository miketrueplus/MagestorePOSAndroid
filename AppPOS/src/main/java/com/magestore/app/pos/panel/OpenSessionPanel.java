package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.SessionController;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OpenSessionPanel extends AbstractDetailPanel<RegisterShift> {
    Button bt_open;
    RelativeLayout rl_add_value;
    SessionController mSessionController;

    public void setSessionController(SessionController mSessionController) {
        this.mSessionController = mSessionController;
    }

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
        bt_open = (Button) getView().findViewById(R.id.bt_open);
        rl_add_value = (RelativeLayout) getView().findViewById(R.id.rl_add_value);
        initValue();
    }

    @Override
    public void initValue() {
        rl_add_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionController.addValue();
            }
        });
    }
}
