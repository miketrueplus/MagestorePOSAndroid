package com.magestore.app.lib.controller;

import android.os.AsyncTask;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Task thực hiện cập nhật số liệu
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class UpdateListTask<TModel extends Model>
        extends AbstractListTask<AbstractListController<TModel>, TModel, Void, Boolean> {

    /**
     * Khởi tạo với controller
     * @param controller
     */
    public UpdateListTask(AbstractListController<TModel> controller) {
        super(controller);
    }

    @Override
    protected Boolean doInBackground(TModel... models) {
        try {
            return mListController.onUpdateDataBackGround(models);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mListController.onUpdatePostExecute(aBoolean);
        mListController.doShowProgress(false);
    }
}
