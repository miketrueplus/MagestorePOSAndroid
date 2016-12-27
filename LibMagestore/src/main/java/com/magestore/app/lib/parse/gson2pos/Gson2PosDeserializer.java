package com.magestore.app.lib.parse.gson2pos;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.magestore.app.lib.entity.pos.PosProduct;
import com.magestore.app.lib.parse.ParseEntity;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

/**
 * Created by Mike on 12/19/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class Gson2PosDeserializer implements JsonDeserializer<Gson2PosListProduct> {
    Gson2PosListProduct mParseEntity;
    public Gson2PosDeserializer(Gson2PosListProduct parseEntity) {
        mParseEntity = parseEntity;
    }
    @Override
    public Gson2PosListProduct deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        return mParseEntity;
        JsonObject obj = json.getAsJsonObject();
//        JsonArray arr = json.getAsJsonArray();
//        json.
        mParseEntity.items.add(null);
        return mParseEntity;
    }

}
