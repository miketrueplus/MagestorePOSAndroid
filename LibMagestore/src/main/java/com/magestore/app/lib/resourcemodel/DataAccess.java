package com.magestore.app.lib.resourcemodel;

import com.magestore.app.lib.context.MagestoreContext;

/**
 * Các API kết nối đến Rest API của magestore
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface DataAccess {
    void setContext(MagestoreContext context);
    MagestoreContext getContext();
    void setParseImplement(Class clParseImplement);
    void setParseEntity(Class clParseEntity);
    void setSession(DataAccessSession session);
}
