package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.SessionController;
import com.magestore.app.pos.databinding.CardOpenSessionListContentBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;
import com.magestore.app.view.EditTextInteger;

import java.util.HashMap;

/**
 * Created by Johan on 5/30/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OpenSessionListPanel extends AbstractSimpleRecycleView<OpenSessionValue> {
    HashMap<OpenSessionValue, EditTextFloat> mapValue;
    HashMap<OpenSessionValue, EditTextInteger> mapAmount;
    HashMap<OpenSessionValue, TextView> mapSubtotal;
    HashMap<OpenSessionValue, Float> mapTotal;
    SessionController mSessionController;

    public void setSessionController(SessionController mSessionController) {
        this.mSessionController = mSessionController;
    }

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
    public void initLayout() {
        mapValue = new HashMap<>();
        mapAmount = new HashMap<>();
        mapSubtotal = new HashMap<>();
        mapTotal = new HashMap<>();
    }

    @Override
    protected void bindItem(View view, OpenSessionValue item, int position) {
        CardOpenSessionListContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setOpenSessionValue(item);

        EditTextFloat et_value = (EditTextFloat) view.findViewById(R.id.et_value);
        mapValue.put(item, et_value);
        EditTextInteger et_amount = (EditTextInteger) view.findViewById(R.id.et_amount);
        mapAmount.put(item, et_amount);
        TextView tv_subtotal = (TextView) view.findViewById(R.id.tv_subtotal);
        mapSubtotal.put(item, tv_subtotal);
        ImageView im_remove_value = (ImageView) view.findViewById(R.id.im_remove_value);
        actionChangeValue(item, et_value);
        actionChangeAmount(item, et_amount);
        actionClickRemove(item, im_remove_value);
    }

    private void actionClickRemove(final OpenSessionValue item, ImageView im_remove_value) {
        im_remove_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mapValue.remove(item);
                mapAmount.remove(item);
                mapSubtotal.remove(item);
                mapTotal.remove(item);
                updateFloatAmount();
                mSessionController.removeValue(item);
            }
        });
    }

    @Override
    protected void onClickItem(View view, OpenSessionValue item, int position) {

    }

    private void actionChangeValue(final OpenSessionValue item, final EditTextFloat et_value){
        et_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float value = et_value.getValueFloat();
                EditTextInteger et_amount = mapAmount.get(item);
                int amount = et_amount.getValueInteger();
                TextView tv_subtotal = mapSubtotal.get(item);
                float subtotal = value * amount;
                mapTotal.put(item, subtotal);
                tv_subtotal.setText(ConfigUtil.formatPrice(subtotal));
                item.setValue(value);
                item.setAmount(amount);
                item.setSubtotal(subtotal);
                updateFloatAmount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionChangeAmount(final OpenSessionValue item, final EditTextInteger et_amount){
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int amount = et_amount.getValueInteger();
                EditTextFloat et_value = mapValue.get(item);
                float value = et_value.getValueFloat();
                TextView tv_subtotal = mapSubtotal.get(item);
                float subtotal = value * amount;
                mapTotal.put(item, subtotal);
                tv_subtotal.setText(ConfigUtil.formatPrice(subtotal));
                item.setValue(value);
                item.setAmount(amount);
                item.setSubtotal(subtotal);
                updateFloatAmount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void updateFloatAmount(){
        float total = 0;
        for (float subtotal: mapTotal.values()) {
            total += subtotal;
        }
        mSessionController.updateFloatAmount(total);
    }
}
