package com.magestore.app.pos.model.exception;

import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Mike on 2/13/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosMessageException500 extends PosAbstractModel implements MessageException {

    class Error {
        String code;
        String message;
        String trace;
    };
    class Messages {
        List<Error> error;
    }
    Messages messages;

    /**
     * get message
     * @return
     */
    @Override
    public String getMessage() {
        if (messages == null) return "Unknow exception";
        if (messages.error == null) return "Unknow exception";
        if (messages.error.size() == 0) return "Unknow exception";
        return messages.error.get(0).message;
    }

    /**
     * get trace
     * @return
     */
    @Override
    public String getTrace() {
        if (messages == null) return "Unknow trace";
        if (messages.error == null) return "Unknow trace";
        if (messages.error.size() == 0) return "Unknow trace";
        return messages.error.get(0).trace;
    }
}
