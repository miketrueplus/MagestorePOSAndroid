package com.magestore.app.lib.connection.http;

import android.util.Log;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;

import java.io.IOException;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tạo các truy vấn đến API, điền các tham số vào
 * Truy vấn được tạo từ BaseURL, query và tham số
 * Tham số truy vấn có dạng ${tên_tham_số}
 *  Ví dụ
 *    BaseURL = "http://api.androidhive.info/"
 *    query = "/contacts/name/${name}/age/${age}"
 *    thì chuỗi truy vấn kết quả = "http://api.androidhive.info/contacts/name/${name}/age/${age}"
 *    Trong đó ${name} và ${age} là tham số
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreStatement implements Statement {
    // mặc định là GET
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private String mstrMethod = METHOD_GET;

    // Connect JSON quản lý HTTP connection
    private MagestoreConnection mConnection = null;
    private HttpURLConnection mHttpConnection = null;

    // Chuỗi truy vấn bao gồm base url
    // Ví dụ
    //   BaseURL = "http://api.androidhive.info/"
    //   query = "/contacts/name/${name}/age/${age}"
    //   thì chuỗi kết quả = "http://api.androidhive.info/contacts/name/${name}/age/${age}"
    private String mstrPreparedQuery;
    private Object mobjPrepareParam;

    // Bảng map chứa các tham số của truy vấn
    private Map mValuesMap = null;


    /**
     * Khởi tạo với 1 JSON Connection tương ứng
     * @param connection
     */
    protected MagestoreStatement(MagestoreConnection connection) {
        mConnection = connection;
    }

    /**
     * Getter connection json
     * @return Connection
     */
    @Override
    public Connection getConnection() {
        return mConnection;
    }

    /**
     * Chuẩn bị câu truy vấn
     * Nếu truy vấn có tham số thì các tham số được đặt them mẫu ${tên_tham_số}
     * @param pstrQuery là chuỗi truy vấn, không bao gồm base url, ví dụ "/contacts/name/${name}/age/${age}"
     * @throws ConnectionException
     */
    @Override
    public void prepareQuery(String pstrQuery) throws ConnectionException {
        // Nếu BaseURL và query đã có dấu "/" thì ghép 2 chuỗi lại thành URL
        // Ví dụ
        //   BaseURL = "http://api.androidhive.info/"
        //   query = "/contacts/name/${name}/age/${age}"
        //   thì chuỗi kết quả = "http://api.androidhive.info/contacts/name/${name}/age/${age}"
        String strBaseURL = ((MagestoreConnection)getConnection()).getBaseURL();
        if (!pstrQuery.startsWith("/") && !strBaseURL.endsWith("/"))
            mstrPreparedQuery = strBaseURL + "/" + pstrQuery;
        else if (pstrQuery.startsWith("/") && strBaseURL.endsWith("/"))
            mstrPreparedQuery = strBaseURL + pstrQuery.replaceFirst("/", "");
        else
            mstrPreparedQuery = strBaseURL + pstrQuery;

        // Clear map tham số
        if (mValuesMap != null) mValuesMap.clear();
        mValuesMap = null;
    }

    /**
     * Gán các tham số cho truy vấn
     * Tham số trong URL có dạng ${tên_tham_số}
     * @param pstrName
     * @param pstrValue
     * @throws ConnectionException
     */
    @Override
    public void setParam(String pstrName, String pstrValue) throws ConnectionException {
        if (mValuesMap == null)
            mValuesMap = new HashMap<String, String>();
        mValuesMap.put(pstrName, pstrValue);
    }

    /**
     * Gán các tham số cho truy vấn
     * Tham số trong URL có dạng ${tên_tham_số}
     * @param pstrName
     * @param pintValue
     * @throws ConnectionException
     */
    @Override
    public void setParam(String pstrName, int pintValue) throws ConnectionException {
        if (mValuesMap == null)
            mValuesMap = new HashMap<String, String>();
        mValuesMap.put(pstrName, pintValue);
    }

    /**
     * Gán các tham số cho truy vấn
     * Tham số trong URL có dạng ${tên_tham_số}
     * @param params
     * @throws ConnectionException
     */
    public void setParams(String ...params) throws ConnectionException {
        String strName = null;
        for (String param:params) {
            if (strName == null) strName = param;
            else {
                setParam(strName, param);
                strName = null;
            }
        }
    }

    /**
     * Trả lại excutequery trên url
     * @return
     */
    private String buildFinalQuery() {
        String excuteQuery = null;
        if (mValuesMap != null) {
            StrSubstitutor sub = new StrSubstitutor(mValuesMap);
            excuteQuery = sub.replace(mstrPreparedQuery);
        }
        else
            excuteQuery =  mstrPreparedQuery;
        return excuteQuery;
    }

    /**
     * Chuẩn bị HTTP connection với method post và object
     * @param parseEntity
     * @throws IOException
     */
    @Override
    public void setParam(Object parseEntity) throws ConnectionException, IOException {
        mobjPrepareParam = parseEntity;
    }

    /**
     * Thực hiện truy vấn đến server
     * @return ResultReading
     * @throws ConnectionException
     */
    @Override
    public ResultReading execute() throws ConnectionException, IOException {
        // Chuẩn bị chuỗi URL truy vấn, thay các tham số bằng các giá trị tương ứng
        String excuteQuery = buildFinalQuery();

        // Tham chiếu đến connection
        MagestoreConnection magestoreConnection = (MagestoreConnection) getConnection();
        if (!magestoreConnection.isOpenned())
            magestoreConnection.open();

        // Khởi tạo HTTP Connection với query, giao thức GET
        mHttpConnection = magestoreConnection.openHTTPConnection(excuteQuery, mstrMethod);

        // Nếu có object làm tham số. ghi json vào http body
        if (mobjPrepareParam != null) {
            // Đặt lại các tham số cho HTTP Connection
            mHttpConnection.setDoOutput(true);
            mHttpConnection.setDoInput(true);
            mHttpConnection.setRequestProperty("Content-Type", "application/json");
            mHttpConnection.setRequestProperty("Accept", "application/json");
            mHttpConnection.setRequestMethod(METHOD_POST);

            // Viết nội dung của object thành dạng json cho input
            Gson gson = new Gson();
            OutputStreamWriter wr = new OutputStreamWriter(mHttpConnection.getOutputStream());
            gson.toJson(mobjPrepareParam, wr);
            wr.flush();
            wr.close();
        }

        // Chạy query, lấy resultset về
        int statusCode = mHttpConnection.getResponseCode();
        InputStream is = null;
        if (statusCode == 200)
            return new MagestoreResultReading(mHttpConnection.getInputStream());
        else
            return new MagestoreResultReadingException(mHttpConnection.getErrorStream(), statusCode);

    }

    /**
     * Thực thi truy vấn luôn, theo câu query
     * @param pstrQuery
     * @return ResultReading
     * @throws ConnectionException
     */
    @Override
    public ResultReading execute(String pstrQuery) throws ConnectionException, IOException {
        prepareQuery(pstrQuery);
        return execute();
    }

    /**
     * Thực thi truy vấn luôn, tham số là 1 object
     * @param parseEntity
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ResultReading execute(Object parseEntity) throws ConnectionException, IOException {
        setParam(parseEntity);
        return execute();
    }

    /**
     * Thực thi truy vấn luôn, tham số là 1 object với câu query
     * @param pstrQuery
     * @param parseEntity
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ResultReading execute(String pstrQuery, Object parseEntity) throws ConnectionException, IOException {
        prepareQuery(pstrQuery);
        setParam(parseEntity);
        return execute();
    }

    /**
     * Thực thi truy vấn luôn với các cặp tahm số
     * @param params
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ResultReading execute(String... params) throws ConnectionException, IOException {
        setParams(params);
        return execute();
    }

    /**
     * Thực thi truy vấn luôn với tham số là params và object
     * @param parseEntity
     * @param params
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ResultReading execute(Object parseEntity, String... params) throws ConnectionException, IOException {
        setParams(params);
        setParam(parseEntity);
        return execute();
    }

    /**
     * Thực thi truy vấn luôn với tham số là params và object và câu truy vấn
     * @param pstrQuery
     * @param parseEntity
     * @param params
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ResultReading execute(String pstrQuery, Object parseEntity, String... params) throws ConnectionException, IOException {
        prepareQuery(pstrQuery);
        setParams(params);
        setParam(parseEntity);
        return execute();
    }

    /**
     * Thực thi truy vấn luôn với tham số là params và object và câu truy vấn
     * @param pstrQuery
     * @param params
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ResultReading execute(String pstrQuery, String... params) throws ConnectionException, IOException {
        prepareQuery(pstrQuery);
        setParams(params);
        return execute();
    }

    /**
     * Đóng statement lại, giải phóng bộ nhớ
     * @throws ConnectionException
     */
    @Override
    public void close() {
        if (mValuesMap != null) mValuesMap.clear();
        mValuesMap = null;
        mstrPreparedQuery = null;
        mHttpConnection = null;
    }
}