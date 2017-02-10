package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardRegisterShiftListContentBinding;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftListPanel extends AbstractListPanel<RegisterShift> {

    public RegisterShiftListPanel(Context context) {
        super(context);
    }

    public RegisterShiftListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Xử lý sự kiện floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void bindItem(View view, RegisterShift item, int position) {
        CardRegisterShiftListContentBinding binding = DataBindingUtil.bind(view);
        binding.setRegisterShift(item);
    }
}
