package com.magestore.app.lib.connection;

/**
 * Quản lý các exception của Connection
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
