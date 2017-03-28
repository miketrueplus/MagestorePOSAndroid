package com.magestore.app.pos;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.controller.SettingListController;
import com.magestore.app.pos.panel.SettingDetailPanel;
import com.magestore.app.pos.panel.SettingListPanel;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Created by Johan on 2/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingActivity extends AbstractActivity {
    // Toolbar ứng dụng
    private Toolbar mToolbar;

    SettingListPanel mSettingListPanel = null;
    SettingDetailPanel mSettingDetailPanel = null;

    SettingListController mSettingListController;

    // xác định loại màn hình 1 pane hay 2 pane
    private boolean mblnTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu);

        initLayout();
        initModel();
        initValue();

        super.setheader();
    }

    @Override
    protected void initLayout() {
        // chuẩn bị tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        initToolbarMenu(mToolbar);

        mSettingListPanel = (SettingListPanel) findViewById(R.id.setting_list_panel);

        mSettingDetailPanel = (SettingDetailPanel) findViewById(R.id.setting_detail_panel);

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
        ConfigService service = null;
        UserService userService = null;
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            service = factory.generateConfigService();
            userService = factory.generateUserService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        mSettingListController = new SettingListController();
        mSettingListController.setMagestoreContext(magestoreContext);
        mSettingListController.setUserService(userService);
        mSettingListController.setConfigService(service);
        mSettingListController.setListPanel(mSettingListPanel);
        mSettingListController.setDetailPanel(mSettingDetailPanel);

        mSettingListPanel.initModel();
        mSettingDetailPanel.initModel();
    }

    @Override
    protected void initValue() {
        mSettingListController.doRetrieve();
    }
}
