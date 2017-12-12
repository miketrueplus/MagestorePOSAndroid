package com.magestore.app.pos.api.m1.registershift;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.DataRegisterShift;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.registershift.PosDataRegisterShift;
import com.magestore.app.pos.model.registershift.PosRegisterShift;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListRegisterShift;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 8/28/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSRegisterShiftDataAccessM1 extends POSAbstractDataAccessM1 implements RegisterShiftDataAccess {
    private static String ADD_MAKE_ADJUSTMENT = "add";

    private class RegisterShiftEntity {
        SessionDataParam data;
        SessionParam shift;
        CashTransaction cashTransaction;
    }

    private class MakeAdjusmentEntity {
        CashTransactionDataParam data;
    }

    private class SessionDataParam {
        String till_id;
        String staff_id;
        String opened_at;
        String closed_at;
        float opening_amount;
        float base_opening_amount;
        float closed_amount;
        float base_closed_amount;
        float cash_left;
        float base_cash_left;
        float cash_added;
        float base_cash_added;
        float cash_removed;
        float base_cash_removed;
        float cash_sale;
        float base_cash_sale;
        String report_currency_code;
        String base_currency_code;
        String sale_by_payments;
        String sales_summary;
        String opened_note;
        String note;
        String status;
        String shift_code;
        String id;
    }

    private class CashTransactionDataParam {
        String shift_id;
        String shift_code;
        String location_id;
        float amount;
        float base_amount;
        float balance;
        float base_balance;
        String created_at;
        String note;
        String type;
        String base_currency_code;
        String transaction_currency_code;
        String open_shift_title;
        String balance_title;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_REGISTER_SHIFTS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSortOrderDESC("id")
                    .setFilterEqual("pos_id", ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId())
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListRegisterShift.class);
            Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) rp.doParse();
            return listRegisterShift.total_count;
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_REGISTER_SHIFTS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("id")
                    .setFilterEqual("pos_id", ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId())
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListRegisterShift.class);
            Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) rp.doParse();
            List<RegisterShift> list = (List<RegisterShift>) (List<?>) (listRegisterShift.items);
//            return sumBalance(list);
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_REGISTER_SHIFTS_SAVE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

//            // thực thi truy vấn và parse kết quả thành object
//            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
//
//            SessionDataParam sessionDataParam = new SessionDataParam();
//            sessionDataParam.till_id = ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId();
//            sessionDataParam.staff_id = sessionParam.getStaffId();
//            sessionDataParam.opened_at = sessionParam.getOpenedAt();
//            sessionDataParam.closed_at = "";
//            sessionDataParam.opening_amount = sessionParam.getFloatAmount();
//            sessionDataParam.base_opening_amount = sessionParam.getBaseFloatAmount();
//            sessionDataParam.closed_amount = sessionParam.getCloseAmount();
//            sessionDataParam.base_closed_amount = sessionParam.getBaseClosedAmount();
//            sessionDataParam.cash_left = sessionParam.getCashLeft();
//            sessionDataParam.base_cash_left = sessionParam.getBaseCashLeft();
//            sessionDataParam.cash_added = sessionParam.getCashAdded();
//            sessionDataParam.base_cash_added = sessionParam.getBaseCashAdded();
//            sessionDataParam.cash_removed = sessionParam.getCashRemoved();
//            sessionDataParam.base_cash_removed = sessionParam.getBaseCashRemoved();
//            sessionDataParam.cash_sale = sessionParam.getCashSale();
//            sessionDataParam.base_cash_sale = sessionParam.getBaseCashSale();
//            sessionDataParam.report_currency_code = sessionParam.getShiftCurrencyCode();
//            sessionDataParam.base_currency_code = sessionParam.getBaseCurrencyCode();
//            sessionDataParam.opened_note = sessionParam.getOpenedNote();
//            sessionDataParam.status = sessionParam.getStatus();
//            sessionDataParam.shift_code = ConfigUtil.getItemIdInCurrentTime() + "";
//
//            registerShiftEntity.data = sessionDataParam;
//
//            rp = statement.execute(registerShiftEntity);
//            rp.setParseImplement(getClassParseImplement());
//            rp.setParseModel(PosDataRegisterShift.class);
//            List<RegisterShift> list = new ArrayList<>();
//            DataRegisterShift dataRegisterShift = (PosDataRegisterShift) rp.doParse();
//            list.add(dataRegisterShift.getRegisterShift());
//            return sumBalance(list);

            // thực thi truy vấn và parse kết quả thành object
            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
            registerShiftEntity.shift = sessionParam;

            rp = statement.execute(registerShiftEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosDataRegisterShift.class);
            DataRegisterShift dataRegisterShift = (PosDataRegisterShift) rp.doParse();
            return dataRegisterShift.getListRegisterShift();
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
    public List<RegisterShift> insertMakeAdjustment(CashTransaction cashTransaction) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_REGISTER_SHIFTS_MAKE_ADJUSTMENT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
