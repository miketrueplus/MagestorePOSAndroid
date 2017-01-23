package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.model.registershift.PosCashTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftCashListController extends AbstractListController<CashTransaction> {
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
        setParentController(mRegisterShiftListController);
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
    protected List<CashTransaction> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho comment
     *
     * @param registerShift
     */
    public void doSelectRegisterShift(RegisterShift registerShift) {
        mSelectedRegisterShift = registerShift;
        mList = addOpenShift(registerShift, registerShift.getCashTransaction());
        mView.bindList(mList);
    }

    private List<CashTransaction> addOpenShift(RegisterShift registerShift, List<CashTransaction> listCash) {
        String openShift = mMagestoreContext.getActivity().getString(R.string.register_shift_content_open_shift);
        String balance = mMagestoreContext.getActivity().getString(R.string.register_shift_content_item_float);
        List<CashTransaction> nListCash = mRegisterShiftService.createListCashTransaction(registerShift, openShift, balance);
        nListCash.addAll(listCash);
        return nListCash;
    }
}
