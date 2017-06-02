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
    public String getPosName() {
        return pos_name;
    }

    @Override
    public String getLocationId() {
        return location_id;
    }

    @Override
    public String getStoreId() {
        return store_id;
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
