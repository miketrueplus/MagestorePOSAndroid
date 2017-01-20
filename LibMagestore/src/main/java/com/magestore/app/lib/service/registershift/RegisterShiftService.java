package com.magestore.app.lib.service.registershift;

import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface RegisterShiftService extends Service{
    List<RegisterShift> retrieveRegisterShiftList() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<RegisterShift> insertMakeAdjustment(RegisterShift registerShift) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    CashTransaction createCashTransaction();

    List<CashTransaction> createListCashTransaction(RegisterShift registerShift, String openShift, String balance);
}
