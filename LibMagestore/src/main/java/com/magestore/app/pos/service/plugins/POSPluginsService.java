package com.magestore.app.pos.service.plugins;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.plugins.PluginsDataAccess;
import com.magestore.app.lib.service.plugins.PluginsService;
import com.magestore.app.pos.model.plugins.PosGiftCard;
import com.magestore.app.pos.model.plugins.PosGiftCardRemoveParam;
import com.magestore.app.pos.model.plugins.PosRewardPoint;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSPluginsService extends AbstractService implements PluginsService {

    @Override
    public RewardPoint createRewardPoint() {
        return new PosRewardPoint();
    }

    @Override
    public GiftCard createGiftCard() {
        return new PosGiftCard();
    }

    @Override
    public GiftCardRemoveParam createGiftCardRemoveParam() {
        return new PosGiftCardRemoveParam();
    }

    @Override
    public Checkout applyRewarPoint(RewardPoint rewardPoint) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo plugin gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        PluginsDataAccess pluginsDataAccess = factory.generatePluginsDataAccess();
        return pluginsDataAccess.applyRewarPoint(rewardPoint);
    }

    @Override
    public Checkout addGiftCard(GiftCard giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo plugin gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        PluginsDataAccess pluginsDataAccess = factory.generatePluginsDataAccess();
        return pluginsDataAccess.addGiftCard(giftCard);
    }

    @Override
    public Checkout removeGiftCard(GiftCardRemoveParam giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo plugin gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        PluginsDataAccess pluginsDataAccess = factory.generatePluginsDataAccess();
        return pluginsDataAccess.removeGiftCard(giftCard);
    }
}
