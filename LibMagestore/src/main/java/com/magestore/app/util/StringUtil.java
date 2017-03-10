package com.magestore.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Tiện ích xử lý chuỗi
 * Created by Mike on 12/13/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class StringUtil {
    public static String STRING_EMPTY = "";
    public static String STRING_DOT = ".";
    public static String STRING_ZERO = "0";


    /**
     * Tạo String Builder từ InputStream
     * @param is
     * @return
     * @throws IOException
     */
    public static StringBuilder convertStreamToStringBuilder(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb;
    }

    /**
     * Tạo String từ InputStream
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        return convertStreamToStringBuilder(is).toString();
    }
}
