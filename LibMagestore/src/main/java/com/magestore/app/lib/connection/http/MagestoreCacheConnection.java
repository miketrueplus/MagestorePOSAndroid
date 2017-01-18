package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Load API và lưu cache nhằm tăng tốc độ load
 * Created by Mike on 1/17/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreCacheConnection<TParseImplement extends ParseImplement, TParseModel extends ParseModel> {
    private final String CACHE_DIR = "/MAGESTORE_API_CACHE/";
    // statement thực thi truy vấn
    Statement statement;

    // tên file cache
    String cacheFileName;

    // class để convert thành model
    Class<TParseImplement> clazzParseImplement;
    Class<TParseModel> clazzParseModel;

    // bật tắt thực hiện cached hoặc không
    boolean isEnableCache = true;

    // bật tắt ép buộc cache hết hạn
    boolean isForceOutOfDate = false;

    // nếu hết hạn cached thì chờ load lại luôn hay cho lên 1 thread
    boolean isReloadCacheLater = true;

    // thời hạn lưu cache bằng giờ
    int expireHour = 300;

    /**
     * Xác định load cache ngay và luôn không
     * @param later
     * @return
     */
    public MagestoreCacheConnection setReloadCacheLater(boolean later) {
        this.isReloadCacheLater = later;
        return this;
    }

    /**
     * Cho phép cache hay không
     *
     * @param enable
     * @return
     */
    public MagestoreCacheConnection setCacheEnable(boolean enable) {
        this.isEnableCache = enable;
        return this;
    }

    /**
     * Bắt buộc xóa cache luôn
     * @param force
     * @return
     */
    public MagestoreCacheConnection setForceOutOfDate(boolean force) {
        this.isForceOutOfDate = force;
        return this;
    }

    /**
     * Thời hạn lưu cache tính bằng giờ
     * @param hours
     * @return
     */
    public MagestoreCacheConnection setTimeOutOfDate(int hours) {
        this.expireHour = hours;
        return this;
    }

    /**
     * Statement thực hiện lấy data từ API
     * @param statement
     * @return
     */
    public MagestoreCacheConnection setStatement(Statement statement) {
        this.statement = statement;
        return this;
    }

    /**
     * Tên cache sử dụng là khóa unique quản lý cached
     * @param cacheName
     * @return
     */
    public MagestoreCacheConnection setCacheName(String cacheName) {
        this.cacheFileName = CACHE_DIR + cacheName;
        return this;
    }

    /**
     * Xác định phương thức parse implement sang model
     * @param clazzParseImplement
     * @return
     */
    public MagestoreCacheConnection setParseImplement(Class<TParseImplement> clazzParseImplement) {
        this.clazzParseImplement = clazzParseImplement;
        return this;
    }

    /**
     * Xác định model để parse
     * @param clazzParseModel
     * @return
     */
    public MagestoreCacheConnection setParseModel(Class<TParseModel> clazzParseModel) {
        this.clazzParseModel = clazzParseModel;
        return this;
    }

    /**
     * Kiểm tra xem có khả năng cache không
     *
     * @return
     * @throws IOException
     */
    private boolean isCacheable() throws IOException {
        // Kiểm tra có thưc mục cache k0 đã, nếu k0 có thì phải tạo;
        File cachedDir = FileUtil.getCachedFile(CACHE_DIR);
        if (!cachedDir.exists()) {
            if (!cachedDir.mkdir()) return false;
        }
        return true;
    }

    /**
     * Xem cache có quá hạn không
     *
     * @return
     * @throws IOException
     */
    private boolean isCacheOutOfDate() throws IOException {
        return isForceOutOfDate;
    }

    /**
     * Kiểm tra xem có cache k0 - cache hit
     *
     * @return
     * @throws IOException
     */
    private boolean haveCache() throws IOException {
        File cachedFile = FileUtil.getCachedFile(cacheFileName);
        return cachedFile.exists();
    }

    /**
     * Parse dữ liệu, lấy kết quả từ cache hoặc từ API
     *
     * @return
     * @throws IOException
     */
    public ParseModel excute() throws ParseException, IOException {
        ResultReading rp = null;
        // Load trước từ cache
        // điều kiện: cho phép cache, có thể cache, có cache
        if (isEnableCache && isCacheable() && haveCache()) {
            // Xem cache chưa hết hạn hoặc hết hạn nhưng được yêu cầu reload sau
            if (!isCacheOutOfDate() || (isCacheOutOfDate() && isReloadCacheLater)) {
                try {
                    // load từ cache parse xem có được không
                    ParseModel model;
                    synchronized(this) {
                        rp = excuteFromCache();
                        rp.setParseImplement(clazzParseImplement);
                        rp.setParseModel(clazzParseModel);
                        model = rp.doParse();
                    }
                    // nếu cache đã hết hạn, cho load lại cache trên thread
                    if (isCacheOutOfDate())
                        runLoadToCacheThread();

                    // đọc cache thuận lợi return luôn
                    return model;
                } finally {
                    if (rp != null) rp.close();
                }
            }
        }

        // nếu có lỗi khi parse từ cache hoặc không có cache hoặc cache hết hạn
        try {
            rp = excuteNoCache();
            rp.setParseImplement(clazzParseImplement);
            rp.setParseModel(clazzParseModel);
            ParseModel model = rp.doParse();
            return model;
        } catch (ParseException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (rp != null) rp.close();
        }

    }

    /**
     * Thực hiện execute từ cache
     *
     * @return
     * @throws IOException
     */
    private ResultReading excuteFromCache() throws IOException {
        File cachedFile = FileUtil.getCachedFile(cacheFileName);
        return new MagestoreResultReading(new FileInputStream(cachedFile));
    }

    /**
     * Xóa cache nếu có
     *
     * @return
     */
    private MagestoreCacheConnection deleteCache() {
        File cachedFile = null;
        try {
            cachedFile = FileUtil.getCachedFile(cacheFileName);
        } catch (IOException e) {
            return this;
        }
        if (cachedFile.exists()) cachedFile.delete();
        return this;
    }

    /**
     * Chạy API và lưu vào cached
     */
    private void runLoadToCacheThread() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    loadToCache();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    /**
     * Thực hiện truy vấn nhưng không có cache, lưu cache nếu có
     *
     * @return
     * @throws IOException
     */
    private ResultReading excuteNoCache() throws IOException {
        // Xóa cache
        deleteCache();

        // nếu có thể cache
        if (isEnableCache && isCacheable()) {
            // đọc từ mạng vào cache
            loadToCache();

            // tham chiếu sang cached
            return excuteFromCache();
        }
        else {
            // Load từ mạng
            ResultReading rp = statement.execute();

            // trả tham chiếu từ mạng nếu không lưu cache
            return rp;
        }


    }

    /**
     * Đọc từ API vào cache
     * @throws IOException
     */
    private synchronized void loadToCache() throws IOException {
        // Load từ mạng
        ResultReading rp = null;
        // nếu có thể cache
        try {
            rp = statement.execute();
            // lưu vào cache
            rp.writeToFile(FileUtil.getCachedFile(cacheFileName));
        } catch (IOException e) {
            throw e;
        } finally {
            // đóng result từ mạng lại
            if (rp!=null) rp.close();
        }
    }
}
