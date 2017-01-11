package com.magestore.app.pos.api.m2.config;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;

import java.io.IOException;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSConfigDataAccess extends POSAbstractDataAccess implements ConfigDataAccess {

    /**
     * Trả lại 1 danh sách các config
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
        Config config = (PosConfig) doAPI(PosConfig.class,
                POSAPI.REST_CONFIG_GET_LISTING,
                null,
                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID
        );
        return (Config) config;
    }

    /**
     * Trả lại 1 config dưới dạng 1 string
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
