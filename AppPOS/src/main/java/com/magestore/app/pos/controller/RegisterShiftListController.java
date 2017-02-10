package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.service.registershift.RegisterShiftService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftListController extends AbstractListController<RegisterShift> {
    static final int ACTION_CODE_MAKE_ADJUSTMENT = 0;

    /**
     * Service xử lý các vấn đề liên quan đến register shift
     */
    RegisterShiftService mRegisterShiftService;

    /**
     * Thiết lập service
     *
     * @param mRegisterShiftService
     */
    public void setRegisterShiftService(RegisterShiftService mRegisterShiftService) {
        this.mRegisterShiftService = mRegisterShiftService;
        setListService(mRegisterShiftService);
    }

    /**
     * Trả lại register shift service
     *
     * @return
     */
    public RegisterShiftService getRegisterShiftService() {
        return mRegisterShiftService;
    }

    @Override
    public void onRetrievePostExecute(List<RegisterShift> list) {
        super.onRetrievePostExecute(list);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_CODE_MAKE_ADJUSTMENT) {
            mRegisterShiftService.insertMakeAdjustment(((RegisterShift) models[0]));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_CODE_MAKE_ADJUSTMENT) {
            RegisterShift registerShift = ((RegisterShift) models[0]);
        }
    }

    public void doInputMakeAdjustment(RegisterShift registerShift)  {
        doAction(ACTION_CODE_MAKE_ADJUSTMENT, null, null, registerShift);
    }

    public CashTransaction createCashTransaction(){
        return mRegisterShiftService.createCashTransaction();
    }
}