//            MakeAdjusmentEntity makeAdjusmentEntity = new MakeAdjusmentEntity();
//            CashTransactionDataParam cashTransactionDataParam = new CashTransactionDataParam();
//            cashTransactionDataParam.shift_id = cashTransaction.getParamShiftId();
//            cashTransactionDataParam.shift_code = cashTransaction.getShiftId();
//            cashTransactionDataParam.location_id = cashTransaction.getLocationId();
//            if (cashTransaction.getType().equals(ADD_MAKE_ADJUSTMENT)) {
//                cashTransactionDataParam.amount = cashTransaction.getValue();
//                cashTransactionDataParam.base_amount = cashTransaction.getBaseValue();
//            } else {
//                cashTransactionDataParam.amount = (0 - cashTransaction.getValue());
//                cashTransactionDataParam.base_amount = (0 - cashTransaction.getBaseValue());
//            }
//            cashTransactionDataParam.balance = cashTransaction.getBalance();
//            cashTransactionDataParam.base_balance = cashTransaction.getBaseBalance();
//            cashTransactionDataParam.created_at = cashTransaction.getCreatedAt();
//            cashTransactionDataParam.note = cashTransaction.getNote();
//            cashTransactionDataParam.type = cashTransaction.getType();
//            cashTransactionDataParam.base_currency_code = cashTransaction.getBaseCurrencyCode();
//            cashTransactionDataParam.transaction_currency_code = cashTransaction.getTransactionCurrencyCode();
//            cashTransactionDataParam.open_shift_title = cashTransaction.getOpenShiftTitle();
//            cashTransactionDataParam.balance_title = cashTransaction.getBalanceTitle();
//
//            makeAdjusmentEntity.data = cashTransactionDataParam;
//
//            rp = statement.execute(makeAdjusmentEntity);
//            rp.setParseImplement(getClassParseImplement());
//            rp.setParseModel(PosDataRegisterShift.class);
//            List<RegisterShift> list = new ArrayList<>();
//            DataRegisterShift dataRegisterShift = (PosDataRegisterShift) rp.doParse();
//            list.add(dataRegisterShift.getRegisterShift());
//            return sumBalance(list);

            // thực thi truy vấn và parse kết quả thành object
            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
            registerShiftEntity.cashTransaction = cashTransaction;

            rp = statement.execute(registerShiftEntity);
            String json = rp.readResult2String();
            Gson gson = new Gson();
            List<RegisterShift> list = new ArrayList<>();
            JSONArray arrShift = new JSONArray(json);
            for (int i = 0; i < arrShift.length() ; i++) {
                PosRegisterShift registerShift = gson.fromJson(arrShift.get(i).toString(), PosRegisterShift.class);
                list.add((RegisterShift) registerShift);
            }
            return list;
        } catch (ConnectionException ex) {
            throw new DataAccessException(ex);
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        } catch (JSONException e) {
            throw new DataAccessException(e);
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
    public List<RegisterShift> closeSession(SessionParam sessionParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_REGISTER_SHIFTS_SAVE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

//            // thực thi truy vấn và parse kết quả thành object
//            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
//
//            SessionDataParam sessionDataParam = new SessionDataParam();
//            sessionDataParam.id = sessionParam.getID();
//            sessionDataParam.till_id = ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId();
//            sessionDataParam.staff_id = sessionParam.getStaffId();
//            sessionDataParam.opened_at = sessionParam.getOpenedAt();
//            sessionDataParam.closed_at = sessionParam.getCloseAt();
//            sessionDataParam.opening_amount = sessionParam.getFloatAmount();
//            sessionDataParam.base_opening_amount = sessionParam.getBaseFloatAmount();
//            sessionDataParam.closed_amount = sessionParam.getCloseAmount();
//            sessionDataParam.base_closed_amount = sessionParam.getBaseClosedAmount();
//            sessionDataParam.cash_left = sessionParam.getCashLeft();
//            sessionDataParam.base_cash_left = sessionParam.getBaseCashLeft();
//            sessionDataParam.cash_added = sessionParam.getCashAdded();
//            sessionDataParam.base_cash_added = sessionParam.getBaseCashAdded();
//            sessionDataParam.cash_removed = sessionParam.getCashRemoved();
//            sessionDataParam.base_cash_removed = sessionParam.getBaseCashRemoved();
//            sessionDataParam.cash_sale = sessionParam.getCashSale();
//            sessionDataParam.base_cash_sale = sessionParam.getBaseCashSale();
//            sessionDataParam.report_currency_code = sessionParam.getShiftCurrencyCode();
//            sessionDataParam.base_currency_code = sessionParam.getBaseCurrencyCode();
//            sessionDataParam.opened_note = sessionParam.getOpenedNote();
//            sessionDataParam.note = sessionParam.getClosedNote();
//            sessionDataParam.status = sessionParam.getStatus();
//            sessionDataParam.shift_code = sessionParam.getShiftId();
//
//            registerShiftEntity.data = sessionDataParam;
//
//            rp = statement.execute(registerShiftEntity);
//            rp.setParseImplement(getClassParseImplement());
//            rp.setParseModel(PosDataRegisterShift.class);
//            List<RegisterShift> list = new ArrayList<>();
//            DataRegisterShift dataRegisterShift = (PosDataRegisterShift) rp.doParse();
//            list.add(dataRegisterShift.getRegisterShift());
//            return sumBalance(list);

            // thực thi truy vấn và parse kết quả thành object
            sessionParam.setID(null);
            RegisterShiftEntity registerShiftEntity = new RegisterShiftEntity();
            registerShiftEntity.shift = sessionParam;

            rp = statement.execute(registerShiftEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosDataRegisterShift.class);
            DataRegisterShift dataRegisterShift = (PosDataRegisterShift) rp.doParse();
            return dataRegisterShift.getListRegisterShift();
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

                float total_base_sales = 0;
                List<SaleSummary> listSale = shift.getSalesSummary();
                if (listSale != null && listSale.size() > 0) {
                    for (SaleSummary sale : listSale) {
                        total_base_sales += sale.getBasePaymentAmount();
                    }
                }
                shift.setBaseTotalSales(total_base_sales);
                shift.setTotalSales(ConfigUtil.convertToPrice(total_base_balance));
            }
        }
        return shiftList;
    }
}
