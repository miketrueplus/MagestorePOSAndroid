package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.DataListRegisterShift;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataListRegisterShift extends PosAbstractModel implements DataListRegisterShift {
    private List<PosRegisterShift> items;
    private int total_count;

    @Override
    public List<RegisterShift> getItems() {
        return (List<RegisterShift>) (List<?>) items;
    }

    @Override
    public int getTotalCount() {
        return total_count;
    }
}
