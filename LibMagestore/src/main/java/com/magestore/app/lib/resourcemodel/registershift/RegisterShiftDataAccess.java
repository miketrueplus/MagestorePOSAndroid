package com.magestore.app.lib.resourcemodel.registershift;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface RegisterShiftDataAccess {
    List<RegisterShift> getRegisterShifts() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
