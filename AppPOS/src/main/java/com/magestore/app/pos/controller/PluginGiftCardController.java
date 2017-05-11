package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
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

public class PluginGiftCardController extends AbstractListController<GiftCard> {
    static final int ACTION_TYPE_ADD_GIFTCARD = 0;
    static final int ACTION_TYPE_REMOVE_GIFTCARD = 1;
    CheckoutListController mCheckoutListController;
    PluginGiftCardListPanel mPluginGiftCardListPanel;
    PluginsService pluginsService;
    List<GiftCard> listGiftCard;
    Map<String, Object> wraper;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

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
        mCheckoutListController.isShowLoadingDetail(true);
        String quote_id = mCheckoutListController.getSelectedItem().getQuoteId();
        giftCard.setQuoteId(quote_id);
        doAction(ACTION_TYPE_ADD_GIFTCARD, null, wraper, giftCard);
    }

    public void doInputRemoveGiftCard(GiftCard giftCard) {
        String quote_id = mCheckoutListController.getSelectedItem().getQuoteId();
        giftCard.setQuoteId(quote_id);
        if (!StringUtil.isNullOrEmpty(giftCard.getCouponCode())) {
            mCheckoutListController.isShowLoadingDetail(true);
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
            GiftCard giftCard = (GiftCard) models[0];
            GiftCardRemoveParam giftCardRemoveParam = pluginsService.createGiftCardRemoveParam();
            giftCardRemoveParam.setQuoteId(giftCard.getQuoteId());
            giftCardRemoveParam.setCode(giftCard.getCouponCode());
            wraper.put("remove_gift_card_respone", pluginsService.removeGiftCard(giftCardRemoveParam));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onActionPostExecute(success, actionType, actionCode, wraper, models);
        if (success && actionType == ACTION_TYPE_ADD_GIFTCARD) {
            Checkout checkout = (Checkout) wraper.get("add_gift_card_respone");
            GiftCard giftCard = (GiftCard) models[0];
            mCheckoutListController.updateToTal(checkout);
            setAmountToGiftCard(checkout, listGiftCard.get(listGiftCard.indexOf(giftCard)));
            mPluginGiftCardListPanel.enableGiftCodeValue(giftCard);
            mPluginGiftCardListPanel.enableUseMaxPoint(giftCard);
            mCheckoutListController.isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_REMOVE_GIFTCARD) {
            Checkout checkout = (Checkout) wraper.get("remove_gift_card_respone");
            if (listGiftCard.size() == 1) {
                addFirstGiftCard();
            } else {
                listGiftCard.remove((GiftCard) models[0]);
            }

            mPluginGiftCardListPanel.bindList(listGiftCard);
            mCheckoutListController.updateToTal(checkout);
            mCheckoutListController.isShowLoadingDetail(false);
        }
    }

    @Override
    public void onCancelledBackground(Exception exp, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        mCheckoutListController.isShowLoadingDetail(false);
        mPluginGiftCardListPanel.showError(exp.getMessage());
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

    private void setAmountToGiftCard(Checkout checkout, GiftCard giftCard) {
        if (checkout.getGiftCard() != null) {
            List<GiftCard> giftCardList = checkout.getGiftCard().getUsedCodes();
            if (giftCardList != null && giftCardList.size() > 0) {
                for (GiftCard item : giftCardList) {
                    if (item.getCouponCode().equals(giftCard.getCouponCode().toUpperCase())) {
                        giftCard.setAmount(item.getAmount());
                        giftCard.setBalance(item.getBalance());
                        mPluginGiftCardListPanel.updateBalance(giftCard);
                    }
                }
            }
        }
    }

    public void resetListGiftCard(){
        mPluginGiftCardListPanel.resetListGiftCard();
        addFirstGiftCard();
        mPluginGiftCardListPanel.bindList(listGiftCard);
    }
}
