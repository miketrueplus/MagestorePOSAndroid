package com.magestore.app.pos.parse.sax2pos;

import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.ParseImplement;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Dự tính sử dụng để parseXML nhưng chưa dúng đến, làm sau
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class Sax2PosAbstract extends DefaultHandler implements ParseImplement {
    private SAXParser msaxParser;
    private InputStream mInput;
    private Class<ParseModel> mClassEntntiy;
    protected Collection<ParseModel> mCollectionModel;
    protected Map<String, ParseModel> mMapModel;
    protected Sax2PosAbstract() {
    }

    @Override
    public void prepareParse(InputStream input, ParseModel parseModel) throws ParseException, IOException {

    }

    /**
     * Khởi tạo SAX để load XML
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void prepareParse(InputStream input, Class<ParseModel> classEntntiy) throws ParseException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            msaxParser = factory.newSAXParser();
        }
        catch (Exception ex) {
                throw new ParseException(ex);
        }
        mClassEntntiy = classEntntiy;
//        mMapModel = mapModel;
//        mCollectionModel = mCollectionModel;

        mInput = input;
    }

    @Override
    public void prepareParse(InputStream input, Type typeOf) throws ParseException, IOException {

    }

    @Override
    public void doParse() throws ParseException, IOException {
        try {
            msaxParser.parse(mInput, this);
        } catch (SAXException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public ParseModel getParseModel() {
        return null;
    }

    /**
     *
     * @return
     */
    protected ParseModel newParseModel() {
        try {
            return (ParseModel) mClassEntntiy.newInstance();
        } catch (Exception ex) {
            return null;
        }
    }

    private byte mbyteArrageOrder = 0;
    private ParseModel mParseModel = null;
    private String mstrQName = null;
    private StringBuilder mStrBuilder;
    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes)
            throws SAXException {
        if ("item".equalsIgnoreCase(qName) && mbyteArrageOrder == 2) {
            // Check nếu đang ở node sản phẩm, khởi tạo entity mới,
            mParseModel = newParseModel();
            mstrQName = qName;

            // Chuẩn bị nếu node này toàn XML
            mStrBuilder = new StringBuilder();
        }
        else if (mbyteArrageOrder >= 3) {
            // Kể từ các node dưới, lưu lại cả chuỗi XML vào Model
            mStrBuilder.append("<");
            mStrBuilder.append(qName);
            mStrBuilder.append(">");
        }
        // Đếm xem đang ở node độ sâu mấy
        mbyteArrageOrder++;
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if (mbyteArrageOrder > 3) {
            // Đóng chuỗi XML lại
            mStrBuilder.append("<");
            mStrBuilder.append(qName);
            mStrBuilder.append("/>");

            // Đặt giá trị chuỗi XML của node con vào
            if (mParseModel != null) {
//                mParseModel.setValue(this, mstrQName, mStrBuilder.toString());
            }
        }
//        else if (mbyteArrageOrder == 3) {
//            if (mCollectionModel != null && mParseModel!= null) mCollectionModel.add(mParseModel);
//            if (mMapModel != null && mParseModel!= null && mParseModel.getEntityKey() != null) mMapEntity.put(mParseModel.getEntityKey(), mParseModel);
//        }
        // Đếm xem đang ở node độ sâu mấy
        mbyteArrageOrder--;
    }

    /**
     * Lấy nội dung parsing
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
        // Nếu đang ở node sản phẩm
        if (mbyteArrageOrder == 3)
            // Nếu ở note product đầu tiên,
            if (mParseModel != null) {
//                mParseModel.setValue(this, mstrQName, new String(ch, start, length));
            }
            else if (mbyteArrageOrder > 3) {
                // Nếu đang k0 ở node product, lưu cả chuỗi XML lại
                mStrBuilder.append(ch, start, length);
            }
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() {

    }
}
