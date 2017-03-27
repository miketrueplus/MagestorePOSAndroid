package com.magestore.app.lib.task;

import android.os.AsyncTask;
import android.os.Build;

import com.google.common.reflect.TypeToken;
import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.model.Model;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/23/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class GenericActionModelTask<TModel extends Model, TReturn> extends AsyncTask<TModel, Void, TReturn> {
    String mstrFunctionPostExcute;
    String mstrFunctionPreExcute;
    String mstrFunctionBackground;
    String mstrFunctionCancel;
    TModel[] models;
    Map<String, Object> mWrapper;
    AbstractController mController;

    // class model và return để gọi method tương ứng
    Class<TModel> mClazzModel;
    Class<TReturn> mClazzReturn;

    Exception mException = null;

    public GenericActionModelTask(AbstractController controller, Class<TModel> clazzModel, Class<TReturn> clazzReturn) {
        super();
        mController = controller;
        mClazzModel = clazzModel;
        mClazzReturn = clazzReturn;
    }


    public TModel[] getModels() {
        return models;
    }

    public Map<String, Object> getWrapper() {
        return mWrapper;
    }

    public Exception getException() {
        return mException;
    }

    public GenericActionModelTask doAction(String strFunctionBackground) {
        mstrFunctionBackground = strFunctionBackground;
        return this;
    }

    public GenericActionModelTask wrap(String code, Object value) {
        if (mWrapper == null) mWrapper = new HashMap<String, Object>();
        mWrapper.put(code, value);
        return this;
    }

    public GenericActionModelTask wrap(Map<String, Object> wrapper) {
        mWrapper = wrapper;
        return this;
    }

    public GenericActionModelTask withModels(TModel... models) {
        this.models = models;
        return this;
    }

    public GenericActionModelTask doPreExcuteBy(String strFunction) {
        this.mstrFunctionPreExcute = strFunction;
        return this;
    }

    public GenericActionModelTask doPostExcuteBy(String strFunction) {
        this.mstrFunctionPostExcute = strFunction;
        return this;
    }

    public GenericActionModelTask sendExceptionTo(String strFunction) {
        this.mstrFunctionCancel = strFunction;
        return this;
    }

    @Override
    protected TReturn doInBackground(TModel... models) {
        this.models = models;
        try {
            Method method = mController.getClass().getMethod(mstrFunctionBackground, getClass(), models.getClass());
            Object value = method.invoke(mController, this, models);
            return (TReturn) value;
        } catch (Exception exp) {
            mException = exp;
            cancel(true);
            return null;
        }
    }

    public void doExcute() {
        // chạy task load data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, models);
        else // Below Api Level 13
            super.execute(models);
    }

    public void doExecute(TModel... models) {
        this.models = models;

        // chạy task load data
        doExcute();
    }

    public void doExcute(Map<String, Object> wrapper, TModel... models) {
        this.models = models;
        mWrapper = wrapper;

        // chạy task load data
        doExcute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mstrFunctionPreExcute == null) return;
        try {
            Method method = mController.getClass().getMethod(mstrFunctionPreExcute, getClass(), mClazzModel);
            method.invoke(mController, this, models);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mstrFunctionCancel == null) return;
        try {
            Method method = mController.getClass().getMethod(mstrFunctionCancel, getClass(), Exception.class, mClazzModel);
            method.invoke(mController, this, mException, models);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(TReturn tReturn) {
        try {
            Type typeOfObjectsList = new TypeToken<ArrayList<TReturn>>() {}.getType();
            Method method = mController.getClass().getMethod(mstrFunctionPostExcute, getClass(), tReturn.getClass(), mClazzModel);
            method.invoke(mController, this, tReturn, models);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
