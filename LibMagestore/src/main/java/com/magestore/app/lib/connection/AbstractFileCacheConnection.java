package com.magestore.app.lib.connection;

import com.magestore.app.lib.connection.http.MagestoreResultReading;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mike on 2/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractFileCacheConnection implements CacheConnection {
    // tên file cache
    protected String cacheDirName;
    protected String cacheFileName;
    protected String cacheFileNameInfo;

    // bật tắt thực hiện cached hoặc không
    protected boolean isEnableCache = true;

    // bật tắt ép buộc cache hết hạn
    protected boolean isForceOutOfDate = false;

    // nếu hết hạn cached thì chờ load lại luôn hay cho lên 1 thread
    protected boolean isReloadCacheLater = true;

    // Đánh dấu cache đã được ghi lại hết chưa, chỉ khi cờ này bật, các thread khác mới được giải phóng tài nguyên
    protected boolean isFinishBackgroud = false;

    // thời hạn lưu cache bằng giờ, mặc định là 1 ngày
    protected int expireMilisecond = 86400000;

    /**
     * Báo hiệu đã hoàn thành việc cache, các thread khác có thể giải phóng tài nguyên
     *
     * @return
     */
    @Override
    public boolean isFinishBackgroud() {
        return isFinishBackgroud;
    }

    /**
     * Xác định load cache ngay và luôn không
     *
     * @param later
     * @return
     */
    @Override
    public CacheConnection setReloadCacheLater(boolean later) {
        this.isReloadCacheLater = later;
        return this;
    }

    /**
     * Cho phép cache hay không
     *
     * @param enable
     * @return
     */
    @Override
    public CacheConnection setCacheEnable(boolean enable) {
        this.isEnableCache = enable;
        return this;
    }

    /**
     * Bắt buộc xóa cache luôn
     *
     * @param force
     * @return
     */
    @Override
    public CacheConnection setForceOutOfDate(boolean force) {
        this.isForceOutOfDate = force;
        return this;
    }

    /**
     * Thời hạn lưu cache tính bằng giờ
     *
     * @param hours
     * @return
     */
    @Override
    public CacheConnection setHourCacheOutOfDate(int hours) {
        this.expireMilisecond = hours * 3600000; // 1h = 60m * 60s * 1000ms
        return this;
    }

    /**
     * Chỉ định thời hạn cache tính theo ngày
     *
     * @param day
     * @return
     */
    @Override
    public CacheConnection setDayCacheOutOfDate(int day) {
        this.expireMilisecond = day * 86400000; // 1d = 24h * 60m * 60s * 1000ms;
        return this;
    }


    /**
     * Xóa cache nếu có
     *
     * @return
     */
    @Override
    public CacheConnection deleteCache() {
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
     * Xem cache có quá hạn không
     *
     * @return
     * @throws IOException
     */
    @Override
    public boolean isCacheOutOfDate() throws IOException {
        // nếu buộc hết hạn cache luôn
        if (isForceOutOfDate) return true;

        // Lấy thời gian tạo cache và so sánh với thời gian hiện tại
        File fileCached = FileUtil.getCachedFile(cacheFileName);
        long timeCached = fileCached.lastModified();
        long timeCurrent = System.currentTimeMillis();

        // nếu chênh nhau thì cache đã hết hạn
        return (Math.abs(timeCached - timeCurrent) > expireMilisecond);

    }

    /**
     * Kiểm tra xem có cache k0 - cache hit
     *
     * @return
     * @throws IOException
     */
    @Override
    public boolean haveCache() throws IOException {
        File cachedFile = FileUtil.getCachedFile(cacheFileName);
        return cachedFile.exists();
    }

    /**
     * Kiểm tra xem có khả năng cache không
     *
     * @return
     * @throws IOException
     */
    @Override
    public boolean isCacheable() throws IOException {
        // Kiểm tra có thưc mục cache k0 đã, nếu k0 có thì phải tạo;
        File cachedDir = FileUtil.getCachedFile(cacheDirName);
        if (!cachedDir.exists()) {
            if (!cachedDir.mkdir()) return false;
        }
        return true;
    }

    /**
     * Tên cache sử dụng là khóa unique quản lý cached
     *
     * @param cacheName
     * @return
     */
    @Override
    public CacheConnection setCacheName(String cacheName) {
        this.cacheFileName = cacheDirName + cacheName;
        this.cacheFileNameInfo = this.cacheFileName + ".info";
        return this;
    }

    /**
     * Chạy API và lưu vào cached
     */
    protected synchronized void runLoadToCacheThread() {
        Thread thread = new Thread() {
            public void run() {
                isFinishBackgroud = false;
                try {
                    loadToCache();

                    // giải phóng nếu tiến trình local đã xử lý xong,

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // đánh dấu kết thúc để giải phóng tài nguyên
                    isFinishBackgroud = true;
                }
            }
        };
        thread.start();
    }

    /**
     * Thực hiện execute từ cache
     *
     * @return
     * @throws IOException
     */
    protected InputStream excuteFromCache() throws IOException {
        File cachedFile = FileUtil.getCachedFile(cacheFileName);
        return (new FileInputStream(cachedFile));
    }

    /**
     * Parse dữ liệu, lấy kết quả từ cache hoặc từ API
     *
     * @return
     * @throws IOException
     */

    public synchronized InputStream execute() throws IOException {
        // Load trước từ cache
        // điều kiện: cho phép cache, có thể cache, có cache
        if (isEnableCache && isCacheable() && haveCache()) {
            // Xem cache chưa hết hạn hoặc hết hạn nhưng được yêu cầu reload sau
            if (!isCacheOutOfDate() || (isCacheOutOfDate() && isReloadCacheLater)) {
                // load từ cache parse xem có được không
                InputStream inputStream = null;
                synchronized (this) {
                    try {
                        inputStream = excuteFromCache();
                    } catch (Exception e) {e.printStackTrace();}
                }
                // nếu cache đã hết hạn, cho load lại cache trên thread
                if (isCacheOutOfDate()) {
                    isFinishBackgroud = false;
                    runLoadToCacheThread();
                }

                // đọc cache thuận lợi return luôn
                if (inputStream != null)
                    return inputStream;
            }
        }

        // nếu có lỗi khi parse từ cache hoặc không có cache hoặc cache hết hạn
        // Xóa cache
        deleteCache();

        // nếu có thể cache thì lưu cache
        if (isEnableCache && isCacheable()) {
            // đọc từ mạng vào cache
            loadToCache();

            // tham chiếu sang load từ cached
            return excuteFromCache();
        } else {
            // trả tham chiếu từ mạng nếu không lưu cache
            return openInputStreamIgnoreCache();
        }
    }

    /**
     * Đọc từ API vào cache
     * @throws IOException
     */
    protected synchronized void loadToCache() throws IOException {
        InputStream inputStream = null;
        try {
            // lưu vào cache
            inputStream = openInputStreamIgnoreCache();
            File fileCached = FileUtil.getCachedFile(cacheFileName);
            FileUtil.writeInputStream(fileCached, inputStream);

            // đánh dấu thời gian lưu cache
            fileCached.setLastModified(System.currentTimeMillis());
        } catch (IOException e) {
            throw e;
        } finally {
            // đóng result từ mạng lại
            if (inputStream!=null) inputStream.close();
        }
    }

    protected abstract InputStream openInputStreamIgnoreCache() throws IOException;
}
