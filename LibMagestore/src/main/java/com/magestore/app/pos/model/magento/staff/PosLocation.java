package com.magestore.app.pos.model.magento.staff;

import com.magestore.app.lib.model.staff.Location;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 3/28/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosLocation extends PosAbstractModel implements Location {
    String location_id;
    String location_name;
    String location_address;

    @Override
    public String getID() {
        return location_id;
    }

    @Override
    public void setLocationId(String strLocationId) {
        location_id = strLocationId;
    }

    @Override
    public String getLocationName() {
        return location_name;
    }

    @Override
    public void setLocationName(String strLocationName) {
        location_name = strLocationName;
    }

    @Override
    public String getLocationAddress() {
        return location_address;
    }

    @Override
    public void setLocationAddress(String strLocationAddress) {
        location_address = strLocationAddress;
    }
}
