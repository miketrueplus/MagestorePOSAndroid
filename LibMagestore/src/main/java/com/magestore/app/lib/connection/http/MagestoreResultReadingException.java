package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.exception.MessageException;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.model.exception.PosMessagException;
import com.magestore.app.pos.parse.gson2pos.Gson2PosMesssageExceptionImplement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public void setParseModel(Class parseEntity) throws ConnectionException, IOException {
        super.setParseModel(PosMessagException.class);
    }

    @Override
    public ParseModel doParse() throws ParseException, IOException, ConnectionException {
        PosMessagException msgExp = (PosMessagException) super.doParse();
        ConnectionException exp = new ConnectionException(msgExp.getMessage());
//        ConnectionException exp = new ConnectionException("Hi");
        throw exp;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        doParse();
        return null;
    }

    @Override
    public void writeOutputStream(OutputStream out) throws ParseException, ConnectionException, IOException {
        doParse();
    }

    @Override
    public void writeToFile(File file) throws ParseException, ConnectionException, IOException {
        doParse();
    }

    @Override
    public String readResult2String() throws ConnectionException, IOException {
        super.setParseImplement(Gson2PosMesssageExceptionImplement.class);
        super.setParseModel(PosMessagException.class);
        PosMessagException msgExp = (PosMessagException) super.doParse();
        return msgExp.getMessage();
    }
}
