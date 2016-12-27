package com.magestore.app.lib.gateway;

/**
 * Gateway Connection kết nối đến toàn hệ thống API, quản lý các tham số của hệ thống
 * Chỉ 1 kết nối duy nhất
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface GatewayConnection {
    /**
     * Mở kết nối đến hệ thống
     * @throws GatewayException
     */
    void open() throws GatewayException;
    /**
     * Đóng kết nối lại
     * @throws GatewayException
     */
    void close() throws GatewayException;

    /**
     * Kiểm tra xem có kết nối đến hệ thống k0
     * @return
     */
    boolean isOpen();
}
