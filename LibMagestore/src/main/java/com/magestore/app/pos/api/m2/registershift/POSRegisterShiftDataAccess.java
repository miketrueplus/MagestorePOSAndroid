package com.magestore.app.pos.api.m2.registershift;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
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
    /**
     * Trả về list register shift
     *
     * @return Danh sách register shift
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<RegisterShift> getRegisterShifts() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) doAPI(Gson2PosListRegisterShift.class, POSAPI.REST_REGISTER_SHIFTS_GET_LISTING, null, POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID);
        List<RegisterShift> list = (List<RegisterShift>) (List<?>) (listRegisterShift.items);
        return list;
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
            Object wrapCash = new Object() {
                public PosCashTransaction cashTransaction = (PosCashTransaction) pCashTransaction;
            };
            rp = statement.execute(wrapCash);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListRegisterShift.class);
            Gson2PosListRegisterShift listRegisterShift = (Gson2PosListRegisterShift) rp.doParse();
            List<RegisterShift> list = (List<RegisterShift>) (List<?>) listRegisterShift;
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
}
