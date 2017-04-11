package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.PluginGiftCardController;
import com.magestore.app.pos.databinding.PluginGiftCardContentLayoutBinding;
import com.magestore.app.util.ConfigUtil;

import java.util.HashMap;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PluginGiftCardListPanel extends AbstractSimpleRecycleView<GiftCard> {
    PluginGiftCardController mPluginGiftCardController;
    HashMap<GiftCard, EditText> mTextGiftCode;
    HashMap<GiftCard, EditText> mTextGiftCodeValue;

    public void setPluginGiftCardController(PluginGiftCardController mPluginGiftCardController) {
        this.mPluginGiftCardController = mPluginGiftCardController;
    }

    public PluginGiftCardListPanel(Context context) {
        super(context);
    }

    public PluginGiftCardListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PluginGiftCardListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initLayout() {
        mTextGiftCode = new HashMap<>();
        mTextGiftCodeValue = new HashMap<>();
    }

    @Override
    protected void bindItem(View view, final GiftCard item, int position) {
        PluginGiftCardContentLayoutBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setGiftCard(item);

        EditText gift_code = (EditText) view.findViewById(R.id.gift_code);
        mTextGiftCode.put(item, gift_code);
        EditText gift_code_value = (EditText) view.findViewById(R.id.gift_code_value);
        mTextGiftCodeValue.put(item, gift_code_value);
        Button bt_apply = (Button) view.findViewById(R.id.bt_apply);
        actionChangeGiftCode(item, gift_code, bt_apply);

        bt_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonApply(item);
            }
        });

        RelativeLayout rl_remove_payment = (RelativeLayout) view.findViewById(R.id.rl_remove_gift_code);
        rl_remove_payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionRemoveGiftCode(item);
            }
        });
    }

    @Override
    protected void onClickItem(View view, GiftCard item, int position) {

    }

    private void actionChangeGiftCode(final GiftCard item, final EditText gift_code, final Button bt_apply) {
        gift_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String coupon_code = gift_code.getText().toString();
                if (coupon_code.length() > 0) {
                    bt_apply.setEnabled(true);
                    item.setCouponCode(coupon_code);
                } else {
                    bt_apply.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onClickButtonApply(GiftCard giftCard) {
        mPluginGiftCardController.doInputAddGiftCard(giftCard);
    }

    private void actionRemoveGiftCode(GiftCard giftCard){
        mPluginGiftCardController.doInputRemoveGiftCard(giftCard);
    }

    public void enableGiftCodeValue(GiftCard giftCard){
        EditText gift_code_value = mTextGiftCodeValue.get(giftCard);
        gift_code_value.setText(ConfigUtil.formatNumber(giftCard.getBalance()));
    }
}
