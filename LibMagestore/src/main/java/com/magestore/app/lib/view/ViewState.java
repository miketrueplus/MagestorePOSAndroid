package com.magestore.app.lib.view;

/**
 * Created by Mike on 2/19/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ViewState {
    // trạng thái bình thường, hiển thị nội dung
    public static final int STATE_NORMAL = 1;

    // trạng thái hiển thị loading progress
    public static final int STATE_LOADING = 2;

    // trạng thái hiện cảnh báo
    public static final int STATE_WARNING = 4;

    // trạng thái không có dữ liệu (null data)
    public static final int STATE_NODATA = 8;

    // trạng thái lỗi
    public static final int STATE_ERROR = 16;

    // trạng thái đang biên tập nội dung
    public static final int STATE_EDITTING = 32;

    // trạng thái đang đợi click để insert
    public static final int STATE_WAIT_INSERT = 64;

    // trạng thái disable trạng thái
    public static final int STATE_DISABLE = 128;

    // đặt state mặc định khởi tạo là normal
    private int mState = STATE_NORMAL;
    private String strMsg = null;

    public void setStateNormal() {
        mState = mState | STATE_NORMAL;
    }

    public void setStateLoading() {
        mState = STATE_LOADING;
    }

    public void setStateWarning() {
        mState = STATE_WARNING;
    }

    public void setStateNodata() {
        mState = STATE_NODATA;
    }

    public void setStateError() {
        mState = STATE_ERROR;
    }

    public void setStateEditting() {
        mState = STATE_EDITTING;
    }

    public void setStateWaitInsert() {
        mState = STATE_WAIT_INSERT;
    }

    public void setStateDisable() {
        mState = STATE_DISABLE;
    }

    public boolean isStateNormal() {
        return mState == STATE_NORMAL;
    }

    public boolean isStateLoading() {
        return mState == STATE_LOADING;
    }

    public boolean isStateWarning() {
        return mState == STATE_WARNING;
    }

    public boolean isStateNoData() {
        return mState == STATE_NODATA;
    }

    public boolean isStateError() {
        return mState == STATE_ERROR;
    }

    public boolean isStateWaitInsert() {
        return mState == STATE_WAIT_INSERT;
    }

    public boolean isStateDisable() {
        return mState == STATE_DISABLE;
    }

    public String getStrMsg() {
        return strMsg;
    }

    public void setStrMsg(String strMsg) {
        this.strMsg = strMsg;
    }
}
