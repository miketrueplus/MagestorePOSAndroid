package com.magestore.app.lib.connection.http;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Xây dựng các tham số cho magestore statement
 * Created by Mike on 1/16/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreParamBuilder implements ParamBuilder {
    // các ký tự
    private final static String OPEN_SQUARE = "[";
    private final static String CLOSE_SQUARE = "]";
    private final static String OPEN_BLANKET = "{";
    private final static String CLOSE_BLANKET = "}";
    private final static String CURRENCY_SYMBOL = "$";
    private final static String EQUAL = "=";


    // phân nhóm cho tìm kiếm
    private final static String SEARCH_DEFAULT_GROUP_NAME = "MAGESTORE";
    private final static String SEARCH_CRITERIA_GROUP = "searchCriteria[filterGroups]";
    private final static String SEARCH_CRITERIA_FILTER = "[filters]";
    private final static String SEARCH_CRITERIA_FIELD = "[field]";
    private final static String SEARCH_CRITERIA_VALUE = "[value]";
    private final static String SEARCH_CRITERIA_CONDITION_TYPE = "[conditionType]";

    // Danhf cho sắp xếp
    private final static String SEARCH_CRITERIA_DIRECTION = "[direction]";
    // phân trang
    private final static String SEARCH_CRITERIA_PAGESIZE = "searchCriteria[pageSize]=${magestore_pageSize}";
    private final static String SEARCH_CRITERIA_PAGE = "searchCriteria[currentPage]=${magestore_currentPage}";
    private final static String CURRENT_PAGE = "magestore_currentPage";
    private final static String PAGE_SIZE = "magestore_pageSize";

    // sắp xếp
    private final static String SEARCH_CRITERIA_SORT_ORDER = "searchCriteria[sortOrders]";
    private final static String SEARCH_CRITERIA_SORT_FIELD = "[field]";

    // session
    private final static String SESSION_PARAM = "session=${magestore_sessionID}";
    private final static String SESSION = "magestore_sessionID";;

    // so sanhs cho condition type
    private static final String SEARCH_CONDITION_EQUAL = "eq";
    private static final String SEARCH_CONDITION_GREATER = "gt";
    private static final String SEARCH_CONDITION_LESS = "lt";
    private static final String SEARCH_CONDITION_LIKE = "like";

    // chiều sắp xếp
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String GREATER_SYMBOL = ">";
    private static final String LESS_SYMBOL = "<";
    private static final String AND_SYMBOL = "&";
    private static final String UNDERLINE_SYMBOL = "_";

    // quản lý các nhóm filter
    Map<String, Table<String, String, String>> mapFilterGroup = new HashMap<String, Table<String, String, String>>();

    // quản lý tham số để sắp xếp
    Map<String, String> mapSortOrder = new HashMap<String, String>();

    // Chứa key và value
    Statement mStatement;
    Map mMapKeyValue;


    // Sử dụng để phân trang
    int mintPage = 0;
    int mintPageSize = 0;

    // session
    String mstrSessionID;

    // chứa chuỗi đã được build, chưa thêm tham số
    StringBuilder mstrBuilderQuery;

    /**
     * Khởi tạo với map key value có sẵn
     */
    public MagestoreParamBuilder(Statement statement, Map mapKeyValue) {
        mMapKeyValue = mapKeyValue;
        mStatement = statement;
    }

    /**
     * Bổ sung filter them một model
     *
     * @param model
     * @return
     */
    @Override
    public boolean setFilter(Model model) {
        return false;
    }

    /**
     * Đặt phân trang
     *
     * @param pageSize
     * @return
     */
    public boolean setPageSize(int pageSize) {
        mintPageSize = pageSize;
        mMapKeyValue.put(PAGE_SIZE, mintPageSize);
        return true;
    }

    /**
     * Đặt phân trang
     *
     * @param page
     * @return
     */
    public boolean setPage(int page) {
        mintPage = page;
        mMapKeyValue.put(CURRENT_PAGE, mintPage);
        return true;
    }

    @Override
    public boolean setSessionID(String strSessionID) {
        mstrSessionID = strSessionID;
        mMapKeyValue.put(SESSION, mstrSessionID);
        return true;
    }

    @Override
    public void setParam(String pstrName, String pstrValue) {
        mMapKeyValue.put(pstrName, pstrValue);
    }

    /**
     * Gán các tham số cho truy vấn
     * Tham số trong URL có dạng ${tên_tham_số}
     * @param pstrName
     * @param pintValue
     * @throws ConnectionException
     */
    @Override
    public void setParam(String pstrName, int pintValue) {
        mMapKeyValue.put(pstrName, pintValue);
    }

    @Override
    public void clear() {
        if (mMapKeyValue != null) mMapKeyValue.clear();
        if (mapSortOrder != null) mapSortOrder.clear();
        if (mapFilterGroup != null) mapFilterGroup.clear();
        mstrBuilderQuery = null;
    }

    /**
     * Đặt tham số để sắp xếp
     *
     * @param strFieldName
     * @param strDirection
     * @return
     */
    @Override
    public boolean setSortOrder(String strFieldName, String strDirection) {
        boolean blnHasMoreParam = !mapSortOrder.containsKey(strFieldName);
        mapSortOrder.put(strFieldName, strDirection);
        return blnHasMoreParam;
    }

    /**
     * Đặt tham số để sắp xếp
     *
     * @param strFieldName
     * @return
     */
    @Override
    public boolean setSortOrderASC(String strFieldName) {
        return setSortOrder(strFieldName, ASC);
    }

    /**
     * Đặt tham số để sắp xếp
     *
     * @param strFieldName
     * @return
     */
    @Override
    public boolean setSortOrderDESC(String strFieldName) {
        return setSortOrder(strFieldName, DESC);
    }


    /**
     * Đặt filter với name, điều kiện và giá trị
     *
     * @param strFieldName
     * @param strConditionType
     * @param strFieldValue
     * @return
     */
    @Override
    public boolean setFilter(String strFieldName, String strConditionType, String strFieldValue) {
        return setFilter(SEARCH_DEFAULT_GROUP_NAME, strFieldName, strConditionType, strFieldValue);
    }

    /**
     * Đặt filter với field name và giá trị tương ứng
     *
     * @param strFieldName
     * @param strValue
     * @return
     */
    @Override
    public boolean setFilterEqual(String strFieldName, String strValue) {
        return setFilter(SEARCH_DEFAULT_GROUP_NAME, strFieldName, SEARCH_CONDITION_EQUAL, strValue);
    }

    /**
     * Đặt filter với field name và giá trị tương ứng
     *
     * @param strFieldName
     * @param strValue
     * @return
     */
    @Override
    public boolean setFilterGreater(String strFieldName, String strValue) {
        return setFilter(SEARCH_DEFAULT_GROUP_NAME, strFieldName, SEARCH_CONDITION_GREATER, strValue);
    }

    /**
     * Đặt filter với field name và giá trị tương ứng
     *
     * @param strFieldName
     * @param strValue
     * @return
     */
    @Override
    public boolean setFilterLess(String strFieldName, String strValue) {
        return setFilter(SEARCH_DEFAULT_GROUP_NAME, strFieldName, SEARCH_CONDITION_LESS, strValue);
    }

    /**
     * Đặt filter với field name và giá trị tương ứng
     *
     * @param strFieldName
     * @param strValue
     * @return
     */
    @Override
    public boolean setFilterLike(String strFieldName, String strValue) {
        return setFilter(SEARCH_DEFAULT_GROUP_NAME, strFieldName, SEARCH_CONDITION_LIKE, strValue);
    }

    /**
     * Đặt filter với giá trị tương ứng theo field
     *
     * @param strFieldName
     * @param strValue
     * @return
     */
    @Override
    public boolean setFilter(String strFieldName, String strValue) {
        return setFilterEqual(strFieldName, strValue);
    }

    /**
     * Bổ sung 1 điều kiện filter
     *
     * @param strGroupName
     * @param strFieldName
     * @param strFieldValue
     */
    public boolean setFilter(String strGroupName, String strFieldName, String strConditionType, String strFieldValue) {
        boolean hasMoreParams = false;

        // đặt thên tham số trên url
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(strGroupName);
        strBuilder.append(UNDERLINE_SYMBOL);
        strBuilder.append(strConditionType);
        strBuilder.append(UNDERLINE_SYMBOL);
        strBuilder.append(strFieldName);
        strBuilder.append(UNDERLINE_SYMBOL);
        String strKey = strBuilder.toString();

        // Kiểm tra đã có tên group chưa
        if (!mapFilterGroup.containsKey(strGroupName)) {
            Table<String, String, String> filter = HashBasedTable.create();
            filter.put(strFieldName, strConditionType, strKey);
            mMapKeyValue.put(strKey, strFieldValue);
            mapFilterGroup.put(strGroupName, filter);
            hasMoreParams = true;
            return hasMoreParams;
        }

        // đã có group thì đưa nội dung vào filter
        Table<String, String, String> filter = mapFilterGroup.get(strGroupName);
        hasMoreParams = !(filter.contains(strFieldName, strConditionType));
        filter.put(strFieldName, strConditionType, strKey);
        mMapKeyValue.put(strKey, strFieldValue);

        // trả về true nếu có tham số mới, false nếu ngược lại
        return hasMoreParams;
    }

    /**
     * Trả lại map key value
     *
     * @return
     */
    public Map<String, String> getMapKeyValue() {
        return mMapKeyValue;
    }

    /**
     * Xây dựng câu truy vấn theo map
     *
     * @return
     */
    public StringBuilder buidQuery() {
        // String builderKey
        mstrBuilderQuery = new StringBuilder();

        // quét session
        if (mstrSessionID != null) {
            mstrBuilderQuery.append(AND_SYMBOL);
            mstrBuilderQuery.append(SESSION_PARAM);
        }

        // quét phần phân trang
        if (mintPageSize > 0) {
            mstrBuilderQuery.append(AND_SYMBOL);
            mstrBuilderQuery.append(SEARCH_CRITERIA_PAGESIZE);
        }

        if (mintPage > 0) {
            mstrBuilderQuery.append(AND_SYMBOL);
            mstrBuilderQuery.append(SEARCH_CRITERIA_PAGE);
        }

        // quét phần sắp xếp
        int i = 0;
        for (String key : mapSortOrder.keySet()) {
            mstrBuilderQuery.append(AND_SYMBOL);
            mstrBuilderQuery.append(SEARCH_CRITERIA_SORT_ORDER);
            mstrBuilderQuery.append(OPEN_SQUARE);
            mstrBuilderQuery.append(i);
            mstrBuilderQuery.append(CLOSE_SQUARE);
            mstrBuilderQuery.append(SEARCH_CRITERIA_SORT_FIELD);
            mstrBuilderQuery.append(EQUAL);
            mstrBuilderQuery.append(key);

            mstrBuilderQuery.append(AND_SYMBOL);
            mstrBuilderQuery.append(SEARCH_CRITERIA_SORT_ORDER);
            mstrBuilderQuery.append(OPEN_SQUARE);
            mstrBuilderQuery.append(i);
            mstrBuilderQuery.append(CLOSE_SQUARE);
            mstrBuilderQuery.append(SEARCH_CRITERIA_DIRECTION);
            mstrBuilderQuery.append(EQUAL);
            mstrBuilderQuery.append(mapSortOrder.get(key));

            i++;
        }

        // quét vần tham số tìm kiếm
        i = 0;
        for (String groupName : mapFilterGroup.keySet()) {
            int j = 0;
            Table<String, String, String> filter = mapFilterGroup.get(groupName);

            for (Table.Cell cell : filter.cellSet()) {
                // tên field
                mstrBuilderQuery.append(AND_SYMBOL);
                mstrBuilderQuery.append(SEARCH_CRITERIA_GROUP);
                mstrBuilderQuery.append(OPEN_SQUARE);
                mstrBuilderQuery.append(i);
                mstrBuilderQuery.append(CLOSE_SQUARE);
                mstrBuilderQuery.append(SEARCH_CRITERIA_FILTER);
                mstrBuilderQuery.append(OPEN_SQUARE);
                mstrBuilderQuery.append(j);
                mstrBuilderQuery.append(CLOSE_SQUARE);
                mstrBuilderQuery.append(SEARCH_CRITERIA_FIELD);
                mstrBuilderQuery.append(EQUAL);
                mstrBuilderQuery.append(cell.getRowKey());

                // điều kiện tìm kiếm nếu có
                if (cell.getRowKey() != null) {
                    mstrBuilderQuery.append(AND_SYMBOL);
                    mstrBuilderQuery.append(SEARCH_CRITERIA_GROUP);
                    mstrBuilderQuery.append(OPEN_SQUARE);
                    mstrBuilderQuery.append(i);
                    mstrBuilderQuery.append(CLOSE_SQUARE);
                    mstrBuilderQuery.append(SEARCH_CRITERIA_FILTER);
                    mstrBuilderQuery.append(OPEN_SQUARE);
                    mstrBuilderQuery.append(j);
                    mstrBuilderQuery.append(CLOSE_SQUARE);
                    mstrBuilderQuery.append(SEARCH_CRITERIA_CONDITION_TYPE);
                    mstrBuilderQuery.append(EQUAL);
                    mstrBuilderQuery.append(cell.getColumnKey());

                }

                // giá trị tìm kiếm
                mstrBuilderQuery.append(AND_SYMBOL);
                mstrBuilderQuery.append(SEARCH_CRITERIA_GROUP);
                mstrBuilderQuery.append(OPEN_SQUARE);
                mstrBuilderQuery.append(i);
                mstrBuilderQuery.append(CLOSE_SQUARE);
                mstrBuilderQuery.append(SEARCH_CRITERIA_FILTER);
                mstrBuilderQuery.append(OPEN_SQUARE);
                mstrBuilderQuery.append(j);
                mstrBuilderQuery.append(CLOSE_SQUARE);
                mstrBuilderQuery.append(SEARCH_CRITERIA_VALUE);
                mstrBuilderQuery.append(EQUAL);
                mstrBuilderQuery.append(CURRENCY_SYMBOL);
                mstrBuilderQuery.append(OPEN_BLANKET);
                mstrBuilderQuery.append(cell.getValue());
                mstrBuilderQuery.append(CLOSE_BLANKET);

                j++;
            }
            i++;
        }

        // cuối cùng trả về String builder;
        return mstrBuilderQuery;
    }
}
