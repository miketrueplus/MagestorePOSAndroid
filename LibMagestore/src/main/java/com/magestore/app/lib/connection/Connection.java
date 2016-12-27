package com.magestore.app.lib.connection;

/**
 * Quản lý các kết nối đến cơ sở dữ liệu server
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface Connection {


    /**
     * Mở kết nối
     * @throws ConnectionException
     */
    void open() throws ConnectionException;

    /**
     * Kiểm tra xem đã kết nối chưa
     * @return
     */
    boolean isOpenned() throws ConnectionException;

    /**
     * Đóng kết nối
     * @throws ConnectionException
     */
    void close();

    /**
     * Kiểm tra xem đã đóng kết nối chưa
     * @return
     */
    boolean isClosed() throws ConnectionException;

    /**
     * Tạo statement, các mẫu truy vấn đến server
     * @return
     * @throws ConnectionException
     */
    Statement createStatement() throws ConnectionException;

    /**
     * Commit lưu lên cơ sở dữ liệu
     */
//    public abstract void commit();

    /**
     * Rollback quay lại dữ liệu đã lưu
     */
//    public abstract void rollback();
}
