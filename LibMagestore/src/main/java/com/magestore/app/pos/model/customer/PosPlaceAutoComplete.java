package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.PlaceAutoComplete;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 11/30/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceAutoComplete extends PosAbstractModel implements PlaceAutoComplete {
    String description;
    String place_id;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String strDescription) {
        description = strDescription;
    }

    @Override
    public String getPlaceId() {
        return place_id;
    }

    @Override
    public void setPlaceId(String strPlaceId) {
        place_id = strPlaceId;
    }

    @Override
    public String getDisplayContent() {
        return description;
    }
}
