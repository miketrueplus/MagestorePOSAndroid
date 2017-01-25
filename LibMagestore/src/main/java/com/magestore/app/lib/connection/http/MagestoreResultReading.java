package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.lib.parse.ParseFactory;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.util.FileUtil;
import com.magestore.app.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Parsing kết quả response trả về từ server
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class MagestoreResultReading implements ResultReading {
    private InputStream mInputStream;
    private Class mclParseModel;
    private ParseImplement mParseImplement;
    private HttpURLConnection mHttpConnection;

    /**
     * Nhận kết quả từ inputstream và chuyển thành chuỗi JSON
     *
     * @param inputStream
     * @throws IOException
     */
    protected MagestoreResultReading(InputStream inputStream) throws ConnectionException {
        mInputStream = new BufferedInputStream(inputStream);
    }

    /**
     * Lưu lại http connection để đóng cùng với input stream
     *
     * @param connection
     */
    protected void setHttpConnection(HttpURLConnection connection) {
        mHttpConnection = connection;
    }

    /**
     * Đóng stream đọc kết quả
     *
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
     *
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public String readResult2String() throws ConnectionException, IOException {
        return StringUtil.convertStreamToString(new BufferedInputStream(mInputStream));
    }

    /**
     * Đọc kết quả từ result sang string
     *
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public String readResult2String(String strDefault) throws ConnectionException, IOException {
        String strReturn = readResult2String();
        if (strReturn == null) return strDefault;
        return strReturn;
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
//    public ParseModel readResult(Class<ParseImplement> cl, Class<ParseModel> clEntity) throws ConnectionException, ParseException, IOException {
////        Type type = new TypeToken<Gson2PosListParseModel<PosProduct>>(){}.getType();
//        ParseImplement jsonParse = (ParseImplement) ParseFactory.generateParseImplement(cl);
//        jsonParse.prepareParse(getInputStream(), clEntity);
//        jsonParse.doParse();
//        return jsonParse.getParseEntity();
//    }

    /**
     * Trả lại input stream
     *
     * @return
     */
    @Override
    public InputStream getInputStream() {
        return mInputStream;
    }

    /**
     * Đặt parse Model class
     *
     * @param parseModel
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public void setParseModel(Class parseModel) throws ConnectionException, IOException {
        mclParseModel = parseModel;
    }

    /**
     * Đặt parse implement để chuyển đổi kết quả thành đối tượng
     *
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
     *
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
     *
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ParseModel doParse() throws ParseException, IOException {
        // Thực thi parse
        mParseImplement.prepareParse(getInputStream(), mclParseModel);
        mParseImplement.doParse();

        // Trả về parse Model
        return mParseImplement.getParseModel();
    }

    /**
     * Parse kết quả trả về parse Model
     *
     * @param parseModel
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ParseModel doParse(Class parseModel) throws IOException, ParseException {
        setParseModel(parseModel);
        return doParse();
    }

    /**
     * Parse kết quả trả về parse Model
     *
     * @param parseImplement
     * @param parseModel
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public ParseModel doParse(ParseImplement parseImplement, Class parseModel) throws IOException, ParseException {
        setParseImplement(parseImplement);
        setParseModel(parseModel);
        return doParse();
    }

    /**
     * Ghi nội dung ra output stream
     * @param out
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public void writeOutputStream(OutputStream out) throws ConnectionException, IOException {
        final int BUFFER_SIZE = 2048;
        byte[] buf = new byte[BUFFER_SIZE];
        InputStream is = getInputStream();

        // ghi chuỗi ra file
        int n;
        while ((n = is.read(buf)) > 0) {
            out.write(buf, 0, n);
        }

        // flush
        out.flush();

    }

    /**
     * Ghi nội dung ra file
     * @param file
     * @throws ConnectionException
     * @throws IOException
     */
    @Override
    public void writeToFile(File file) throws ConnectionException, IOException {
        FileUtil.writeInputStream(file, getInputStream());
    }
}