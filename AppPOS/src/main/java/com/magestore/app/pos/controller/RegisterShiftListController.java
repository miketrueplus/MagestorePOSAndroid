package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.service.registershift.RegisterShiftService;

import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftListController extends AbstractListController<RegisterShift> {

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
    protected List<RegisterShift> loadDataBackground(Void... params) throws Exception {
        List<RegisterShift> listRegisterShift = mRegisterShiftService.retrieveRegisterShiftList();
        return listRegisterShift;
    }
}
