package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.pos.model.magento.checkout.payment.PosAuthorizenetParams;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosAuthorizenetParseModel extends Gson2PosAbstractParseImplement {
    @Override
    public Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<PosAuthorizenetParams>(){}
                .getType(), new AuthorizenetParamsConverter());
        return builder.create();
    }

    private static final String JSON_AUTHORIZE_URL = "url";
    private static final String JSON_AUTHORIZE_PARAM = "params";

    public class AuthorizenetParamsConverter implements JsonDeserializer<PosAuthorizenetParams> {
        @Override
        public PosAuthorizenetParams deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            PosAuthorizenetParams authorizenetParams = new PosAuthorizenetParams();
            authorizenetParams.url = json.getAsJsonObject().get(JSON_AUTHORIZE_URL).getAsString();

            JsonElement param = json.getAsJsonObject().get(JSON_AUTHORIZE_PARAM);
            authorizenetParams.params = new HashMap<>();
            Set<Map.Entry<String, JsonElement>> entry = param.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> item: entry)
                authorizenetParams.params.put(item.getKey(), item.getValue().getAsString());
            return authorizenetParams;
        }
    }
}
