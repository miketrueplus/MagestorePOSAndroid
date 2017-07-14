package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomValue;

import java.util.List;

/**
 * Created by folio on 3/3/2017.
 */

public interface ProductOptionCustom extends Model {
    // option type
    public static final String OPTION_TYPE_CUSTOM = "custom";
    public static final String OPTION_TYPE_CONFIG = "config";
    public static final String OPTION_TYPE_BUNDLE = "bundle";
    public static final String OPTION_TYPE_GROUPED = "grouped";

    // type
    public static final String TYPE_MULTIPE = "multipe";
    public static final String TYPE_MULTI = "multi";
    public static final String TYPE_DROP_DOWN = "drop_down";
    public static final String TYPE_RADIO = "radio";
    public static final String TYPE_FIELD = "field";
    public static final String TYPE_CHECKBOX = "checkbox";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_TIME = "time";
    public static final String TYPE_DATETIME = "date_time";
    public static final String TYPE_COLOR = "color";
    public static final String TYPE_SIZE = "size";
    public static final String TYPE_GROUP = "group";

    // giá type
    public static final String PRICE_TYPE_PERCENT = "percent";
    public static final String PRICE_TYPE_FIXED = "fixed";


    void setID(String id);

    String getOptionID();

    String getOptionCode();

    String getProductID();

    String getType();

    boolean isRequired();

    String getSku();

    int getMaxCharacters();

    String getFileExtension();

    String getImageSizeX();

    String getImageSizeY();

    String getSortOrder();

    String getDefaultTitle();

    String getStoreTitle();

    String getTitle();

    String getDefaultPrice();

    String getDefaultPriceType();

    String getStorePrice();

    String getStorePriceType();

    String getPrice();

    String getPriceType();

    boolean isPriceTypeFixed();

    boolean isPriceTypePercent();

    List<PosProductOptionCustomValue> getOptionValueList();

    boolean isTypeSelectOne();

    boolean isTypeSelectMultipe();

//    boolean isTypeChooseQuantity();

    boolean isTypeTime();

    boolean isTypeDate();

    boolean isTypeDateTime();

    void setOptionID(String option_id);

    void setOptionCode(String option_code);

    void setProductID(String product_id);

    void setOptionValueList(List<PosProductOptionCustomValue> values);

    void setType(String type);

    void setRequire(boolean isRequire);

    void setDefaultTitle(String default_title);

    void setStoreTitle(String store_title);

    void setTitle(String title);

    boolean isCustomOption();

    boolean isConfigOption();

    boolean isBundleOption();

    void setOptionType(String option_type);
}
