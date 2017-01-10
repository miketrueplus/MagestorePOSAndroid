package com.magestore.app.lib.parse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Thực thi Parse từ GSON/InputStream sang các entity
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ParseImplement {
    /**
     *
     * @param input
     * @param parseEntity
     * @throws ParseException
     * @throws IOException
     */
    void prepareParse(InputStream input, ParseEntity parseEntity) throws ParseException, IOException;

    /**
     *
     * @param input
     * @param cl
     * @throws ParseException
     * @throws IOException
     */
    void prepareParse(InputStream input, Class<ParseEntity> cl) throws ParseException, IOException;

    /**
     *
     * @param input
     * @param typeOf
     * @throws ParseException
     * @throws IOException
     */
    void prepareParse(InputStream input, Type typeOf) throws ParseException, IOException;

    /**
     *
     * @throws ParseException
     * @throws IOException
     */
    void doParse() throws ParseException, IOException;

    /**
     * Đóng input stream lại
     */
    void close();

    /**
     * Kiểm tra xem đã đóng stream chưa
     * @return true nếu đã đóng, false nếu chưa
     */
    boolean isClosed();

    /**
     * Kiểm tra xem còn mở stream không
     * @return true nếu đã đóng, false nếu chưa
     */
    boolean isOpen();

    /**
     * Nhận kết quả trả về
     * @return
     */
    ParseEntity getParseEntity();
}