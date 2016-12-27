package com.magestore.app.lib.gateway;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class GatewayException extends RuntimeException {
    public GatewayException(String message) {
        super(message);
    }
    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }
    public GatewayException(Throwable cause) {
        super(cause);
    }
}