package com.magestore.app.pos.api.m2.plugins;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.plugins.PluginsDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.checkout.PosCheckout;
import java.io.IOException;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSPluginsDataAccess extends POSAbstractDataAccess implements PluginsDataAccess {

    @Override
    public Checkout addGiftCard(GiftCard giftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PLUGIN_ADD_GIFTCARD);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(giftCard);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);
            return (Checkout) rp.doParse();
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
    public Checkout removeGiftCard(GiftCard giftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PLUGIN_REMOVE_GIFTCARD);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(giftCard);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);
            return (Checkout) rp.doParse();
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
