package com.magestore.app.lib.parse.gson2pos;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.magestore.app.lib.parse.ParseEntity;

import java.lang.reflect.Type;
import java.util.Vector;

/**
 * Created by Mike on 12/19/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class Gson2PosGenericParseEntity<T> implements ParseEntity {
    public T items;
//    public Gson2PosGenericParseEntity(T pitem) {
//        items = pitem;
//    }
}