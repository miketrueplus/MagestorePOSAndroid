package com.magestore.app.lib.service;

import com.magestore.app.lib.exception.MagestoreException;

/**
 * Exception các lỗi của usecase
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ServiceException extends MagestoreException {
    public static final String EXCEPTION_QUANTITY_NOT_ENOUGH = "com.magestore.app.lib.connection.ServiceException.QuantityNotEnough";
    public static final String EXCEPTION_QUANTITY_OUT_OF_STOCK = "com.magestore.app.lib.connection.ServiceException.QuantityOutOfStock";
    public static final String EXCEPTION_QUANTITY_REACH_MAXIMUM = "com.magestore.app.lib.connection.ServiceException.QuantityReachMaximum";
    public static final String EXCEPTION_QUANTITY_REACH_MINIMUM = "com.magestore.app.lib.connection.ServiceException.QuantityReachMinimum";

    public ServiceException(String strCode, String message) {
        super(strCode, message);
    }

    public ServiceException(String strCode, String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String strCode, Throwable cause) {
        super(strCode, cause);
    }
}
