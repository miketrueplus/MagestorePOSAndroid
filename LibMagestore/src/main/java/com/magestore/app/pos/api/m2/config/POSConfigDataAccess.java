package com.magestore.app.pos.api.m2.config;

import com.google.common.util.concurrent.Service;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreCacheConnection;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProduct;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSConfigDataAccess extends POSAbstractDataAccess implements ConfigDataAccess {

    /**
     * Trả lại 1 danh sách các config
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Config getConfig() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Class oldImplement = getClassParseImplement();
        setParseImplement(Gson2PosConfigParseImplement.class);

        Connection connection = null;
        Statement statement = null;
        ParamBuilder paramBuilder = null;
        Thread thread = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CONFIG_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // load cache
            final MagestoreCacheConnection cache = new MagestoreCacheConnection<Gson2PosConfigParseImplement, PosConfig>()
                    .setCacheName("POSConfigDataAccess.getConfig")
                    .setStatement(statement)
                    .setForceOutOfDate(false)
                    .setReloadCacheLater(true)
                    .setParseImplement(Gson2PosConfigParseImplement.class)
                    .setParseModel(PosConfig.class);
            Config config = (Config) cache.excute();

//            thread = new Thread() {
//                public ParamBuilder paramBuilder;
//                public Connection connection;
//                public Statement statement;
//                @Override
//                public void run() {
//                    if (cache != null)
//                        while (!cache.isFinishBackgroud()) {
//                            try {
//                                sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    // đóng param builder
//                    if (paramBuilder != null) paramBuilder.clear();
//                    paramBuilder = null;
//
////                     đóng statement
//                    if (statement != null) statement.close();
//                    statement = null;
//
////                     đóng connection
//                    if (connection != null) connection.close();
//                    connection = null;
//                }
//            };
//            thread.

            return config;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (thread != null)
                thread.start();
            // đóng param builder
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;

            // đóng statement
//            if (statement != null)statement.close();
//            statement = null;

            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
        }
    }

    /**
     * Trả lại 1 config dưới dạng 1 string
     *
     * @param configPath
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        String value = doApi2String(POSAPI.REST_CONFIG_GET_,
//                null,
//                POSAPI.PARAM_CONFIG_PATH, configPath,
//                POSAPI.PARAM_CONFIG_ID, id,
//                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID
//        );
//        return value;        }

        String str = doApi2String(POSAPI.REST_CONFIG_GET_LISTING,
                null,
                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID);
        return str;
    }
}
