package com.magestore.app.lib.task;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;

/**
 * Async task thực hiện insert dữ liệu
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class InsertListTask<TModel extends Model>
        extends AbstractListTask<AbstractListController<TModel>, TModel, Void, Boolean> {
    TModel[] paramsModel;
    /**
     * Khởi tạo với controller
     * @param controller
     */
    public InsertListTask(AbstractListController<TModel> controller) {
        super(controller);
    }

    @Override
    protected Boolean doInBackground(TModel... models) {
        paramsModel = models;
        try {
            return mListController.onInsertBackground(models);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mListController.onInsertPostExecute(aBoolean, paramsModel);
//        mListController.doShowProgress(false);
    }
}
