package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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

public class OpenSessionDetailPanel extends AbstractDetailPanel<RegisterShift> {
    static String OPEN_SESSION = "0";
    Button bt_open;
    RelativeLayout rl_add_value;
    TextView txt_staff_login;
    EditTextFloat et_float_amount;
    EditText et_note;
    SimpleSpinner sp_pos;
    OpenSessionListValuePanel openSessionListValuePanel;

    public OpenSessionDetailPanel(Context context) {
        super(context);
    }

    public OpenSessionDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenSessionDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        sp_pos = (SimpleSpinner) findViewById(R.id.sp_pos);
        et_float_amount = (EditTextFloat) findViewById(R.id.et_float_amount);
        bt_open = (Button) findViewById(R.id.bt_open);
        rl_add_value = (RelativeLayout) findViewById(R.id.rl_add_value);
        et_note = (EditText) findViewById(R.id.et_note);
        txt_staff_login = (TextView) findViewById(R.id.txt_staff_login);
        openSessionListValuePanel = (OpenSessionListValuePanel) findViewById(R.id.open_session_list_panel);
        initValue();
    }

    @Override
    public void initValue() {
        rl_add_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SessionController) mController).addValue();
            }
        });

        txt_staff_login.setText(ConfigUtil.getStaff().getStaffName());

        bt_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionParam param = ((SessionController) mController).createSessionParam();
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
                ((SessionController) mController).doInputOpenSession(param);
            }
        });
    }

    @Override
    public void initModel() {
        List<PointOfSales> listPos = ((SessionController) mController).getListPos();
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
        List<PointOfSales> listPos = ((SessionController) mController).getListPos();
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
