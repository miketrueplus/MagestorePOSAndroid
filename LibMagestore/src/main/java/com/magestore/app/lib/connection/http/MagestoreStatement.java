package com.magestore.app.lib.connection.http;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.magestore.app.lib.connection.CacheConnection;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.StatementAction;
import com.magestore.app.pos.model.exception.PosMessageException;
import com.magestore.app.pos.model.exception.PosMessageException500;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.pos.parse.gson2pos.Gson2PosMesssageExceptionImplement;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
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
    // Param builder
    ParamBuilder mParambuilder;

    // cache
    MagestoreFileCacheConnection mCacheConnection;

    // String builder excuteQuery
    StringBuilder mstrPreparedQuery;
    String mstrLastPreparedQuery;
    String mstrExecuteQuery;

    // http header
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCEPT = "Accept";
    private static final String SLASH = StringUtil.STRING_SLASH;

    private static final int HTTP_CODE_RESPONSE_SUCCESS = 200;

    // mặc định là GET
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";
    private String mstrMethod = METHOD_GET;

    // Connect JSON quản lý HTTP connection
    private MagestoreConnection mConnection = null;
    private HttpURLConnection mHttpConnection = null;

    // Chuỗi truy vấn bao gồm base url
    // Ví dụ
    //   BaseURL = "http://api.androidhive.info/"
    //   query = "/contacts/name/${name}/age/${age}"
    //   thì chuỗi kết quả = "http://api.androidhive.info/contacts/name/${name}/age/${age}"
