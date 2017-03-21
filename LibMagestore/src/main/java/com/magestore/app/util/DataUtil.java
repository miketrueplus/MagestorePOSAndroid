package com.magestore.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class DataUtil {
    private static String MAGESTORE_PREFERENCES = "magestore";
    public static String QUOTE = "quote";
    public static String STORE_ID = "store_id";
    public static String CHOOSE_STORE = "choose_store";

    public static void saveDataStringToPreferences(Context context, String key, String data){
        SharedPreferences shared_preferences = context.getSharedPreferences(MAGESTORE_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static void saveDataBooleanToPreferences(Context context, String key, boolean isSave){
        SharedPreferences shared_preferences = context.getSharedPreferences(MAGESTORE_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putBoolean(key, isSave);
        editor.commit();
    }

    public static String getDataStringToPreferences(Context context, String key){
        SharedPreferences shared_preferences = context.getSharedPreferences(MAGESTORE_PREFERENCES, context.MODE_PRIVATE);
        String data = shared_preferences.getString(key, "");
        return data;
    }

    public static boolean getDataBooleanToPreferences(Context context, String key){
        SharedPreferences shared_preferences = context.getSharedPreferences(MAGESTORE_PREFERENCES, context.MODE_PRIVATE);
        boolean isSave = shared_preferences.getBoolean(key, false);
        return isSave;
    }

    public static void removeDataToPreferences(Context context, String key){
        SharedPreferences shared_preferences = context.getSharedPreferences(MAGESTORE_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
