package com.magestore.app.lib.gateway;

import com.magestore.app.lib.parse.ParseImplement;

/**
 * Các API kết nối đến Rest API của magestore
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface Gateway {
    void setParseImplement(Class clParseImplement);
    void setParseEntity(Class clParseEntity);
    void setSession(GatewaySession session);
}
