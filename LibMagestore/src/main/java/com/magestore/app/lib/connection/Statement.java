package com.magestore.app.lib.connection;

import java.io.IOException;

/**
 * Thực hiện các statement và tham số truy vấn
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface Statement {
    /**
     * Chuẩn bị query với dạng tham số
     * @param pstrQuery
     */
    void prepareQuery(String pstrQuery) throws ConnectionException;

    /**
     * Truyền tham số cho Query
     * @param pstrName
     * @param pstrValue
     */
    void setParam(String pstrName, String pstrValue) throws ConnectionException;

    /**
     * Truyền tham số cho Query
     * @param params
     */
    void setParams(String ...params) throws ConnectionException;

    /**
     * Truyền tham số cho Query, trên URL cho dạng GET
     * @param pstrName
     * @param pintValue
     */
    void setParam(String pstrName, int pintValue) throws ConnectionException, IOException;

    /**
     * Truyền tham số cho query, trên URL cho dạng GET
     * @param parseEntity
     * @throws ConnectionException
     * @throws IOException
     */
    void setParam(Object parseEntity) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với câu Query
     * @param pstrQuery
     */
    ResultReading execute(String pstrQuery) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với câu Query với các params
     * @param params
     */
    ResultReading execute(String ...params) throws ConnectionException, IOException;


    /**
     * Thực thi truy vấn với câu Query với các params
     * @param params
     */
    ResultReading execute(Object parseEntity, String ...params) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với câu Query với các params
     * @param params
     */
    ResultReading execute(String pstrQuery, Object parseEntity, String ...params) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với câu Query với các params
     * @param params
     */
    ResultReading execute(String pstrQuery, String ...params) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với object làm tham số
     * @param parseEntity
     */
    ResultReading execute(Object parseEntity) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với object làm tham số
     * @param parseEntity
     * @param parseEntity
     */
    ResultReading execute(String pstrQuery, Object parseEntity) throws ConnectionException, IOException;

    /**
     * Thực thi truy vấn với object và param đã truyền trước đó
     */
    ResultReading execute() throws ConnectionException, IOException;



    /**
     * Thực thi truy vấn, cập nhật dữ liệu trên server
     */
//    void executeUpdate() throws ConnectionException;


    /**
     * Đóng statement lại, giải phóng tài nguyên
     * @throws ConnectionException
     */
    void close();

    /**
     * Trả lại connection
     * @return
     */
    Connection getConnection();
}
