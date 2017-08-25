package com.magestore.app.pos.service.plugins;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.plugins.PluginsDataAccess;
import com.magestore.app.lib.service.plugins.PluginsService;
import com.magestore.app.pos.model.magento.plugins.PosGiftCard;
import com.magestore.app.pos.model.magento.plugins.PosGiftCardRemoveParam;
import com.magestore.app.pos.model.magento.plugins.PosRewardPoint;
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
    public Checkout applyRewarPoint(Checkout checkout, RewardPoint rewardPoint) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo plugin gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        PluginsDataAccess pluginsDataAccess = factory.generatePluginsDataAccess();
        return pluginsDataAccess.applyRewarPoint(checkout, rewardPoint);
    }

    @Override
    public Checkout addGiftCard(Checkout checkout, GiftCard giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo plugin gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        PluginsDataAccess pluginsDataAccess = factory.generatePluginsDataAccess();
        return pluginsDataAccess.addGiftCard(checkout, giftCard);
    }

    @Override
    public Checkout removeGiftCard(Checkout checkout, GiftCardRemoveParam giftCard) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo plugin gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        PluginsDataAccess pluginsDataAccess = factory.generatePluginsDataAccess();
        return pluginsDataAccess.removeGiftCard(checkout, giftCard);
    }
}
