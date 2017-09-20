package com.magestore.app.pos.api.odoo.registershift;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.registershift.CashBox;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.DataListRegisterShift;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.registershift.PosCashTransaction;
import com.magestore.app.pos.model.registershift.PosDataListRegisterShift;
import com.magestore.app.pos.model.registershift.PosPointOfSales;
import com.magestore.app.pos.model.registershift.PosRegisterShift;
import com.magestore.app.pos.model.registershift.PosSaleSummary;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListSessionParseModelOdoo;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSRegisterShiftDataAccessOdoo extends POSAbstractDataAccessOdoo implements RegisterShiftDataAccess {
    private static String ADD_MAKE_ADJUSTMENT = "add";

    private class CashTransactionEntity {
        String session_id;
        float amount;
        String reason;
    }

    private class OpenSessionEntity {
        String config_id;
        boolean cash_control;
        List<CashBoxEntity> cashbox_lines_ids;
    }

    private class CashBoxEntity {
        int number;
        float coin_value;
    }

    private class CloseSessionEntity {
        String session_id;
        boolean cash_control;
        List<CashBoxEntity> cashbox_lines_ids;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListSessionParseModelOdoo());
            rp.setParseModel(PosDataListRegisterShift.class);
            DataListRegisterShift listRegisterShift = (DataListRegisterShift) rp.doParse();
            return listRegisterShift.getTotalCount();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListSessionParseModelOdoo());
            rp.setParseModel(PosDataListRegisterShift.class);
            DataListRegisterShift listRegisterShift = (DataListRegisterShift) rp.doParse();
            return sumBalance(listRegisterShift.getItems());
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_OPEN_SESSION);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            OpenSessionEntity openSessionEntity = new OpenSessionEntity();
            openSessionEntity.cash_control = ConfigUtil.getPointOfSales().getCashControl();
            openSessionEntity.config_id = sessionParam.getPosId();

            List<CashBoxEntity> listCashBox = new ArrayList<>();
            HashMap<OpenSessionValue, CashBox> mCashBox = sessionParam.getCashBox();
            if (mCashBox != null && mCashBox.size() > 0) {
                for (CashBox cashBox : mCashBox.values()) {
                    CashBoxEntity cashBoxEntity = new CashBoxEntity();
                    cashBoxEntity.number = cashBox.getQty();
                    cashBoxEntity.coin_value = cashBox.getValue();
                    listCashBox.add(cashBoxEntity);
                }
            }
            openSessionEntity.cashbox_lines_ids = listCashBox;

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(openSessionEntity);
            rp.setParseImplement(new Gson2PosListSessionParseModelOdoo());
            rp.setParseModel(PosDataListRegisterShift.class);
            DataListRegisterShift listRegisterShift = (DataListRegisterShift) rp.doParse();
            return sumBalance(listRegisterShift.getItems());
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
    public List<RegisterShift> insertMakeAdjustment(CashTransaction cashTransaction) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_MAKE_ADJUSTMENT);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            CashTransactionEntity cashTransactionEntity = new CashTransactionEntity();
            cashTransactionEntity.session_id = cashTransaction.getParamShiftId();
            if (cashTransaction.getType().equals(ADD_MAKE_ADJUSTMENT)) {
                cashTransactionEntity.amount = cashTransaction.getValue();
            } else {
                cashTransactionEntity.amount = (0 - cashTransaction.getValue());
            }
            cashTransactionEntity.reason = cashTransaction.getNote();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(cashTransactionEntity);
            rp.setParseImplement(new Gson2PosListSessionParseModelOdoo());
            rp.setParseModel(PosDataListRegisterShift.class);
            DataListRegisterShift listRegisterShift = (DataListRegisterShift) rp.doParse();
            return sumBalance(listRegisterShift.getItems());
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

    private static String VALIDATE = "1";

    @Override
    public List<RegisterShift> closeSession(SessionParam sessionParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            if (sessionParam.getStatus().equals(VALIDATE)) {
                statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_VALIDATE_SESSION);
            } else {
                statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_CLOSE_SESSION);
            }
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            CloseSessionEntity closeSessionEntity = new CloseSessionEntity();
            closeSessionEntity.session_id = sessionParam.getID();
            if (!sessionParam.getStatus().equals(VALIDATE)) {
                closeSessionEntity.cash_control = ConfigUtil.isCashControl();

                List<CashBoxEntity> listCashBox = new ArrayList<>();
                HashMap<OpenSessionValue, CashBox> mCashBox = sessionParam.getCashBox();
                if (mCashBox != null && mCashBox.size() > 0) {
                    for (CashBox cashBox : mCashBox.values()) {
                        CashBoxEntity cashBoxEntity = new CashBoxEntity();
                        cashBoxEntity.number = cashBox.getQty();
                        cashBoxEntity.coin_value = cashBox.getValue();
                        listCashBox.add(cashBoxEntity);
                    }
                }
                closeSessionEntity.cashbox_lines_ids = listCashBox;
            }

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(closeSessionEntity);
            rp.setParseImplement(new Gson2PosListSessionParseModelOdoo());
            rp.setParseModel(PosDataListRegisterShift.class);
            DataListRegisterShift listRegisterShift = (DataListRegisterShift) rp.doParse();
            return sumBalance(listRegisterShift.getItems());
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

    private List<RegisterShift> sumBalance(List<RegisterShift> shiftList) {
        if (shiftList != null && shiftList.size() > 0) {
            for (RegisterShift shift : shiftList) {
                float total_base_balance = 0;
                total_base_balance += shift.getBaseFloatAmount();
                List<CashTransaction> listCash = shift.getCashTransaction();
                if (listCash != null && listCash.size() > 0) {
                    for (CashTransaction cash : listCash) {
                        total_base_balance += cash.getBaseValue();
                        cash.setBaseBalance(total_base_balance);
                        cash.setBalance(ConfigUtil.convertToPrice(total_base_balance));
                    }
                }
                shift.setBaseBalance(total_base_balance);
                shift.setBalance(ConfigUtil.convertToPrice(total_base_balance));
            }
        }
        return shiftList;
    }
}
