package com.magestore.app.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class WelcomeActivity extends AbstractActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_menu);
    }
}
