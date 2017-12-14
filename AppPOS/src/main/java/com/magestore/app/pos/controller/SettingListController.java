package com.magestore.app.pos.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesActivity;
import com.magestore.app.pos.panel.SettingDetailPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingListController extends AbstractListController<Setting> {
    ConfigService mConfigService;
    UserService mUserService;
    List<Currency> currencyList;
    Staff mStaff;
    Map<String, Object> wraper;
    static int TYPE_ACTION_CHANGE_INFFORMATION = 0;
    static int TYPE_ACTION_CHANGE_CURRENCY = 1;
    public static String RESET_DATA_TO_SALE_ACTIVITY = "com.magestore.app.pos.controller.settting.resetdata";

    public void setConfigService(ConfigService mConfigService) {
        this.mConfigService = mConfigService;
        wraper = new HashMap<>();
    }

    public void setUserService(UserService mUserService) {
        this.mUserService = mUserService;
    }

    @Override
    public List<Setting> onRetrieveBackground(int page, int pageSize) throws Exception {
        currencyList = mConfigService.getCurrencies();
        mStaff = mConfigService.getStaff();
        Map<String, String> listTitleConfig = new LinkedTreeMap<>();
        listTitleConfig.put("0", getMagestoreContext().getActivity().getString(R.string.setting_account));
        listTitleConfig.put("1", getMagestoreContext().getActivity().getString(R.string.checkout));
        listTitleConfig.put("2", getMagestoreContext().getActivity().getString(R.string.setting_print));
        listTitleConfig.put("3", getMagestoreContext().getActivity().getString(R.string.setting_currency));
        listTitleConfig.put("4", getMagestoreContext().getActivity().getString(R.string.setting_store));
        return mConfigService.getListSetting(listTitleConfig);
    }

    @Override
    public synchronized void onRetrievePostExecute(List<Setting> list) {
        if (StringUtil.isNullOrEmpty(ConfigUtil.getGoogleKey()) && !ConfigUtil.isShowAvailableQty()) {
            if (list.size() > 0) {
                for (Setting setting : list) {
                    if (setting.getType() == 1) {
                        list.remove(setting);
                        break;
                    }
                }
            }
        }
        super.onRetrievePostExecute(list);
        ((SettingDetailPanel) mDetailView).setStaffDataSet(mStaff);
        ((SettingDetailPanel) mDetailView).setCurrencyDataSet(currencyList);
        ((SettingDetailPanel) mDetailView).setPrintDataSet();
    }

    public void doInputChangeInformation(Staff staff) {
        ((SettingDetailPanel) mDetailView).isShowLoading(true);
        doAction(TYPE_ACTION_CHANGE_INFFORMATION, null, wraper, staff);
    }

    public void doInputChangeCurrency(String code) {
        ((SettingDetailPanel) mDetailView).isShowLoading(true);
        wraper.put("currency_code", code);
        doAction(TYPE_ACTION_CHANGE_CURRENCY, null, wraper, null);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == TYPE_ACTION_CHANGE_INFFORMATION) {
            Staff staff = (Staff) models[0];
            wraper.put("staff", mConfigService.changeInformationStaff(staff));
            return true;
        } else if (actionType == TYPE_ACTION_CHANGE_CURRENCY) {
            String code = (String) wraper.get("currency_code");
            mConfigService.changeCurrency(code);
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == TYPE_ACTION_CHANGE_INFFORMATION) {
            Staff staff = (Staff) wraper.get("staff");
            if (staff.getResponeType()) {
                try {
                    mConfigService.setStaff(staff);
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
            ((SettingDetailPanel) mDetailView).showAlertRespone(staff.getErrorMessage());
        } else {
            Intent i = new Intent();
            i.setAction(RESET_DATA_TO_SALE_ACTIVITY);
            getMagestoreContext().getActivity().sendBroadcast(i);
            getMagestoreContext().getActivity().finish();

            Intent intent = new Intent();
            intent.setAction(AbstractActivity.BACK_TO_HOME);
            getMagestoreContext().getActivity().sendBroadcast(intent);

//            Intent intent = new Intent(getMagestoreContext().getActivity(), SalesActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            getMagestoreContext().getActivity().startActivity(intent);
        }
        ((SettingDetailPanel) mDetailView).isShowLoading(false);
    }

    @Override
    public void onCancelledBackground(Exception exp, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onCancelledBackground(exp, actionType, actionCode, wraper, models);
        ((SettingDetailPanel) mDetailView).isShowLoading(false);
    }

    @Override
    public void bindItem(Setting item) {
        super.bindItem(item);
        ((SettingDetailPanel) mDetailView).bindItem(item);
    }

    public Staff createStaff() {
        return mConfigService.createStaff();
    }

    public void backToLoginActivity() {
        new AlertDialog.Builder(getMagestoreContext().getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_change_store)
                .setMessage(R.string.ask_are_you_sure_to_close)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataUtil.saveDataBooleanToPreferences(getMagestoreContext().getActivity(), DataUtil.CHOOSE_STORE, false);
                        ConfigUtil.setCheckFirstOpenSession(false);
                        Intent intent = new Intent(getMagestoreContext().getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getMagestoreContext().getActivity().startActivity(intent);
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
