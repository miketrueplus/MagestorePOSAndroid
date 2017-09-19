package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosListCustomerPaserModelOdoo extends Gson2PosAbstractParseImplement {
    @Override
    protected Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<List<PosCustomer>>() {
        }
                .getType(), new CustomerConverter());
        return builder.create();
    }

    private String CUSTOMER_ID = "id";
    private String CUSTOMER_CREATE_AT = "create_date";
    private String CUSTOMER_EMAIL = "email";
    private String CUSTOMER_NAME = "name";
    private String CUSTOMER_PHONE = "phone";
    private String CUSTOMER_STATE_ID = "state_id";
    private String CUSTOMER_STATE_NAME = "state_name";
    private String CUSTOMER_STATE_CODE = "state_code";
    private String CUSTOMER_COMPANY = "company_name";
    private String CUSTOMER_COMPANY_ID = "company_id";
    private String CUSTOMER_COMPANY_TYPE = "company_type";
    private String CUSTOMER_POSCODE = "zip";
    private String CUSTOMER_COUNTRY_ID = "country_id";
    private String CUSTOMER_STREET = "street";
    private String CUSTOMER_STREET2 = "street2";
    private String CUSTOMER_VAT = "vat";
    private String CUSTOMER_CITY = "city";

    public class CustomerConverter implements JsonDeserializer<List<PosCustomer>> {

        @Override
        public List<PosCustomer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<PosCustomer> listCustomer = new ArrayList<>();
            if (json.isJsonArray()) {
                JsonArray arr_customer = json.getAsJsonArray();
                if (arr_customer != null && arr_customer.size() > 0) {
                    for (JsonElement el_customer : arr_customer) {
                        List<String> listStreet = new ArrayList<>();
                        JsonObject obj_customer = el_customer.getAsJsonObject();
                        PosCustomer customer = new PosCustomer();
                        String id = obj_customer.remove(CUSTOMER_ID).getAsString();
                        customer.setID(id);
                        String create_at = obj_customer.remove(CUSTOMER_CREATE_AT).getAsString();
                        customer.setCreateAt(create_at);
                        String customer_email = obj_customer.remove(CUSTOMER_EMAIL).getAsString();
                        String customer_name = obj_customer.remove(CUSTOMER_NAME).getAsString();
                        String customer_phone = obj_customer.remove(CUSTOMER_PHONE).getAsString();
                        String state_id = "";
                        if (obj_customer.has(CUSTOMER_STATE_ID)) {
                            state_id = obj_customer.remove(CUSTOMER_STATE_ID).getAsString();
                        }
                        String state_name = "";
                        if (obj_customer.has(CUSTOMER_STATE_NAME)) {
                            state_name = obj_customer.remove(CUSTOMER_STATE_NAME).getAsString();
                        }
                        String state_code = "";
                        if (obj_customer.has(CUSTOMER_STATE_CODE)) {
                            state_code = obj_customer.remove(CUSTOMER_STATE_CODE).getAsString();
                        }
                        String company_name = obj_customer.remove(CUSTOMER_COMPANY).getAsString();
                        String company_type = obj_customer.remove(CUSTOMER_COMPANY_TYPE).getAsString();
                        String company_id = obj_customer.remove(CUSTOMER_COMPANY_ID).getAsString();
                        String poscode = obj_customer.remove(CUSTOMER_POSCODE).getAsString();
                        String country_id = obj_customer.remove(CUSTOMER_COUNTRY_ID).getAsString();
                        String street = "";
                        if (obj_customer.has(CUSTOMER_STREET)) {
                            street = obj_customer.remove(CUSTOMER_STREET).getAsString();
                        }
                        String street2 = "";
                        if (obj_customer.has(CUSTOMER_STREET2)) {
                            street2 = obj_customer.remove(CUSTOMER_STREET2).getAsString();
                        }
                        String vat = "";
                        if (obj_customer.has(CUSTOMER_VAT)) {
                            vat = obj_customer.remove(CUSTOMER_VAT).getAsString();
                        }
                        String city = "";
                        if (obj_customer.has(CUSTOMER_CITY)) {
                            city = obj_customer.remove(CUSTOMER_CITY).getAsString();
                        }
                        if (StringUtil.checkJsonData(street)) {
                            listStreet.add(street);
                        }
                        if (StringUtil.checkJsonData(street2)) {
                            listStreet.add(street2);
                        }
                        customer.setEmail(StringUtil.checkJsonData(customer_email) ? customer_email : "");
                        customer.setFirstName(StringUtil.checkJsonData(customer_name) ? customer_name : "");
                        customer.setLastName("");
                        customer.setTelephone(StringUtil.checkJsonData(customer_phone) ? customer_phone : "");
                        customer.setGroupID(getCustomerGroupId(company_type));
                        PosCustomerAddress customerAddress = new PosCustomerAddress();
                        customerAddress.setCustomer(id);
                        customerAddress.setFirstName(StringUtil.checkJsonData(customer_name) ? customer_name : "");
                        customerAddress.setLastName("");
                        customerAddress.setRegionID(StringUtil.checkJsonData(state_id) ? state_id : "");
                        String country = StringUtil.checkJsonData(country_id) ? country_id : "";
                        customerAddress.setCountry(getCountryCode(country));
                        customerAddress.setStreet1(StringUtil.checkJsonData(street) ? street : "");
                        customerAddress.setStreet2(StringUtil.checkJsonData(street2) ? street2 : "");
                        customerAddress.setTelephone(StringUtil.checkJsonData(customer_phone) ? customer_phone : "");
                        customerAddress.setPostCode(StringUtil.checkJsonData(poscode) ? poscode : "");
                        customerAddress.setCity(StringUtil.checkJsonData(city) ? city : "");
                        customerAddress.setVAT(StringUtil.checkJsonData(vat) ? vat : "");
                        customerAddress.setCompany(StringUtil.checkJsonData(company_name) ? company_name : "");
                        customerAddress.setDefaultBilling("1");
                        PosRegion region = new PosRegion();
                        int stateId;
                        try {
                            stateId = Integer.parseInt(state_id);
                        } catch (Exception e) {
                            stateId = 0;
                        }
                        region.setRegionID(stateId);
                        region.setRegionName(state_name);
                        region.setRegionCode(state_code);
                        customerAddress.setRegion(region);
                        List<CustomerAddress> listCustomerAddress = new ArrayList<>();
                        listCustomerAddress.add(customerAddress);
                        customer.setAddressList(listCustomerAddress);
                        listCustomer.add(customer);
                    }
                }
            }
            return listCustomer;
        }
    }

    private String getCustomerGroupId(String code) {
        for (String id : ConfigUtil.getListCustomerGroup().keySet()) {
            String value = ConfigUtil.getListCustomerGroup().get(id);
            if (value.equals(code)) {
                return id;
            }
        }
        return "";
    }

    private String getCountryCode(String id) {
        for (ConfigCountry country : ConfigUtil.getListCountry().values()) {
            if (country.getKey().equals(id)) {
                return country.getCountryID();
            }
        }
        return "";
    }
}


