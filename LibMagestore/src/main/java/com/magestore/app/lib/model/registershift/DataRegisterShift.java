package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 8/28/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataRegisterShift extends Model {
    List<RegisterShift> getListRegisterShift();
}
