package com.magestore.app.lib.connection;

import com.magestore.app.lib.model.Model;

import java.util.Map;

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
    ParamBuilder setFilter(String strGroupName, String strFieldName, String strConditionType, String strFieldValue);
    ParamBuilder setFilter(String strFieldName, String strConditionType, String strFieldValue);
    ParamBuilder setFilterEqual(String strFieldName, String strValue);
    ParamBuilder setFilterGreater(String strFieldName, String strValue);
    ParamBuilder setFilterLess(String strFieldName, String strValue);
    ParamBuilder setFilterLike(String strFieldName, String strValue);
    ParamBuilder setFilter(String strFieldName, String strValue);

    /**
     * Dành cho sắp xếp
     */
    ParamBuilder setSortOrder(String strFieldName, String strDirection);
    ParamBuilder setSortOrderASC(String strFieldName);
    ParamBuilder setSortOrderDESC(String strFieldName);

    /**
     * Phân trang
     */
    ParamBuilder setPageSize(int pageSize);
    ParamBuilder setPage(int page);

    /**
     * Đặt session
     * @param strSessionID
     * @return
     */
    ParamBuilder setSessionID(String strSessionID);

    /**
     * Truyền tham số và giá trị
     * @param pstrName
     * @param pstrValue
     */
    ParamBuilder setParam(String pstrName, String pstrValue);
    ParamBuilder setParam(String pstrName, int pintValue);

    /**
     * Dựng câu querry
     * @return
     */
    StringBuilder buildQuery();

    // giải phóng
    ParamBuilder clear();

    // trả lại
    Map getValueMap();
}
