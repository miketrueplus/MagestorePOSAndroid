package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.ConnectionException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn*/

public class MagestoreResultReadingException extends MagestoreResultReading {
    /**
     * Nhận kết quả từ inputstream và chuyển thành chuỗi JSON
     *
     * @param inputStream
     */
    protected MagestoreResultReadingException(InputStream inputStream, int httpErrorCode) throws ConnectionException, IOException {
        super(inputStream);
    }
//
//    @Override
//    public void setParseImplement(Class parseImplement) throws ConnectionException, IOException, ParseException {
//        super.setParseImplement(Gson2PosMesssageExceptionImplement.class);
//    }
//
//    @Override
//    public void setParseModel(Class parseEntity) throws ConnectionException, IOException {
//        super.setParseModel(PosMessageException.class);
//    }
//
//    @Override
//    public ParseModel doParse() throws ParseException, IOException, ConnectionException {
//        super.setParseImplement(Gson2PosMesssageExceptionImplement.class);
//        super.setParseModel(PosMessageException.class);
//        PosMessageException msgExp = (PosMessageException) super.doParse();
//        ConnectionException exp = new ConnectionException(msgExp.getMessage());
//        throw exp;
//    }
//
//    @Override
//    public InputStream getInputStream() throws IOException {
//        doParse();
//        return null;
//    }
//
//    @Override
//    public void writeOutputStream(OutputStream out) throws ParseException, ConnectionException, IOException {
//        doParse();
//    }
//
//    @Override
//    public void writeToFile(File file) throws ParseException, ConnectionException, IOException {
//        doParse();
//    }
//
//    @Override
//    public String readResult2String() throws ConnectionException, IOException {
//        super.setParseImplement(Gson2PosMesssageExceptionImplement.class);
//        super.setParseModel(PosMessageException.class);
//        PosMessageException msgExp = (PosMessageException) super.doParse();
//        return msgExp.getMessage();
//    }
}
