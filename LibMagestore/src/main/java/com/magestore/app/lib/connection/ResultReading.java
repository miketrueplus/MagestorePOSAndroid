package com.magestore.app.lib.connection;

import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.lib.parse.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * Chỉ định parse model sẽ sử dụng
     * @param parseModel
     */
    void setParseModel(Class parseModel) throws ConnectionException, IOException;

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
    ParseModel doParse() throws IOException, ParseException;

    /**
     * Parse inputstream sang result
     * @param clParsemodel
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    ParseModel doParse(Class clParsemodel) throws IOException, ParseException;

    /**
     * Parse inputstream sang result
     * @param clParseModel
     * @param parseImplement
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    ParseModel doParse(ParseImplement parseImplement, Class clParseModel) throws IOException, ParseException;

    /**
     * Ghi kết quả ra output stream
     * @param out
     * @throws ParseException
     * @throws ConnectionException
     * @throws IOException
     */
    void writeOutputStream(OutputStream out) throws ParseException, ConnectionException, IOException;

    /**
     * Ghi kết quả ra file
     * @throws ParseException
     * @throws ConnectionException
     * @throws IOException
     */
    void writeToFile(File file) throws ParseException, ConnectionException, IOException;
}
