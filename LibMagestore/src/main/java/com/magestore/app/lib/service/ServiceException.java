package com.magestore.app.lib.service;

/**
 * Exception các lỗi của usecase
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
