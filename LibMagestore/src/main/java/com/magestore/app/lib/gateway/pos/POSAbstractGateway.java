package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.gateway.Gateway;
import com.magestore.app.lib.gateway.GatewayException;
import com.magestore.app.lib.gateway.GatewaySession;
import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.lib.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.lib.parse.gson2pos.Gson2PosListProduct;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Lớp cha gateway kết nối API của hệ thống Magestore
 * Created by Mike on 12/21/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class POSAbstractGateway implements Gateway {
    private Class mclParseImplement;
    private Class mclParseEntity;
    private POSGatewaySession mSession;

    public POSAbstractGateway() {
        setParseImplement(Gson2PosAbstractParseImplement.class);
        setSession(new POSGatewaySession());
    }

    @Override
    public void setSession(GatewaySession session) {
        mSession = (POSGatewaySession) session;
    }

    @Override
    public void setParseEntity(Class clParseEntity) {
        mclParseEntity = clParseEntity;
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

    protected String doApi2String(String pstrQuery, Object objParam, String ...params) throws ParseException, ConnectionException, GatewayException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = MagestoreConnection.getConnection(POSGatewaySession.REST_BASE_URL, POSGatewaySession.REST_USER_NAME, POSGatewaySession.REST_PASSWORD);

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
//            throw new GatewayException(ex);
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

    protected ParseEntity doAPI(Class parseEntity, String pstrQuery, Object objParam, String ...params) throws ParseException, ConnectionException, GatewayException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = MagestoreConnection.getConnection(POSGatewaySession.REST_BASE_URL, POSGatewaySession.REST_USER_NAME, POSGatewaySession.REST_PASSWORD);

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
