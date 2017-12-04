package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.PlaceAddressComponent;
import com.magestore.app.lib.model.customer.PlaceDetail;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 12/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceDetail extends PosAbstractModel implements PlaceDetail {
    List<PosPlaceAddressComponent> address_components;

    @Override
    public List<PlaceAddressComponent> getAddressComponents() {
        return (List<PlaceAddressComponent>) (List<?>) address_components;
    }

    @Override
    public void setAddressComponents(List<PlaceAddressComponent> mListAddressComponents) {
        address_components = (List<PosPlaceAddressComponent>) (List<?>) mListAddressComponents;
    }
}
