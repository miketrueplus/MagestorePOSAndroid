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

    /**
     * Xác định phương thức parse implement sang model
     * @param clazzParseImplement
     * @return
     */
//    public MagestoreFileCacheConnection setParseImplement(Class<TParseImplement> clazzParseImplement) {
//        this.clazzParseImplement = clazzParseImplement;
//        return this;
//    }
//
//    /**
//     * Xác định model để parse
//     * @param clazzParseModel
//     * @return
//     */
//    public MagestoreFileCacheConnection setParseModel(Class<TParseModel> clazzParseModel) {
//        this.clazzParseModel = clazzParseModel;
//        return this;
//    }



    /**
     * Parse dữ liệu, lấy kết quả từ cache hoặc từ API
     *
     * @return
     * @throws IOException
     */
//    public synchronized ParseModel excute() throws ParseException, IOException {
//        ParseModel model;
//        ResultReading rp = new MagestoreResultReading(openInputStream());
//        rp.setParseImplement(clazzParseImplement);
//        rp.setParseModel(clazzParseModel);
//        model = rp.doParse();
//        return model;
//    }

//    /**
//     * Thực hiện execute từ cache
//     *
//     * @return
//     * @throws IOException
//     */
//    private ResultReading excuteFromCache() throws IOException {
//        File cachedFile = FileUtil.getCachedFile(cacheFileName);
//        return new MagestoreResultReading(new FileInputStream(cachedFile));
//    }

    @Override
    protected InputStream openInputStreamIgnoreCache() throws IOException {
        return statement.doExecute().getInputStream();
    }
}