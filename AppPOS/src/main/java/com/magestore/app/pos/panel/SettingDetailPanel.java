package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.SettingListController;
import com.magestore.app.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingDetailPanel extends AbstractDetailPanel<Setting> {
    LinearLayout ll_setting_account, ll_setting_currency, ll_setting_store;
    static int TYPE_ACCOUNT = 0;
    static int TYPE_CURRENCY = 1;
    static int TYPE_STORE = 2;
    List<LinearLayout> listLayout;
    SimpleSpinner sp_currency;
    EditText edt_name, edt_current_password, edt_new_password, edt_confirm_password;

    public SettingDetailPanel(Context context) {
        super(context);
    }

    public SettingDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        listLayout = new ArrayList<>();
        ll_setting_account = (LinearLayout) findViewById(R.id.ll_setting_account);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_current_password = (EditText) findViewById(R.id.edt_current_password);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        ll_setting_currency = (LinearLayout) findViewById(R.id.ll_setting_currency);
        sp_currency = (SimpleSpinner) findViewById(R.id.sp_currency);
        ll_setting_store = (LinearLayout) findViewById(R.id.ll_setting_store);
        listLayout.add(ll_setting_account);
        listLayout.add(ll_setting_currency);
        listLayout.add(ll_setting_store);
    }

    @Override
    public void bindItem(Setting item) {
        super.bindItem(item);
        if (item.getType() == TYPE_ACCOUNT) {
            selectLinearLayout(ll_setting_account, listLayout);
        } else if (item.getType() == TYPE_CURRENCY) {
            selectLinearLayout(ll_setting_currency, listLayout);
        } else if (item.getType() == TYPE_STORE) {
            ((SettingListController) getController()).backToLoginActivity();
        }
    }

    public void setCurrencyDataSet(List<Currency> currencyList) {
        sp_currency.bind(currencyList.toArray(new Currency[0]));
    }

    public void setStaffDataSet(Staff staff) {
        if (staff != null) {
            edt_name.setText(staff.getStaffName());
        }
    }

    private void selectLinearLayout(LinearLayout linearLayout, List<LinearLayout> listLayout) {
        for (LinearLayout layout : listLayout) {
            if (layout == linearLayout) {
                layout.setVisibility(VISIBLE);
            } else {
                layout.setVisibility(GONE);
            }
        }
    }
}
