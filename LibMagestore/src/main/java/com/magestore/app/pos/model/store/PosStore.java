package com.magestore.app.pos.model.store;

import com.magestore.app.lib.model.store.Store;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosStore extends PosAbstractModel implements Store {
    String store_id;
    String code;
    String website_id;
    String group_id;
    String name;
    String sort_order;
    String is_active;

    @Override
    public String getID() {
        return store_id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getWebsiteId() {
        return website_id;
    }

    @Override
    public String getGroupId() {
        return group_id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSortOrder() {
        return sort_order;
    }

    @Override
    public String getIsActive() {
        return is_active;
    }

    @Override
    public String getDisplayContent() {
        return name;
    }
}
