package com.magestore.app.pos.model.magento.plugins;

import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRespone;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 4/11/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosGiftCardRespone extends PosAbstractModel implements GiftCardRespone {
    List<PosGiftCard> used_codes;

    @Override
    public List<GiftCard> getUsedCodes() {
        return (List<GiftCard>) (List<?>) used_codes;
    }

    @Override
    public void setUsedCodes(List<GiftCard> listUsedCodes) {
        used_codes = (List<PosGiftCard>) (List<?>) listUsedCodes;
    }
}
