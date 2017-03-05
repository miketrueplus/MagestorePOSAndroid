package com.magestore.app.lib.task;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.model.Model;

import java.util.Map;

/**
 * Created by folio on 3/4/2017.
 */

public class LoadModelTask<TModel extends Model> extends AsyncTask<TModel, Void, Boolean> {
    AbstractController mController;
    Exception mException = null;
    Model[] models;

    public LoadModelTask(AbstractController controller) {
        super();
        mController = controller;
    }

    @Override
    protected Boolean doInBackground(TModel... params) {
        this.models = params;
        try {
            return mController.doLoadItemBackground(params);
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return false;
        }
    }

    public void doExcute(TModel... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else // Below Api Level 13
            super.execute(params);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mController.onLoadItemPreExecute(this.models);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mController.onCancelledBackground(mException);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mController.onLoadItemPostExecute(aBoolean, this.models);
    }
}
