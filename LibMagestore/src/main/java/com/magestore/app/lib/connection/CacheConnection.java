package com.magestore.app.lib.connection;

import java.io.IOException;

/**
 * Created by Mike on 2/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CacheConnection {
    boolean isFinishBackgroud();

    CacheConnection setReloadCacheLater(boolean later);

    CacheConnection setCacheEnable(boolean enable);

    CacheConnection setForceOutOfDate(boolean force);

    CacheConnection setHourCacheOutOfDate(int hours);

    CacheConnection setDayCacheOutOfDate(int day);

    CacheConnection deleteCache();

    boolean isCacheOutOfDate() throws IOException;

    boolean haveCache() throws IOException;

    boolean isCacheable() throws IOException;

    CacheConnection setCacheName(String cacheName);
}
