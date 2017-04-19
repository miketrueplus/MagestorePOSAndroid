package com.magestore.app.lib.model.checkout.payment;

import com.magestore.app.lib.model.Model;

import java.util.Map;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface AuthorizenetParams extends Model {
    String getUrl();
    Map<String, String> getParams();
}
