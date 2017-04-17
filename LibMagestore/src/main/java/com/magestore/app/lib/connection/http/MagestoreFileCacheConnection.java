package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.AbstractFileCacheConnection;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
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

public class MagestoreFileCacheConnection<TParseImplement extends ParseImplement, TParseModel extends ParseModel>
extends AbstractFileCacheConnection {
    private static final String CACHE_DIR = "/MAGESTORE_API_CACHE/";
    // statement thực thi truy vấn
    MagestoreStatement statement;

    // class để convert thành model
    Class<TParseImplement> clazzParseImplement;
    Class<TParseModel> clazzParseModel;

    /**
     * Khởi tạo đặt thư mục cache
     */
    public MagestoreFileCacheConnection() {
        cacheDirName = CACHE_DIR;
    }

    /**
     * Statement thực hiện lấy data từ API
     * @param statement
     * @return
     */
    public MagestoreFileCacheConnection setStatement(MagestoreStatement statement) {
        this.statement = statement;
        return this;
    }

    @Override
    protected InputStream openInputStreamIgnoreCache() throws IOException {
        return statement.doExecute().getInputStream();
    }
}