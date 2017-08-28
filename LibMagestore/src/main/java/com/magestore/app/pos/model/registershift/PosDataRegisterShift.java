package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.DataRegisterShift;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 8/28/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataRegisterShift extends PosAbstractModel implements DataRegisterShift {
    PosRegisterShift data;

    @Override
    public RegisterShift getRegisterShift() {
        return data;
    }
}
