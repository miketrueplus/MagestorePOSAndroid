package com.magestore.app.lib.resourcemodel.registershift;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.ListDataAccess;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface RegisterShiftDataAccess extends DataAccess, ListDataAccess<RegisterShift> {
    List<RegisterShift> openSession(SessionParam sessionParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
    List<RegisterShift> insertMakeAdjustment(CashTransaction cashTransaction) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
    List<RegisterShift> closeSession(SessionParam sessionParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
