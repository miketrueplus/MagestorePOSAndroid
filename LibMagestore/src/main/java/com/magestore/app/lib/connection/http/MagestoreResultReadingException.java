package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.pos.model.exception.PosMessagException;
import com.magestore.app.pos.parse.gson2pos.Gson2PosMesssageExceptionImplement;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class MagestoreResultReadingException extends MagestoreResultReading {
    /**
     * Nhận kết quả từ inputstream và chuyển thành chuỗi JSON
     *
     * @param inputStream
     */
    protected MagestoreResultReadingException(InputStream inputStream, int httpErrorCode) throws ConnectionException, IOException {
        super(inputStream);
    }

    @Override
    public void setParseImplement(Class parseImplement) throws ConnectionException, IOException, ParseException {
        super.setParseImplement(Gson2PosMesssageExceptionImplement.class);
    }

    @Override
    public void setParseEntity(Class parseEntity) throws ConnectionException, IOException {
        super.setParseEntity(PosMessagException.class);
    }

    @Override
    public ParseModel doParse() throws ParseException, IOException, ConnectionException {
        PosMessagException msgExp = (PosMessagException) super.doParse();
        ConnectionException exp = new ConnectionException(msgExp.getMessage());
        throw exp;
    }
}
