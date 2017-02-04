package com.magestore.app.lib.connection;

import com.magestore.app.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mike on 2/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class BitmapFileCacheConnection extends AbstractFileCacheConnection {
    private static final String CACHE_DIR = "/MAGESTORE_BMP_CACHE/";
    private String mstrBmpURL;

    /**
     * Khởi tạo đặt thư mục cache
     */
    public BitmapFileCacheConnection() {
        cacheDirName = CACHE_DIR;
    }

    /**
     * Chỉ định path của bitmap
     * @param strURL
     */
    public void setBmpURL(String strURL) {
        mstrBmpURL = strURL;
    }

    /**
     * Đọc từ API vào cache
     * @throws IOException
     */
    @Override
    protected synchronized void loadToCache() throws IOException {
        // Load từ mạng
        URL url = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;

        // nếu có thể cache
        try {
            // mở input stream từ bitmap url
            url = new URL(mstrBmpURL);
            con = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(con.getInputStream());

            // lưu vào cache
            File fileCached = FileUtil.getCachedFile(cacheFileName);
            FileUtil.writeInputStream(fileCached, inputStream);

            // đánh dấu thời gian lưu cache
            fileCached.setLastModified(System.currentTimeMillis());
        } catch (IOException e) {
            throw e;
        } finally {
            // đóng connection từ mạng lại
            if (inputStream!=null) inputStream.close();
            if (con!=null) con.disconnect();

            inputStream = null;
            con = null;
            url = null;
        }
    }

    @Override
    protected InputStream openInputStreamIgnoreCache() throws IOException {
        // mở input stream từ bitmap url
        URL url = new URL(mstrBmpURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream inputStream = new BufferedInputStream(con.getInputStream());
        return inputStream;
    }
}
