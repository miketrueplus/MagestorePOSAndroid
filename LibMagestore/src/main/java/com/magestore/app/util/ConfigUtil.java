package com.magestore.app.util;

import android.text.format.Time;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.staff.Staff;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Các tiện ích cấu hình hệ thống, các format
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ConfigUtil {
    //    public static Config mConfig;
    private static DecimalFormat mCurrencyFormat;
    private static DecimalFormat mCurrencyNoSymbolFormat;
    private static DecimalFormat mFloatFormat;
    private static DecimalFormat mIntegerFormat;
    private static Staff mStaff;
    private static Customer mCustomerGuest;
    private static boolean mShowDeliveryTime;

    /**
     * @param format
     */
    public static void setCurrencyFormat(DecimalFormat format) {
        mCurrencyFormat = format;
    }

    /**
     * @param format
     */
    public static void setIntegerFormat(DecimalFormat format) {
        mIntegerFormat = format;
    }

    /**
     * @param format
     */
    public static void setFloatFormat(DecimalFormat format) {
        mFloatFormat = format;
    }

    /**
     * @param format
     */
    public static void setCurrencyNoSymbolFormat(DecimalFormat format) {
        mCurrencyNoSymbolFormat = format;
    }

    /**
     * Trả lại format price
     *
     * @param number
     * @return
     */
    public static String formatPrice(float number) {
        return formatCurrency(number);
    }

    /**
     * Trả lại format price
     *
     * @return
     */
    public static String formatPrice(String quantity) {
        if (quantity == null || StringUtil.STRING_EMPTY.equals(quantity.trim()))
            return formatPrice(Float.parseFloat(StringUtil.STRING_ZERO));
        return formatPrice(Float.parseFloat(quantity));
    }

    /**
     * Trả lại format price
     *
     * @param quantity
     * @return
     */
    public static String formatPrice(CharSequence quantity) {
        return formatPrice(Float.parseFloat(quantity.toString()));
    }

    /**
     * Trả lại format số lượng
     *
     * @param quantity
     * @return
     */
    public static String formatQuantity(float quantity) {
        return formatNumber(quantity);
    }

    /**
     * Trả lại format số lượng
     *
     * @param quantity
     * @return
     */
    public static String formatQuantity(int quantity) {
        return formatNumber(quantity);
    }

    /**
     * Trả lại format số lượng
     *
     * @param quantity
     * @return
     */
    public static String formatQuantity(String quantity) {
        return formatNumber(Float.parseFloat(quantity));
    }

    /**
     * Trả lại format số lượng
     *
     * @param quantity
     * @return
     */
    public static String formatQuantity(CharSequence quantity) {
        return formatNumber(Float.parseFloat(quantity.toString()));
    }

    /**
     * Format tiền
     *
     * @param number
     * @return
     */
    private static String formatCurrency(float number) {
        return getPriceFormat().format(number);
    }

    private static DecimalFormat getPriceFormat() {
        if (mCurrencyFormat == null) {
            mCurrencyFormat = new DecimalFormat();
        }
        return mCurrencyFormat;
    }

    private static DecimalFormat getPriceNoSymbolFormat() {
        if (mCurrencyNoSymbolFormat == null) {
            mCurrencyNoSymbolFormat = new DecimalFormat();
        }
        return mCurrencyNoSymbolFormat;
    }

    private static DecimalFormat getFloatFormat() {
        if (mFloatFormat == null) {
            mFloatFormat = new DecimalFormat();
        }
        return mFloatFormat;
    }

    private static DecimalFormat getIntegerFormat() {
        if (mIntegerFormat == null) {
            mIntegerFormat = new DecimalFormat();
        }
        return mIntegerFormat;
    }

    public static float parseFloat(String value) {
        try {
            return getFloatFormat().parse(truncateFloatDigit(value)).floatValue();
        } catch (ParseException e) {
            return 0f;
        }
    }

    public static double parseDouble(String value) {
        try {
            return getFloatFormat().parse(truncateFloatDigit(value)).doubleValue();
        } catch (ParseException e) {
            return 0d;
        }
    }

    public static int parseInteger(String value) {
        try {
            return getFloatFormat().parse(truncateIntegerDigit(value)).intValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Parse value sang integer
     *
     * @param value
     * @return
     */
    public static int parseIntCurrencyFromEdit(String value) {
        try {
            return getPriceFormat().parse(value).intValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Parse value sang fliat
     *
     * @param value
     * @return
     */
    public static float parseFloatCurrencyFromEdit(String value) {
        try {
            return getPriceNoSymbolFormat().parse(value).floatValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Parse value sang double
     *
     * @param value
     * @return
     */
    public static double parseDoubleCurrencyFromEdit(String value) {
        try {
            return getPriceNoSymbolFormat().parse(value).doubleValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Trả lại số number digit
     *
     * @return
     */
    private static String DIGIT_PRICE;
    private static String DIGIT_REGEX_PRICE;

    public static String getPriceDigit() {
        if (DIGIT_PRICE == null) {
            DecimalFormat decimalFormat = getPriceFormat();
            DIGIT_PRICE = StringUtil.STRING_DIGIT + decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();// + mCurrencyFormat.getDecimalFormatSymbols().getGroupingSeparator();
        }
        return DIGIT_PRICE;
    }

    public static String truncatePrice(String price) {
        if (DIGIT_REGEX_PRICE == null)
            DIGIT_REGEX_PRICE = StringUtil.STRING_OPEN_SQUARE + "^" + ConfigUtil.getPriceDigit() + StringUtil.STRING_CLOSE_SQUARE;
        return price.replaceAll(DIGIT_REGEX_PRICE, "");
    }

    /**
     * Trả lại số number digit
     *
     * @return
     */
    private static String DIGIT_REGEX_FLOAT;
    private static String DIGIT_FLOAT;

    public static String getFloatDigit() {
        if (DIGIT_FLOAT == null) {
            DecimalFormat decimalFormat = getFloatFormat();
            DIGIT_FLOAT = StringUtil.STRING_DIGIT + decimalFormat.getDecimalFormatSymbols().getDecimalSeparator() + decimalFormat.getDecimalFormatSymbols().getGroupingSeparator();// + mCurrencyFormat.getDecimalFormatSymbols().getGroupingSeparator();
        }
        return DIGIT_FLOAT;
    }

    public static String truncateFloatDigit(String number) {
        if (DIGIT_REGEX_FLOAT == null)
            DIGIT_REGEX_FLOAT = StringUtil.STRING_OPEN_SQUARE + "^" + ConfigUtil.getFloatDigit() + StringUtil.STRING_CLOSE_SQUARE;
        return number.replaceAll(DIGIT_REGEX_FLOAT, "");
    }

    /**
     * Trả lại số number digit
     *
     * @return
     */
    private static String DIGIT_REGEX_INTEGER;
    private static String DIGIT_INTEGER;

    public static String getIntegerDigit() {
        if (DIGIT_INTEGER == null) {
            DecimalFormat decimalFormat = getIntegerFormat();
            DIGIT_INTEGER = StringUtil.STRING_DIGIT + decimalFormat.getDecimalFormatSymbols().getGroupingSeparator(); // + mCurrencyFormat.getDecimalFormatSymbols().getGroupingSeparator();
        }
        return DIGIT_INTEGER;
    }

    public static String truncateIntegerDigit(String number) {
        if (DIGIT_REGEX_INTEGER == null)
            DIGIT_REGEX_INTEGER = StringUtil.STRING_OPEN_SQUARE + "^" + ConfigUtil.getIntegerDigit() + StringUtil.STRING_CLOSE_SQUARE;
        return number.replaceAll(DIGIT_REGEX_INTEGER, "");
    }

    /**
     * Format con số
     *
     * @param number
     * @return
     */
    public static String formatInteger(String number) {
        if ((number == null) || (StringUtil.STRING_EMPTY.equals(number)))
            return getIntegerFormat().format(Integer.parseInt(StringUtil.STRING_ZERO));
//        return getIntegerFormat().format(Integer.parseInt(number));
        try {
            return getIntegerFormat().format(getIntegerFormat().parse(number));
        } catch (ParseException e) {
            return getIntegerFormat().format(0);
        }
    }

    /**
     * Format con số
     *
     * @param number
     * @return
     */
    public static String formatFloat(String number) {
        if ((number == null) || (StringUtil.STRING_EMPTY.equals(number)))
            return getFloatFormat().format(Double.parseDouble(StringUtil.STRING_ZERO));
//        return getFloatFormat().format(Double.parseDouble(number));
        try {
            return getFloatFormat().format(getFloatFormat().parse(number));
        } catch (ParseException e) {
            return getFloatFormat().format(0);
        }
    }

    /**
     * Format con số
     *
     * @param number
     * @return
     */
    public static String formatNumber(String number) {
        if ((number == null) || (StringUtil.STRING_EMPTY.equals(number)))
            return getFloatFormat().format(Double.parseDouble(StringUtil.STRING_ZERO));
//        return getIntegerFormat().format(Integer.parseInt(number));
        try {
            return getIntegerFormat().format(getIntegerFormat().parse(number));
        } catch (ParseException e) {
            return getIntegerFormat().format(0);
        }
    }

    /**
     * Format con số
     *
     * @param nummber
     * @return
     */
    public static String formatNumber(double nummber) {
        return getFloatFormat().format(nummber);
    }

    /**
     * Format con số
     *
     * @param nummber
     * @return
     */
    public static String formatNumber(float nummber) {
        return getFloatFormat().format(nummber);
    }

    /**
     * Format con số
     *
     * @param number
     * @return
     */
    public static String formatNumber(int number) {
        return getIntegerFormat().format(number);
    }

    /**
     * Format date
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateFormat = null;
        try {
            dateFormat = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateFormat == null) {
            return date;
        }
        return DateFormat.getDateInstance().format(dateFormat);
    }

    /**
     * Format time
     *
     * @param date
     * @return
     */
    public static String formatTime(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateFormat = null;
        try {
            dateFormat = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateFormat == null) {
            return date;
        }
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(dateFormat);
    }

    /**
     * Format date and time
     *
     * @param date
     * @return
     */
    public static String formatDateTime(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateFormat = null;
        try {
            dateFormat = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateFormat == null) {
            return date;
        }
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(dateFormat);
    }

    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("hh:mm");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getCurrentDateTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * convert current time to default
     * @param date_time
     * @return
     */
    public static String convertToGMTTime(String date_time){
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss" ;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(Time.getCurrentTimezone()));
        Date dateFormat = null;
        try {
            dateFormat = sdf.parse(date_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT);
        sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateTimeString =  sdf1.format(dateFormat);
        return dateTimeString;
    }

    public static float convertToBasePrice(float number) {
        return number;
    }

    public static void setStaff(Staff staff) {
        mStaff = staff;
    }

    public static Staff getStaff() {
        return mStaff;
    }

    public static Customer getCustomerGuest() {
        return mCustomerGuest;
    }

    public static void setCustomerGuest(Customer customerGuest) {
        mCustomerGuest = customerGuest;
    }

    public static void setShowDeliveryTime(boolean showDeliveryTime) {
        mShowDeliveryTime = showDeliveryTime;
    }

    public static boolean isShowDeliveryTime() {
        return mShowDeliveryTime;
    }
}
