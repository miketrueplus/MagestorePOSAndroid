package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.service.registershift.RegisterShiftService;

import java.io.IOException;
import java.text.ParseException;
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

    public void makeAdjustment(RegisterShift registerShift)  {
        if (mRegisterShiftService != null) try {
            mRegisterShiftService.insertMakeAdjustment(registerShift);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public CashTransaction createCashTransaction(){
        return mRegisterShiftService.createCashTransaction();
    }
}
