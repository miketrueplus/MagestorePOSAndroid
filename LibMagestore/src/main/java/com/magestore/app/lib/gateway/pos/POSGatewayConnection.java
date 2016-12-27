package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.gateway.GatewayConnection;
import com.magestore.app.lib.gateway.GatewayException;

/**
 * Kết nối chung đến toàn hệ thống API, chỉ có 1 instance duy nhất
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSGatewayConnection implements GatewayConnection {
    // Chỉ có thể có một instance duy nhất
    private static POSGatewayConnection instance = new POSGatewayConnection();

    // quản lý đóng mở kết nối
    boolean mblnOpen = true;

    /**
     *
     */
    protected POSGatewayConnection() {
    }

    /**
     *
     * @throws GatewayException
     */
    @Override
    public void open() throws GatewayException {
        mblnOpen = true;
    }

    /**
     *
     * @throws GatewayException
     */
    @Override
    public void close() throws GatewayException {
        mblnOpen = false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isOpen() {
        return mblnOpen;
    }

    /**
     * Trả về một instance duy nhất
     * @return
     */
    public static POSGatewayConnection getInstance(){
        return instance;
    }
}