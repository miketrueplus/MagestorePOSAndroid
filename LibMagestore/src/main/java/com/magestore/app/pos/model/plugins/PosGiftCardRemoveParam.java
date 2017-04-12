package com.magestore.app.pos.model.plugins;

import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 4/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosGiftCardRemoveParam extends PosAbstractModel implements GiftCardRemoveParam {
    String quote_id;
    String code;

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
    }
}
