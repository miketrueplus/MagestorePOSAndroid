package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CloseSessionSaleListController;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 6/5/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CloseSessionPanel extends AbstractDetailPanel<RegisterShift> {
    static String OPEN_SESSION = "0";
    static String VALIDATE = "1";
    static String CLOSE_SESSION = "2";
    OpenSessionListValuePanel close_session_list_panel;
    RelativeLayout rl_add_value, rl_close_session_list, rl_set_value, rl_content;
    EditTextFloat et_r_close_balance;
    TextView tv_session_back, tv_open_session_balance, tv_t_close_balance, tv_transaction, tv_difference, tv_session_title;
    TextView txt_staff_login, txt_pos, txt_open_date, make_adjustment_put_money, make_adjustment_take_money, total_balance_value;
    Button bt_close, bt_cancel, bt_adjustment, bt_validate;
    EditText et_note;
    float balance_different, total_value;
    View line;
    RegisterShiftSaleListPanel mRegisterShiftSaleListPanel;
    CloseSessionSaleListController mCloseSessionSaleListController;
    LinearLayout ll_sale_summary;

    public CloseSessionPanel(Context context) {
        super(context);
    }

    public CloseSessionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloseSessionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_close_session, null);
        addView(view);
        tv_session_back = (TextView) findViewById(R.id.tv_session_back);
        tv_session_title = (TextView) findViewById(R.id.tv_session_title);
        line = (View) findViewById(R.id.line);
        txt_staff_login = (TextView) findViewById(R.id.txt_staff_login);
        txt_pos = (TextView) findViewById(R.id.txt_pos);
        txt_open_date = (TextView) findViewById(R.id.txt_open_date);
        make_adjustment_put_money = (TextView) findViewById(R.id.make_adjustment_put_money);
        make_adjustment_take_money = (TextView) findViewById(R.id.make_adjustment_take_money);
        rl_add_value = (RelativeLayout) findViewById(R.id.rl_add_value);
        et_r_close_balance = (EditTextFloat) view.findViewById(R.id.et_r_close_balance);
        et_r_close_balance.setPriceFormat(true);
        tv_t_close_balance = (TextView) view.findViewById(R.id.tv_t_close_balance);
        tv_open_session_balance = (TextView) view.findViewById(R.id.tv_open_session_balance);
        tv_transaction = (TextView) view.findViewById(R.id.tv_transaction);
        tv_difference = (TextView) view.findViewById(R.id.tv_difference);
        rl_set_value = (RelativeLayout) view.findViewById(R.id.rl_set_value);
        rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
        et_note = (EditText) view.findViewById(R.id.et_note);
        bt_close = (Button) view.findViewById(R.id.bt_close);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_adjustment = (Button) view.findViewById(R.id.bt_adjustment);
        bt_validate = (Button) view.findViewById(R.id.bt_validate);
        rl_close_session_list = (RelativeLayout) view.findViewById(R.id.rl_close_session_list);
        total_balance_value = (TextView) view.findViewById(R.id.total_balance_value);
        close_session_list_panel = (OpenSessionListValuePanel) view.findViewById(R.id.close_session_list_panel);
        close_session_list_panel.setType(OpenSessionListValuePanel.TYPE_CLOSE_SESSION);
        ll_sale_summary = (LinearLayout) view.findViewById(R.id.ll_sale_summary);
        mRegisterShiftSaleListPanel = (RegisterShiftSaleListPanel) view.findViewById(R.id.register_shift_sales);
    }

    @Override
    public void initValue() {
        et_r_close_balance.setText(ConfigUtil.formatPrice(0));
        tv_difference.setText(ConfigUtil.formatPrice(0));
        close_session_list_panel.setRegisterShiftListController((RegisterShiftListController) mController);
        ((RegisterShiftListController) mController).setOpenSessionListPanel(close_session_list_panel);

        rl_add_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterShiftListController) getController()).addValueClose();
            }
        });
        tv_session_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterShiftListController) getController()).dismissDialogCloseSession();
            }
        });
    }

    @Override
    public void initModel() {
        mCloseSessionSaleListController = new CloseSessionSaleListController();
        mCloseSessionSaleListController.setRegisterShiftListController(((RegisterShiftListController) mController));
        mCloseSessionSaleListController.setView(mRegisterShiftSaleListPanel);
        mCloseSessionSaleListController.setMagestoreContext(((RegisterShiftListController) mController).getMagestoreContext());
    }

    @Override
    public void bindItem(final RegisterShift item) {
        super.bindItem(item);
        tv_session_title.setText(getContext().getString(R.string.close_session_id_title, item.getShiftId()));
        txt_staff_login.setText(ConfigUtil.getStaff().getStaffName());
        txt_pos.setText(item.getPosName());
        txt_open_date.setText(ConfigUtil.formatDateTime(item.getOpenedAt()));
        ll_sale_summary.setVisibility(item.checkSaleSummary() ? VISIBLE : GONE);
        mCloseSessionSaleListController.doSelectRegisterShift(item);
        if (item.getStatus().equals(CLOSE_SESSION)) {
            et_r_close_balance.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(item.getBaseClosedAmount())));
            et_note.setText(item.getClosedNote());
            bt_cancel.setVisibility(VISIBLE);
            bt_adjustment.setVisibility(GONE);
            make_adjustment_put_money.setVisibility(GONE);
            make_adjustment_take_money.setVisibility(GONE);
            bt_close.setVisibility(GONE);
            bt_validate.setVisibility(VISIBLE);
            close_session_list_panel.setEnableAction(false);
        } else {
            bt_cancel.setVisibility(GONE);
            bt_adjustment.setVisibility(VISIBLE);
            bt_close.setVisibility(VISIBLE);
            bt_validate.setVisibility(GONE);
            close_session_list_panel.setEnableAction(true);
            make_adjustment_put_money.setVisibility(ConfigUtil.isManagerShiftAdjustment() ? VISIBLE : GONE);
            make_adjustment_take_money.setVisibility(ConfigUtil.isManagerShiftAdjustment() ? VISIBLE : GONE);
        }

        ((RegisterShiftListController) getController()).bindListValueClose();
        et_r_close_balance.setEnabled(item.getStatus().equals(CLOSE_SESSION) ? false : true);
        et_note.setEnabled(item.getStatus().equals(CLOSE_SESSION) ? false : true);
        tv_open_session_balance.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(item.getBaseFloatAmount())));
        tv_t_close_balance.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(item.getBaseBalance())));
        float transaction = item.getBaseBalance() - item.getBaseFloatAmount();
        tv_transaction.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(transaction)));
        actionChangeBalance(item);

        bt_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_close.getText().toString().equals(getContext().getString(R.string.confirm))) {
                    et_r_close_balance.setText(ConfigUtil.formatPrice(total_value));
                    bt_adjustment.setText(getContext().getString(R.string.register_shift_dialog_close_set_balance));
                    bt_adjustment.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
                    rl_content.setVisibility(VISIBLE);
                    rl_set_value.setVisibility(GONE);
                    bt_close.setText(getContext().getString(R.string.register_shift_bottom_close_shift));
                    tv_session_back.setVisibility(VISIBLE);
                    line.setVisibility(VISIBLE);
                    tv_session_title.setText(getContext().getString(R.string.close_session_id_title, item.getShiftId()));
                    total_balance_value.setVisibility(GONE);
                } else {
                    SessionParam param = ((RegisterShiftListController) getController()).createSessionParam();
                    float r_balance = et_r_close_balance.getValueFloat();
                    float base_r_balance = ConfigUtil.convertToBasePrice(r_balance);
                    param.setBalance(item.getBalance());
                    param.setBaseBalance(item.getBaseBalance());
                    param.setBaseCashAdded(item.getBaseCashAdded());
                    param.setBaseFloatAmount(item.getBaseFloatAmount());
                    param.setFloatAmount(item.getFloatAmount());
                    param.setBaseCurrencyCode(ConfigUtil.getBaseCurrencyCode());
                    param.setCashAdded(item.getCashAdded());
                    param.setOpenedNote(item.getOpenedNote());
                    param.setClosedNote(et_note.getText().toString());
                    param.setTotalSales(item.getTotalSales());
                    param.setBaseTotalSales(item.getBaseTotalSales());
                    param.setCloseAmount(r_balance);
                    param.setBaseClosedAmount(base_r_balance);
                    param.setCashRemoved(item.getCashRemoved()); // real amount
                    param.setBaseCashRemoved(item.getBaseCashRemoved());
                    param.setCashLeft(r_balance);
                    param.setBaseCashLeft(base_r_balance);
                    param.setShiftCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());
                    param.setStaffId(item.getStaffId());
                    param.setLocationId(item.getLocationId());
                    param.setOpenedAt(item.getOpenedAt());
                    param.setCloseAt(ConfigUtil.getCurrentDateTime());
                    param.setShiftId(item.getShiftId());
                    param.setPosId(item.getPosId());
                    param.setStatus(CLOSE_SESSION);
                    ((RegisterShiftListController) getController()).doInputCloseSession(item, param);
                }
            }
        });

        bt_validate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionParam param = ((RegisterShiftListController) getController()).createSessionParam();
                float different = item.getBaseBalance() - item.getBaseClosedAmount();
                if (different < 0) {
                    different = 0;
                }
                param.setBalance(ConfigUtil.convertToPrice(different));
                param.setBaseBalance(different);
                param.setBaseCashAdded(item.getBaseCashAdded());
                param.setBaseFloatAmount(item.getBaseFloatAmount());
                param.setFloatAmount(item.getFloatAmount());
                param.setBaseCurrencyCode(ConfigUtil.getBaseCurrencyCode());
                param.setCashAdded(item.getCashAdded());
                param.setOpenedNote(item.getOpenedNote());
                param.setClosedNote(et_note.getText().toString());
                param.setTotalSales(item.getTotalSales());
                param.setBaseTotalSales(item.getBaseTotalSales());
                param.setCloseAmount(item.getClosedAmount());
                param.setBaseClosedAmount(item.getBaseClosedAmount());
                param.setCashRemoved(item.getClosedAmount()); // real amount
                param.setBaseCashRemoved(item.getBaseClosedAmount());
                param.setCashLeft(item.getCashLeft());
                param.setBaseCashLeft(item.getBaseCashLeft());
                param.setShiftCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());
                param.setStaffId(item.getStaffId());
                param.setLocationId(item.getLocationId());
                param.setOpenedAt(item.getOpenedAt());
                param.setCloseAt(ConfigUtil.getCurrentDateTime());
                param.setShiftId(item.getShiftId());
                param.setPosId(item.getPosId());
                param.setStatus(VALIDATE);
                ((RegisterShiftListController) getController()).doInputValidateSession(item, param);
            }
        });

        bt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionParam param = crateSessionParam(OPEN_SESSION, item);
                ((RegisterShiftListController) getController()).doInputCancelSession(item, param, 0);
            }
        });

        tv_session_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getStatus().equals(CLOSE_SESSION)) {
                    SessionParam param = crateSessionParam(OPEN_SESSION, item);
                    ((RegisterShiftListController) getController()).doInputCancelSession(item, param, 1);
                } else {
                    ((RegisterShiftListController) getController()).dismissDialogCloseSession();
                }
            }
        });

        bt_adjustment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_adjustment.getText().toString().equals(getContext().getString(R.string.cancel))) {
                    bt_adjustment.setText(getContext().getString(R.string.register_shift_dialog_close_set_balance));
                    bt_adjustment.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color));
                    rl_content.setVisibility(VISIBLE);
                    rl_set_value.setVisibility(GONE);
                    bt_close.setText(getContext().getString(R.string.register_shift_bottom_close_shift));
                    tv_session_back.setVisibility(VISIBLE);
                    line.setVisibility(VISIBLE);
                    tv_session_title.setText(getContext().getString(R.string.close_session_id_title, item.getShiftId()));
                    total_balance_value.setVisibility(GONE);
                } else {
                    bt_adjustment.setText(getContext().getString(R.string.cancel));
                    bt_adjustment.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                    rl_content.setVisibility(GONE);
                    rl_set_value.setVisibility(VISIBLE);
                    bt_close.setText(getContext().getString(R.string.confirm));
                    tv_session_back.setVisibility(GONE);
                    line.setVisibility(GONE);
                    tv_session_title.setText(getContext().getString(R.string.open_session_set_balance_value));
                    total_balance_value.setVisibility(VISIBLE);
                }
            }
        });

        make_adjustment_put_money.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterShiftListController) getController()).showDialogMakeAdjusment(true);
            }
        });

        make_adjustment_take_money.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterShiftListController) getController()).showDialogMakeAdjusment(false);
            }
        });
    }

    private SessionParam crateSessionParam(String type, RegisterShift item) {
        SessionParam param = ((RegisterShiftListController) getController()).createSessionParam();
        param.setBalance(item.getBalance());
        param.setBaseBalance(item.getBaseBalance());
        param.setBaseCashAdded(item.getBaseCashAdded());
        param.setBaseFloatAmount(item.getBaseFloatAmount());
        param.setFloatAmount(item.getFloatAmount());
        param.setBaseCurrencyCode(ConfigUtil.getBaseCurrencyCode());
        param.setCashAdded(item.getCashAdded());
        param.setOpenedNote(item.getOpenedNote());
        param.setTotalSales(item.getTotalSales());
        param.setBaseTotalSales(item.getBaseTotalSales());
        param.setCloseAmount(item.getClosedAmount());
        param.setBaseClosedAmount(item.getBaseClosedAmount());
        param.setCashRemoved(item.getCashRemoved());
        param.setBaseCashRemoved(item.getBaseCashRemoved());
        param.setCashLeft(item.getCashLeft());
        param.setBaseCashLeft(item.getBaseCashLeft());
        param.setShiftCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());
        param.setStaffId(item.getStaffId());
        param.setLocationId(item.getLocationId());
        param.setOpenedAt(item.getOpenedAt());
        param.setCloseAt(item.getClosedAt());
        param.setShiftId(item.getShiftId());
        param.setPosId(item.getPosId());
        param.setStatus(type);

        return param;
    }

    public void actionChangeBalance(final RegisterShift item) {
        et_r_close_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float balance = et_r_close_balance.getValueFloat();
                float different = ConfigUtil.convertToPrice(item.getBaseBalance()) - balance;
                if (different < 0) {
                    tv_difference.setText(ConfigUtil.formatPrice(0 - different));
                    balance_different = 0 - different;
                } else {
                    tv_difference.setText(ConfigUtil.formatPrice(different));
                    balance_different = different;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showViewValidation() {
        et_r_close_balance.setEnabled(false);
        rl_add_value.setVisibility(GONE);
        bt_cancel.setVisibility(VISIBLE);
        bt_adjustment.setVisibility(GONE);
        bt_close.setVisibility(GONE);
        bt_validate.setVisibility(VISIBLE);
    }

    public void updateFloatAmount(float total) {
//        et_r_close_balance.setText(ConfigUtil.formatNumber(total));
        total_value = total;
        total_balance_value.setText(ConfigUtil.formatPrice(total));
    }

    public void backToLoginActivity() {
        DataUtil.saveDataBooleanToPreferences(((RegisterShiftListController) getController()).getMagestoreContext().getActivity(), DataUtil.CHOOSE_STORE, false);
        ConfigUtil.setCheckFirstOpenSession(false);
        Intent intent = new Intent(((RegisterShiftListController) getController()).getMagestoreContext().getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((RegisterShiftListController) getController()).getMagestoreContext().getActivity().startActivity(intent);
    }

    public void setEnableBtClose(boolean isEnable) {
        bt_close.setEnabled(isEnable ? true : false);
    }

    public void setEnableBtValidate(boolean isEnable) {
        bt_validate.setEnabled(isEnable ? true : false);
    }

    public void setEnableCancel(boolean isEnable) {
        bt_cancel.setEnabled(isEnable ? true : false);
        tv_session_back.setEnabled(isEnable ? true : false);
    }
}
