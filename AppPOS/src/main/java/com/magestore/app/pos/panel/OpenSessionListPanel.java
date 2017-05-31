package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;

/**
 * Created by Johan on 5/30/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OpenSessionListPanel extends AbstractSimpleRecycleView<OpenSessionValue> {

    public OpenSessionListPanel(Context context) {
        super(context);
    }

    public OpenSessionListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenSessionListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, OpenSessionValue item, int position) {

    }

    @Override
    protected void onClickItem(View view, OpenSessionValue item, int position) {

    }
}
