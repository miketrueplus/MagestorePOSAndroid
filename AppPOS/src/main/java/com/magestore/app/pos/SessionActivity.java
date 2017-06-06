package com.magestore.app.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.controller.SessionController;
import com.magestore.app.pos.panel.OpenSessionListPanel;
import com.magestore.app.pos.panel.OpenSessionListValuePanel;
import com.magestore.app.pos.panel.OpenSessionDetailPanel;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SessionActivity extends AbstractActivity {
    OpenSessionDetailPanel mOpenSessionPanel;
    OpenSessionListValuePanel mOpenSessionListValuePanel;
    OpenSessionListPanel mOpenSessionListPanel;
    SessionController mSessionController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // chuẩn bị control layout
        initLayout();

        initModel();
    }

    @Override
    protected void initLayout() {
        mOpenSessionPanel = (OpenSessionDetailPanel) findViewById(R.id.open_session_panel);
        mOpenSessionListPanel = (OpenSessionListPanel) findViewById(R.id.open_session_list_panel);
        mOpenSessionListValuePanel = (OpenSessionListValuePanel) mOpenSessionPanel.findViewById(R.id.open_session_list_panel);
    }

    @Override
    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        MagestoreContext magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // chuẩn bị service
        ServiceFactory factory;
        RegisterShiftService service = null;
        UserService userService = null;
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            service = factory.generateRegisterShiftService();
            userService = factory.generateUserService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        mSessionController = new SessionController();
        mSessionController.setMagestoreContext(magestoreContext);
        mSessionController.setUserService(userService);
        mSessionController.setRegisterShiftService(service);
        mSessionController.setDetailPanel(mOpenSessionPanel);
        mSessionController.setListPanel(mOpenSessionListPanel);
        mSessionController.setOpenSessionListPanel(mOpenSessionListValuePanel);

        mOpenSessionListValuePanel.setSessionController(mSessionController);
        mOpenSessionPanel.initModel();
    }
}
