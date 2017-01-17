package com.magestore.app.lib.connection;

import com.magestore.app.lib.model.Model;

/**
 * Xây dựng câu truy vấn
 * Created by Mike on 1/16/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ParamBuilder {

    /**
     * Xây dựng truy vấn tìm kiếm với Model
     * @param model
     * @return
     */
    boolean setFilter(Model model);

    /**
     * Xây dựng truy vấn theo nhóm, trường và tên điều kiện
     * @param strGroupName
     * @param strFieldName
     * @param strConditionType
     * @param strFieldValue
     * @return
     */
    boolean setFilter(String strGroupName, String strFieldName, String strConditionType, String strFieldValue);
    boolean setFilter(String strFieldName, String strConditionType, String strFieldValue);
    boolean setFilterEqual(String strFieldName, String strValue);
    boolean setFilterGreater(String strFieldName, String strValue);
    boolean setFilterLess(String strFieldName, String strValue);
    boolean setFilterLike(String strFieldName, String strValue);
    boolean setFilter(String strFieldName, String strValue);

    /**
     * Dành cho sắp xếp
     */
    boolean setSortOrder(String strFieldName, String strDirection);
    boolean setSortOrderASC(String strFieldName);
    boolean setSortOrderDESC(String strFieldName);

    /**
     * Phân trang
     */
    boolean setPageSize(int pageSize);
    boolean setPage(int page);

    /**
     * Đặt session
     * @param strSessionID
     * @return
     */
    boolean setSessionID(String strSessionID);

    /**
     * Truyền tham số và giá trị
     * @param pstrName
     * @param pstrValue
     */
    void setParam(String pstrName, String pstrValue);
    void setParam(String pstrName, int pintValue);

    // giải phóng
    void clear();
}
