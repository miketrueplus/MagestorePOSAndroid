package com.magestore.app.lib.parse;

import com.magestore.app.lib.exception.MagestoreException;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ParseException extends MagestoreException {
    public static final String EXCEPTION_RESPONSE_FORMAT = "com.magestore.app.lib.parse.ParseException.FormatException";

    public ParseException(String code, String message) {
        super(code, message);
    }

    public ParseException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ParseException(String code, Throwable cause) {
        super(code, cause);
    }

    public ParseException(String code) {
        super(code);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}
