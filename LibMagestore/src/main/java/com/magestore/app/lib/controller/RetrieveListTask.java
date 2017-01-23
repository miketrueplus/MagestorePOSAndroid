package com.magestore.app.lib.controller;

import android.os.AsyncTask;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Task thực hiện lấy danh sách
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class RetrieveListTask<TModel extends Model>
        extends AbstractListTask<AbstractListController<TModel>, Integer, Void, List<TModel>> {


    /**
     * Khởi tạo với list controller
     *
     * @param controller
     */
    public RetrieveListTask(AbstractListController<TModel> controller)
    {
        super(controller);
    }

    @Override
    protected List<TModel> doInBackground(Integer... params) {
        try {
            return mListController.onRetrieveDataBackground(params[0], params[1]);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<TModel> ts) {
        super.onPostExecute(ts);
        mListController.onRetrievePostExecute(ts);
        mListController.doShowProgress(false);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
//        mListController.onProgressUpdateLoadData();
    }


}