package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractListPanel;

/**
 * Created by Johan on 5/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OpenSessionListPanel extends AbstractListPanel<RegisterShift>{
    public OpenSessionListPanel(Context context) {
        super(context);
    }

    public OpenSessionListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenSessionListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
