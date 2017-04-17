package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.SettingListController;
import com.magestore.app.pos.util.EditTextUtil;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.util.StringUtil;

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
    Button btn_save;
    RelativeLayout setting_background_loading;
    boolean checkFirst;

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
        btn_save = (Button) findViewById(R.id.btn_save);
        listLayout.add(ll_setting_account);
        listLayout.add(ll_setting_currency);
        listLayout.add(ll_setting_store);

        setting_background_loading = (RelativeLayout) findViewById(R.id.setting_background_loading);

        initValue();
    }

    @Override
    public void initValue() {
        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkRequied()) {
                    return;
                }
                Staff staff = ((SettingListController) getController()).createStaff();
                staff.setStaffName(edt_name.getText().toString().trim());
                staff.setCurrentPassword(edt_current_password.getText().toString().trim());
                staff.setNewPassword(edt_new_password.getText().toString().trim());
                staff.setConfirmPassword(edt_confirm_password.getText().toString().trim());
                ((SettingListController) getController()).doInputChangeInformation(staff);
            }
        });

        sp_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkFirst) {
                    String code = sp_currency.getSelection();
                    ((SettingListController) getController()).doInputChangeCurrency(code);
                } else {
                    checkFirst = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        sp_currency.setSelection(ConfigUtil.getCurrentCurrency().getCode());
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

    public boolean checkRequied() {
        if (!isRequied(edt_name)) {
            return false;
        }

        if (!isRequied(edt_current_password)) {
            return false;
        }

        return true;
    }

    public boolean isRequied(EditText editText) {
        return EditTextUtil.checkRequied(getContext(), editText);
    }

    public void isShowLoading(boolean isShow) {
        setting_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showAlertRespone(String message) {
        if (StringUtil.STRING_EMPTY.equals(message)) {
            message = getContext().getString(R.string.err_request);
        }
        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.ok);
    }
}
