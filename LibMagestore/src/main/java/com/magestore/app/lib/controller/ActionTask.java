package com.magestore.app.lib.controller;

import android.os.AsyncTask;

import java.util.List;

/**
 * Task thực hiện các action nói chung
 * Created by Mike on 1/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ActionTask<TModel> extends AsyncTask<TModel, Void, TModel> {
    Controller mController;
    public ActionTask(Controller controller) {
        super();
        mController = controller;
    }

    @Override
    protected TModel doInBackground(TModel... models) {
        return null;
    }
}