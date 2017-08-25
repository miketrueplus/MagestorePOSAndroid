package com.magestore.app.pos.model.magento.checkout.payment;

import com.magestore.app.lib.model.checkout.payment.AuthorizenetParams;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.Map;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosAuthorizenetParams extends PosAbstractModel implements AuthorizenetParams{
    public String url;
    public Map<String, String> params;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
