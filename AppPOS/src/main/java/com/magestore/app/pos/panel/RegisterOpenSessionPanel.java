package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.CashBox;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.controller.SessionController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.view.EditTextFloat;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterOpenSessionPanel extends AbstractDetailPanel<RegisterShift> {
    static String OPEN_SESSION = "0";
    Button bt_open, bt_open_balance;
    RelativeLayout rl_add_value, rl_set_balance, rl_loading;
    LinearLayout ll_open_balance, ll_note, ll_float_amount;
    TextView txt_staff_login, tv_session_back, error_pos, tv_session_title, total_balance_value;
    View line;
    EditTextFloat et_float_amount;
    EditText et_note;
    SimpleSpinner sp_pos;
    RegisterShiftListController mRegisterShiftListController;
    OpenSessionListValuePanel openSessionListValuePanel;
    float total_value = 0;
    HashMap<OpenSessionValue, CashBox> mCashBox;

    public void setRegisterShiftListController(RegisterShiftListController mRegisterShiftListController) {
        this.mRegisterShiftListController = mRegisterShiftListController;
    }

    public RegisterOpenSessionPanel(Context context) {
        super(context);
    }

    public RegisterOpenSessionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterOpenSessionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_open_session_in_register, null);
        addView(view);
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        tv_session_back = (TextView) view.findViewById(R.id.tv_session_back);
        tv_session_title = (TextView) view.findViewById(R.id.tv_session_title);
        line = (View) view.findViewById(R.id.line);
        rl_set_balance = (RelativeLayout) view.findViewById(R.id.rl_set_balance);
        ll_open_balance = (LinearLayout) view.findViewById(R.id.ll_open_balance);
        sp_pos = (SimpleSpinner) view.findViewById(R.id.sp_pos);
        error_pos = (TextView) view.findViewById(R.id.error_pos);
        ll_float_amount = (LinearLayout) view.findViewById(R.id.ll_float_amount);
        et_float_amount = (EditTextFloat) view.findViewById(R.id.et_float_amount);
        et_float_amount.setPriceFormat(true);
        bt_open = (Button) view.findViewById(R.id.bt_open);
        bt_open_balance = (Button) view.findViewById(R.id.bt_open_balance);
        rl_add_value = (RelativeLayout) view.findViewById(R.id.rl_add_value);
        et_note = (EditText) view.findViewById(R.id.et_note);
        ll_note = (LinearLayout) view.findViewById(R.id.ll_note);
        txt_staff_login = (TextView) view.findViewById(R.id.txt_staff_login);
        total_balance_value = (TextView) view.findViewById(R.id.total_balance_value);
        openSessionListValuePanel = (OpenSessionListValuePanel) view.findViewById(R.id.open_session_list_panel);

        initValue();
    }

    @Override
    public void initValue() {
        ll_note.setVisibility(ConfigUtil.isShiftOpenNote() ? VISIBLE : GONE);
        et_float_amount.setEnabled(ConfigUtil.isEnableOpenFloatAmount() ? true : false);
        et_float_amount.setText(ConfigUtil.formatPrice(0));

        rl_add_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterShiftListController.addValueOpen();
            }
        });

        txt_staff_login.setText(getContext().getString(R.string.open_session_login, ConfigUtil.getStaff().getStaffName()));

        bt_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_open.getText().toString().equals(getContext().getString(R.string.confirm))) {
                    et_float_amount.setText(ConfigUtil.formatPrice(total_value));
                    rl_set_balance.setVisibility(GONE);
                    ll_open_balance.setVisibility(VISIBLE);
                    tv_session_title.setText(getContext().getString(R.string.open_session_title));
                    tv_session_back.setVisibility(VISIBLE);
                    line.setVisibility(VISIBLE);
                    ll_note.setVisibility(VISIBLE);
                    total_balance_value.setVisibility(GONE);
                    bt_open_balance.setText(getContext().getString(R.string.open_session_open_balance));
                    bt_open_balance.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
                    bt_open.setText(getContext().getString(R.string.open_session));
                } else {
                    List<PointOfSales> listPos = mRegisterShiftListController.getListPos();
                    if (listPos != null && listPos.size() > 0) {
                        if (!sp_pos.getSelection().equals("")) {
                            SessionParam param = mRegisterShiftListController.createSessionParam();
                            float float_amount = et_float_amount.getValueFloat();
                            float base_float_amount = ConfigUtil.convertToBasePrice(float_amount);
                            param.setBalance(float_amount);
                            param.setBaseBalance(base_float_amount);
                            param.setBaseCashAdded(base_float_amount);
                            param.setBaseFloatAmount(base_float_amount);
                            param.setFloatAmount(float_amount);
                            param.setBaseCurrencyCode(ConfigUtil.getBaseCurrencyCode());
                            param.setCashAdded(float_amount);
                            param.setOpenedNote(et_note.getText().toString());
                            param.setShiftCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());
                            param.setStaffId(ConfigUtil.getStaff().getID());
                            param.setLocationId(ConfigUtil.getPointOfSales().getLocationId());
                            param.setOpenedAt(ConfigUtil.getCurrentDateTime());
                            param.setCloseAt(ConfigUtil.getCurrentDateTime());
                            // tự sinh id shift khi open
                            param.setShiftId("off_id_auto_shift_" + ConfigUtil.getItemIdInCurrentTime());
                            param.setPosId(sp_pos.getSelection());
                            param.setCashBox(mCashBox);
                            param.setStatus(OPEN_SESSION);
                            mRegisterShiftListController.doInputOpenSession(param);
                        } else {
                            DialogUtil.confirm(getContext(), getContext().getString(R.string.notify_select_pos), R.string.ok);
                        }
                    } else {
                        DialogUtil.confirm(getContext(), getContext().getString(R.string.notify_select_pos), R.string.ok);
                    }
                }
            }
        });

        bt_open_balance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_open_balance.getText().toString().equals(getContext().getString(R.string.cancel))) {
                    rl_set_balance.setVisibility(GONE);
                    ll_open_balance.setVisibility(VISIBLE);
                    tv_session_title.setText(getContext().getString(R.string.open_session_title));
                    tv_session_back.setVisibility(VISIBLE);
                    line.setVisibility(VISIBLE);
                    ll_note.setVisibility(VISIBLE);
                    total_balance_value.setVisibility(GONE);
                    bt_open_balance.setText(getContext().getString(R.string.open_session_open_balance));
                    bt_open_balance.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
                    bt_open.setText(getContext().getString(R.string.open_session));
                } else {
                    rl_set_balance.setVisibility(VISIBLE);
                    ll_open_balance.setVisibility(GONE);
                    tv_session_title.setText(getContext().getString(R.string.open_session_set_balance_value));
                    tv_session_back.setVisibility(GONE);
                    line.setVisibility(GONE);
                    ll_note.setVisibility(GONE);
                    total_balance_value.setVisibility(VISIBLE);
                    bt_open_balance.setText(getContext().getString(R.string.cancel));
                    bt_open_balance.setTextColor(ContextCompat.getColor(getContext(), R.color.open_session_text_color));
                    bt_open.setText(getContext().getString(R.string.confirm));
                }
            }
        });

        tv_session_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterShiftListController.dismissDialogOpenSession();
            }
        });

        et_float_amount.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mRegisterShiftListController.getMagestoreContext().getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });
    }

    @Override
    public void initModel() {
        openSessionListValuePanel.setType(OpenSessionListValuePanel.TYPE_OPEN_SESSION_IN_REGISTER);
        openSessionListValuePanel.setRegisterShiftListController(mRegisterShiftListController);
        mRegisterShiftListController.setOpenSessionListPanel(openSessionListValuePanel);
        mRegisterShiftListController.doInputGetListPos();
    }

    public void setDataPos(List<PointOfSales> listPos) {
        if (listPos != null && listPos.size() > 0) {
            error_pos.setVisibility(GONE);
            sp_pos.setVisibility(VISIBLE);
            sp_pos.bind(listPos.toArray(new PointOfSales[0]));

            sp_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PointOfSales pos = getPointOfSales(sp_pos.getSelection());
                    LoginActivity.STORE_ID = pos.getStoreId();
                    DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_ID, pos.getStoreId());
                    ConfigUtil.setPointOfSales(pos);
                    bt_open_balance.setVisibility(pos.getCashControl() ? GONE : VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            error_pos.setVisibility(VISIBLE);
            sp_pos.setVisibility(GONE);
        }
    }

    private PointOfSales getPointOfSales(String posID) {
        List<PointOfSales> listPos = mRegisterShiftListController.getListPos();

        if (listPos != null && listPos.size() > 0) {
            for (PointOfSales pos : listPos) {
                if (pos.getID().equals(posID)) {
                    return pos;
                }
            }
        }
        return null;
    }

    public void updateFloatAmount(float total) {
        total_value = total;
        total_balance_value.setText(ConfigUtil.formatPrice(total));
    }

    public void updateCashBoxOpen(HashMap<OpenSessionValue, CashBox> mCashBox) {
        this.mCashBox = mCashBox;
    }

    public void setEnableBtOpen(boolean isEnable) {
        bt_open.setEnabled(isEnable ? true : false);
    }

    public void showLoading(boolean isShow) {
        rl_loading.setVisibility(isShow ? VISIBLE : GONE);
    }
}
