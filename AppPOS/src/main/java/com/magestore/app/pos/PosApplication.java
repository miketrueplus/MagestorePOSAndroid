package com.magestore.app.pos;

import android.app.Application;

import com.magestore.app.util.FileUtil;

/**
 * Created by Mike on 1/17/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // xác định các thư mục cấu hình file
        FileUtil.registerDir(getApplicationContext());
    }
}
