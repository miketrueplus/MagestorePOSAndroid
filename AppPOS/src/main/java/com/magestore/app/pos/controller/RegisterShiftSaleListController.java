package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.service.registershift.RegisterShiftService;

import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftSaleListController extends AbstractListController<SaleSummary> {
    RegisterShift mSelectedRegisterShift;
    RegisterShiftService mRegisterShiftService;
    RegisterShiftListController mRegisterShiftListController;

    /**
     * Thiết lập controller
     *
     * @param mRegisterShiftListController
     */
    public void setRegisterShiftListController(RegisterShiftListController mRegisterShiftListController) {
        this.mRegisterShiftListController = mRegisterShiftListController;
//        setParentController(mRegisterShiftListController);
    }

    /**
     * Thiết lập service
     *
     * @param mRegisterShiftService
     */
    public void setRegisterShiftService(RegisterShiftService mRegisterShiftService) {
        this.mRegisterShiftService = mRegisterShiftService;
    }

    @Override
    protected List<SaleSummary> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho comment
     *
     * @param registerShift
     */
    public void doSelectRegisterShift(RegisterShift registerShift) {
        mSelectedRegisterShift = registerShift;
        mList = registerShift.getSalesSummary();
        mView.bindList(mList);
    }
}
