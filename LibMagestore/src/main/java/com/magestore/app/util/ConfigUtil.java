package com.magestore.app.util;

import android.text.format.Time;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.config.ActiveKey;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOptionSwatch;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigPrint;
import com.magestore.app.lib.model.config.ConfigProductOption;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.model.directory.Currency;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Các tiện ích cấu hình hệ thống, các format
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ConfigUtil {
    // platform
    private static String mPlatForm;
    public static String PLATFORM_MAGENTO_1 = "magento1";
    public static String PLATFORM_MAGENTO_2 = "magento2";
    public static String PLATFORM_ODOO = "Odoo";

    // check tablet or mobile
    private static boolean mTablet;

    // public static Config mConfig;
    private static DecimalFormat mCurrencyFormat;
    private static DecimalFormat mCurrencyNoSymbolFormat;
    private static DecimalFormat mFloatFormat;
    private static DecimalFormat mIntegerFormat;
    private static DecimalFormat mQuantityFormat;

    private static ConfigPrint mConfigPrint;
    private static Currency currentCurrency;
    private static String mBaseCurrencyCode;
    private static Staff mStaff;
    private static Customer mCustomerGuest;
    private static boolean mShowDeliveryTime;
    private static boolean mEnableStoreCredit;
    private static boolean mEnableRewardPoint;
    private static boolean mEnableGiftCard;
    private static boolean mEnableSession;
    private static boolean mCheckFirstOpenSession;
    private static boolean mEnableDeleteOrder;
    private static List<ConfigTaxClass> mConfigTaxClass;
    private static List<CheckoutPayment> mListPayment;
    private static String mTypePrint;
    private static int mStarPrintArea;
    private static PointOfSales mPointOfSales;
    private static String mPosId;
    private static String mShiftId;
    private static String mLocationId;
    private static String mRegisterShiftId;
    private static boolean mCashControl = true;
    private static ConfigPriceFormat mConfigPriceFormat;

    // permisson order
    private static boolean mChangeStaff;
    private static boolean mCreateOrder;
    private static boolean mManageAllOrder;
    private static boolean mManageOrderByMe;
    private static boolean mManageOrderByLocation;
    private static boolean mManageOrderOtherStaff;
    private static boolean mCanUseRefund;
    private static boolean mSendEmail;
    private static boolean mShip;
    private static boolean mCancel;
    private static boolean mAddComment;
    private static boolean mReOder;
    private static boolean mPartialInvoice;
    // permisson discount
    private static boolean mCustomSales;
    private static boolean mManageAllDiscount;
    private static boolean mDiscountPerCart;
    private static boolean mApplyCoupon;
    private static boolean mDiscountPerItem;
    private static boolean mApplyCustomPrice;
    private static String mDiscountProductId;
    // permisson session
    private static boolean mManageShiftAdjustment;
    private static boolean mShiftOpenNote;
    private static boolean mEnableOpenFloatAmount;
    private static boolean mShiftCloseNote;
    private static boolean mEnableCloseAmount;
    private static boolean mCancelCloseSession;
    private static boolean mPrintSession;
    // permisson checkout
    private static boolean mNeedToShip;
    private static boolean mMarkAsShip;
    // customer
    private static boolean mAddAddressDefault;
    private static boolean mShippingAddress;
    private static boolean mAddAddress;
    private static boolean mLastName;
    private static boolean mCompany;
    private static boolean mSubscribe;
    private static boolean mEditState;
    private static boolean mSameAddress;
    private static boolean mRequiedFirstName;
    private static boolean mRequiedLastName;
    private static boolean mRequiedEmail;
    private static boolean mRequiedPhone;
    private static boolean mRequiedStreet1;
    private static boolean mRequiedCity;
    private static boolean mRequiedZipCode;
    private static Map<String, ConfigCountry> mListCountry;
    private static Map<String, String> mListCustomerGroup;
    // setting
    private static Map<String, String> mListSetting;

    // order status
    private static Map<String, String> mListOrderStatus;

    // color Swatch
    private static List<ConfigProductOption> mColorSwatch;

    // Active key
    private static boolean mCheckActiveKey;
    private static ActiveKey mActiveKey;
    private static boolean isDevLicense;

    // Print
    private static boolean mOpenCash;
    private static boolean mAutoPrint;

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
    public static void setQuantityFormat(DecimalFormat format) {
        mQuantityFormat = format;
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
     * Trả lại currency symbol
     *
     * @return
     */
    public static String getCurrencySymbol() {
        return getPriceFormat().getDecimalFormatSymbols().getCurrencySymbol();
    }

    /**
     * Trả lại format price
     *
     * @param number
     * @return
     */
    public static String formatPriceProduct(float number) {
        number = convertToPrice(number);
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
        return getQuantityFormat().format(quantity);
    }

    /**
     * Trả lại format số lượng
     *
     * @param quantity
     * @return
     */
    public static String formatQuantity(int quantity) {
        return getQuantityFormat().format(quantity);
    }

    /**
     * Trả lại format số lượng
     *
     * @param quantity
     * @return
     */
    public static String formatQuantity(String quantity) {
        return getQuantityFormat().format(quantity);
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
        String price_format = getPriceFormat().format(number);
        String decima_symbol = getConfigPriceFormat().getDecimalSymbol();
        boolean isDiscount = false;
        if (price_format.contains("-")) {
            isDiscount = true;
        }
        String price_r = StringUtil.removeAllSymbol(price_format);
        String text_f = price_r.substring(0, price_r.length() - getConfigPriceFormat().getPrecision());
        String text_s = price_r.substring(price_r.length() - getConfigPriceFormat().getPrecision(), price_r.length());
        float amount = ConfigUtil.parseFloat(text_f + decima_symbol + text_s);
        String s_amount = ConfigUtil.formatNumber(amount);
        if ((getConfigPriceFormat().getPattern().indexOf(StringUtil.STRING_CURRENCY) == 0)) {
            s_amount = (isDiscount ? "-" : "") + getConfigPriceFormat().getCurrencySymbol() + s_amount;
        } else {
            s_amount = (isDiscount ? "-" : "") + s_amount + " " + getConfigPriceFormat().getCurrencySymbol();
        }
        return s_amount;
    }

    public static String formatDecimalQuantity(float number) {
        String price_format = getPriceFormat().format(number);
        String decima_symbol = getConfigPriceFormat().getDecimalSymbol();
        boolean isDiscount = false;
        if (price_format.contains("-")) {
            isDiscount = true;
        }
        String price_r = StringUtil.removeAllSymbol(price_format);
        String text_f = price_r.substring(0, price_r.length() - getConfigPriceFormat().getPrecision());
        String text_s = price_r.substring(price_r.length() - getConfigPriceFormat().getPrecision(), price_r.length());
        float amount = ConfigUtil.parseFloat(text_f + decima_symbol + text_s);
        String s_amount = (isDiscount ? "-" : "") + ConfigUtil.formatNumber(amount);
        return s_amount;
    }

    private static DecimalFormat getPriceFormat() {
        if (mCurrencyFormat == null) {
            mCurrencyFormat = new DecimalFormat();
        }
//        String sym = mCurrencyFormat.getCurrency().getSymbol();
        return mCurrencyFormat;
    }

    private static DecimalFormat getQuantityFormat() {
        if (mQuantityFormat == null) {
            mQuantityFormat = new DecimalFormat();
        }
//        String sym = mCurrencyFormat.getCurrency().getSymbol();
        return mQuantityFormat;
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

//    private static boolean isPriceEqual(float a, float b) {
//         Math.abs(a - b)
//        getFloatFormat().getMinimumFractionDigits()
//    }

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
        SimpleDateFormat format;
        if (date.contains("-")) {
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        } else {
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        }
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
        SimpleDateFormat format;
        if (date.contains("-")) {
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        } else {
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        }
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
        SimpleDateFormat format;
        if (date.contains("-")) {
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        } else {
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        }
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
        Calendar calendar = Calendar.getInstance();
        TimeZone mTimeZone = calendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String date = df.format(calendar.getTimeInMillis() - mGMTOffset);
        return date;
    }

    public static String getCurrentTimeNow() {
        DateFormat df = new SimpleDateFormat("kk:mm");
        Calendar calendar = Calendar.getInstance();
        String date = df.format(calendar.getTimeInMillis());
        return date;
    }

    public static String getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        TimeZone mTimeZone = calendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String date = df.format(calendar.getTimeInMillis() - mGMTOffset);
        return date;
    }

    public static boolean lessThanSevenDay(String date) {
        if (date == null) {
            return false;
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
            return true;
        }
        long time = dateFormat.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        TimeZone mTimeZone = calendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        long current_time = calendar.getTimeInMillis() - mGMTOffset;
        long seven_day = 7 * 24 * 60 * 60 * 1000;
        long time_rate = current_time - time;
        if (time_rate < seven_day) {
            return true;
        }
        return false;
    }

    /**
     * convert current time to default
     *
     * @param date_time
     * @return
     */
    public static String convertToGMTTime(String date_time) {
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(Time.getCurrentTimezone()));
        Date dateFormat = null;
        try {
            dateFormat = sdf.parse(date_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        TimeZone mTimeZone = calendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String dateTimeString = sdf1.format(dateFormat.getTime() - mGMTOffset);
        return dateTimeString;
    }

    public static long getItemIdInCurrentTime() {
        long time = System.currentTimeMillis() / 1000;
        return time;
    }

    public static void setCurrentCurrency(Currency currentCurrency) {
        ConfigUtil.currentCurrency = currentCurrency;
    }

    public static Currency getCurrentCurrency() {
        return currentCurrency;
    }

    public static float convertToBasePrice(float number) {
        number = number / ((float) currentCurrency.getCurrencyRate());
        return number;
    }

    public static float convertToPrice(float number) {
        number = number * ((float) currentCurrency.getCurrencyRate());
        return number;
    }

    public static boolean isTablet() {
        return mTablet;
    }

    public static void setTablet(boolean mTablet) {
        ConfigUtil.mTablet = mTablet;
    }

    public static void setPlatForm(String mPlatForm) {
        ConfigUtil.mPlatForm = mPlatForm;
    }

    public static String getPlatForm() {
        return mPlatForm;
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

    public static void setEnableGiftCard(boolean enableGiftCard) {
        mEnableGiftCard = enableGiftCard;
    }

    public static void setEnableStoreCredit(boolean enableStoreCredit) {
        mEnableStoreCredit = enableStoreCredit;
    }

    public static void setEnableRewardPoint(boolean enableRewardPoint) {
        mEnableRewardPoint = enableRewardPoint;
    }

    public static void setEnableSession(boolean mEnableSession) {
        ConfigUtil.mEnableSession = mEnableSession;
    }

    public static boolean isEnableGiftCard() {
        return mEnableGiftCard;
    }

    public static boolean isEnableStoreCredit() {
        return mEnableStoreCredit;
    }

    public static boolean isEnableRewardPoint() {
        return mEnableRewardPoint;
    }

    public static boolean isEnableSession() {
        return mEnableSession;
    }

    public static void setConfigPrint(ConfigPrint mConfigPrint) {
        ConfigUtil.mConfigPrint = mConfigPrint;
    }

    public static ConfigPrint getConfigPrint() {
        return mConfigPrint;
    }

    public static void setStarPrintArea(int mStarPrintArea) {
        ConfigUtil.mStarPrintArea = mStarPrintArea;
    }

    public static int getStarPrintArea() {
        return mStarPrintArea;
    }

    public static void setBaseCurrencyCode(String mBaseCurrencyCode) {
        ConfigUtil.mBaseCurrencyCode = mBaseCurrencyCode;
    }

    public static String getBaseCurrencyCode() {
        return mBaseCurrencyCode;
    }

    public static void setListPayment(List<CheckoutPayment> mListPayment) {
        ConfigUtil.mListPayment = mListPayment;
    }

    public static List<CheckoutPayment> getListPayment() {
        return mListPayment;
    }

    public static void setConfigTaxClass(List<ConfigTaxClass> mConfigTaxClass) {
        ConfigUtil.mConfigTaxClass = mConfigTaxClass;
    }

    public static List<ConfigTaxClass> getConfigTaxClass() {
        return mConfigTaxClass;
    }

    public static void setTypePrint(String mTypePrint) {
        ConfigUtil.mTypePrint = mTypePrint;
    }

    public static String getTypePrint() {
        return mTypePrint;
    }

    public static PointOfSales getPointOfSales() {
        return mPointOfSales;
    }

    public static void setPointOfSales(PointOfSales mPointOfSales) {
        ConfigUtil.mPointOfSales = mPointOfSales;
    }

    public static String getPosId() {
        return mPosId;
    }

    public static void setPosId(String mPosId) {
        ConfigUtil.mPosId = mPosId;
    }

    public static String getRegisterShiftId() {
        return mRegisterShiftId;
    }

    public static void setRegisterShiftId(String mRegisterShiftId) {
        ConfigUtil.mRegisterShiftId = mRegisterShiftId;
    }

    public static boolean isCashControl() {
        return mCashControl;
    }

    public static void setCashControl(boolean mCashControl) {
        ConfigUtil.mCashControl = mCashControl;
    }

    public static String getShiftId() {
        return mShiftId;
    }

    public static void setShiftId(String mShiftId) {
        ConfigUtil.mShiftId = mShiftId;
    }

    public static String getLocationId() {
        return mLocationId;
    }

    public static void setLocationId(String mLocationId) {
        ConfigUtil.mLocationId = mLocationId;
    }

    public static void setCheckFirstOpenSession(boolean mCheckFirstOpenSession) {
        ConfigUtil.mCheckFirstOpenSession = mCheckFirstOpenSession;
    }

    public static boolean isCheckFirstOpenSession() {
        return mCheckFirstOpenSession;
    }

    public static void setConfigPriceFormat(ConfigPriceFormat mConfigPriceFormat) {
        ConfigUtil.mConfigPriceFormat = mConfigPriceFormat;
    }

    public static ConfigPriceFormat getConfigPriceFormat() {
        return mConfigPriceFormat;
    }

    // permisson
    public static boolean isChangeStaff() {
        return mChangeStaff;
    }

    public static void setChangeStaff(boolean mChangeStaff) {
        ConfigUtil.mChangeStaff = mChangeStaff;
    }

    public static boolean isCreateOrder() {
        return mCreateOrder;
    }

    public static void setCreateOrder(boolean mCreateOrder) {
        ConfigUtil.mCreateOrder = mCreateOrder;
    }

    public static boolean isManagerAllOrder() {
        return mManageAllOrder;
    }

    public static void setManagerAllOrder(boolean mManageAllOrder) {
        ConfigUtil.mManageAllOrder = mManageAllOrder;
    }

    public static boolean isManageOrderByMe() {
        return mManageOrderByMe;
    }

    public static void setManageOrderByMe(boolean mManageOrderByMe) {
        ConfigUtil.mManageOrderByMe = mManageOrderByMe;
    }

    public static boolean isManageOrderByLocation() {
        return mManageOrderByLocation;
    }

    public static void setManageOrderByLocation(boolean mManageOrderByLocation) {
        ConfigUtil.mManageOrderByLocation = mManageOrderByLocation;
    }

    public static boolean isManageOrderOtherStaff() {
        return mManageOrderOtherStaff;
    }

    public static void setManageOrderOtherStaff(boolean mManageOrderOtherStaff) {
        ConfigUtil.mManageOrderOtherStaff = mManageOrderOtherStaff;
    }

    public static boolean isCanUseRefund() {
        return mCanUseRefund;
    }

    public static void setCanUseRefund(boolean mCanUseRefund) {
        ConfigUtil.mCanUseRefund = mCanUseRefund;
    }

    public static boolean isSendEmail() {
        return mSendEmail;
    }

    public static void setSendEmail(boolean mSendEmail) {
        ConfigUtil.mSendEmail = mSendEmail;
    }

    public static boolean isShip() {
        return mShip;
    }

    public static void setShip(boolean mShip) {
        ConfigUtil.mShip = mShip;
    }

    public static boolean isCancel() {
        return mCancel;
    }

    public static void setCancel(boolean mCancel) {
        ConfigUtil.mCancel = mCancel;
    }

    public static boolean isAddComment() {
        return mAddComment;
    }

    public static void setAddComment(boolean mAddComment) {
        ConfigUtil.mAddComment = mAddComment;
    }

    public static boolean isReOder() {
        return mReOder;
    }

    public static void setReOder(boolean mReOder) {
        ConfigUtil.mReOder = mReOder;
    }

    public static boolean isPartialInvoice() {
        return mPartialInvoice;
    }

    public static void setPartialInvoice(boolean mPartialInvoice) {
        ConfigUtil.mPartialInvoice = mPartialInvoice;
    }

    public static boolean isCustomSales() {
        return mCustomSales;
    }

    public static void setCustomSales(boolean mCustomSales) {
        ConfigUtil.mCustomSales = mCustomSales;
    }

    public static boolean isManageAllDiscount() {
        return mManageAllDiscount;
    }

    public static void setManageAllDiscount(boolean mManageAllDiscount) {
        ConfigUtil.mManageAllDiscount = mManageAllDiscount;
    }

    public static boolean isDiscountPerCart() {
        return mDiscountPerCart;
    }

    public static void setDiscountPerCart(boolean mDiscountPerCart) {
        ConfigUtil.mDiscountPerCart = mDiscountPerCart;
    }

    public static boolean isApplyCoupon() {
        return mApplyCoupon;
    }

    public static void setApplyCoupon(boolean mApplyCoupon) {
        ConfigUtil.mApplyCoupon = mApplyCoupon;
    }

    public static boolean isDiscountPerItem() {
        return mDiscountPerItem;
    }

    public static void setDiscountPerItem(boolean mDiscountPerItem) {
        ConfigUtil.mDiscountPerItem = mDiscountPerItem;
    }

    public static boolean isApplyCustomPrice() {
        return mApplyCustomPrice;
    }

    public static void setApplyCustomPrice(boolean mApplyCustomPrice) {
        ConfigUtil.mApplyCustomPrice = mApplyCustomPrice;
    }

    public static String getDiscountProductId() {
        return mDiscountProductId;
    }

    public static void setDiscountProductId(String mDiscountProductId) {
        ConfigUtil.mDiscountProductId = mDiscountProductId;
    }

    public static boolean isManagerShiftAdjustment() {
        return mManageShiftAdjustment;
    }

    public static void setManagerShiftAdjustment(boolean mManageShiftAdjustment) {
        ConfigUtil.mManageShiftAdjustment = mManageShiftAdjustment;
    }

    public static boolean isShiftOpenNote() {
        return mShiftOpenNote;
    }

    public static void setShiftOpenNote(boolean mShiftOpenNote) {
        ConfigUtil.mShiftOpenNote = mShiftOpenNote;
    }

    public static boolean isEnableOpenFloatAmount() {
        return mEnableOpenFloatAmount;
    }

    public static void setEnableOpenFloatAmount(boolean mEnableOpenFloatAmount) {
        ConfigUtil.mEnableOpenFloatAmount = mEnableOpenFloatAmount;
    }

    public static boolean isShiftCloseNote() {
        return mShiftCloseNote;
    }

    public static void setShiftCloseNote(boolean mShiftCloseNote) {
        ConfigUtil.mShiftCloseNote = mShiftCloseNote;
    }

    public static boolean isEnableCloseAmount() {
        return mEnableCloseAmount;
    }

    public static void setEnableCloseAmount(boolean mEnableCloseAmount) {
        ConfigUtil.mEnableCloseAmount = mEnableCloseAmount;
    }

    public static boolean isCancelCloseSession() {
        return mCancelCloseSession;
    }

    public static void setCancelCloseSession(boolean mCancelCloseSession) {
        ConfigUtil.mCancelCloseSession = mCancelCloseSession;
    }

    public static boolean isPrintSession() {
        return mPrintSession;
    }

    public static void setPrintSession(boolean mPrintSession) {
        ConfigUtil.mPrintSession = mPrintSession;
    }

    public static boolean isNeedToShip() {
        return mNeedToShip;
    }

    public static void setNeedToShip(boolean mNeedToShip) {
        ConfigUtil.mNeedToShip = mNeedToShip;
    }

    public static boolean isMarkAsShip() {
        return mMarkAsShip;
    }

    public static void setMarkAsShip(boolean mMarkAsShip) {
        ConfigUtil.mMarkAsShip = mMarkAsShip;
    }

    public static boolean isAddAddressDefault() {
        return mAddAddressDefault;
    }

    public static void setAddAddressDefault(boolean mAddAddressDefault) {
        ConfigUtil.mAddAddressDefault = mAddAddressDefault;
    }

    public static boolean isShippingAddress() {
        return mShippingAddress;
    }

    public static void setShippingAddress(boolean mShippingAddress) {
        ConfigUtil.mShippingAddress = mShippingAddress;
    }

    public static boolean isAddAddress() {
        return mAddAddress;
    }

    public static void setAddAddress(boolean mAddAddress) {
        ConfigUtil.mAddAddress = mAddAddress;
    }

    public static boolean isLastName() {
        return mLastName;
    }

    public static void setLastName(boolean mLastName) {
        ConfigUtil.mLastName = mLastName;
    }

    public static boolean isCompany() {
        return mCompany;
    }

    public static void setCompany(boolean mCompany) {
        ConfigUtil.mCompany = mCompany;
    }

    public static boolean isSubscribe() {
        return mSubscribe;
    }

    public static void setSubscribe(boolean mSubscribe) {
        ConfigUtil.mSubscribe = mSubscribe;
    }

    public static boolean isEditState() {
        return mEditState;
    }

    public static void setEditState(boolean mEditState) {
        ConfigUtil.mEditState = mEditState;
    }

    public static boolean isSameAddress() {
        return mSameAddress;
    }

    public static void setSameAddress(boolean mSameAddress) {
        ConfigUtil.mSameAddress = mSameAddress;
    }

    public static boolean isRequiedFirstName() {
        return mRequiedFirstName;
    }

    public static void setRequiedFirstName(boolean mRequiedFirstName) {
        ConfigUtil.mRequiedFirstName = mRequiedFirstName;
    }

    public static boolean isRequiedLastName() {
        return mRequiedLastName;
    }

    public static void setRequiedLastName(boolean mRequiedLastName) {
        ConfigUtil.mRequiedLastName = mRequiedLastName;
    }

    public static boolean isRequiedEmail() {
        return mRequiedEmail;
    }

    public static void setRequiedEmail(boolean mRequiedEmail) {
        ConfigUtil.mRequiedEmail = mRequiedEmail;
    }

    public static boolean isRequiedPhone() {
        return mRequiedPhone;
    }

    public static void setRequiedPhone(boolean mRequiedPhone) {
        ConfigUtil.mRequiedPhone = mRequiedPhone;
    }

    public static boolean isRequiedStreet1() {
        return mRequiedStreet1;
    }

    public static void setRequiedStreet1(boolean mRequiedStreet1) {
        ConfigUtil.mRequiedStreet1 = mRequiedStreet1;
    }

    public static boolean isRequiedCity() {
        return mRequiedCity;
    }

    public static void setRequiedCity(boolean mRequiedCity) {
        ConfigUtil.mRequiedCity = mRequiedCity;
    }

    public static boolean isRequiedZipCode() {
        return mRequiedZipCode;
    }

    public static void setRequiedZipCode(boolean mRequiedZipCode) {
        ConfigUtil.mRequiedZipCode = mRequiedZipCode;
    }

    public static Map<String, ConfigCountry> getListCountry() {
        return mListCountry;
    }

    public static void setListCountry(Map<String, ConfigCountry> mListCountry) {
        ConfigUtil.mListCountry = mListCountry;
    }

    public static Map<String, String> getListCustomerGroup() {
        return mListCustomerGroup;
    }

    public static void setListCustomerGroup(Map<String, String> mListCustomerGroup) {
        ConfigUtil.mListCustomerGroup = mListCustomerGroup;
    }

    public static Map<String, String> getListSetting() {
        return mListSetting;
    }

    public static void setListSetting(Map<String, String> mListSetting) {
        ConfigUtil.mListSetting = mListSetting;
    }

    public static void setListOrderStatus(Map<String, String> mListOrderStatus) {
        ConfigUtil.mListOrderStatus = mListOrderStatus;
    }

    public static Map<String, String> getListOrderStatus() {
        return mListOrderStatus;
    }

    public static void setColorSwatch(List<ConfigProductOption> mColorSwatch) {
        ConfigUtil.mColorSwatch = mColorSwatch;
    }

    public static List<ConfigProductOption> getColorSwatch() {
        return mColorSwatch;
    }

    public static void setCheckActiveKey(boolean mCheckActiveKey) {
        ConfigUtil.mCheckActiveKey = mCheckActiveKey;
    }

    public static boolean isCheckActiveKey() {
        return mCheckActiveKey;
    }

    public static void setActiveKey(ActiveKey mActiveKey) {
        ConfigUtil.mActiveKey = mActiveKey;
    }

    public static ActiveKey getActiveKey() {
        return mActiveKey;
    }

    public static boolean isDevLicense() {
        return isDevLicense;
    }

    public static void setIsDevLicense(boolean isDevLicense) {
        ConfigUtil.isDevLicense = isDevLicense;
    }

    public static void setEnableDeleteOrder(boolean mEnableDeleteOrder) {
        ConfigUtil.mEnableDeleteOrder = mEnableDeleteOrder;
    }

    public static boolean isEnableDeleteOrder() {
        return mEnableDeleteOrder;
    }

    public static String getValueColorSwatch(String parent_code, String value_id) {
        for (ConfigProductOption productOption : mColorSwatch) {
            if (parent_code.equals(productOption.getAttributeCode())) {
                ConfigOptionSwatch optionSwatch = productOption.getColorSwatch().get(value_id);
                return optionSwatch.getValue();
            }
        }
        return null;
    }

    public static boolean isOpenCash() {
        return mOpenCash;
    }

    public static void setOpenCash(boolean mOpenCash) {
        ConfigUtil.mOpenCash = mOpenCash;
    }

    public static boolean isAutoPrint() {
        return mAutoPrint;
    }

    public static void setAutoPrint(boolean mAutoPrint) {
        ConfigUtil.mAutoPrint = mAutoPrint;
    }
}

