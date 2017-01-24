package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShipmentTrackParams extends PosAbstractModel implements OrderShipmentTrackParams {
    String trackNumber;

    @Override
    public String getTrackNumber() {
        return trackNumber;
    }

    @Override
    public void setTrackNumber(String strTrackNumber) {
        trackNumber = strTrackNumber;
    }
}
