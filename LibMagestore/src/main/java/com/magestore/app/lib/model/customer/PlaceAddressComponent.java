package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 12/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceAddressComponent extends Model {
    String getLongName();
    String getShortName();
    List<String> getTypes();
}
