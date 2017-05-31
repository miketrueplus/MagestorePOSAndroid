package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.panel.OpenSessionListPanel;
import com.magestore.app.pos.panel.OpenSessionPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 5/30/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SessionController extends AbstractListController<RegisterShift> {
    OpenSessionListPanel mOpenSessionListPanel;
    /**
     * Service xử lý các vấn đề liên quan đến register shift
     */
    RegisterShiftService mRegisterShiftService;
    Map<String, Object> wraper;
    List<OpenSessionValue> listValue;

    /**
     * Thiết lập service
     *
     * @param mRegisterShiftService
     */
    public void setRegisterShiftService(RegisterShiftService mRegisterShiftService) {
        this.mRegisterShiftService = mRegisterShiftService;
        setListService(mRegisterShiftService);
        wraper = new HashMap<>();
        listValue = new ArrayList<>();
    }

    public void addValue() {
        OpenSessionValue value = mRegisterShiftService.createOpenSessionValue();
        listValue.add(value);
        mOpenSessionListPanel.bindList(listValue);
    }

    public void setOpenSessionListPanel(OpenSessionListPanel mOpenSessionListPanel) {
        this.mOpenSessionListPanel = mOpenSessionListPanel;
    }

    public void updateFloatAmount(float total){
        ((OpenSessionPanel) mDetailView).updateFloatAmount(total);
    }

    public void removeValue(OpenSessionValue value){
        listValue.remove(value);
        mOpenSessionListPanel.bindList(listValue);
    }
}
