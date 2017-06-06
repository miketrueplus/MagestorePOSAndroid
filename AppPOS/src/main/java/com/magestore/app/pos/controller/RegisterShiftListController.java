package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.panel.OpenSessionListValuePanel;
import com.magestore.app.pos.panel.RegisterShiftDetailPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftListController extends AbstractListController<RegisterShift> {
    static final int ACTION_CODE_MAKE_ADJUSTMENT = 0;
    static final int ACTION_TYPE_CLOSE_SESSION = 1;
    OpenSessionListValuePanel mOpenSessionListPanel;
    List<OpenSessionValue> listValue;
    Map<String, Object> wraper;

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
        wraper = new HashMap<>();
        listValue = new ArrayList<>();
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
        } else if (actionType == ACTION_TYPE_CLOSE_SESSION) {
            SessionParam param = (SessionParam) wraper.get("param_close_session");
            wraper.put("close_session_respone", mRegisterShiftService.closeSession(param));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_CODE_MAKE_ADJUSTMENT) {
            RegisterShift registerShift = ((RegisterShift) models[0]);
        } else if(success && actionType == ACTION_TYPE_CLOSE_SESSION){
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("close_session_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            isShowLoadingDetail(false);
        }
    }

    /**
     * cập nhật lại order trong list
     *
     * @param oldRegisterShift
     * @param newRegisterShift
     */
    private void setNewRegisterToList(RegisterShift oldRegisterShift, RegisterShift newRegisterShift) {
        int index = mList.indexOf(oldRegisterShift);
        mList.remove(index);
        mList.add(index, newRegisterShift);
        bindList(mList);
    }

    @Override
    public void onCancelledBackground(Exception exp, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onCancelledBackground(exp, actionType, actionCode, wraper, models);
        isShowLoadingDetail(false);
    }

    public void doInputCloseSession(RegisterShift registerShift, SessionParam param) {
        dismissDialogCloseSession();
        isShowLoadingDetail(true);
        wraper.put("param_close_session", param);
        doAction(ACTION_TYPE_CLOSE_SESSION, null, wraper, registerShift);
    }

    public void doInputMakeAdjustment(RegisterShift registerShift) {
        doAction(ACTION_CODE_MAKE_ADJUSTMENT, null, null, registerShift);
    }

    public CashTransaction createCashTransaction() {
        return mRegisterShiftService.createCashTransaction();
    }

    public void setOpenSessionListPanel(OpenSessionListValuePanel mOpenSessionListPanel) {
        this.mOpenSessionListPanel = mOpenSessionListPanel;
    }

    public void addValue() {
        OpenSessionValue value = mRegisterShiftService.createOpenSessionValue();
        listValue.add(value);
        mOpenSessionListPanel.bindList(listValue);
    }

    public void removeValue(OpenSessionValue value) {
        listValue.remove(value);
        mOpenSessionListPanel.bindList(listValue);
    }

    public void updateFloatAmount(float total) {
        ((RegisterShiftDetailPanel) mDetailView).updateFloatAmount(total);
    }

    public void isShowLoadingDetail(boolean isShow){
        ((RegisterShiftDetailPanel) mDetailView).isShowLoadingDetail(isShow);
    }

    public SessionParam createSessionParam() {
        return mRegisterShiftService.createSessionParam();
    }

    public void dismissDialogCloseSession(){
        ((RegisterShiftDetailPanel) mDetailView).dismissDialogCloseSession();
    }
}
