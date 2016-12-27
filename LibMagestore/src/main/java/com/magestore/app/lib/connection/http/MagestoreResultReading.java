package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseFactory;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;

/**
 * Parsing kết quả response trả về từ server
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class MagestoreResultReading implements ResultReading {
    private InputStream mInputStream;
    private Class mclParseEntity;
    private ParseImplement mParseImplement;
    private HttpURLConnection mHttpConnection;

    /**
     * Nhận kết quả từ inputstream và chuyển thành chuỗi JSON
     * @param inputStream
     * @throws IOException
     */
    protected MagestoreResultReading(InputStream inputStream) throws ConnectionException {
        mInputStream = new BufferedInputStream(inputStream);
    }

    /**
     * Lưu lại http connection để đóng cùng với input stream
     * @param connection
     */
    protected void setHttpConnection(HttpURLConnection connection) {
        mHttpConnection = connection;
    }

    /**
     * Đóng stream đọc kết quả
     * @throws ConnectionException
     */
    @Override
    public void close() {
        // đóng parse
        if (mParseImplement != null) mParseImplement.close();
            mParseImplement = null;

        // Đóng input stream
        if (mInputStream != null)
            try {
                mInputStream.close();
            } catch (IOException e) {
            }
        mInputStream = null;

        // Disconnect HTTP Connection
        if (mHttpConnection != null)
            mHttpConnection.disconnect();
        mHttpConnection = null;
    }

    public boolean isOpenned() {
        return (mInputStream != null);
    }

    public boolean isClosed() {
        return (mInputStream == null);
    }

    /**
     * Đọc kết quả từ result sang string
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public String readResult2String() throws ConnectionException, IOException {
        return StringUtil.convertStreamToString(new BufferedInputStream(mInputStream));
    }

    /**
     * Đọc kết quả từ result sang object
     * @param cl
     * @param clEntity
     * @return
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     */
//    public ParseEntity readResult(Class<ParseImplement> cl, Class<ParseEntity> clEntity) throws ConnectionException, ParseException, IOException {
////        Type type = new TypeToken<Gson2PosListParseEntity<PosProduct>>(){}.getType();
//        ParseImplement jsonParse = (ParseImplement) ParseFactory.generateParseImplement(cl);
//        jsonParse.prepareParse(getInputStream(), clEntity);
//        jsonParse.doParse();
//        return jsonParse.getParseEntity();
//    }

    /**
     * Trả lại input stream
     * @return
     */
    @Override
    public InputStream getInputStream() {
        return mInputStream;
    }

    /**
     * Đặt parse entity class
     * @param parseEntity
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public void setParseEntity(Class parseEntity) throws ConnectionException, IOException {
        mclParseEntity = parseEntity;
    }

    /**
     * Đặt parse implement để chuyển đổi kết quả thành đối tượng
     * @param parseImplement
     * @throws ConnectionException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public void setParseImplement(Class parseImplement) throws ConnectionException, IOException, ParseException {
        mParseImplement = ParseFactory.generateParseImplement(parseImplement);
    }

    /**
     * Đặt parse implement để chuyển dodỏi kết quả thành đối tượng
     * @param parseImplement
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public void setParseImplement(ParseImplement parseImplement) throws ConnectionException, IOException {
        mParseImplement = parseImplement;
    }

    /**
     * Parse kết quả trả về parse entity
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ParseEntity doParse() throws ParseException, IOException {
        // Thực thi parse
        mParseImplement.prepareParse(getInputStream(), mclParseEntity);
        mParseImplement.doParse();

        // Trả về parse entity
        return mParseImplement.getParseEntity();
    }

    /**
     * Parse kết quả trả về parse entity
     * @param parseEntity
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ParseEntity doParse(Class parseEntity) throws IOException, ParseException {
        setParseEntity(parseEntity);
        return doParse();
    }

    /**
     * Parse kết quả trả về parse entity
     * @param parseImplement
     * @param parseEntity
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ParseEntity doParse(ParseImplement parseImplement, Class parseEntity) throws IOException, ParseException {
        setParseImplement(parseImplement);
        setParseEntity(parseEntity);
        return doParse();
    }
}