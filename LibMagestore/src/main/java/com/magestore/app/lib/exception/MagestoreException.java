package com.magestore.app.lib.exception;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.util.StringUtil;

/**
 * Quản lý các exception của Connection
 * Created by Mike on 17/04/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreException extends RuntimeException {
    // Mã lỗi để sử dụng trong từ điển
    String mstrCode;


    /**
     * Khởi tạo
     *
     * @param code
     * @param message
     */
    public MagestoreException(String code, String message) {
        super(message);
        setCode(code);
    }

    /**
     * Khởi tạo
     *
     * @param code
     * @param message
     * @param cause
     */
    public MagestoreException(String code, String message, Throwable cause) {
        super(message, cause);
        setCode(code);
    }

    /**
     * Khởi tạo
     *
     * @param code
     * @param cause
     */
    public MagestoreException(String code, Throwable cause) {
        super(cause);
        setCode(code);
    }

    /**
     * Khởi tạo code
     * @param code
     */
    public MagestoreException(String code) {
        super();
        setCode(code);
    }

    public MagestoreException(Throwable cause) {
        super(cause);
        setCode(cause.getClass().getName());
        if (cause instanceof MagestoreException) {
            setCode(((MagestoreException) cause).getCode());
        }
        else {
            setCode(cause.getClass().getName());
        }
    }

    /**
     * Mã lỗi sử dụng trong từ điển
     *
     * @return
     */
    public String getCode() {
        return mstrCode;
    }

    /**
     * Đặt code lỗi
     *
     * @param code
     */
    protected void setCode(String code) {
        mstrCode = code;
    }
}
