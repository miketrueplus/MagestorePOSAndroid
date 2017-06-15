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
import com.magestore.app.pos.controller.RegisterShiftListController;
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

public class OpenSessionListValuePanel extends AbstractSimpleRecycleView<OpenSessionValue> {
    public static int TYPE_CLOSE_SESSION = 1;
    public static int TYPE_OPEN_SESSION_IN_REGISTER = 2;
    HashMap<OpenSessionValue, EditTextFloat> mapValue;
    HashMap<OpenSessionValue, EditTextInteger> mapAmount;
    HashMap<OpenSessionValue, TextView> mapSubtotal;
    HashMap<OpenSessionValue, Float> mapTotal;
    SessionController mSessionController;
    RegisterShiftListController mRegisterShiftListController;
    int type;
    boolean enableAction = true;

    public void setEnableAction(boolean enableAction) {
        this.enableAction = enableAction;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRegisterShiftListController(RegisterShiftListController mRegisterShiftListController) {
        this.mRegisterShiftListController = mRegisterShiftListController;
    }

    public void setSessionController(SessionController mSessionController) {
        this.mSessionController = mSessionController;
    }

    public OpenSessionListValuePanel(Context context) {
        super(context);
    }

    public OpenSessionListValuePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenSessionListValuePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initLayout() {
        mapValue = new HashMap<>();
        mapAmount = new HashMap<>();
        mapSubtotal = new HashMap<>();
        mapTotal = new HashMap<>();
        getRecycleViewLayoutManager().setReverseLayout(true);
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

        if (enableAction) {
            et_value.setEnabled(true);
            et_amount.setEnabled(true);
            im_remove_value.setVisibility(VISIBLE);
        } else {
            et_value.setEnabled(false);
            et_amount.setEnabled(false);
            im_remove_value.setVisibility(GONE);
        }

        if(mList.indexOf(item) == (mList.size() - 1)){
            et_value.requestFocus();
        }
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
                if (type == TYPE_CLOSE_SESSION) {
                    mRegisterShiftListController.removeValueClose(item);
                } else if (type == TYPE_OPEN_SESSION_IN_REGISTER) {
                    mRegisterShiftListController.removeValueOpen(item);
                } else {
                    mSessionController.removeValue(item);
                }
            }
        });
    }

    @Override
    protected void onClickItem(View view, OpenSessionValue item, int position) {

    }

    private void actionChangeValue(final OpenSessionValue item, final EditTextFloat et_value) {
        et_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float value = et_value.getValueFloat();
                EditTextInteger et_amount = mapAmount.get(item);
                int amount = 0;
                if (et_amount != null) {
                    amount = et_amount.getValueInteger();
                }
                float subtotal = value * amount;
                mapTotal.put(item, subtotal);
                TextView tv_subtotal = mapSubtotal.get(item);
                if (tv_subtotal != null) {
                    tv_subtotal.setText(ConfigUtil.formatPrice(subtotal));
                }
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

    private void actionChangeAmount(final OpenSessionValue item, final EditTextInteger et_amount) {
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int amount = et_amount.getValueInteger();
                EditTextFloat et_value = mapValue.get(item);
                float value = 0;
                if (et_value != null) {
                    value = et_value.getValueFloat();
                }
                float subtotal = value * amount;
                mapTotal.put(item, subtotal);
                TextView tv_subtotal = mapSubtotal.get(item);
                if (tv_subtotal != null) {
                    tv_subtotal.setText(ConfigUtil.formatPrice(subtotal));
                }
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

    public void updateFloatAmount() {
        float total = 0;
        for (float subtotal : mapTotal.values()) {
            total += subtotal;
        }
        if (type == TYPE_CLOSE_SESSION) {
            mRegisterShiftListController.updateFloatAmountClose(total);
        } else if (type == TYPE_OPEN_SESSION_IN_REGISTER) {
            mRegisterShiftListController.updateFloatAmountOpen(total);
        } else {
            mSessionController.updateFloatAmount(total);
        }
    }

    public void focusEditText(){
        EditTextFloat et_value = mapValue.get(mList.get(0));

    }
}
