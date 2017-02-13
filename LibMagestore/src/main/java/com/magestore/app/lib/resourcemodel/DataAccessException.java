package com.magestore.app.lib.resourcemodel;

/**
 * Các exception do lớp data access sinh ra
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}