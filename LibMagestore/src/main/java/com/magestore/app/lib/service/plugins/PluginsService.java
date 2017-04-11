package com.magestore.app.lib.service.plugins;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PluginsService extends Service {
    GiftCard createGiftCard();

    Checkout addGiftCard(GiftCard giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout removeGiftCard(GiftCard giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
