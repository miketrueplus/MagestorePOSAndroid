package com.magestore.app.pos.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.exception.MagestoreException;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.RegisterShiftActivity;
import com.magestore.app.pos.SettingActivity;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.ui.PosUI;
import com.magestore.app.pos.CustomerActivity;
import com.magestore.app.pos.OrderActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractActivity
        extends AppCompatActivity
        implements
        PosUI,
        NavigationView.OnNavigationItemSelectedListener {
    TextView staff_name, staff_location;
    private static int positionSelectActivity = -1;
    Map<Integer, LinearLayout> listActivity = new HashMap<>();

    /**
     * Cấu hình lại các control layout
     */
    protected void initLayout() {

    }

    /**
     * Cấu hình lại các model
     */
    protected void initModel() {

    }

    /**
     * Tự động điền giá trị cho các trường trong form
     */
    protected void initValue() {

    }

    /**
     * Kiểm tra giá trịc ác control trước khi login
     *
     * @return
     */
    protected boolean validControlValue() {
        return false;
    }

    public void setheader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_layout = navigationView.findViewById(R.id.nav_header_menu);
        staff_name = (TextView) header_layout.findViewById(R.id.staff_name);
        staff_location = (TextView) header_layout.findViewById(R.id.staff_location);
        if (ConfigUtil.getStaff() != null) {
            staff_name.setText(ConfigUtil.getStaff().getStaffName());
            staff_location.setText(ConfigUtil.getStaff().getStaffLocation().getLocationName());
        }
    }

    /**
     * Lưu value trong shared preference
     *
     * @param strKey
     * @param strValue
     */
    public void saveSharedValue(String strKey, String strValue) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(strKey, strValue);
        editor.commit();
    }

    public String getSharedValue(String strKey, String strDefault) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        return sharedPref.getString(strKey, strDefault);
    }

    public Context getContext() {
        return this;
    }

    public void showProgress(boolean show) {

    }

    public void showUI(PosUI ui) {

    }

    /**
     * Hiển thị thông báo lỗi
     *
     * @param strMsg
     */
    public void showErrorMsg(String strMsg) {
        new AlertDialog.Builder(this)
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * @param aString
     * @return
     */
    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    /**
     * Dịch thông báo lỗi
     */
    public String getExceptionMessage(Exception exp) {
        String strReturn = exp.getLocalizedMessage();
        if (StringUtil.isNullOrEmpty(strReturn)) strReturn = StringUtil.STRING_EMPTY;

        if (exp instanceof MagestoreException) {
            String packageName = getPackageName();
            if (((MagestoreException) exp).getCode() != null) {
                int resId = getResources().getIdentifier(((MagestoreException) exp).getCode(), "string", packageName);
                if (resId > 0) return getString(resId) + "\n" + strReturn;
            }
        }
        return strReturn;
    }

    /**
     * Hiện thông báo lỗi
     *
     * @param exp
     */
    public void showErrorMsg(Exception exp) {
        exp.printStackTrace();
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_warning)
                .setMessage(getExceptionMessage(exp))
                .setPositiveButton(R.string.ok, null)
//                    .setPositiveButton(R.string.reload, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            close
//                        }
//                    })
//                    .setNegativeButton(R.string.no, null)
                .show();
    }

    public void close() {
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
//         Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_customers) {
            if (this instanceof CustomerActivity) return true;
            Intent intent = new Intent(getContext(), CustomerActivity.class);
            startActivity(intent);
            if (!(this instanceof SalesActivity)) finish();
        } else if (id == R.id.nav_sales) {
            if (this instanceof SalesActivity) return true;
//            Intent intent = new Intent(getContext(), SalesActivity.class);
//            startActivity(intent);
            if (!(this instanceof SalesActivity)) finish();
        } else if (id == R.id.nav_orders_history) {
            if (this instanceof OrderActivity) return true;
            Intent intent = new Intent(getContext(), OrderActivity.class);
            startActivity(intent);
            if (!(this instanceof SalesActivity)) finish();
//        }/ else if (id == R.id.nav_onhold_orders) {
//            Intent intent = new Intent(getContext(), OrderActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_register_shift) {
            if (this instanceof RegisterShiftActivity) return true;
            Intent intent = new Intent(getContext(), RegisterShiftActivity.class);
            startActivity(intent);
            if (!(this instanceof SalesActivity)) finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
            if (!(this instanceof SalesActivity)) finish();
        } else if (id == R.id.nav_logout) {
            backToLoginActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void backToLoginActivity() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.ask_are_you_sure_to_close)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataUtil.saveDataBooleanToPreferences(getContext(), DataUtil.CHOOSE_STORE, false);
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void initToolbarMenu(Toolbar toolbar) {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sales_navigation_drawer_open, R.string.sales_navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // lấy menu và đặt các event xử lý menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        LinearLayout nav_logout = (LinearLayout) navigationView.findViewById(R.id.nav_logout);
        LinearLayout nav_checkout = (LinearLayout) navigationView.findViewById(R.id.nav_checkout);
        LinearLayout nav_order_history = (LinearLayout) navigationView.findViewById(R.id.nav_order_history);
        LinearLayout nav_register_shift = (LinearLayout) navigationView.findViewById(R.id.nav_register_shift);
        LinearLayout nav_customer = (LinearLayout) navigationView.findViewById(R.id.nav_customer);
        LinearLayout nav_general = (LinearLayout) navigationView.findViewById(R.id.nav_general);
        nav_logout.setOnClickListener(onClickNav);
        nav_checkout.setOnClickListener(onClickNav);
        nav_order_history.setOnClickListener(onClickNav);
        nav_register_shift.setOnClickListener(onClickNav);
        nav_customer.setOnClickListener(onClickNav);
        nav_general.setOnClickListener(onClickNav);
        listActivity.put(0, nav_logout);
        listActivity.put(-1, nav_checkout);
        listActivity.put(1, nav_order_history);
        listActivity.put(2, nav_customer);
        listActivity.put(3, nav_general);
        listActivity.put(4, nav_register_shift);
    }

    View.OnClickListener onClickNav = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.nav_customer) {
                positionSelectActivity = 2;
                if (!(AbstractActivity.this instanceof CustomerActivity)) {
                    Intent intent = new Intent(getContext(), CustomerActivity.class);
                    startActivity(intent);
                }
            } else if (id == R.id.nav_checkout) {
                if (!(AbstractActivity.this instanceof SalesActivity)) finish();
            } else if (id == R.id.nav_order_history) {
                if (!(AbstractActivity.this instanceof OrderActivity)) {
                    positionSelectActivity = 1;
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    startActivity(intent);
                }
//        }/ else if (id == R.id.nav_onhold_orders) {
//            Intent intent = new Intent(getContext(), OrderActivity.class);
//            startActivity(intent);
            } else if (id == R.id.nav_register_shift) {
                positionSelectActivity = 4;
                if (!(AbstractActivity.this instanceof RegisterShiftActivity)) {
                    Intent intent = new Intent(getContext(), RegisterShiftActivity.class);
                    startActivity(intent);
                }
            } else if (id == R.id.nav_general) {
                positionSelectActivity = 3;
                if (!(AbstractActivity.this instanceof SettingActivity)) {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
                }
            } else if (id == R.id.nav_logout) {
                positionSelectActivity = 0;
                backToLoginActivity();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer != null) drawer.closeDrawer(GravityCompat.START);
        }
    };

    public void changeBackgroundSelect() {
        for (Map.Entry<Integer, LinearLayout> entry : listActivity.entrySet()) {
            int position = entry.getKey();
            if (position == positionSelectActivity) {
                LinearLayout layout = entry.getValue();
                layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.menu_left_select_color));
            }
        }
    }

    public void navigationView(int type) {
        if (type == 4) {
            positionSelectActivity = 4;
            if (!(AbstractActivity.this instanceof RegisterShiftActivity)) {
                Intent intent = new Intent(getContext(), RegisterShiftActivity.class);
                startActivity(intent);
            }
        }
    }
}
