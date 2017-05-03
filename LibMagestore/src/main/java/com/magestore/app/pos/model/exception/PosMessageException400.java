package com.magestore.app.pos.model.exception;

import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Mike on 2/13/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosMessageException400 extends PosMessageException implements MessageException {
    List<String> parameters;

    /**
     * get message
     * @return
     */
    @Override
    public String getMessage() {
        String returnMsg = super.getMessage();

        return super.getMessage();
    }
}
