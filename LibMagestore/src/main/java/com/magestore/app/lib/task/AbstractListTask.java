package com.magestore.app.lib.task;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Class abstract quản lý các hoạt động task liên quan đến list như thêm xóa sửa
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractListTask <TController extends AbstractListController, TTaskParam, TTaskProgress, TResult>
        extends AsyncTask<TTaskParam, TTaskProgress, TResult> {
    protected TController mListController;
    protected Exception mException;

    /**
     * Khởi tạo với list controller
     * @param controller
     */
    public AbstractListTask(TController controller) {
        super();
        mListController = controller;
    }

    /**
     * Khi ngừng hoạt động, nếu có lỗi ra thông báo
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListController.onCancelledBackground(mException);
    }

    /**
     * Thực thi tiến trình
     * @param param
     */
    public void doExecute(TTaskParam... param) {
        // chạy task load data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
        else // Below Api Level 13
            super.execute(param);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListController.doShowProgress(true);
    }
}
