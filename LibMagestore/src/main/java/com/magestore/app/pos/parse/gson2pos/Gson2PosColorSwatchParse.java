package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.model.config.ConfigOptionSwatch;
import com.magestore.app.pos.model.magento.config.PosConfigOptionSwatch;
import com.magestore.app.pos.model.magento.config.PosConfigProductOption;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 7/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosColorSwatchParse extends Gson2PosAbstractParseImplement {

    @Override
    public Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<List<PosConfigProductOption>>(){}
                .getType(), new ColorSwatchConverter());
        return builder.create();
    }

    private static final String JSON_ATTRIBUTE_ID = "attribute_id";
    private static final String JSON_ATTRIBUTE_CODE = "attribute_code";
    private static final String JSON_ATTRIBUTE_LABEL = "attribute_label";
    private static final String JSON_SWATCHES = "swatches";
    private static final String JSON_SWATCH_ID = "swatch_id";
    private static final String JSON_OPTION_ID = "option_id";
    private static final String JSON_STORE_ID = "store_id";
    private static final String JSON_TYPE = "type";
    private static final String JSON_VALUE = "value";
    public class ColorSwatchConverter implements JsonDeserializer<List<PosConfigProductOption>> {
        @Override
        public List<PosConfigProductOption> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<PosConfigProductOption> listColorSwatch = new ArrayList<>();
            for (JsonElement item : json.getAsJsonArray()) {
                PosConfigProductOption configProductOption = new PosConfigProductOption();
                configProductOption.setAttributeId(item.getAsJsonObject().remove(JSON_ATTRIBUTE_ID).getAsString());
                configProductOption.setAttributeCode(item.getAsJsonObject().remove(JSON_ATTRIBUTE_CODE).getAsString());
                configProductOption.setAttributeLabel(item.getAsJsonObject().remove(JSON_ATTRIBUTE_LABEL).getAsString());
                Map<String, ConfigOptionSwatch> mapConfigOptionSwatch = new HashMap<>();
                JsonElement swatches = item.getAsJsonObject().get(JSON_SWATCHES);
                for (Map.Entry<String, JsonElement> entry: swatches.getAsJsonObject().entrySet()){
                    ConfigOptionSwatch configOptionSwatch = new PosConfigOptionSwatch();
                    configOptionSwatch.setSwatchId(entry.getValue().getAsJsonObject().remove(JSON_SWATCH_ID).getAsString());
                    configOptionSwatch.setOptionId(entry.getValue().getAsJsonObject().remove(JSON_OPTION_ID).getAsString());
                    configOptionSwatch.setStoreId(entry.getValue().getAsJsonObject().remove(JSON_STORE_ID).getAsString());
                    configOptionSwatch.setType(entry.getValue().getAsJsonObject().remove(JSON_TYPE).getAsString());
                    configOptionSwatch.setValue(entry.getValue().getAsJsonObject().remove(JSON_VALUE).getAsString());
                    mapConfigOptionSwatch.put(entry.getKey(), configOptionSwatch);
                }
                configProductOption.setColorSwatch(mapConfigOptionSwatch);
                listColorSwatch.add(configProductOption);
            }
            return listColorSwatch;
        }
    }
}
