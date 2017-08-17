package com.magestore.app.lib.connection.http;


import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.Statement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Quản lý HTTP Connection đến API Server
 * BaseURL là địa chỉ gốc API
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreConnection implements Connection {
    // Timeout kết nối mặc định theo ms
    public static int DEFAULT_TIMEOUT = 60000;

    // HTTP connection kết nối đến API server
//    private HttpURLConnection mHttpConn = null;

    private String mstrBaseURL = null;
    private String mstrUserName = null;
    private String mstrPassword = null;
    private int mintTimeout;
    private boolean mbnlIsOpen = false;

    /**
     * Getter URL
     * @return URL of connection
     */
    protected String getBaseURL() {
        return mstrBaseURL;
    }

    /**
     * Setter URL
     * @param strBaseURL
     */
    protected void setBaseURL(String strBaseURL) {
        this.mstrBaseURL = strBaseURL;
    }

    /**
     * Getter User Name
     * @return
     */
    protected String getUserName() {
        return mstrUserName;
    }

    /**
     * Setter User Name
     * @param mstrUserName
     */
    protected void setUserName(String mstrUserName) {
        this.mstrUserName = mstrUserName;
    }

    /**
     * Getter Password
     * @return
     */
    protected String getPassword() {
        return mstrPassword;
    }

    /**
     * Setter Password
     * @param mstrPassword
     */
    protected void setPassword(String mstrPassword) {
        this.mstrPassword = mstrPassword;
    }

    /**
     * Setter Timeout
     * @param pintTimeOut
     */
    protected void setTimeOut(int pintTimeOut) {
        mintTimeout = pintTimeOut;
    }

    /**
     *
     * @return timeout kết nối
     */
    protected int getTimeout() {
        return mintTimeout;
    }

    /**
     * Khởi tạo với Base URL và User Name, Password truy cập API
     * @param pstrBaseURL
     * @param pstrUserName
     * @param pstrPassword
     * @see Connection
     */
    protected MagestoreConnection(String pstrBaseURL, String pstrUserName, String pstrPassword) {
        this.mstrBaseURL = pstrBaseURL;
        this.mstrUserName = mstrUserName;
        this.mstrPassword = mstrPassword;
        setTimeOut(DEFAULT_TIMEOUT);
    }

    /**
     * Khởi tạo với Base URL và User Name, Password truy cập API
     * @param pstrURL
     * @param pstrUserName
     * @param pstrPassword
     * @return
     */
    public static Connection getConnection(String pstrURL, String pstrUserName, String pstrPassword) {
        return new MagestoreConnection(pstrURL, pstrUserName, pstrPassword);
    }

    /**
     * Mở kết nối HTTP, tuy nhiên cái này không dùng đến do phải có tham số URL
     * @throws ConnectionException
     */
    @Override
    public void open() throws ConnectionException {
        mbnlIsOpen = true;
    }

    /**
     * Mở kết nối HTTP, với tham số URL, method đầy đủ
     * @param strURL
     * @throws IOException
     */
    protected HttpURLConnection openHTTPConnection(String strURL, String strMethod) throws IOException {
        URL url = new URL(strURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setConnectTimeout(getTimeout());
        httpConn.setReadTimeout(getTimeout());
        httpConn.setRequestMethod(strMethod);
        return httpConn;
    }

    /**
     * Mở kết nối HTTP, với tham số URL, method đầy đủ
     * @param strURL
     * @throws IOException
     */
    protected HttpURLConnection prepareHTTPConnection(String strURL, String strMethod) throws IOException {
        URL url = new URL(strURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setConnectTimeout(getTimeout());
        httpConn.setReadTimeout(getTimeout());
        httpConn.setRequestMethod(strMethod);
        return httpConn;
    }

    /**
     * Kiểm tra xem kết nối đã được mở chưa
     * @return
     * @throws ConnectionException
     */
    @Override
    public boolean isOpenned() throws ConnectionException {
        return mbnlIsOpen;
    }

    /**
     * Đóng kết nối lại
     * @throws ConnectionException
     */
    @Override
    public void close() {
        mbnlIsOpen = false;
    }

    /**
     * Tạo statement để tạo truy vấn API
     * @return
     * @throws ConnectionException
     */
    @Override
    public Statement createStatement() throws ConnectionException {
        MagestoreStatement statement = new MagestoreStatement(this);
        return statement;
    }

    /**
     * Kiểm tra xem connection đã được đóng chưa
     * @return
     * @throws ConnectionException
     */
    @Override
    public boolean isClosed() throws ConnectionException {
        return !mbnlIsOpen;
    }
}