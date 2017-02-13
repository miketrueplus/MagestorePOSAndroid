package com.magestore.app.lib.parse;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ParseException extends RuntimeException {
    public ParseException(String message) {
        super(message);
    }
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
    public ParseException(Throwable cause) {
        super(cause);
    }
}
