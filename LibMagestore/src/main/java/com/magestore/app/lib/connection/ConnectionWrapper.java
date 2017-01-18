package com.magestore.app.lib.connection;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.pos.api.m2.POSDataAccessSession;

/**
 * Created by Mike on 1/17/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class ConnectionWrapper  {
        MagestoreContext context;
        Connection connection;
        Statement statement;
        ParamBuilder paramBuilder;
        ResultReading resultReading;

        public ConnectionWrapper(MagestoreContext context) {
            this.context = context;
        };

    ConnectionWrapper setContext(MagestoreContext context) {
            this.context = context;
            return this;
        }

    ConnectionWrapper createConnection() {
            connection = ConnectionFactory.generateConnection(context, POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            return this;
        }

    ConnectionWrapper setQuery(String pstrQuery) {
            statement = connection.createStatement();
            statement.prepareQuery(pstrQuery);
            return this;
        }

    ParamBuilder getParamBuilder() {
        return statement.getParamBuilder();
    }

    ConnectionWrapper executeAndClose() {
        return this;
    }

    ConnectionWrapper close() {
        if (resultReading != null) resultReading.close();
        if (statement != null) statement.close();
        if (connection!=null) connection.close();
        return this;
    }
}
