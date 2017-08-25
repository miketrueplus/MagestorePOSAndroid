package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.pos.model.magento.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.magento.catalog.PosProductOptionJsonConfigOptionPrice;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mike on 2/16/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class Gson2PosProductOptionParseImplement extends Gson2PosAbstractParseImplement  {
    /**
     * Bổ sung MapKeySerialization cho Gson
     * @return
     */
    @Override
    public Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
//        builder.registerTypeAdapter(new TypeToken<HashMap<String, PosProductOptionConfigOption>>(){}
//                .getType(), new HashMapConfigOptionConverter());
        builder.registerTypeAdapter(new TypeToken<PosProductOptionConfigOption>(){}
                .getType(), new ConfigOptionConverter());
        builder.registerTypeAdapter(new TypeToken<PosProductOptionJsonConfigOptionPrice>(){}
                .getType(), new ProductConfigOptionPriceConverter());
        return builder.create();
    }

    /**
     * Map từ điển của config_options sang
     * PosProduct.ConfigOption
     * Đoạn json mẫu
     * {"optionId":"90","optionLabel":"Color","53":"Green","58":"Red","59":"White"}
     */
//    public class HashMapConfigOptionConverter implements JsonDeserializer<HashMap<String, PosProductOptionConfigOption>> {
//        public HashMap<String, PosProductOptionConfigOption> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
//            Set<Map.Entry<String, JsonElement>> map = json.getAsJsonObject().entrySet();
//            HashMap<String, PosProductOptionConfigOption> returnMap = new HashMap<String, PosProductOptionConfigOption>();
//            for (Map.Entry<String, JsonElement> element: map)
//                returnMap.put(element.getKey(), (PosProductOptionConfigOption) ctx.deserialize(element.getValue(), PosProductOptionConfigOption.class));
//            return returnMap;
//        }
//    }

    /**
     * Map từ điển của config_options sang
     * PosProduct.ConfigOption
     * Đoạn json mẫu
     * {"optionId":"90","optionLabel":"Color","53":"Green","58":"Red","59":"White"}
     */
    private static final String JSON_OPTION_ID = "optionId";
    private static final String JSON_OPTION_LABEL = "optionLabel";
    public class ConfigOptionConverter implements JsonDeserializer<PosProductOptionConfigOption> {
        public PosProductOptionConfigOption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
            // khởi tạo product config
            PosProductOptionConfigOption configOption = new PosProductOptionConfigOption();
            //
//            // đặt option Id và label
            configOption.optionId = json.getAsJsonObject().remove(JSON_OPTION_ID).getAsString();
            configOption.optionLabel = json.getAsJsonObject().remove(JSON_OPTION_LABEL).getAsString();

            // gán cho option Values
            configOption.optionValues = new HashMap<String, String>();
            Set<Map.Entry<String, JsonElement>> entry = json.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> item: json.getAsJsonObject().entrySet())
                configOption.optionValues.put(item.getKey(), item.getValue().getAsString());
            return configOption;
        }
    }

    /**
     * Convert đoạn json
     * {"oldPrice":{"amount":"52"},"basePrice":{"amount":"48.036950501155"},"finalPrice":{"amount":"52"}}
     * Sang PosProductConfigOptionPrice
     */
    private static final String PRICE_OLD = "oldPrice";
    private static final String PRICE_BASE = "basePrice";
    private static final String PRICE_FINAL = "finalPrice";
    private static final String PRICE_AMOUNT = "amount";
    public class ProductConfigOptionPriceConverter implements JsonDeserializer<PosProductOptionJsonConfigOptionPrice> {
        public PosProductOptionJsonConfigOptionPrice deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
            PosProductOptionJsonConfigOptionPrice optionPrice =  new PosProductOptionJsonConfigOptionPrice();
            if (json.getAsJsonObject().get(PRICE_BASE).getAsJsonObject().has(PRICE_AMOUNT)) {
                String price = json.getAsJsonObject().get(PRICE_BASE).getAsJsonObject().get(PRICE_AMOUNT).getAsString();
                optionPrice.setBasePrice(price == null || price.trim().equals("") ? 0 : Float.parseFloat(price));
            }
            if (json.getAsJsonObject().get(PRICE_FINAL).getAsJsonObject().has(PRICE_AMOUNT)) {
                String price = json.getAsJsonObject().get(PRICE_FINAL).getAsJsonObject().get(PRICE_AMOUNT).getAsString();
                optionPrice.setFinalPrice(price == null || price.trim().equals("") ? 0 : Float.parseFloat(price));
            }
            if (json.getAsJsonObject().get(PRICE_OLD).getAsJsonObject().has(PRICE_AMOUNT)) {
                String price = json.getAsJsonObject().get(PRICE_OLD).getAsJsonObject().get(PRICE_AMOUNT).getAsString();
                optionPrice.setOldPrice(price == null || price.trim().equals("") ? 0 : Float.parseFloat(price));
            }
            return optionPrice;
        }
    }
}
