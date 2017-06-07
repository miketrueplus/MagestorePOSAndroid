package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.panel.OpenSessionListValuePanel;
import com.magestore.app.pos.panel.RegisterShiftDetailPanel;
import com.magestore.app.pos.panel.RegisterShiftListPanel;
import com.magestore.app.util.ConfigUtil;

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
    static final int ACTION_TYPE_VALIDATE_SESSION = 2;
    static final int ACTION_TYPE_OPEN_SESSION = 3;
    static final int ACTION_TYPE_CANCEL_SESSION = 4;
    OpenSessionListValuePanel mOpenSessionListPanel;
    List<OpenSessionValue> listValueClose;
    List<OpenSessionValue> listValueOpen;
    Map<String, Object> wraper;

    UserService userService;

    /**
     * Service xử lý các vấn đề liên quan đến register shift
     */
    RegisterShiftService mRegisterShiftService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Thiết lập service
     *
     * @param mRegisterShiftService
     */
    public void setRegisterShiftService(RegisterShiftService mRegisterShiftService) {
        this.mRegisterShiftService = mRegisterShiftService;
        setListService(mRegisterShiftService);
        wraper = new HashMap<>();
        listValueClose = new ArrayList<>();
        listValueOpen = new ArrayList<>();
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
        if (list.size() > 0) {
            RegisterShift registerShift = list.get(0);
            if (registerShift.getStatus().equals("0")) {
                ConfigUtil.setShiftId(registerShift.getShiftId());
//                getMagestoreContext().getActivity().finish();
                ((RegisterShiftListPanel) mView).isShowButtonOpenSession(false);
            }
            if (registerShift.getStatus().equals("1")) {
                openSessionList();
                ((RegisterShiftListPanel) mView).isShowButtonOpenSession(true);
            }
            if (registerShift.getStatus().equals("2")) {
                ((RegisterShiftDetailPanel) mDetailView).showCloseShift(list.get(0));
            }
        } else {
            openSessionList();
        }
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_CODE_MAKE_ADJUSTMENT) {
            wraper.put("make_adjusment_respone", mRegisterShiftService.insertMakeAdjustment(((RegisterShift) models[0])));
            return true;
        } else if (actionType == ACTION_TYPE_CLOSE_SESSION) {
            SessionParam param = (SessionParam) wraper.get("param_close_session");
            wraper.put("close_session_respone", mRegisterShiftService.closeSession(param));
            return true;
        } else if (actionType == ACTION_TYPE_VALIDATE_SESSION) {
            SessionParam param = (SessionParam) wraper.get("param_validate_session");
            wraper.put("close_validate_respone", mRegisterShiftService.closeSession(param));
            return true;
        } else if (actionType == ACTION_TYPE_OPEN_SESSION) {
            SessionParam param = (SessionParam) models[0];
            wraper.put("open_session_respone", mRegisterShiftService.openSession(param));
            return true;
        } else if (actionType == ACTION_TYPE_CANCEL_SESSION) {
            SessionParam param = (SessionParam) wraper.get("param_cancel_session");
            wraper.put("cancel_session_respone", mRegisterShiftService.closeSession(param));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_CODE_MAKE_ADJUSTMENT) {
            RegisterShift oldRegisterShift = ((RegisterShift) models[0]);
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("make_adjusment_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_CLOSE_SESSION) {
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("close_session_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            bindItemCloseSessionPanel(listRegister.get(0));
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_VALIDATE_SESSION) {
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("close_validate_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            dismissDialogCloseSession();
            ((RegisterShiftListPanel) mView).isShowButtonOpenSession(true);
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_OPEN_SESSION) {
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("open_session_respone");
            ConfigUtil.setShiftId(listRegister.get(0).getShiftId());
            mList.add(0, listRegister.get(0));
            bindList(mList);
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            ((RegisterShiftListPanel) mView).notifyDataSetChanged();
            dismissDialogOpenSession();
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_CANCEL_SESSION) {
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("cancel_session_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            bindItemCloseSessionPanel(listRegister.get(0));
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
        isShowLoadingDetail(true);
        wraper.put("param_close_session", param);
        doAction(ACTION_TYPE_CLOSE_SESSION, null, wraper, registerShift);
    }

    public void doInputValidateSession(RegisterShift registerShift, SessionParam param) {
        isShowLoadingDetail(true);
        wraper.put("param_validate_session", param);
        doAction(ACTION_TYPE_VALIDATE_SESSION, null, wraper, registerShift);
    }

    public void doInputCancelSession(RegisterShift registerShift, SessionParam param) {
        isShowLoadingDetail(true);
        wraper.put("param_cancel_session", param);
        doAction(ACTION_TYPE_CANCEL_SESSION, null, wraper, registerShift);
    }

    public void doInputOpenSession(SessionParam param) {
        isShowLoadingDetail(true);
        doAction(ACTION_TYPE_OPEN_SESSION, null, wraper, param);
    }

    public void doInputMakeAdjustment(RegisterShift registerShift) {
        doAction(ACTION_CODE_MAKE_ADJUSTMENT, null, wraper, registerShift);
    }

    public CashTransaction createCashTransaction() {
        return mRegisterShiftService.createCashTransaction();
    }

    public void setOpenSessionListPanel(OpenSessionListValuePanel mOpenSessionListPanel) {
        this.mOpenSessionListPanel = mOpenSessionListPanel;
    }

    public void addValueOpen() {
        OpenSessionValue value = mRegisterShiftService.createOpenSessionValue();
        listValueOpen.add(value);
        mOpenSessionListPanel.bindList(listValueOpen);
    }

    public void removeValueOpen(OpenSessionValue value) {
        listValueOpen.remove(value);
        mOpenSessionListPanel.bindList(listValueOpen);
    }

    public void addValueClose() {
        OpenSessionValue value = mRegisterShiftService.createOpenSessionValue();
        listValueClose.add(value);
        mOpenSessionListPanel.bindList(listValueClose);
    }

    public void removeValueClose(OpenSessionValue value) {
        listValueClose.remove(value);
        mOpenSessionListPanel.bindList(listValueClose);
    }

    public void updateFloatAmountClose(float total) {
        ((RegisterShiftDetailPanel) mDetailView).updateFloatAmount(total);
    }

    public void updateFloatAmountOpen(float total) {
        ((RegisterShiftListPanel) mView).updateFloatAmount(total);
    }

    public void isShowLoadingDetail(boolean isShow) {
        ((RegisterShiftDetailPanel) mDetailView).isShowLoadingDetail(isShow);
    }

    public void openSessionList() {
        ((RegisterShiftListPanel) mView).openSession();
    }

    public SessionParam createSessionParam() {
        return mRegisterShiftService.createSessionParam();
    }

    public void dismissDialogCloseSession() {
        ((RegisterShiftDetailPanel) mDetailView).dismissDialogCloseSession();
    }

    public void dismissDialogOpenSession() {
        ((RegisterShiftListPanel) mView).dismissDialogOpenSession();
    }

    public void bindItemCloseSessionPanel(RegisterShift item) {
        ((RegisterShiftDetailPanel) mDetailView).bindItemCloseSessionPanel(item);
    }

    public List<PointOfSales> getListPos() {
        try {
            return userService.getListPos();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
