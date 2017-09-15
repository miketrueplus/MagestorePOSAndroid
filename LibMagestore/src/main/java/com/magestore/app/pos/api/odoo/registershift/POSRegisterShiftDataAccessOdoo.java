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
import com.magestore.app.pos.model.registershift.PosCashTransaction;
import com.magestore.app.pos.model.registershift.PosDataListRegisterShift;
import com.magestore.app.pos.model.registershift.PosPointOfSales;
import com.magestore.app.pos.model.registershift.PosRegisterShift;
import com.magestore.app.pos.model.registershift.PosSaleSummary;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

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

    public class Gson2PosListOrderParseModelOdoo extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<PosRegisterShift>>() {
            }
                    .getType(), new RegisterShiftConverter());
            return builder.create();
        }

        private String POS_ID = "config_id";
        private String POS_NAME = "config_name";
        private String SHIFT_ID = "id";
        private String SHIFT_OPEN_AT = "start_at";
        private String SHIFT_CLOSE_AT = "stop_at";
        private String SHIFT_STATUS = "state";
        private String SHIFT_OPEN = "opened";
        private String OPEN_AMOUNT = "cash_register_balance_start";
        private String CLOSE_AMOUNT = "cash_register_balance_end_real";
        private String CASH_SALE = "base_cash_sale";
        private String TOTAL_SALE = "base_total_sales";
        private String CASH_TRANSACTION = "cash_transaction";
        private String PAYMENT_DETAIL = "statement_detail";
        private String PAYMENT_NAME = "journal_name";
        private String PAYMENT_ID = "journal_id";
        private String PAYMENT_TYPE = "journal_type";
        private String PAYMENT_AMOUNT = "base_payment_amount";
        private String PAYMENT_REFERENCE = "reference";
        private String PAYMENT_DIFFERENCE_ZERO = "is_difference_zero";
        private String POS_CONFIG = "pos_config";
        private String POS_SESSION_USENAME = "pos_session_username";
        private String POS_TAX_INCL = "iface_tax_included";
        private String POS_RECEIPT_HEADER = "receipt_header";
        private String POS_CASH_CONTROL = "cash_control";
        private String POS_TIP_PRODUCT_ID = "tip_product_id";
        private String POS_CURRENT_SESSION_STATE = "current_session_state";
        private String POS_CREATE_DATE = "create_date";
        private String POS_PRINT_AUTO = "iface_print_auto";
        private String POS_INVOICE = "iface_invoicing";
        private String POS_RECEIPT_FOOTER = "receipt_footer";
        private String POS_CASH_DRAWER = "iface_cashdrawer";
        private String POS_DISCOUNT = "iface_discount";
        private String POS_DISCOUNT_PC = "discount_pc";
        private String POS_DISCOUNT_PRODUCT_ID = "discount_product_id";

        public class RegisterShiftConverter implements JsonDeserializer<List<PosRegisterShift>> {
            @Override
            public List<PosRegisterShift> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<PosRegisterShift> listRegisterShift = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_shift = json.getAsJsonArray();
                    if (arr_shift != null && arr_shift.size() > 0) {
                        for (JsonElement el_shift : arr_shift) {
                            JsonObject obj_shift = el_shift.getAsJsonObject();
                            PosRegisterShift shift = new PosRegisterShift();
                            String id = obj_shift.remove(SHIFT_ID).getAsString();
                            String pos_id = obj_shift.remove(POS_ID).getAsString();
                            String pos_name = obj_shift.remove(POS_NAME).getAsString();
                            String create_at = obj_shift.remove(SHIFT_OPEN_AT).getAsString();
                            String close_at = obj_shift.remove(SHIFT_CLOSE_AT).getAsString();
                            String status = obj_shift.remove(SHIFT_STATUS).getAsString();
                            float open_amount = obj_shift.remove(OPEN_AMOUNT).getAsFloat();
                            float close_amount = obj_shift.remove(CLOSE_AMOUNT).getAsFloat();
                            float cash_sale = obj_shift.remove(CASH_SALE).getAsFloat();
                            float total_sale = obj_shift.remove(TOTAL_SALE).getAsFloat();
                            shift.setID(id);
                            shift.setPosId(pos_id);
                            shift.setPosName(StringUtil.checkJsonData(pos_name) ? pos_name : "");
                            shift.setOpenedAt(StringUtil.checkJsonData(create_at) ? create_at : "");
                            shift.setClosedAt(StringUtil.checkJsonData(close_at) ? close_at : "");
                            shift.setStatus(status.equals(SHIFT_OPEN) ? "0" : "1");
                            shift.setFloatAmount(open_amount);
                            shift.setBaseFloatAmount(ConfigUtil.convertToBasePrice(open_amount));
                            shift.setClosedAmount(close_amount);
                            shift.setBaseClosedAmount(ConfigUtil.convertToBasePrice(close_amount));
                            shift.setBaseCashSales(cash_sale);
                            shift.setBaseTotalSales(total_sale);
                            List<CashTransaction> listTransaction = new ArrayList<>();
                            if (obj_shift.has(CASH_TRANSACTION) && obj_shift.get(CASH_TRANSACTION).isJsonArray()) {
                                JsonArray arr_transaction = obj_shift.getAsJsonArray(CASH_TRANSACTION);
                                if (arr_transaction != null && arr_transaction.size() > 0) {
                                    for (JsonElement el_transation : arr_transaction) {
                                        JsonObject obj_transaction = el_transation.getAsJsonObject();
                                        PosCashTransaction transaction = new Gson().fromJson(obj_transaction, PosCashTransaction.class);
                                        listTransaction.add(transaction);
                                    }
                                }
                            }
                            shift.setCashTransaction(listTransaction);
                            List<SaleSummary> listSaleSummary = new ArrayList<>();
                            if (obj_shift.has(PAYMENT_DETAIL) && obj_shift.get(PAYMENT_DETAIL).isJsonArray()) {
                                JsonArray arr_payment = obj_shift.getAsJsonArray(PAYMENT_DETAIL);
                                if (arr_payment != null && arr_payment.size() > 0) {
                                    for (JsonElement el_payment : arr_payment) {
                                        JsonObject obj_payment = el_payment.getAsJsonObject();
                                        PosSaleSummary saleSummary = new PosSaleSummary();
                                        String payment_id = obj_payment.remove(PAYMENT_ID).getAsString();
                                        String payment_name = obj_payment.remove(PAYMENT_NAME).getAsString();
                                        String payment_type = obj_payment.remove(PAYMENT_TYPE).getAsString();
                                        float payment_amount = obj_payment.remove(PAYMENT_AMOUNT).getAsFloat();
                                        boolean payment_reference = obj_payment.remove(PAYMENT_REFERENCE).getAsBoolean();
                                        boolean payment_difference_zero = obj_payment.remove(PAYMENT_DIFFERENCE_ZERO).getAsBoolean();
                                        saleSummary.setID(payment_id);
                                        saleSummary.setPaymentMethod(payment_type);
                                        saleSummary.setMethodTitle(payment_name);
                                        saleSummary.setBasePaymentAmount(payment_amount);
                                        if (payment_amount > 0) {
                                            listSaleSummary.add(saleSummary);
                                        }
                                    }
                                }
                            }
                            shift.setSalesSummary(listSaleSummary);
                            if (obj_shift.has(POS_CONFIG) && obj_shift.get(POS_CONFIG).isJsonObject()) {
                                JsonObject obj_pos = obj_shift.get(POS_CONFIG).getAsJsonObject();
                                PosPointOfSales pos = new PosPointOfSales();
                                boolean cash_control = obj_pos.remove(POS_CASH_CONTROL).getAsBoolean();
                                pos.setCashControl(cash_control);
                                if (obj_pos.has(POS_DISCOUNT)) {
                                    boolean pos_discount = obj_pos.remove(POS_DISCOUNT).getAsBoolean();
                                    pos.setIfaceDiscount(pos_discount);
                                }
                                if (obj_pos.has(POS_DISCOUNT_PC)) {
                                    float pos_discount_pc = obj_pos.remove(POS_DISCOUNT_PC).getAsFloat();
                                    pos.setDiscountPC(pos_discount_pc);
                                }
                                if (obj_pos.has(POS_DISCOUNT_PRODUCT_ID)) {
                                    String pos_discount_product_id = obj_pos.remove(POS_DISCOUNT_PRODUCT_ID).getAsString();
                                    pos.setDiscountProductId(pos_discount_product_id);
                                }
                                shift.setPosConfig(pos);
                            }
                            listRegisterShift.add(shift);
                        }
                    }
                }
                return listRegisterShift;
            }
        }
    }

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
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
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
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
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
            openSessionEntity.cash_control = ConfigUtil.isCashControl();
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
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
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
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
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
    public List<RegisterShift> closeSession(SessionParam sessionParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_CLOSE_SESSION);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            CloseSessionEntity closeSessionEntity = new CloseSessionEntity();
            closeSessionEntity.cash_control = ConfigUtil.isCashControl();
            closeSessionEntity.session_id = sessionParam.getID();

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

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(closeSessionEntity);
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
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
