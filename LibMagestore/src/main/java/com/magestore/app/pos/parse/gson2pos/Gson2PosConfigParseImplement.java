package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Chuyển đổi Gson sang config
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class Gson2PosConfigParseImplement extends Gson2PosAbstractParseImplement {
    /**
     * Bổ sung MapKeySerialization cho Gson
     * @return
     */
    @Override
    public Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<Map>(){}
                        .getType(), new ConfigDictionaryConverter());
        return builder.create();
    }

    /**
     * Map từ điển của config {path -> value}
     */
    public class ConfigDictionaryConverter implements JsonDeserializer<Map<?, ?>> {
        public Map<Object, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
            Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(typeOfT, $Gson$Types.getRawType(typeOfT));

            Map<Object, Object> vals = new HashMap<Object, Object>();
            for (JsonElement item : json.getAsJsonArray()) {
                Object key = ctx.deserialize(item.getAsJsonObject().get("path"), keyAndValueTypes[0]);
                Object value = ctx.deserialize(item.getAsJsonObject().get("value"), keyAndValueTypes[1]);
                vals.put(key, value);
            }
            return vals;
        }
    }
}
