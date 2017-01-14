package com.magestore.app.pos.model.exception;

import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Quản lý các message từ server khi có lỗi
 * Created by Mike on 1/13/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosMessagException extends PosAbstractModel implements MessageException {
    String message;
    String trace;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getTrace() {
        return trace;
    }
}
