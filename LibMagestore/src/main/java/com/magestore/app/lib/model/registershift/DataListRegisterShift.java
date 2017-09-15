package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataListRegisterShift extends Model{
    List<RegisterShift> getItems();
    int getTotalCount();
}
