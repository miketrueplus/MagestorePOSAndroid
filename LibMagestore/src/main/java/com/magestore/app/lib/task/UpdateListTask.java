package com.magestore.app.lib.task;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;

/**
 * Task thực hiện cập nhật số liệu
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class UpdateListTask<TModel extends Model>
        extends AbstractListTask<AbstractListController<TModel>, TModel, Void, Boolean> {

    TModel[] models;

    /**
     * Khởi tạo với controller
     * @param controller
     */
    public UpdateListTask(AbstractListController<TModel> controller) {
        super(controller);
    }

    @Override
    protected Boolean doInBackground(TModel... models) {
        this.models = models;
        try {
            return mListController.onUpdateBackGround(models[0], models[1]);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mListController.onUpdatePostExecute(aBoolean, models[0], models[1]);
//        mListController.doShowProgress(false);
    }
}
