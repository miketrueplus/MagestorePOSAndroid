package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 11/30/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceAutoComplete extends Model {
    String getDescription();
    void setDescription(String strDescription);

    String getPlaceId();
    void setPlaceId(String strPlaceId);
}
