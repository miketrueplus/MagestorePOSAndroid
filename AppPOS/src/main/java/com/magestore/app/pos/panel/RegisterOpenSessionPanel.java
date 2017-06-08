package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.magestore.app.view.EditTextFloat;

import java.util.List;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterOpenSessionPanel extends AbstractDetailPanel<RegisterShift> {
    static String OPEN_SESSION = "0";
    Button bt_open;
    RelativeLayout rl_add_value;
    TextView txt_staff_login, tv_session_back;
    EditTextFloat et_float_amount;
    EditText et_note;
    SimpleSpinner sp_pos;
    RegisterShiftListController mRegisterShiftListController;
    OpenSessionListValuePanel openSessionListValuePanel;

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
        tv_session_back = (TextView) view.findViewById(R.id.tv_session_back);
        sp_pos = (SimpleSpinner) view.findViewById(R.id.sp_pos);
        et_float_amount = (EditTextFloat) view.findViewById(R.id.et_float_amount);
        bt_open = (Button) view.findViewById(R.id.bt_open);
        rl_add_value = (RelativeLayout) view.findViewById(R.id.rl_add_value);
        et_note = (EditText) view.findViewById(R.id.et_note);
        txt_staff_login = (TextView) view.findViewById(R.id.txt_staff_login);
        openSessionListValuePanel = (OpenSessionListValuePanel) view.findViewById(R.id.open_session_list_panel);

        initValue();
    }

    @Override
    public void initValue() {
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
                param.setStatus(OPEN_SESSION);
                mRegisterShiftListController.doInputOpenSession(param);
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
        List<PointOfSales> listPos = mRegisterShiftListController.getListPos();

        if (listPos != null && listPos.size() > 0) {
            sp_pos.bind(listPos.toArray(new PointOfSales[0]));

            sp_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PointOfSales pos = getPointOfSales(sp_pos.getSelection());
                    LoginActivity.STORE_ID = pos.getStoreId();
                    DataUtil.saveDataStringToPreferences(getContext(), DataUtil.STORE_ID, pos.getStoreId());
                    ConfigUtil.setPointOfSales(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
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
        et_float_amount.setText(ConfigUtil.formatNumber(total));
    }
}
