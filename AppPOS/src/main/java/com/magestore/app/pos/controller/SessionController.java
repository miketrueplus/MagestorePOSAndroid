package com.magestore.app.pos.controller;

import android.content.Intent;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.registershift.CashBox;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.SalesActivity;
import com.magestore.app.pos.panel.OpenSessionListValuePanel;
import com.magestore.app.pos.panel.OpenSessionDetailPanel;

import java.io.IOException;
import java.text.ParseException;
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
    static final int ACTION_TYPE_OPEN_SESSION = 0;

    OpenSessionListValuePanel mOpenSessionListPanel;
    /**
     * Service xử lý các vấn đề liên quan đến register shift
     */
    RegisterShiftService mRegisterShiftService;
    Map<String, Object> wraper;
    List<OpenSessionValue> listValue;
    UserService userService;

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
        listValue = new ArrayList<>();
    }

    public void doInputOpenSession(SessionParam param) {
        doAction(ACTION_TYPE_OPEN_SESSION, null, wraper, param);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_TYPE_OPEN_SESSION) {
            SessionParam param = (SessionParam) models[0];
            wraper.put("open_session_respone", mRegisterShiftService.openSession(param));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_TYPE_OPEN_SESSION) {
            navigationToSalesActivity();
        }
    }

    @Override
    public void onCancelledBackground(Exception exp, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onCancelledBackground(exp, actionType, actionCode, wraper, models);
    }

    private void navigationToSalesActivity() {
        // Đăng nhập thành công, mở sẵn form sales
        Intent intent = new Intent(getMagestoreContext().getActivity(), SalesActivity.class);
        getMagestoreContext().getActivity().startActivity(intent);
        getMagestoreContext().getActivity().finish();
    }

    public void addValue() {
        OpenSessionValue value = mRegisterShiftService.createOpenSessionValue();
        listValue.add(value);
        mOpenSessionListPanel.bindList(listValue);
    }

    public void setOpenSessionListPanel(OpenSessionListValuePanel mOpenSessionListPanel) {
        this.mOpenSessionListPanel = mOpenSessionListPanel;
    }

    public void updateFloatAmount(float total) {
        ((OpenSessionDetailPanel) mDetailView).updateFloatAmount(total);
    }

    public void updateCashBoxOpen(HashMap<OpenSessionValue, CashBox> mCashBox) {
        ((OpenSessionDetailPanel) mDetailView).updateCashBoxOpen(mCashBox);
    }

    public void removeValue(OpenSessionValue value) {
        listValue.remove(value);
        mOpenSessionListPanel.bindList(listValue);
    }

    public SessionParam createSessionParam() {
        return mRegisterShiftService.createSessionParam();
    }

    public List<PointOfSales> getListPos(){
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
