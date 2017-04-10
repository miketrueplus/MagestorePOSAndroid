package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.service.plugins.PluginsService;
import com.magestore.app.pos.panel.PluginGiftCardListPanel;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PluginGiftCardController extends AbstractListController<Checkout> {
    static final int ACTION_TYPE_ADD_GIFTCARD = 0;
    static final int ACTION_TYPE_REMOVE_GIFTCARD = 1;
    PluginGiftCardListPanel mPluginGiftCardListPanel;
    PluginsService pluginsService;
    List<GiftCard> listGiftCard;
    Map<String, Object> wraper;

    public void setPluginGiftCardListPanel(PluginGiftCardListPanel mPluginGiftCardListPanel) {
        this.mPluginGiftCardListPanel = mPluginGiftCardListPanel;
    }

    public void setPluginsService(PluginsService pluginsService) {
        this.pluginsService = pluginsService;
    }

    public void bindDataToGiftCardList() {
        wraper = new HashMap<>();
        addFirstGiftCard();
        mPluginGiftCardListPanel.bindList(listGiftCard);
    }

    public void doInputAddGiftCard(GiftCard giftCard) {
        doAction(ACTION_TYPE_ADD_GIFTCARD, null, wraper, giftCard);
    }

    public void doInputRemoveGiftCard(GiftCard giftCard) {
        if (!StringUtil.isNullOrEmpty(giftCard.getCouponCode())) {
            doAction(ACTION_TYPE_REMOVE_GIFTCARD, null, wraper, giftCard);
        } else {
            if (listGiftCard.size() == 1) {
                addFirstGiftCard();
            } else {
                listGiftCard.remove(giftCard);
                mPluginGiftCardListPanel.bindList(listGiftCard);
            }
        }
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_TYPE_ADD_GIFTCARD) {
            wraper.put("add_gift_card_respone", pluginsService.addGiftCard((GiftCard) models[0]));
            return true;
        } else if (actionType == ACTION_TYPE_REMOVE_GIFTCARD) {
            wraper.put("remove_gift_card_respone", pluginsService.addGiftCard((GiftCard) models[0]));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_TYPE_ADD_GIFTCARD) {
            // TODO: action sau khi respone order của giftcard
        } else if (success && actionType == ACTION_TYPE_REMOVE_GIFTCARD) {
            if (listGiftCard.size() == 1) {
                addFirstGiftCard();
            } else {
                listGiftCard.remove((GiftCard) models[0]);
            }

            mPluginGiftCardListPanel.bindList(listGiftCard);
        }
    }

    private void addFirstGiftCard() {
        listGiftCard = new ArrayList<>();
        GiftCard firstGiftCard = pluginsService.createGiftCard();
        listGiftCard.add(firstGiftCard);
    }

    public void addMoreGiftCard() {
        GiftCard giftCard = pluginsService.createGiftCard();
        listGiftCard.add(giftCard);
        mPluginGiftCardListPanel.bindList(listGiftCard);
    }
}
