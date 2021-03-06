package com.magestore.app.pos.api.m1;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.DataAccessSession;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosMesssageExceptionImplement;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Johan on 8/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSAbstractDataAccessM1 implements DataAccess{
    private Class mclParseImplement;
    private Class mclParseEntity;
    private POSDataAccessSessionM1 mSession;
    private MagestoreContext mContext;

    public POSAbstractDataAccessM1() {
        setParseImplement(Gson2PosAbstractParseImplement.class);
        setSession(new POSDataAccessSessionM1());
    }

    @Override
    public void setSession(DataAccessSession session) {
        mSession = (POSDataAccessSessionM1) session;
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

    protected Class getClassExceptionParseImplement() {
        return Gson2PosMesssageExceptionImplement.class;
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);

            // Xử lý truy vấn
            statement = connection.createStatement();
            statement.prepareQuery(pstrQuery);
            statement.setParams(params);
            statement.setParam(objParam);
            rp = statement.execute();

            // parse kết quả thành object
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(parseEntity);
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

    /**
     * Đọc hiểu 1 message từ server khi có exception
     * @param rp
     * @return
     * @throws IOException
     * @throws ParseException
     */
    protected MessageException processException(ResultReading rp) throws IOException, ParseException {
        // parse kết quả thành object
        rp.setParseImplement(getClassExceptionParseImplement());
        rp.setParseModel(MessageException.class);
        return (MessageException) rp.doParse();
    }
}
