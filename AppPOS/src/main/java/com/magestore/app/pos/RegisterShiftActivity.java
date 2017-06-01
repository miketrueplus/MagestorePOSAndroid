package com.magestore.app.pos;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.panel.RegisterShiftDetailPanel;
import com.magestore.app.pos.panel.RegisterShiftListPanel;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftActivity extends AbstractActivity {
    RegisterShiftListPanel mRegisterShiftListPanel;
    RegisterShiftDetailPanel mRegisterShiftDetailPanel;
    RegisterShiftListController mRegisterShiftListController;

    // Toolbar ứng dụng
    private Toolbar toolbar_order;

    // xác định loại màn hình 1 pane hay 2 pane
    private boolean mblnTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_shift_menu);

        initLayout();
        initModel();
        initValue();

        super.setheader();
        super.changeBackgroundSelect();
    }

    @Override
    protected void initLayout() {
        toolbar_order = (Toolbar) findViewById(R.id.toolbar_order);
        initToolbarMenu(toolbar_order);

        // chuẩn bị panel danh sách register shift
        mRegisterShiftListPanel = (RegisterShiftListPanel) findViewById(R.id.register_shift_list_panel);
        // chuẩn bị panel thông tin đơn hàng chi tiết
        mRegisterShiftDetailPanel = (RegisterShiftDetailPanel) findViewById(R.id.register_shift_detail_panel);

        // xem giao diện 2 pane hay 1 pane
        mblnTwoPane = findViewById(R.id.two_pane) != null;
    }

    @Override
    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        MagestoreContext magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // chuẩn bị service
        ServiceFactory factory;
        RegisterShiftService service = null;
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            service = factory.generateRegisterShiftService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        mRegisterShiftListController = new RegisterShiftListController();
        mRegisterShiftListController.setMagestoreContext(magestoreContext);
        mRegisterShiftListController.setRegisterShiftService(service);
        mRegisterShiftListController.setListPanel(mRegisterShiftListPanel);
        mRegisterShiftListController.setDetailPanel(mRegisterShiftDetailPanel);

        mRegisterShiftListPanel.initModel();
        mRegisterShiftDetailPanel.initModel();
    }

    @Override
    protected void initValue() {
        // load danh sách register shift
        mRegisterShiftListController.doRetrieve();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mRegisterShiftDetailPanel.setVisibility(View.INVISIBLE);
            mRegisterShiftListPanel.setVisibility(View.VISIBLE);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            initToolbarMenu(toolbar_order);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
