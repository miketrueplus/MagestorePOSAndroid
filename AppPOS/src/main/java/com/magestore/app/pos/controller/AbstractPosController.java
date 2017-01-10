package com.magestore.app.pos.controller;

import android.app.Activity;
import android.view.View;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Controller abstract cho các controller phổ biến của MageStorePOS
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractPosController<TView> implements Controller<TView> {
    protected TView mView;
    protected MagestoreContext mMagestoreContext;

    @Override
    public void setView(TView view) {
        mView = view;
    }

    @Override
    public void setMagestoreContext(MagestoreContext context) {
        mMagestoreContext = context;
    }

    @Override
    public void setRoolController(Controller controller) {

    }

    @Override
    public void setParentController(Controller controller) {

    }

    @Override
    public void doStart() {

    }

    @Override
    public void doFinish() {

    }

    @Override
    public void doShowErrorMsg(Exception exp) {

    }

    @Override
    public void doShowProgress(boolean blnShow) {

    }
}
