package com.magestore.app.lib.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;

import java.util.ArrayList;
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
    protected TModel mSelectedItem;
    protected TModel mBackupSelectedItem;

    /**
     * Thiết lập danh sách, cho hiển thị lên view
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        mView.bindList(mList);
    }

    /**
     * Sự kiện lúc chọn được data, cập nhật cả view và dữ liệu
     * @param item
     */
    @Override
    public void bindItem(TModel item) {
        super.bindItem(item);
        setSelectedItem(item);
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

    /**
     * Đặt list panel để quản lý hiển thị cho panel
     * @param view
     */
    public void setListPanel(AbstractListPanel<TModel> view) {
        setView(view);
    }

    //////////////////////////////////////////////////////////

    /**
     * Thực hiện tải dữ liệu, xác định xem có tự động chọn item đầu tiên không
     * @param blnAutoChooseFirstItem
     */
    public void doLoadData(boolean blnAutoChooseFirstItem) {
        mblnAutoChooseFirstItem = blnAutoChooseFirstItem;
        doLoadData();
    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    @Override
    public void doLoadData() {
        RetrieveListTask<TModel> task = new RetrieveListTask<TModel>(this);
        task.doExecute(0, 30);
    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    @Override
    public void doRetrieveItem(){
        // chuẩn bị task load data
        RetrieveListTask<TModel> task = new RetrieveListTask<TModel>(this);
        task.doExecute(0, 30);
    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    @Override
    public void doRetrieveItem(int page, int pageSize){
        // chuẩn bị task load data
        RetrieveListTask<TModel> task = new RetrieveListTask<TModel>(this);
        task.doExecute(page, pageSize);
    }

    /**
     * Load data từ background ngầm, các lớp con sẽ nạp chồng hàm này
     * @return
     * @throws Exception
     */
    @Override
    public List<TModel> onRetrieveDataBackground(int page, int pageSize) throws Exception {
        return loadDataBackground();
    }

    /**
     * Sự kiện sau khi load dữ liệu
     * @param list
     */
    @Override
    public void onRetrievePostExecute(List<TModel> list) {
        // gọi lại method đặt tên theo phiên bản cũ
        onPostExecuteLoadData(list);
    }

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
     * Load data từ background ngầm, các lớp con sẽ nạp chồng hàm này
     * Bản cũ không dùng nữa
     * @return
     * @throws Exception
     */
    protected List<TModel> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Kích hoạt update model trong list
     * @param models
     */
    @Override
    public void doUpdateItem(TModel... models) {
        // chuẩn bị task load data
        UpdateListTask<TModel> task = new UpdateListTask<TModel>(this);
        task.execute(models);
    }

    /**
     * Overider hàm này để xử lý nghiệp vụ update trên một thread khác
     * @param params
     * @throws Exception
     */
    @Override
    public boolean onUpdateDataBackGround(TModel... params) throws Exception
    { return false;}

    /**
     * Cập nhật thành công
     * @param success
     */
    @Override
    public void onUpdatePostExecute(Boolean success, TModel... models) {

    }

    /**
     * Kích hoạt việc xóa 1 item
     */
    @Override
    public void doDeleteItem(TModel... models) {
        // chuẩn bị task load data
        DeleteListTask<TModel> task = new DeleteListTask<TModel>(this);
        task.doExecute(models);
    }

    /**
     * Overider hàm này để xử lý nghiệp vụ insert
     * @throws Exception
     */
    @Override
    public boolean onDeleteDataBackGround(TModel... models) throws Exception
    {
        return false;
    }

    /**
     * Xóa thành công
     * @param success
     */
    @Override
    public void onDeletePostExecute(Boolean success) {
        if (success) mView.notifyDatasetChanged();
    }

    /**
     * Kích hoạt Thực hiện chèn, tạo mới 1 item
     */
    @Override
    public void doInsertItem(TModel... models) {
        // chuẩn bị task load data
        InsertListTask<TModel> task = new InsertListTask<TModel>(this);
        task.doExecute(models);
    }

    /**
     * Overider hàm này để xử lý nghiệp vụ insert
     * @param params
     * @throws Exception
     */
    @Override
    public boolean onInsertDataBackground(TModel... params)
            throws Exception
    { return false;}

    /**
     * Insert thành công
     * @param success
     */
    @Override
    public void onInsertPostExecute(Boolean success, TModel... models) {
        if (success) mView.notifyDatasetChanged();
    }


    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledLoadData(Exception exp) {
        onCancelledLoadData(exp);
    }

    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledBackground(Exception exp) {
        if (exp != null)
            doShowErrorMsg(exp);
    }

    /**
     * Chỉ định 1 item được chọn về mặt dataset, k0 có update view
     * @param model
     */
    public void setSelectedItem(TModel model) {
        mSelectedItem = model;
    }

    /**
     * Trả về item đã được chọn trên danh sách
     * @return
     */
    public TModel getSelectedItem() {
        return mSelectedItem;
    }

    /**
     * Trả lại danh sách nhiều item được chọn
     * @return
     */
    public List<TModel> getSelectedItems() {
        List<TModel> models = new ArrayList<TModel>();
        models.add(mSelectedItem);
        return models;
    }

    /**
     * Chỉ định tập hợp các item được chọn, k0 thay đổi view, chỉ về mặt dữ liệu
     * @param models
     */
    public void setSelectedItem(List<TModel> models) {
        setSelectedItem(models.get(0));
    }
}