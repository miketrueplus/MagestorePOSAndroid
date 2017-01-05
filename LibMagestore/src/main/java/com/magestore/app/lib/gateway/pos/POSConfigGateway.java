package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.entity.pos.PosConfig;
import com.magestore.app.lib.gateway.ConfigGateway;
import com.magestore.app.lib.gateway.GatewayException;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.gson2pos.Gson2PosConfigParseImplement;

import java.io.IOException;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSConfigGateway extends POSAbstractGateway implements ConfigGateway {

    /**
     * Trả lại 1 danh sách các config
     * @return
     * @throws GatewayException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Config getConfig() throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Class oldImplement = getClassParseImplement();
        setParseImplement(Gson2PosConfigParseImplement.class);
        Config config = (PosConfig) doAPI(PosConfig.class,
                POSAPI.REST_CONFIG_GET_LISTING,
                null,
                POSAPI.PARAM_SESSION_ID, POSGatewaySession.REST_SESSION_ID
        );
        return (Config) config;
    }

    /**
     * Trả lại 1 config dưới dạng 1 string
     * @param configPath
     * @return
     * @throws GatewayException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public String getConfig(String configPath) throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        String value = doApi2String(POSAPI.REST_CONFIG_GET_,
//                null,
//                POSAPI.PARAM_CONFIG_PATH, configPath,
//                POSAPI.PARAM_CONFIG_ID, id,
//                POSAPI.PARAM_SESSION_ID, POSGatewaySession.REST_SESSION_ID
//        );
//        return value;        }

        String str = doApi2String(POSAPI.REST_CONFIG_GET_LISTING,
                null,
                POSAPI.PARAM_SESSION_ID, POSGatewaySession.REST_SESSION_ID);
        return str;
    }
}
