package com.magestore.app.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

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
    public static final String STRING_TWO = "2";
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
    public static final String STRING_TRUE = "true";
    public static final String STRING_FALSE = "false";

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
    public static final String STATUS_PAID = "paid";
    public static final String STATUS_INVOICED = "invoiced";
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_CLOSED = "closed";
    public static final String STATUS_CANCELLED = "canceled";
    public static final String STATUS_CANCEL = "cancel";
    public static final String STATUS_NOT_SYNC = "not_sync";
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

    // place auto complete
    public static final String PLACE_STREET_NUMBER = "street_number";
    public static final String PLACE_ROUTE = "route";
    public static final String PLACE_NEIGHBORHOOD = "neighborhood";
    public static final String PLACE_LOCALITY = "locality";
    public static final String PLACE_SUBLOCALITY_LEVEL_1 = "sublocality_level_1";
    public static final String PLACE_ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";
    public static final String PLACE_ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";
    public static final String PLACE_COUNTRY = "country";
    public static final String PLACE_POSTAL_CODE = "postal_code";
    public static final String PLACE_POSTAL_CODE_SUFFIX = "postal_code_suffix";

    /**
     * Thêm một element trong 1 chuỗi mảng
     *
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
     *
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

    public static String truncateJsonOption(String json) {
        return
                json.replace("\\", "")
                        .replace("\"{\"", "{\"")
                        .replace("}\"", "}")
                        .replace("\"\"", "\\\"\"")
                        .replace(":\\\"\",", ":\"\",");
    }

    /**
     * Chuỗi null hoặc toàn ký tự trắng
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || STRING_EMPTY.equals(str.trim());
    }

    public static String removeAllSymbol(String str) {
        return str.replaceAll("[^0-9]", "");
    }

    public static String capitalizedString(String inputVal) {
        // Empty strings should be returned as-is.
        if (inputVal.length() == 0) return "";
        // Strings with only one character uppercased.
        if (inputVal.length() == 1) return inputVal.toUpperCase();
        // Otherwise uppercase first letter, lowercase the rest.
        return inputVal.substring(0, 1).toUpperCase()
                + inputVal.substring(1).toLowerCase();
    }

    /**
     * Check requied email
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String email_validation_match = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            if (!email.matches(email_validation_match)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkJsonData(String data){
        if(StringUtil.isNullOrEmpty(data) || data.equals(STATUS_FALSE)){
            return false;
        }
        return true;
    }

    public static String getHostUrl(String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            return "";
        }
        try {
            URL host = new URL(url);
            return host.getHost();
        } catch (Exception e) {
            return url;
        }
    }

    public static String getDomain(String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            return "";
        }
        try {
            URL host = new URL(url);
            String mDomain = host.getProtocol() + "://" + host.getHost();
            return mDomain;
        } catch (Exception e) {
            return url;
        }
    }

    // so sánh domain
    public static boolean checkSameDomain(String domain, String licenseDomain) {
        if (domain.contains("www.")) {
            domain = domain.replace("www.", "");
        }
        if (licenseDomain.contains("www.")) {
            licenseDomain = licenseDomain.replace("www.", "");
        }

        if (domain.contains("https://")) {
            domain = domain.replace("https://", "");
        } else if (domain.contains("http://")) {
            domain = domain.replace("http://", "");
        }

        if (domain.length() > 36) {
            domain = domain.substring(0, 36);
        }

        String checkdomain = domain.substring(domain.length() - 1, domain.length());
        if (checkdomain.equals("/")) {
            domain = domain.substring(0, (domain.length() - 1));
        }

        if (licenseDomain.contains("https://")) {
            licenseDomain = licenseDomain.replace("https://", "");
        } else if (licenseDomain.contains("http://")) {
            licenseDomain = licenseDomain.replace("http://", "");
        }

        String checklicenseDomain = licenseDomain.substring(licenseDomain.length() - 1, licenseDomain.length());
        if (checklicenseDomain.equals("/")) {
            licenseDomain = licenseDomain.substring(0, (licenseDomain.length() - 1));
        }

        if (domain.equals(licenseDomain)) return true;

        return false;
    }

    // giải mã với public key
    public static String decryptRSAToString(String encryptedBase64, String privateKey) {
        String decryptedString = "";
        try {
            String rStart = privateKey.replace("-----BEGIN PUBLIC KEY-----", "");
            String rEnd = rStart.replace("-----END PUBLIC KEY-----", "");
            rEnd = rEnd.replaceAll("\r", "");
            rEnd = rEnd.replaceAll("\n", "");
            rEnd = rEnd.replaceAll("\t", "");
            rEnd = rEnd.replaceAll(" ", "");
            KeyFactory keyFac = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyFac.generatePublic(new X509EncodedKeySpec(Base64.decode(rEnd.toString(), Base64.DEFAULT)));

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }
}
