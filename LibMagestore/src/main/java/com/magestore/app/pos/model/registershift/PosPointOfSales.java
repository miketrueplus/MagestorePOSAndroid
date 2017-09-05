package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 6/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPointOfSales extends PosAbstractModel implements PointOfSales {
    String pos_id;
    String pos_name;
    String location_id;
    String staff_id;
    String store_id;
    String status;

    @Override
    public String getID() {
        return pos_id;
    }

    @Override
    public void setPosId(String strPosId) {
        pos_id = strPosId;
    }

    @Override
    public String getPosName() {
        return pos_name;
    }

    @Override
    public void setPosName(String strPosName) {
        pos_name = strPosName;
    }

    @Override
    public String getLocationId() {
        return location_id;
    }

    @Override
    public void setLocationId(String strLocationId) {
        location_id = strLocationId;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getStaffId() {
        return staff_id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getDisplayContent() {
        return pos_name;
    }
}
