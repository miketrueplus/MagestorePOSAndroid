package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.pos.model.config.PosConfigCountry;
import com.magestore.app.pos.model.config.PosConfigOdoo;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.config.PosConfigQuantityFormat;
import com.magestore.app.pos.model.config.PosConfigRegion;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.staff.PosStaff;
import com.magestore.app.util.StringUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 9/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosConfigParseModelOdoo extends Gson2PosAbstractParseImplement {
    @Override
    protected Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<List<PosConfigOdoo>>() {
        }
                .getType(), new ConfigConverter());
        return builder.create();
    }

    private String COUNTRY = "country";
    private String COUNTRY_ID = "id";
    private String COUNTRY_NAME = "name";
    private String COUNTRY_CODE = "code";
    private String STATE = "state";
    private String STATE_ID = "id";
    private String STATE_NAME = "name";
    private String STATE_CODE = "code";
    private String CUSTOMER_GROUP = "customer_group";
    private String CUSTOMER_GROUP_ID = "id";
    private String CUSTOMER_GROUP_CODE = "code";
    private String PRICE_FORMAT = "price_format";
    private String POSITION = "position";
    private String GROUP_SYMBOL = "thousands_sep";
    private String PRECISION = "precision";
    private String GROUP_LENGTH = "grouping";
    private String DECIMAL_SYMBOL = "decimal_point";
    private String COMPANY_CURRENCY = "company_currency";
    private String RATE = "rate";
    private String NAME = "name";
    private String CURRENCY_ID = "id";
    private String SYMBOL = "symbol";
    private String STAFF = "staff";
    private String STAFF_ID = "staff_id";
    private String STAFF_NAME = "staff_name";
    private String GUEST_CUSTOMER = "guest_customer";
    private String GUEST_NAME = "guest_checkout_name";
    private String GUEST_ID = "guest_checkout_id";
    private String ACTIVE_KEY = "active_key";

    public class ConfigConverter implements JsonDeserializer<List<PosConfigOdoo>> {
        @Override
        public List<PosConfigOdoo> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<PosConfigOdoo> listConfig = new ArrayList<>();
            if (json.isJsonArray()) {
                JsonArray arr_config = json.getAsJsonArray();
                if (arr_config != null && arr_config.size() > 0) {
                    Map<String, ConfigCountry> mConfigContry = new LinkedTreeMap<>();
                    Map<String, String> mCustomerGroup = new LinkedTreeMap<>();
                    for (JsonElement el_config : arr_config) {
                        JsonObject obj_config = el_config.getAsJsonObject();
                        PosConfigOdoo configOdoo = new PosConfigOdoo();

                        if (obj_config.has(ACTIVE_KEY)) {
                            String active_key = obj_config.get(ACTIVE_KEY).getAsString();
                            configOdoo.setActiveKey(StringUtil.checkJsonData(active_key) ? active_key : "");
                        }

                        if (obj_config.get(COUNTRY).isJsonArray()) {
                            JsonArray arr_country = obj_config.getAsJsonArray(COUNTRY);
                            if (arr_country != null && arr_country.size() > 0) {
                                for (JsonElement el_country : arr_country) {
                                    JsonObject obj_country = el_country.getAsJsonObject();
                                    PosConfigCountry configCountry = new PosConfigCountry();
                                    String country_id = obj_country.remove(COUNTRY_ID).getAsString();
                                    String country_name = obj_country.remove(COUNTRY_NAME).getAsString();
                                    String country_code = obj_country.remove(COUNTRY_CODE).getAsString();
                                    configCountry.setCountryID(country_code);
                                    configCountry.setKey(country_id);
                                    configCountry.setCountryName(country_name);
                                    if (obj_country.has(STATE)) {
                                        JsonArray arr_state = obj_country.getAsJsonArray(STATE);
                                        if (arr_state != null && arr_state.size() > 0) {
                                            Map<String, ConfigRegion> mConfigRegion = new LinkedTreeMap<>();
                                            for (JsonElement el_state : arr_state) {
                                                JsonObject obj_state = el_state.getAsJsonObject();
                                                PosConfigRegion configRegion = new PosConfigRegion();
                                                String state_id = obj_state.remove(STATE_ID).getAsString();
                                                String state_code = obj_state.remove(STATE_CODE).getAsString();
                                                String state_name = obj_state.remove(STATE_NAME).getAsString();
                                                configRegion.setID(state_id);
                                                configRegion.setCode(state_code);
                                                configRegion.setName(state_name);
                                                mConfigRegion.put(state_id, configRegion);
                                            }
                                            configCountry.setRegions(mConfigRegion);
                                        }
                                    }
                                    mConfigContry.put(country_code, configCountry);
                                }
                            }
                        }
                        configOdoo.setCountry(mConfigContry);

                        if (obj_config.get(CUSTOMER_GROUP).isJsonArray()) {
                            JsonArray arr_customerg = obj_config.getAsJsonArray(CUSTOMER_GROUP);
                            if (arr_customerg != null && arr_customerg.size() > 0) {
                                for (JsonElement el_customerg : arr_customerg) {
                                    JsonObject obj_customerg = el_customerg.getAsJsonObject();
                                    String id = obj_customerg.remove(CUSTOMER_GROUP_ID).getAsString();
                                    String code = obj_customerg.remove(CUSTOMER_GROUP_CODE).getAsString();
                                    mCustomerGroup.put(id, code);
                                }
                            }
                        }
                        configOdoo.setCustomerGroup(mCustomerGroup);

                        String currencySymbol = "$";
                        if (obj_config.has(COMPANY_CURRENCY) && obj_config.get(COMPANY_CURRENCY).isJsonObject()) {
                            JsonObject obj_currency = obj_config.getAsJsonObject(COMPANY_CURRENCY);
                            PosCurrency currency = new PosCurrency();
                            String id = obj_currency.get(CURRENCY_ID).getAsString();
                            float rate = obj_currency.get(RATE).getAsFloat();
                            String code = obj_currency.get(NAME).getAsString();
                            String symbol = obj_currency.get(SYMBOL).getAsString();
                            currency.setCurrencyRate(rate);
                            currency.setCode(code);
                            currency.setCurrenyName(code);
                            currency.setCurrencySymbol(symbol);
                            currency.setIsDefault("1");
                            currency.setID(id);
                            currencySymbol = symbol;
                            configOdoo.setDefaultCurrency(currency);
                        }

                        if (obj_config.has(PRICE_FORMAT) && obj_config.get(PRICE_FORMAT).isJsonObject()) {
                            JsonObject obj_price = obj_config.getAsJsonObject(PRICE_FORMAT);
                            PosConfigPriceFormat priceFormat = new PosConfigPriceFormat();
                            PosConfigQuantityFormat quantityFormat = new PosConfigQuantityFormat();
                            String position = obj_price.get(POSITION).getAsString();
                            String group_symbol = obj_price.get(GROUP_SYMBOL).getAsString();
                            int precision = obj_price.get(PRECISION).getAsInt();
                            int group_length = obj_price.get(GROUP_LENGTH).getAsInt();
                            String decimal_symbol = obj_price.get(DECIMAL_SYMBOL).getAsString();
                            int integerRequiredPrice = 1;
                            int integerRequiredQuantity = 0;

                            String currency_symbol = "";
                            if (currencySymbol.length() > 0) {
                                String sSymbol = currencySymbol.substring(0, 1);
                                if (sSymbol.equals("u")) {
                                    currency_symbol = StringEscapeUtils.unescapeJava("\\" + currencySymbol);
                                } else if (sSymbol.equals("\\")) {
                                    currency_symbol = StringEscapeUtils.unescapeJava(currencySymbol);
                                } else if (currencySymbol.contains("\\u")) {
                                    currency_symbol = StringEscapeUtils.unescapeJava(currencySymbol);
                                } else {
                                    currency_symbol = StringEscapeUtils.unescapeJava(currencySymbol);
                                }
                            } else {
                                currency_symbol = currencySymbol;
                            }
                            priceFormat.setCurrencySymbol(currency_symbol);
                            priceFormat.setPattern(position.equals("before") ? "$%s" : "%s$");
                            priceFormat.setGroupSymbol(group_symbol);
                            priceFormat.setPrecision(precision);
                            priceFormat.setRequirePrecision(precision);
                            priceFormat.setGroupLength(group_length);
                            priceFormat.setDecimalSymbol(decimal_symbol);
                            priceFormat.setIntegerRequied(integerRequiredPrice);

                            quantityFormat.setPattern(position.equals("before") ? "$%s" : "%s$");
                            quantityFormat.setGroupSymbol(group_symbol);
                            quantityFormat.setPrecision(precision);
                            quantityFormat.setRequirePrecision(precision);
                            quantityFormat.setGroupLength(group_length);
                            quantityFormat.setDecimalSymbol(decimal_symbol);
                            quantityFormat.setIntegerRequied(integerRequiredQuantity);

                            configOdoo.setPriceFormat(priceFormat);
                            configOdoo.setQuantityFormat(quantityFormat);
                        }

                        PosStaff staff = new PosStaff();
                        if (obj_config.has(STAFF) && obj_config.get(STAFF).isJsonObject()) {
                            JsonObject obj_staff = obj_config.get(STAFF).getAsJsonObject();
                            String staff_id = obj_staff.get(STAFF_ID).getAsString();
                            String staff_name = obj_staff.get(STAFF_NAME).getAsString();
                            staff.setStaffId(staff_id);
                            staff.setStaffName(staff_name);
                        }
                        configOdoo.setStaff(staff);

                        PosCustomer customer = new PosCustomer();
                        if (obj_config.has(GUEST_CUSTOMER) && obj_config.get(GUEST_CUSTOMER).isJsonObject()) {
                            JsonObject obj_guest = obj_config.get(GUEST_CUSTOMER).getAsJsonObject();
                            String guest_id = obj_guest.get(GUEST_ID).getAsString();
                            String guest_name = obj_guest.get(GUEST_NAME).getAsString();
                            customer.setID(guest_id);
                            customer.setFirstName(guest_name);
                            customer.setLastName("");
                        }
                        configOdoo.setGuestCustomer(customer);

                        listConfig.add(configOdoo);
                    }
                }
            }
            return listConfig;
        }
    }
}
