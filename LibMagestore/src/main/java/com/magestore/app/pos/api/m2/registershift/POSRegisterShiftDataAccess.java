package com.magestore.app.pos.api.m2.registershift;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListRegisterShift;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSRegisterShiftDataAccess extends POSAbstractDataAccess implements RegisterShiftDataAccess {
    /**
     * Trả về list register shift
     *
     * @param pageSize    Số customer trên 1 page
     * @param currentPage Trang hiện lại
     * @return Danh sách order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */

    @Override
    public List<RegisterShift> getRegisterShifts() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) doAPI(Gson2PosListRegisterShift.class, POSAPI.REST_REGISTER_SHIFTS_GET_LISTING, null, POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID);
        List<RegisterShift> list = (List<RegisterShift>) (List<?>) (listRegisterShift.items);
        return list;
    }
}
