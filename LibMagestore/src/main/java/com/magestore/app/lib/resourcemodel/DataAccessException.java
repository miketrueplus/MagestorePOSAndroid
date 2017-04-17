package com.magestore.app.lib.resourcemodel;

import com.magestore.app.lib.exception.MagestoreException;

/**
 * Các exception do lớp data access sinh ra
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class DataAccessException extends MagestoreException {

    public DataAccessException(String code, String message) {
        super(code, message);
    }

    public DataAccessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DataAccessException(String code, Throwable cause) {
        super(code, cause);
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}