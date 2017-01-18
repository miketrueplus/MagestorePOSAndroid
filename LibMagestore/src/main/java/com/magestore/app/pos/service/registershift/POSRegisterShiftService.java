package com.magestore.app.pos.service.registershift;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSRegisterShiftService extends AbstractService implements RegisterShiftService {

    @Override
    public List<RegisterShift> retrieveRegisterShiftList() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo register shift gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        RegisterShiftDataAccess registerShiftDataAccess = factory.generateRegisterShiftDataAccess();
        return registerShiftDataAccess.getRegisterShifts();
    }

}
