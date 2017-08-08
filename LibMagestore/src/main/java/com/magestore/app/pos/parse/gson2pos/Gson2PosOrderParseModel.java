package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 4/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosOrderParseModel extends Gson2PosAbstractParseImplement {
    @Override
    public Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<PosOrder>(){}
                .getType(), new OrderParamsConverter());
        return builder.create();
    }

    private static final String JSON_EXTENSION_ATTRIBUTE = "extension_attributes";
    private static final String JSON_EXTENSION_ITEMS_INFOBUY = "items_info_buy";
    private static final String JSON_EXTENSION_PAYMENT = "payment";
    private static final String JSON_EXTENSION_BILLING_ADDRESS = "billing_address";

    public class OrderParamsConverter implements JsonDeserializer<PosOrder> {
        @Override
        public PosOrder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            obj.remove(JSON_EXTENSION_ATTRIBUTE);
            obj.remove(JSON_EXTENSION_ITEMS_INFOBUY);
            obj.remove(JSON_EXTENSION_ITEMS_INFOBUY);
            obj.remove(JSON_EXTENSION_PAYMENT);
            obj.remove(JSON_EXTENSION_BILLING_ADDRESS);
            PosOrder order = new Gson().fromJson(obj, PosOrder.class);
            return order;
        }
    }
}
