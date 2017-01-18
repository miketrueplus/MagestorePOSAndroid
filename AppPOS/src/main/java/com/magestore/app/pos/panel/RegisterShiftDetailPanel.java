package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftDetailPanel extends AbstractDetailPanel<RegisterShift> {
    public RegisterShiftDetailPanel(Context context) {
        super(context);
    }

    public RegisterShiftDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
