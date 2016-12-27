package com.magestore.app.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Các tiện ích liên quan đến kết nối mạng
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class AndroidNetworkUtil {
    /**
     * Kiểm tra trạng thái có mạng kết nối hay không
     * @param activity
     * @return
     */
    public static boolean isNetworkAvaiable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
