package com.magestore.app.pos.api.m2.config;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreFileCacheConnection;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.model.config.PosConfigDefault;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSConfigDataAccess extends POSAbstractDataAccess implements ConfigDataAccess {
    // Cache config đầu tiên
    private static Config mConfig;

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
    public Config retrieveConfig() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Class oldImplement = getClassParseImplement();
        setParseImplement(Gson2PosConfigParseImplement.class);

        Connection connection = null;
        Statement statement = null;
        ParamBuilder paramBuilder = null;
        Thread thread = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CONFIG_GET_LISTING);
            statement.setEnableCache("POSConfigDataAccess.getConfig");

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực hiện truy vấn
            rp = statement.execute();
            rp.setParseImplement(Gson2PosConfigParseImplement.class);
            rp.setParseModel(PosConfig.class);
            mConfig = (Config) rp.doParse();
            return mConfig;
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
    public String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        // trả lại giá trị
        return mConfig.getValue(configPath).toString();
    }

    /**
     * Lấy customer group trong config
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Map<String, String> getCustomerGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        // Chuyển đối custome
        ArrayList<LinkedTreeMap> customerGroupList = (ArrayList) mConfig.getValue("customerGroup");
        LinkedTreeMap<String, String> returnCustomerGroup = new LinkedTreeMap<String, String>();
        for (LinkedTreeMap customerGroup: customerGroupList) {
            Double id = (Double) customerGroup.get("id");
            returnCustomerGroup.put(String.format("%.0f", id), customerGroup.get("code").toString());
        }
        return returnCustomerGroup;
    }
}
