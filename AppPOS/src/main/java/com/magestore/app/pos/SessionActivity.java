package com.magestore.app.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magestore.app.pos.panel.OpenSessionPanel;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SessionActivity extends AbstractActivity {
    OpenSessionPanel mOpenSessionPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // chuẩn bị control layout
        initLayout();
    }

    @Override
    protected void initLayout() {
        mOpenSessionPanel = (OpenSessionPanel) findViewById(R.id.open_session_panel);
    }
}
