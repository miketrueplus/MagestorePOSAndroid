package com.magestore.app.lib.task;

import com.magestore.app.lib.controller.AbstractListController;
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
    int page;

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
            page = params[0];
            return mListController.onRetrieveBackground(params[0], params[1]);
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

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        if (page == 1) mListController.onCancelledLoadData(mException);
        else mListController.onCancelledBackground(mException);
    }
}