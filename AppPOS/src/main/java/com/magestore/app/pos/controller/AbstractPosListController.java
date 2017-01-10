package com.magestore.app.pos.controller;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.panel.AbstractDetailPanel;
import com.magestore.app.pos.panel.AbstractListPanel;
import com.magestore.app.pos.task.AsyncTaskAbstractTask;

import java.util.List;

/**
 * Controller quản lý việc hiển thị một List và một View
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractPosListController<TModel>
        extends AbstractPosController<AbstractListPanel<TModel>> implements ListController<TModel> {
    AbstractDetailPanel<TModel> mDetailView;

    /**
     * Danh sách dữ liệu chứa Model
     */
    List<TModel> mList;
    TModel mItem;

    /**
     * Thiết lập danh sách, cho hiển thị lên view
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        mView.bindList(mList);
        if (mList != null && mDetailView != null && mList.size() > 0)
            mDetailView.bindItem(mList.get(0));
    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    public void doLoadData(){
        // chuẩn bị task load data
        final AsyncTask<Void, Void, List<TModel>> task = new AsyncTask<Void, Void, List<TModel>>() {
             private Exception mException;
            @Override
            protected List<TModel> doInBackground(Void... params) {
                try {
                    return loadDataBackground();
                } catch (Exception exp) {
                    mException = exp;
                    cancel(true);
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                doShowProgress(true);
                onPreExcuteLoadData();
            }

            @Override
            protected void onPostExecute(List<TModel> ts) {
                super.onPostExecute(ts);
                onPostExecuteLoadData(ts);
                doShowProgress(false);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                onCancelledLoadData(mException);
            }
        };

        // chạy task load data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else // Below Api Level 13
            task.execute();
    }

    /**
     * Load data từ background ngầm, các lớp con sẽ nạp chồng hàm này
//     * @param params
     * @return
     * @throws Exception
     */
    protected abstract List<TModel> loadDataBackground(Void... params) throws Exception;

    /**
     * Sự kiện trước khi load dữ liệu
     */
    protected void onPreExcuteLoadData() {}

    /**
     * Sự kiện sau khi load dữ liệu
     * @param list
     */
    protected void onPostExecuteLoadData(List<TModel> list) {
        bindList(list);
    }

    /**
     * Sự kiện trong quá trình progress dữ liệu
     */
    protected void onProgressUpdateLoadData() {}

    /**
     * Sự kiện lúc canceled load data
     */
    protected void onCancelledLoadData(Exception exp) {
        if (exp != null)
            doShowErrorMsg(exp);
    }

    /**
     * Sự kiện lúc chọn được data
     * @param item
     */
    public void onSelectItem(TModel item) {
        mItem = item;
        if (mDetailView != null)
            mDetailView.bindItem(item);
    }

    /**
     * Xác định controller xử lý detail
     */
    public void setDetailPanel(AbstractDetailPanel<TModel> detailPanel) {
        mDetailView = detailPanel;
    }

    public void setListPanel(AbstractListPanel<TModel> view) {
        setView(view);
    }


    @Override
    public void doDeleteItem(TModel item) {

    }

    @Override
    public void doUpdateItem(TModel item) {
    }
}