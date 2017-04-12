package com.magestore.app.lib.model.plugins;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface GiftCardRemoveParam extends Model {
    void setQuoteId(String strQuoteId);
    void setCode(String strCode);
}
