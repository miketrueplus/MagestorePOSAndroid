package com.magestore.app.lib.resourcemodel.plugins;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PluginsDataAccess extends DataAccess {
    Checkout applyRewarPoint(RewardPoint rewardPoint) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Checkout addGiftCard(GiftCard giftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Checkout removeGiftCard(GiftCardRemoveParam giftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
