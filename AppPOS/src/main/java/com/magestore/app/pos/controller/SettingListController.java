package com.magestore.app.pos.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.SettingDetailPanel;
import com.magestore.app.util.DataUtil;

import java.util.List;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingListController extends AbstractListController<Setting> {
    ConfigService mConfigService;
    UserService mUserService;
    List<Currency> currencyList;

    public void setConfigService(ConfigService mConfigService) {
        this.mConfigService = mConfigService;
    }

    public void setUserService(UserService mUserService) {
        this.mUserService = mUserService;
    }

    @Override
    public List<Setting> onRetrieveBackground(int page, int pageSize) throws Exception {
        currencyList = mConfigService.getCurrencies();
        return mConfigService.getListSetting();
    }

    @Override
    public synchronized void onRetrievePostExecute(List<Setting> list) {
        super.onRetrievePostExecute(list);
        ((SettingDetailPanel) mDetailView).setCurrencyDataSet(currencyList);
    }

    @Override
    public void bindItem(Setting item) {
        super.bindItem(item);
        ((SettingDetailPanel) mDetailView).bindItem(item);
    }

    public void backToLoginActivity(){
        new AlertDialog.Builder(getMagestoreContext().getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_change_store)
                .setMessage(R.string.ask_are_you_sure_to_close)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataUtil.saveDataBooleanToPreferences(getMagestoreContext().getActivity(), DataUtil.CHOOSE_STORE, false);
                        Intent intent = new Intent(getMagestoreContext().getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getMagestoreContext().getActivity().startActivity(intent);
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
