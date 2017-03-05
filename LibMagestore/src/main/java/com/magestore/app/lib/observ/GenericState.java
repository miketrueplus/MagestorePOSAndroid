package com.magestore.app.lib.observ;

import com.magestore.app.lib.controller.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * State cho các observ
 * Created by Mike on 2/12/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class GenericState<TController extends Controller> implements State<TController> {
    // các state code mặc định để sử dụng
    public static final String DEFAULT_STATE_CODE_0 = "FIRE_STATE_0";
    public static final String DEFAULT_STATE_CODE_1 = "FIRE_STATE_1";
    public static final String DEFAULT_STATE_CODE_2 = "FIRE_STATE_2";
    public static final String DEFAULT_STATE_CODE_3 = "FIRE_STATE_3";
    public static final String DEFAULT_STATE_CODE_4 = "FIRE_STATE_4";
    public static final String DEFAULT_STATE_CODE_5 = "FIRE_STATE_5";
    public static final String DEFAULT_STATE_CODE_6 = "FIRE_STATE_6";
    public static final String DEFAULT_STATE_CODE_7 = "FIRE_STATE_7";
    public static final String DEFAULT_STATE_CODE_8 = "FIRE_STATE_8";
    public static final String DEFAULT_STATE_CODE_9 = "FIRE_STATE_9";
    public static final String DEFAULT_STATE_CODE_ON_SELECT_ITEM = "FIRE_STATE_ON_SELECT_ITEM";
    public static final String DEFAULT_STATE_CODE_ON_RETRIEVE = "FIRE_STATE_ON_RETRIEVE";
    public static final String DEFAULT_STATE_CODE_ON_INSERT = "FIRE_STATE_ON_INSERT";
    public static final String DEFAULT_STATE_CODE_ON_UPDATE = "FIRE_STATE_ON_UPDATE";
    public static final String DEFAULT_STATE_CODE_ON_DELETE = "FIRE_STATE_ON_DELETE";
    public static final String DEFAULT_STATE_CODE_ON_LOAD_ITEM = "DEFAULT_STATE_CODE_ON_LOAD_ITEM";

    // controler nơi xuất phát state
    TController mController;

    // state code
    String mStateCode;

    // chứa các giá trị làm tham số
    Map<String, Object> mTags;

    /**
     * Khởi tạo
     * @param controller
     * @param stateCode
     */
    public GenericState(TController controller, String stateCode) {
        setController(controller);
        setStateCode(stateCode);
    }

    /**
     * Đặt controller lúc xuất phát observe
     * @param controller
     */
    public void setController(TController controller) {
        mController = controller;
    }

    /**
     * Trả lại controller lúc xuất phát observe
     * @return
     */
    public TController getController() {
        return mController;
    }

    /**
     * Trả lại tag giá trị
     * @param key
     * @return
     */
    @Override
    public Object getTag(String key) {
        if (mTags == null) return null;
        return mTags.get(key);
    }

    /**
     * Đặt lại tag giá trị
     * @param key
     * @param value
     */
    @Override
    public void setTag(String key, Object value) {
        if (mTags == null) mTags = new HashMap<>();
        mTags.put(key, value);
    }

    /**
     * Trả lại state code
     * @return
     */
    @Override
    public String getStateCode() {
        return mStateCode;
    }

    /**
     * Đặt state code
     * @param code
     */
    @Override
    public void setStateCode(String code) {
        mStateCode = code;
    }
}
