package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.PluginGiftCardController;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PluginGiftCardPanel extends AbstractListPanel<GiftCard> {
    PluginGiftCardController mPluginGiftCardController;

    public void setPluginGiftCardController(PluginGiftCardController mPluginGiftCardController) {
        this.mPluginGiftCardController = mPluginGiftCardController;
    }

    public PluginGiftCardPanel(Context context) {
        super(context);
    }

    public PluginGiftCardPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PluginGiftCardPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        LinearLayout ll_add_giftcard = (LinearLayout) findViewById(R.id.ll_add_giftcard);
        ll_add_giftcard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPluginGiftCardController.addMoreGiftCard();
            }
        });
    }

    public void resetListGiftCard() {
        mPluginGiftCardController.resetListGiftCard();
    }
}
