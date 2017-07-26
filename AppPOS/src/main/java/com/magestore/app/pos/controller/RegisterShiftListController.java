package com.magestore.app.pos.controller;

import android.content.Intent;

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
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.CloseSessionPanel;
import com.magestore.app.pos.panel.OpenSessionListValuePanel;
import com.magestore.app.pos.panel.RegisterOpenSessionPanel;
import com.magestore.app.pos.panel.RegisterShiftDetailPanel;
import com.magestore.app.pos.panel.RegisterShiftListPanel;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;

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
    public static String SEND_NOTI_TO_REGISTER_ACTIVITY = "com.magestore.app.pos.controller.register.controller";
    UserService userService;
    boolean first_add;
    boolean first_check;
    RegisterOpenSessionPanel openSessionPanel;
    CloseSessionPanel panelCloseSessionPanel;

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
        if (!first_add) {
            if (list != null) {
                if (list.size() == 0) {
                    RegisterShift shift_last_title = mRegisterShiftService.create();
                    shift_last_title.setLastSevenDay(true);
                    list.add(shift_last_title);
                } else {
                    if (ConfigUtil.lessThanSevenDay(list.get(0).getOpenedAt())) {
                        for (RegisterShift shift : list) {
                            if (!shift.getLastSevenDay()) {
                                RegisterShift shift_last_title = mRegisterShiftService.create();
                                shift_last_title.setLastSevenDay(true);
                                list.add(0, shift_last_title);
                                break;
                            }
                        }
                    }
                }
                for (RegisterShift shift : list) {
                    if (!shift.getLessSevenDay()) {
                        if (!shift.getLastSevenDay()) {
                            if (!ConfigUtil.lessThanSevenDay(shift.getOpenedAt())) {
                                RegisterShift shift_title = mRegisterShiftService.create();
                                shift_title.setLessSevenDay(true);
                                if (list.size() == 1) {
                                    list.add(shift_title);
                                } else {
                                    list.add(list.indexOf(shift), shift_title);
                                }
                                break;
                            }
                        }
                    }
                }
            }
            first_add = true;
        }
        super.onRetrievePostExecute(list);
        if (!first_check) {
            if (list != null && list.size() > 1) {
                RegisterShift registerShift = list.get(1);
//                if (registerShift.getLessSevenDay()) {
//                    registerShift = list.get(2);
//                }
                bindItem(registerShift);
                ((RegisterShiftListPanel) mView).setSelectPosition();
                if (registerShift.getStatus().equals("0")) {
                    if (!ConfigUtil.isCheckFirstOpenSession()) {
                        ((RegisterShiftListPanel) mView).showDialogContinueCheckout();
                        ConfigUtil.setCheckFirstOpenSession(true);
                    }
                    ConfigUtil.setShiftId(registerShift.getShiftId());
                    ConfigUtil.setLocationId(registerShift.getLocationId());
//                getMagestoreContext().getActivity().finish();
                    DataUtil.saveDataStringToPreferences(getMagestoreContext().getActivity(), DataUtil.STORE_ID, registerShift.getStoreId());
                    ((RegisterShiftListPanel) mView).isShowButtonOpenSession(false);
                }
                if (registerShift.getStatus().equals("1")) {
                    openSessionList();
                    ((RegisterShiftListPanel) mView).isShowButtonOpenSession(true);
                    Intent intent = new Intent();
                    intent.putExtra("is_show", false);
                    intent.setAction(SEND_NOTI_TO_REGISTER_ACTIVITY);
                    getMagestoreContext().getActivity().sendBroadcast(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("is_show", true);
                    intent.setAction(SEND_NOTI_TO_REGISTER_ACTIVITY);
                    getMagestoreContext().getActivity().sendBroadcast(intent);
                }

                if (registerShift.getStatus().equals("2")) {
                    ((RegisterShiftDetailPanel) mDetailView).showCloseShift(list.get(1));
                }
            } else {
                openSessionList();
            }
            first_check = true;
        }
    }

    @Override
    public void bindItem(RegisterShift item) {
        if (!item.getLessSevenDay() && !item.getLastSevenDay()) {
            super.bindItem(item);
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
            setSelectedItem(listRegister.get(0));
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_CLOSE_SESSION) {
            if (panelCloseSessionPanel != null)
                panelCloseSessionPanel.setEnableBtClose(true);
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("close_session_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            bindItemCloseSessionPanel(listRegister.get(0));
            setSelectedItem(listRegister.get(0));
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_VALIDATE_SESSION) {
            if (panelCloseSessionPanel != null)
                panelCloseSessionPanel.setEnableBtValidate(true);
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("close_validate_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            setSelectedItem(listRegister.get(0));
            dismissDialogCloseSession();
            ((RegisterShiftListPanel) mView).isShowButtonOpenSession(true);
            Intent intent = new Intent();
            intent.putExtra("is_show", false);
            intent.setAction(SEND_NOTI_TO_REGISTER_ACTIVITY);
            getMagestoreContext().getActivity().sendBroadcast(intent);
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_OPEN_SESSION) {
            if (openSessionPanel != null)
                openSessionPanel.setEnableBtOpen(true);
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("open_session_respone");
            ConfigUtil.setShiftId(listRegister.get(0).getShiftId());
            ConfigUtil.setLocationId(listRegister.get(0).getLocationId());
            mList.add(1, listRegister.get(0));
            for (RegisterShift shift : mList) {
                if (shift.getLessSevenDay()) {
                    mList.remove(shift);
                    break;
                }
            }
            for (RegisterShift shift : mList) {
                if (!shift.getLessSevenDay()) {
                    if (!shift.getLastSevenDay()) {
                        if (!ConfigUtil.lessThanSevenDay(shift.getOpenedAt())) {
                            RegisterShift shift_title = mRegisterShiftService.create();
                            shift_title.setLessSevenDay(true);
                            if (mList.size() == 1) {
                                mList.add(shift_title);
                            } else {
                                mList.add(mList.indexOf(shift), shift_title);
                            }
                            break;
                        }
                    }
                }
            }
            bindList(mList);
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setSelectedItem(listRegister.get(0));
            ((RegisterShiftListPanel) mView).notifyDataSetChanged();
            Intent intent = new Intent();
            intent.putExtra("is_show", true);
            intent.setAction(SEND_NOTI_TO_REGISTER_ACTIVITY);
            getMagestoreContext().getActivity().sendBroadcast(intent);
            dismissDialogOpenSession();
            ((RegisterShiftListPanel) mView).showDialogContinueCheckout();
            ConfigUtil.setCheckFirstOpenSession(true);
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_CANCEL_SESSION) {
            if (panelCloseSessionPanel != null)
                panelCloseSessionPanel.setEnableCancel(true);
            RegisterShift oldRegisterShift = (RegisterShift) models[0];
            List<RegisterShift> listRegister = (List<RegisterShift>) wraper.get("cancel_session_respone");
            ((RegisterShiftDetailPanel) mDetailView).bindItem(listRegister.get(0));
            setNewRegisterToList(oldRegisterShift, listRegister.get(0));
            setSelectedItem(listRegister.get(0));
            bindItemCloseSessionPanel(listRegister.get(0));
            isShowLoadingDetail(false);
            int type = (int) wraper.get("cancel_session_type");
            if (type == 1) {
                dismissDialogCloseSession();
            }
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
        if (openSessionPanel != null)
            openSessionPanel.setEnableBtOpen(true);
        if (panelCloseSessionPanel != null) {
            panelCloseSessionPanel.setEnableBtClose(true);
            panelCloseSessionPanel.setEnableBtValidate(true);
            panelCloseSessionPanel.setEnableCancel(true);
        }
    }

    public void doInputCloseSession(RegisterShift registerShift, SessionParam param) {
        isShowLoadingDetail(true);
        if (panelCloseSessionPanel != null)
            panelCloseSessionPanel.setEnableBtClose(false);
        wraper.put("param_close_session", param);
        doAction(ACTION_TYPE_CLOSE_SESSION, null, wraper, registerShift);
    }

    public void doInputValidateSession(RegisterShift registerShift, SessionParam param) {
        isShowLoadingDetail(true);
        if (panelCloseSessionPanel != null)
            panelCloseSessionPanel.setEnableBtValidate(false);
        wraper.put("param_validate_session", param);
        doAction(ACTION_TYPE_VALIDATE_SESSION, null, wraper, registerShift);
    }

    public void doInputCancelSession(RegisterShift registerShift, SessionParam param, int type) {
        isShowLoadingDetail(true);
        if (panelCloseSessionPanel != null)
            panelCloseSessionPanel.setEnableCancel(false);
        wraper.put("param_cancel_session", param);
        wraper.put("cancel_session_type", type);
        doAction(ACTION_TYPE_CANCEL_SESSION, null, wraper, registerShift);
    }

    public void doInputOpenSession(SessionParam param) {
        isShowLoadingDetail(true);
        if (openSessionPanel != null)
            openSessionPanel.setEnableBtOpen(false);
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

    public void clearListValueClose() {
        listValueClose.clear();
    }

    public void clearListValueOpen() {
        listValueOpen.clear();
    }

    public void bindListValueClose() {
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

    public void showDialogMakeAdjusment(boolean isType) {
        ((RegisterShiftDetailPanel) mDetailView).showDialogMakeAdjusment(getSelectedItem(), isType);
    }

    public void setOpenSessionPanel(RegisterOpenSessionPanel openSessionPanel) {
        this.openSessionPanel = openSessionPanel;
    }

    public void setPanelCloseSessionPanel(CloseSessionPanel panelCloseSessionPanel) {
        this.panelCloseSessionPanel = panelCloseSessionPanel;
    }

    public List<PointOfSales> getListPos() {
        try {
            List<PointOfSales> listPos = userService.getListPos();
            if (listPos != null) {
                if (listPos.size() > 0) {
                    PointOfSales pointOfSales = userService.createPointOfSales();
                    pointOfSales.setPosId("");
                    pointOfSales.setPosName(getMagestoreContext().getActivity().getString(R.string.select_pos));
                    if (!checkPos(listPos)) {
                        listPos.add(0, pointOfSales);
                    }
                    return listPos;
                } else {
                    return userService.getListPos();
                }
            } else {
                return userService.getListPos();
            }
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

    private boolean checkPos(List<PointOfSales> listPos) {
        boolean isContains = false;
        for (PointOfSales pointOfSales : listPos) {
            if (pointOfSales.getID().equals("")) {
                isContains = true;
            }
        }
        return isContains;
    }
}
