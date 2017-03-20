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
    public static final String STRING_EMPTY = "";
    public static final String STRING_DIGIT = "0123456789";
    public static final String STRING_ZERO = "0";
    public static final String STRING_ONE = "1";
    public static final String STRING_NINE = "9";
    public static final String STRING_SPACE = " ";

    public static final String STRING_DOT = ".";
    public static final String STRING_DOT_SPACE = ". ";

    public static final String STRING_COMMA = ",";
    public static final String STRING_COMMA_SPACE = ", ";


    public static final String STRING_SLASH = "/";
    public static final String STRING_SLASH_SPACE = "/ ";

    public static final String STRING_COLON = ":";
    public static final String STRING_COLON_SPACE = ": ";

    public static final String STRING_QUOTE = "\"";
    public static final String STRING_QUOTE_SPACE = "\" ";

    public static final String STRING_ADD = "+";
    public static final String STRING_ADD_SPACE = "+ ";

    public static final String STRING_MINUS = "-";
    public static final String STRING_MINUS_SPACE = "- ";

    public static final String STRING_DASH = "_";
    public static final String STRING_DASH_SPACE = "_ ";

    public static final String STRING_HASH = "#";
    public static final String STRING_HASH_SPACE = "#";

    public static final String STRING_GREATER = ">";
    public static final String STRING_LESS = "<";
    public static final String STRING_AND = "&";
    public final static String STRING_EQUAL = "=";

    public final static String STRING_OPEN_SQUARE = "[";
    public final static String STRING_CLOSE_SQUARE = "]";
    public final static String STRING_OPEN_BLANKET = "{";
    public final static String STRING_CLOSE_BLANKET = "}";
    public final static String STRING_CURRENCY = "$";

    // http
    public static final String STRING_HTTP = "http://";
    public static final String STRING_HTTPS = "https://";


    // type percent hoặc fix
    public static final String TYPE_PERCENT = "percent";
    public static final String TYPE_FIXED = "fixed";


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
