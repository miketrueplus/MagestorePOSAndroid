package com.magestore.app.pos.model.magento.exception;

import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Quản lý các message từ server khi có lỗi
 * Created by Mike on 1/13/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosMessageException extends PosAbstractModel implements MessageException {
    String code;
    String message;
    String trace;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        if (message == null) return "Unknow exception";
        return message;
    }

    @Override
    public String getTrace() {
        return trace;
    }
}
