package com.magestore.app.lib.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.ParseImplement;

import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Thực thi Parse từ GSON/InputStream sang các entity
 * Created by Mike on 12/16/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class Gson2PosAbstractParseImplement extends DefaultHandler implements ParseImplement {
    private Gson mGSON;
    private Reader mReader;
    private Type mtypeOf;
    private ParseEntity mParseEntity;

    /**
     * Khởi tạo gson
     * @param input
     * @param typeOf
     * @throws ParseException
     */
    @Override
    public void prepareParse(InputStream input, Type typeOf) throws ParseException {
        mReader =  new BufferedReader(new InputStreamReader(input));
        mtypeOf = typeOf;
        mGSON = createGson();
    }

    /**
     * Khởi tạo parse đầu vào là inputstream và entity
     * @param input
     * @param parseEntity
     * @throws ParseException
     * @throws IOException
     */
    @Override
    public void prepareParse(InputStream input, ParseEntity parseEntity) throws ParseException, IOException {
        mParseEntity = parseEntity;
        prepareParse(input, parseEntity.getClass());
    }

    /**
     * Khởi tạo parse đầu vào là inputstream/gson và entity/class
     * @param input
     * @param cl
     * @throws ParseException
     */
    @Override
    public void prepareParse(InputStream input, Class<ParseEntity> cl) throws ParseException {
        prepareParse(input, (Type) cl);
    }

    /**
     * Tạo Gson builder
     * @return
     */
    protected Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    /**
     * Thực hiện parse từ InputStream/Gson sang Model
     * @throws ParseException
     * @throws IOException
     */
    @Override
    public void doParse() throws ParseException, IOException {
        mParseEntity = mGSON.fromJson(mReader, mtypeOf);
        close();
    }

    /**
     * Đóng input stream lại
     */
    @Override
    public void close() {
        if (mReader != null) try {
            mReader.close();
        } catch (IOException e) {
        }
        mReader = null;
    }

    /**
     * Kiểm tra xem đã đóng stream chưa
     * @return
     */
    @Override
    public boolean isClosed() {
        return mReader == null;
    }

    /**
     * Kiểm tra xem còn mở stream không
     * @return
     */
    @Override
    public boolean isOpen() {
        return mReader != null;
    }

    /**
     * Nhận kết quả trả về
     * @return
     */
    @Override
    public ParseEntity getParseEntity() {
        return mParseEntity;
    }
}