//    private String mstrPreparedQuery;
    private Object mobjPrepareParam;
    private StatementAction mAction;

    // Bảng map chứa các tham số của truy vấn
    private Map mValuesMap = null;

    class MagestoreExclusionStrategy implements ExclusionStrategy {

        private MagestoreExclusionStrategy() {
        }

        public boolean shouldSkipField(FieldAttributes f) {
            Annotation annotation = f.getAnnotation(Gson2PosExclude.class);
            return (annotation != null);
//            if (annotation != null) {
//                Gson2PosExclude exclude = (Gson2PosExclude) annotation;
//                return !exclude.toJson();
//            }
//            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

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
     * Class xử lý vấn đề xây dựng tham số theo GET
     * @return
     */
    @Override
    public ParamBuilder getParamBuilder() {
        if (mParambuilder == null) {
            if (mValuesMap == null)
                mValuesMap = new HashMap<String, String>();
            mParambuilder = new MagestoreParamBuilder(this, mValuesMap);
        }
        return mParambuilder;
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
        mstrLastPreparedQuery = pstrQuery;
        String strBaseURL = ((MagestoreConnection)getConnection()).getBaseURL();
        mstrPreparedQuery = new StringBuilder(strBaseURL);
        if (!pstrQuery.startsWith(SLASH) && !strBaseURL.endsWith(SLASH)) {
            mstrPreparedQuery.append(SLASH);
        }
        else if (pstrQuery.startsWith(SLASH) && strBaseURL.endsWith(SLASH)) {
            mstrPreparedQuery.deleteCharAt(mstrPreparedQuery.length() - 1);
        }
        mstrPreparedQuery.append(pstrQuery);

        // Clear map tham số
        if (mValuesMap != null) mValuesMap.clear();
        mValuesMap = null;
    }

    @Override
    public void setParamBuilder(ParamBuilder paramBuilder) throws ConnectionException {
        mParambuilder = paramBuilder;
        if (mValuesMap == null)
            mValuesMap = paramBuilder.getValueMap();
        else {
            paramBuilder.getValueMap().putAll(mValuesMap);
            mValuesMap.clear();
            mValuesMap = paramBuilder.getValueMap();
        }
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
        try {
            mValuesMap.put(pstrName, URLEncoder.encode(pstrValue, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
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
     * TODO: cần cải thiện thuật toán tránh build lại query nhiều lần
     */
    public String buildFinalQuery() {
        mstrExecuteQuery = null;
        if (mstrPreparedQuery == null) prepareQuery(mstrLastPreparedQuery);
        StringBuilder strFinalBuilderQuery = mstrPreparedQuery;
        if (mParambuilder != null) {
            strFinalBuilderQuery = new StringBuilder(mstrPreparedQuery);
            strFinalBuilderQuery.append(mParambuilder.buildQuery());
        }
        if (mValuesMap != null) {
            StrSubstitutor sub = new StrSubstitutor(mValuesMap);
            mstrExecuteQuery = sub.replace(strFinalBuilderQuery);
        }
        else
            mstrExecuteQuery = strFinalBuilderQuery.toString();

        return mstrExecuteQuery;
    }

    @Override
    public void setEnableCacle() {
        mCacheConnection = new MagestoreFileCacheConnection();
        mCacheConnection.setStatement(this);
    }

    @Override
    public void setEnableCache(String cacheName) {
        mCacheConnection = new MagestoreFileCacheConnection();
        mCacheConnection.setCacheName(cacheName);
        mCacheConnection.setStatement(this);
    }

    @Override
    public CacheConnection getCacheConnection() {
        return mCacheConnection;
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

    @Override
    public void setAction(StatementAction action) {
        mAction = action;
    }

    /**
     *
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    protected ResultReading doExecute() throws ConnectionException, IOException {
        // Tham chiếu đến connection
        MagestoreConnection magestoreConnection = (MagestoreConnection) getConnection();
        if (!magestoreConnection.isOpenned())
            magestoreConnection.open();

        // Khởi tạo HTTP Connection với query, giao thức GET
        mHttpConnection = magestoreConnection.openHTTPConnection(mstrExecuteQuery, mstrMethod);
        // đặt action method cho http connection
        if (mAction == MagestoreStatementAction.ACTION_DELETE)
            mHttpConnection.setRequestMethod(METHOD_DELETE);
        else
            mHttpConnection.setRequestMethod(METHOD_POST);

        // Nếu có object làm tham số. ghi json vào http body
        if (mobjPrepareParam != null) {
            // Đặt lại các tham số cho HTTP Connection
            mHttpConnection.setDoOutput(true);
            mHttpConnection.setDoInput(true);
            mHttpConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            mHttpConnection.setRequestProperty(ACCEPT, APPLICATION_JSON);

            // đặt action method cho http connection
            if (mAction == MagestoreStatementAction.ACTION_DELETE)
                mHttpConnection.setRequestMethod(METHOD_DELETE);
            else
                mHttpConnection.setRequestMethod(METHOD_POST);
            // Viết nội dung của object thành dạng json cho input
            Gson gson = new GsonBuilder().setExclusionStrategies(new MagestoreExclusionStrategy()).create();
            OutputStreamWriter wr = new OutputStreamWriter(mHttpConnection.getOutputStream());
            gson.toJson(mobjPrepareParam, wr);
            wr.flush();
            wr.close();
        }

        // Chạy query, lấy resultset về
        int statusCode = mHttpConnection.getResponseCode();
        InputStream is = null;

        if (statusCode == HTTP_CODE_RESPONSE_SUCCESS) {
            return new MagestoreResultReading(mHttpConnection.getInputStream());
        }
        else {
            // có lỗi, ném ra exception
            MagestoreResultReadingException rp = new MagestoreResultReadingException(mHttpConnection.getErrorStream(), statusCode);
            rp.setParseImplement(Gson2PosMesssageExceptionImplement.class);
            if (statusCode == 500)  {
                rp.setParseModel(PosMessageException500.class);
                throw new ConnectionException(((PosMessageException500) rp.doParse()).getMessage());
            }
            else {
                rp.setParseModel(PosMessageException.class);
                throw new ConnectionException(((PosMessageException) rp.doParse()).getMessage());
            }
        }
    }

    /**
     * Thực hiện truy vấn đến server
     * @return ResultReading
     * @throws ConnectionException
     */
    @Override
    public ResultReading execute() throws ConnectionException, IOException {
        // Chuẩn bị chuỗi URL truy vấn, thay các tham số bằng các giá trị tương ứng
        String strExecuteQuery = buildFinalQuery();

        // kiểm tra load từ cache trước
        if (mCacheConnection != null) {
            return new MagestoreResultReading(mCacheConnection.execute());
        }

        // k0 chỉ định xử lý cache
        return doExecute();
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
        // clear value map nếu không sử dụng param builder
        if (mValuesMap != null && mParambuilder == null) mValuesMap.clear();
        mValuesMap = null;
        mstrPreparedQuery = null;
        mHttpConnection = null;
    }


}