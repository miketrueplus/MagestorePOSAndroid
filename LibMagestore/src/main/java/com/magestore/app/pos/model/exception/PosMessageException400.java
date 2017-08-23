package com.magestore.app.pos.model.exception;

import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.util.StringUtil;

import java.util.List;

/**
 * Created by Mike on 2/13/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosMessageException400 extends PosMessageException implements MessageException {
    class Error {
        String code;
        String message;
        String trace;
    }

    class Messages {
        List<Error> error;
    }

    Messages messages;

    /**
     * get message
     *
     * @return
     */
    @Override
    public String getCode() {
        if (!StringUtil.isNullOrEmpty(code)) {
            return code;
        }
        if (messages == null) return "Unknow exception";
        if (messages.error == null) return "Unknow exception";
        if (messages.error.size() == 0) return "Unknow exception";
        return messages.error.get(0).code;
    }

    /**
     * get message
     *
     * @return
     */
    @Override
    public String getMessage() {
        if (!StringUtil.isNullOrEmpty(message)) {
            return message;
        }
        if (messages == null) return "Unknow exception";
        if (messages.error == null) return "Unknow exception";
        if (messages.error.size() == 0) return "Unknow exception";
        return messages.error.get(0).message;
    }

    /**
     * get trace
     *
     * @return
     */
    @Override
    public String getTrace() {
        if (!StringUtil.isNullOrEmpty(trace)) {
            return trace;
        }
        if (messages == null) return "Unknow trace";
        if (messages.error == null) return "Unknow trace";
        if (messages.error.size() == 0) return "Unknow trace";
        return messages.error.get(0).trace;
    }
}
