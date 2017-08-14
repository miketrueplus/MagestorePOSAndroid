package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.ConnectionException;

import java.io.InputStream;

/**
 * Created by Johan on 8/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class MagestoreResultReadingExceptionM1 extends MagestoreResultReading{
    /**
     * Nhận kết quả từ inputstream và chuyển thành chuỗi JSON
     *
     * @param inputStream
     */
    protected MagestoreResultReadingExceptionM1(InputStream inputStream) throws ConnectionException {
        super(inputStream);
    }
}
