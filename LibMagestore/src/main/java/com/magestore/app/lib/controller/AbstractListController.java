package com.magestore.app.lib.controller;

import android.view.View;
import android.widget.Toast;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.task.DeleteListTask;
import com.magestore.app.lib.task.InsertListTask;
import com.magestore.app.lib.task.RetrieveListTask;
import com.magestore.app.lib.task.UpdateListTask;

import java.io.IOException;
import java.text.ParseException;
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

    // tham chiếu phân trang
    private int mintPageSize = 500;
    private int mintPageFirst = 1;
    private int mintPageSizeMax = 500;
    private boolean lazyLoading = false;

    // tự động chọn item đầu tiên trong danh sách
    boolean mblnAutoChooseFirstItem = true;

    // view chi tiết
    protected AbstractDetailPanel<TModel> mDetailView;

    /**
     * Danh sách dữ liệu chứa Model
     */
    protected List<TModel> mList;

    /**
     * Item được chọn trên danh sách
     */
    protected TModel mSelectedItem;

    /**
     * Service xử lý
     */
    protected ListService<TModel> mListService;

    public void setAutoSelectFirstItem(boolean auto) {
        mblnAutoChooseFirstItem = auto;
    }

    @Override
    public void setPage(int pageSize, int pageMax) {
        mintPageSize = pageSize;
        mintPageSizeMax = pageMax;
        lazyLoading = true;
    }

    /**
     * Clear danh sách
     */
    public void clearList() {
        mList = null;
        mView.notifyDataSetChanged();
    }

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
        doRetrieve();
    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    @Override
    public void doRetrieve(){
        // chuẩn bị task load data
        doRetrieve(1, mintPageSize);

    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    @Override
    public void doRetrieve(int page, int pageSize){
        // chuẩn bị task load data
        RetrieveListTask<TModel> task = new RetrieveListTask<TModel>(this);
        task.doExecute(page, pageSize);
        doShowProgress(true);

    }

    /**
     * Load data từ background ngầm, các lớp con sẽ nạp chồng hàm này
     * @return
     * @throws Exception
     */
    @Override
    public List<TModel> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (mListService != null)
            return mListService.retrieve(page, pageSize);
        else
            return loadDataBackground();
    }

    /**
     * Sự kiện sau khi load dữ liệu
     * @param list
     */
    @Override
    public void onRetrievePostExecute(List<TModel> list) {
        // tắt progress
        doShowProgress(false);
        if (lazyLoading && ((list == null) || (list.size() <= 0))) return;

        // xóa danh sách cũ đi nếu k0 chạy lazyloading
        if (!lazyLoading) {
            if (mList != null) mList.clear();
            mList = null;
        }

        // gọi lại method đặt tên theo phiên bản cũ
        if (mList != null) {
            mList.addAll(list);
            mView.notifyDataSetChangedLastItem(list);
            mView.setItemLoadingProgress(null, false);
        }
        else {
            bindList(list);

            // Chọn item đầu tiên
            if (mblnAutoChooseFirstItem && mList != null && mDetailView != null && mList.size() > 0)
                bindItem(mList.get(0));
        }
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
     */
    @Override
    public void doUpdate(TModel oldModel, TModel newModels) {
        // chuẩn bị task load data
        UpdateListTask<TModel> task = new UpdateListTask<TModel>(this);
        task.execute(oldModel, newModels);
    }

    /**
     * Overider hàm này để xử lý nghiệp vụ update trên một thread khác
     * @throws Exception
     */
    @Override
    public boolean onUpdateBackGround(TModel oldModel, TModel newModels) throws Exception
    {
        if (mListService != null)
            return mListService.update(oldModel, newModels);
        return false;
    }

    /**
     * Cập nhật thành công
     * @param success
     */
    @Override
    public void onUpdatePostExecute(Boolean success, TModel oldModel, TModel newModels) {

    }

    /**
     * Kích hoạt việc xóa 1 item
     */
    @Override
    public void doDelete(TModel... models) {
        // chuẩn bị task load data
        DeleteListTask<TModel> task = new DeleteListTask<TModel>(this);
        task.doExecute(models);
    }

    /**
     * Overider hàm này để xử lý nghiệp vụ insert
     * @throws Exception
     */
    @Override
    public boolean onDeleteBackGround(TModel... models) throws Exception
    {
        if (mListService != null)
            return mListService.delete(models);
        return false;
    }

    /**
     * Xóa thành công
     * @param success
     */
    @Override
    public void onDeletePostExecute(Boolean success) {
        if (success) mView.notifyDataSetChanged();
    }

    /**
     * Kích hoạt Thực hiện chèn, tạo mới 1 item
     */
    @Override
    public void doInsert(TModel... models) {
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
    public boolean onInsertBackground(TModel... params)
            throws Exception
    {
        if (mListService != null)
            return mListService.insert(params);
        return false;
    }

    /**
     * Insert thành công
     * @param success
     */
    @Override
    public void onInsertPostExecute(Boolean success, TModel... models) {
        if (success) mView.notifyDataSetChanged();
    }


    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledLoadData(Exception exp) {
        onCancelledBackground(exp);
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

    /**
     * Chỉ định list service
     * @param service
     */
    @Override
    public void setListService(ListService<TModel> service) {
        mListService = service;
    }

    /**
     * Trả về list service xử lý thao tác
     * @return
     */
    @Override
    public ListService<TModel> getListService() {
        return mListService;
    }

    /**
     * Khởi tạo mới 1 model
     * @return
     */
    @Override
    public TModel createItem() {
        return getListService().create();
    }

    /**
     * lấy số trang
     * @param page
     */
    @Override
    public void doRetrieveMore(int page) {
        doRetrieve(page, mintPageSize);
        mView.setItemLoadingProgress(null, true);
    }

    /**
     * Bật tắt, hiển thị detail
     * @param show
     */
    @Override
    public void doShowDetailPanel(boolean show) {
        if (mDetailView != null) mDetailView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}