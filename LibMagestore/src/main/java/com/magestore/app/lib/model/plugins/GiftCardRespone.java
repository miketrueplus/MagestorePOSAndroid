package com.magestore.app.lib.model.plugins;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 4/11/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface GiftCardRespone extends Model {
    List<GiftCard> getUsedCodes();
    void setUsedCodes(List<GiftCard> listUsedCodes);
}
