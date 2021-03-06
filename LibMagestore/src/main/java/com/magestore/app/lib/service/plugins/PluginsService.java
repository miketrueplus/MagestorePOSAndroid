package com.magestore.app.lib.service.plugins;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PluginsService extends Service {
    RewardPoint createRewardPoint();

    GiftCard createGiftCard();

    GiftCardRemoveParam createGiftCardRemoveParam();

    Checkout applyRewarPoint(Checkout checkout, RewardPoint rewardPoint) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout addGiftCard(Checkout checkout, GiftCard giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout removeGiftCard(Checkout checkout, GiftCardRemoveParam giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
