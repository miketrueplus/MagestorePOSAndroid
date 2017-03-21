package com.magestore.app.pos.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.RegisterShiftActivity;
import com.magestore.app.pos.SettingActivity;
import com.magestore.app.util.DataUtil;
import com.magestore.app.view.ui.PosUI;
import com.magestore.app.pos.CustomerActivity;
import com.magestore.app.pos.OrderActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesActivity;

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

    public void showErrorMsg(Exception exp) {
        exp.printStackTrace();
        new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.dialog_warning)
                    .setMessage(exp.getLocalizedMessage())
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
//        } else if (id == R.id.nav_register_shift) {
//            Intent intent = new Intent(getContext(), RegisterShiftActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            backToLoginActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void backToLoginActivity(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_close)
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
        navigationView.setNavigationItemSelectedListener(this);
    }
}
