package com.magestore.app.pos.api.m2.registershift;

import android.util.Log;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.registershift.PosCashTransaction;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListRegisterShift;
import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSRegisterShiftDataAccess extends POSAbstractDataAccess implements RegisterShiftDataAccess {

    private class RegisterShiftEntity {
        CashTransaction cashTransaction;
        SessionParam shift;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public List<RegisterShift> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<RegisterShift> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_REGISTER_SHIFTS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListRegisterShift.class);
            Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) rp.doParse();
            List<RegisterShift> list = (List<RegisterShift>) (List<?>) (listRegisterShift.items);
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<RegisterShift> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public RegisterShift retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(RegisterShift oldModel, RegisterShift newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(RegisterShift... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(RegisterShift... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public List<RegisterShift> openSession(SessionParam sessionParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_REGISTER_SHIFTS_SAVE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
            registerShiftEntity.shift = sessionParam;

            // TODO: log params request
            Gson gson = new Gson();
            String json = gson.toJson(registerShiftEntity);
            Log.e("JSON", json.toString());

            rp = statement.execute(registerShiftEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListRegisterShift.class);
            Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) rp.doParse();
            List<RegisterShift> list = (List<RegisterShift>) (List<?>) listRegisterShift;
            return list;
        } catch (ConnectionException ex) {
            throw new DataAccessException(ex);
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<RegisterShift> insertMakeAdjustment(final CashTransaction pCashTransaction) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_REGISTER_SHIFTS_MAKE_ADJUSTMENT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
            registerShiftEntity.cashTransaction = pCashTransaction;

            // TODO: log params request
            Gson gson = new Gson();
            String json = gson.toJson(registerShiftEntity);
            Log.e("JSON", json.toString());

            rp = statement.execute(registerShiftEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListRegisterShift.class);
            Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) rp.doParse();
            List<RegisterShift> list = (List<RegisterShift>) (List<?>) listRegisterShift;
            return list;
        } catch (ConnectionException ex) {
            throw new DataAccessException(ex);
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }
}
