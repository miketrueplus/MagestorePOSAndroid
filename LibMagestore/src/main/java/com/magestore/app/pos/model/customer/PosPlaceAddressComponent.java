package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.PlaceAddressComponent;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 12/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceAddressComponent extends PosAbstractModel implements PlaceAddressComponent {
    String long_name;
    String short_name;
    List<String> types;

    @Override
    public String getLongName() {
        return long_name;
    }

    @Override
    public String getShortName() {
        return short_name;
    }

    @Override
    public List<String> getTypes() {
        return types;
    }
}
