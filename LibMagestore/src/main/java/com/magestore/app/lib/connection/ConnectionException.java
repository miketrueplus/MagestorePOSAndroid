package com.magestore.app.lib.connection;

import com.magestore.app.lib.exception.MagestoreException;

/**
 * Quản lý các exception của Connection
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ConnectionException extends MagestoreException {
    public static final String EXCEPTION_INTERNAL_HOST = "com.magestore.app.lib.connection.ConnectionException.InternalHostException";
    public static final String EXCEPTION_PAGE_NOT_FOUND = "com.magestore.app.lib.connection.ConnectionException.PageNotFoundException";
    public static final String EXCEPTION_TIMEOUT = "com.magestore.app.lib.connection.ConnectionException.TimeoutException";

    public ConnectionException(String strCode) {
        super(strCode);
    }


    public ConnectionException(String strCode, String message) {
        super(strCode, message);
    }

    public ConnectionException(String strCode, String message, Throwable cause) {
        super(strCode, message, cause);
    }

    public ConnectionException(String strCode, Throwable cause) {
        super(strCode, cause);
    }
}
