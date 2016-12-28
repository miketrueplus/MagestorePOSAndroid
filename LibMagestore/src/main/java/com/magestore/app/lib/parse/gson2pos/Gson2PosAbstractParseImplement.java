package com.magestore.app.lib.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.magestore.app.lib.entity.pos.PosProduct;
import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.ParseImplement;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Parse từ GSON sang các entity
 * Created by Mike on 12/16/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class Gson2PosAbstractParseImplement extends DefaultHandler implements ParseImplement {
    private Gson mGSON;
    private Reader mReader;
    private Type mtypeOf;
    private ParseEntity mParseEntity;

    @Override
    public void prepareParse(InputStream input, ParseEntity parseEntity) throws ParseException, IOException {
        mParseEntity = parseEntity;
        prepareParse(input, parseEntity.getClass());
    }

    @Override
    public void prepareParse(InputStream input, Class<ParseEntity> cl) throws ParseException {
        prepareParse(input, (Type) cl);
    }

    protected Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(mtypeOf, new Gson2PosInstance());
//        builder.registerTypeAdapter(mtypeOf, new Gson2PosDeserializer((Gson2PosListProduct) mParseEntity));
//        mGSON = builder.create();
        return builder.create();
    }

    @Override
    public void prepareParse(InputStream input, Type typeOf) throws ParseException {
        mReader =  new BufferedReader(new InputStreamReader(input));
        mtypeOf = typeOf;
        mGSON = createGson();
    }

    @Override
    public void doParse() throws ParseException, IOException {
        mParseEntity = mGSON.fromJson(mReader, mtypeOf);
        close();
    }

    @Override
    public void close() {
        if (mReader != null) try {
            mReader.close();
        } catch (IOException e) {
        }
        mReader = null;
    }

    @Override
    public boolean isClosed() {
        return mReader == null;
    }

    @Override
    public boolean isOpen() {
        return mReader != null;
    }

    @Override
    public ParseEntity getParseEntity() {
        return mParseEntity;
    }
}
