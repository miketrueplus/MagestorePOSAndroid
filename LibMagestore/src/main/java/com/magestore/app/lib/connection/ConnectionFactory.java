package com.magestore.app.lib.connection;

import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.context.MagestoreContext;

/**
 * Quản lý việc sinh ra các connection
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ConnectionFactory {
    /**
     * Khởi tạo 1 connection
     * @param context
     * @param pstrURL
     * @param pstrUserName
     * @param pstrPassword
     * @return
     */
    public static Connection generateConnection(MagestoreContext context, String pstrURL, String pstrUserName, String pstrPassword) {
        //TODO: sau này có nhiều loại connection và context thì thay bằng connection hợp lý hơn
        return MagestoreConnection.getConnection(pstrURL, pstrUserName, pstrPassword);
    }
}
