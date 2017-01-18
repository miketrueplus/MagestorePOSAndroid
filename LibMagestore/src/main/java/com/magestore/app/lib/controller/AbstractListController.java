package com.magestore.app.lib.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;

import java.util.List;

/**
 * Task quản lý việc hiển thị một List và một View
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractListController<TModel extends Model>
        extends AbstractController<TModel, AbstractListPanel<TModel>>
        implements ListController<TModel> {

    // tự động chọn item đầu tiên trong danh sách
    boolean mblnAutoChooseFirstItem = true;

    // view chi tiết
    protected AbstractDetailPanel<TModel> mDetailView;

    /**
     * Danh sách dữ liệu chứa Model
     */
    protected List<TModel> mList;

    /**
     * Thiết lập danh sách, cho hiển thị lên view
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        mView.bindList(mList);
    }

    /**
     * Thực hiện tải dữ liệu, xác định xem có tự động chọn item đầu tiên không
     * @param blnAutoChooseFirstItem
     */
    public void doLoadData(boolean blnAutoChooseFirstItem) {
        mblnAutoChooseFirstItem = blnAutoChooseFirstItem;
        doLoadData();
    }
//    public void doLoadData()

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
        // Map danh sách nhận được với view
        bindList(list);

        // Chọn item đầu tiên
        if (mblnAutoChooseFirstItem && mList != null && mDetailView != null && mList.size() > 0)
            bindItem(mList.get(0));
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
    @Override
    public void bindItem(TModel item) {
        super.bindItem(item);
        if (mDetailView != null)
            mDetailView.bindItem(item);
    }

    /**
     * Xác định controller xử lý detail
     */
    public void setDetailPanel(AbstractDetailPanel<TModel> detailPanel) {
        mDetailView = detailPanel;
        detailPanel.setController(this);
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