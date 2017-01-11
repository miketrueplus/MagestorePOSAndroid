package com.magestore.app.pos.api.m2;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.DataAccessSession;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;

import java.io.IOException;
import java.text.ParseException;

/**
 * Lớp cha gateway kết nối API của hệ thống Magestore
 * Created by Mike on 12/21/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSAbstractDataAccess implements DataAccess {
    private Class mclParseImplement;
    private Class mclParseEntity;
    private POSDataAccessSession mSession;
    private MagestoreContext mContext;

    public POSAbstractDataAccess() {
        setParseImplement(Gson2PosAbstractParseImplement.class);
        setSession(new POSDataAccessSession());
    }



    @Override
    public void setSession(DataAccessSession session) {
        mSession = (POSDataAccessSession) session;
    }

    @Override
    public void setParseEntity(Class clParseEntity) {
        mclParseEntity = clParseEntity;
    }

    @Override
    public void setContext(MagestoreContext context) {
        mContext = context;
    }

    @Override
    public MagestoreContext getContext() {
        return mContext;
    }

    @Override
    public void setParseImplement(Class clParseImplement) {
        mclParseImplement = clParseImplement;
    }

    protected Class getClassParseImplement() {
        return mclParseImplement;
    }

    protected Class getClassParseEntity() {
        return mclParseEntity;
    }

    protected String doApi2String(String pstrQuery, Object objParam, String ...params) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = MagestoreConnection.getConnection(POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);

            // Xử lý truy vấn
            statement = connection.createStatement();
            statement.prepareQuery(pstrQuery);
            statement.setParam(objParam);
            statement.setParams(params);
            rp = statement.execute();

            // parse kết quả thành object
            return rp.readResult2String();
        }
        catch (ConnectionException ex) {
            throw  ex;
        }
        catch (IOException ex) {
            throw ex;
        }
//        catch (Exception ex) {
//            throw new DataAccessException(ex);
//        }
        finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null)statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    protected ParseModel doAPI(Class parseEntity, String pstrQuery, Object objParam, String ...params) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = MagestoreConnection.getConnection(POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);

            // Xử lý truy vấn
            statement = connection.createStatement();
            statement.prepareQuery(pstrQuery);
            statement.setParams(params);
            statement.setParam(objParam);
            rp = statement.execute();

            // parse kết quả thành object
            rp.setParseImplement(getClassParseImplement());
            rp.setParseEntity(parseEntity);
            return rp.doParse();
        }
        catch (ConnectionException ex) {
            throw ex;
        }
        catch (IOException ex) {
            throw ex;
        }
        finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null)statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }
}
