package com.magestore.app.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.controller.SessionController;
import com.magestore.app.pos.panel.OpenSessionListPanel;
import com.magestore.app.pos.panel.OpenSessionPanel;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SessionActivity extends AbstractActivity {
    OpenSessionPanel mOpenSessionPanel;
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
        mOpenSessionPanel = (OpenSessionPanel) findViewById(R.id.open_session_panel);
        mOpenSessionListPanel = (OpenSessionListPanel) mOpenSessionPanel.findViewById(R.id.open_session_list_panel);
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

        mSessionController = new SessionController();
        mSessionController.setMagestoreContext(magestoreContext);
        mSessionController.setRegisterShiftService(service);
        mSessionController.setDetailPanel(mOpenSessionPanel);
        mSessionController.setOpenSessionListPanel(mOpenSessionListPanel);

        mOpenSessionListPanel.setSessionController(mSessionController);
        mOpenSessionPanel.initModel();
    }
}
