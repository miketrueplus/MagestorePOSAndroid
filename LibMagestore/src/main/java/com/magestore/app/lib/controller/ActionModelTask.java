package com.magestore.app.lib.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/23/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ActionModelTask extends AsyncTask<Model, Void, Boolean> {
    AbstractController mController;
    int mActionType;
    String mActionCode;
    Exception mException = null;
    Model[] models;

    public ActionModelTask(AbstractController controller, int actionType, String actionCode) {
        super();
        mController = controller;
        mActionType = actionType;
        mActionCode = actionCode;
    }

    @Override
    protected Boolean doInBackground(Model... models) {
        try {
            return mController.doActionBackround(mActionType, mActionCode, models);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return false;
        }
    }

    public void doExcute(Model... models) {
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
        mController.onActionPostExecute(aBoolean, mActionType, mActionCode, this.models);
    }


}
