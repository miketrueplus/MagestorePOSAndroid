package com.magestore.app.lib.model.exception;

/**
 * Chứa thông điệp báo lỗi từ server
 * Created by Mike on 1/13/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface MessageException {
    String getCode();

    String getMessage();
    String getTrace();
}
