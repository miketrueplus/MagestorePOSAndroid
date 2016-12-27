package com.magestore.app.lib.connection;

import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseImplement;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Quản lý đọc kết quả sau mỗi truy vấn
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ResultReading {
    /**
     * Chuyển kết quả từ input stream sang string
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    String readResult2String() throws ConnectionException, IOException;

    /**
     * Trả về input stream
     * @return
     */
    InputStream getInputStream();

    /**
     * Đóng và giải phóng tài nguyên
     * @throws ConnectionException
     * @throws IOException
     */
    void close();

    /**
     * Kiểm tra đã mở chưa
     * @return
     */
    boolean isOpenned() throws ConnectionException, IOException;

    /**
     * Kiểm tra đã đóng chưa
     * @return
     */
    boolean isClosed() throws ConnectionException, IOException;

    /**
     * Chỉ định parse entity sẽ sử dụng
     * @param parseEntity
     */
    void setParseEntity(Class parseEntity) throws ConnectionException, IOException;

    /**
     * Chỉ định class xử lý implement
     * @param parseImplement
     * @throws ConnectionException
     * @throws IOException
     * @throws ParseException
     */
    void setParseImplement(Class parseImplement) throws ConnectionException, IOException, ParseException;

    /**
     * Chỉ định parse inplement sẽ sử dụng
     */
    void setParseImplement(ParseImplement parseImplement) throws ConnectionException, IOException;

    /**
     * Parse inputstream sang result
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    ParseEntity doParse() throws IOException, ParseException;

    /**
     * Parse inputstream sang result
     * @param clParseEntity
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    ParseEntity doParse(Class clParseEntity) throws IOException, ParseException;

    /**
     * Parse inputstream sang result
     * @param clParseEntity
     * @param parseImplement
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    ParseEntity doParse(ParseImplement parseImplement, Class clParseEntity) throws IOException, ParseException;
}
