package com.magestore.app.lib.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Task thực hiện các action nói chung
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ActionTask<TModel extends Model> extends AsyncTask<TModel, Void, Boolean> {
    AbstractController mController;
    int mActionType;
    String mActionCode;
    Exception mException = null;
    TModel[] models;

    public ActionTask(AbstractController controller, int actionType, String actionCode) {
        super();
        mController = controller;
        mActionType = actionType;
        mActionCode = actionCode;
    }

    @Override
    protected Boolean doInBackground(TModel... models) {
        try {
            return mController.doActionBackround(mActionType, mActionCode, null, models);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return false;
        }
    }

    public void doExcute(TModel... models) {
        this.models = models;
        // chạy task load data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        else // Below Api Level 13
            super.execute(models);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mController.doShowProgress(true);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mController.onCancelledBackground(mException);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mController.onActionPostExecute(aBoolean, mActionType, mActionCode, null, this.models);
    }
}