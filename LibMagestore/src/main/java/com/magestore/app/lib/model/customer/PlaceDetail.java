package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 12/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceDetail extends Model {
    List<PlaceAddressComponent> getAddressComponents();
    void setAddressComponents(List<PlaceAddressComponent> mListAddressComponents);
}
