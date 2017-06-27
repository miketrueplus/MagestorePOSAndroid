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
    public static final String STRING_FIVE = "5";
    public static final String STRING_NINE = "9";
    public static final String STRING_TEN = "10";
    public static final String STRING_DOZEN = "12";
    public static final String STRING_HUNDRED = "100";
    public static final String STRING_THOUSAND = "1000";
    public static final String STRING_TEN_THOUSAND = "10000";
    public static final String STRING_HUNDRED_THOUSAND = "100000";
    public static final String STRING_MILLION = "1000000";
    public static final String STRING_TEN_MILLION = "10000000";
    public static final String STRING_HUNDRED_MILLION = "100000000";
    public static final String STRING_BILLION = "1000000000";
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

    // các kiểu status
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_CLOSED = "closed";
    public static final String STATUS_CANCELLED = "canceled";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_ENABLE = "enable";
    public static final String STATUS_DISABLE = "disable";
    public static final String STATUS_ON = "on";
    public static final String STATUS_OFF = "off";
    public static final String STATUS_RIGHT = "right";
    public static final String STATUS_WRONG = "wrong";
    public static final String STATUS_VISIBLE = "visible";
    public static final String STATUS_INVISIBLE = "invisible";
    public static final String STATUS_TRUE = "true";
    public static final String STATUS_FALSE = "false";
    public static final String STATUS_UP = "up";
    public static final String STATUS_DOWN = "down";
    public static final String STATUS_NaN = "nan";

    /**
     * Thêm một element trong 1 chuỗi mảng
     * @param builder
     * @param element
     * @return
     */
    public static StringBuilder addStringElement(StringBuilder builder, String element) {
        if (builder.length() == 0) builder.append(element);
        else builder.append(STRING_COMMA).append(element);
        return builder;
    }

    /**
     * Thêm 1 element trong 1 chuỗi mảng chỉ nếu status true
     * @param builder
     * @param status
     * @param element
     * @return
     */
    public static StringBuilder addStringElement(StringBuilder builder, boolean status, String element) {
        if (!status) return builder;
        return addStringElement(builder, element);
    }

    /**
     * Tạo String Builder từ InputStream
     *
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
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        return convertStreamToStringBuilder(is).toString();
    }

    /**
     * Chuyen cac chuoi json thanh dang chuan
     *
     * @param json
     * @return
     */
    public static String truncateJson(String json) {
        return
                json.replace("\\", "")
                        .replace("\"{\"", "{\"")
                        .replace("}\"", "}");
    }

    /**
     * Chuỗi null hoặc toàn ký tự trắng
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || STRING_EMPTY.equals(str.trim());
    }

    public static String removeAllSymbol(String str){
        return str.replaceAll("[^0-9]", "");
    }
}
