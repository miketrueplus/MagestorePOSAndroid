package com.magestore.app.lib.controller;

import android.view.View;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.task.DeleteListTask;
import com.magestore.app.lib.task.InsertListTask;
import com.magestore.app.lib.task.RetrieveListTask;
import com.magestore.app.lib.task.UpdateListTask;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Task quản lý việc hiển thị một List và một View
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class AbstractListController<TModel extends Model>
        extends AbstractController<TModel, AbstractListPanel<TModel>, ListService<TModel>>
        implements ListController<TModel> {
    // xác định khi thêm mới chèn vào cuối hay đầu danh sách, mặc định là cuối
    private boolean isInsertAtLast = true;
    private boolean isInsertAfterSuccess = true;

    // chuỗi seảarch
    private String mSearchString;

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
    private TModel mSelectedItem;

    /**
     * Service xử lý
     */
//    protected ListService<TModel> mListService;

    /**
     * Chỉ định có tự động chọn item đầu tiên trong danh sách sau khi load không
     * @param auto
     */
    public void setAutoSelectFirstItem(boolean auto) {
        mblnAutoChooseFirstItem = auto;
    }

    /**
     * Clear danh sách
     */
    public void clearList() {
        mList = null;
        getView().clearList();
    }

    /**
     * Thiết lập danh sách, cho hiển thị lên view
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        getView().bindList(mList);
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

        // báo cho các observ khác về việc bind item
        GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_SELECT_ITEM);
        if (getSubject() != null) getSubject().setState(state);
    }

    @Override
    public void onLongClickItem(TModel item) {
        // bind item bình thường
        super.bindItem(item);
        setSelectedItem(item);
        if (mDetailView != null)
            mDetailView.bindItem(item);

        // báo cho các observ khác về việc long click item
        GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_LONG_CLICK_ITEM);
        if (getSubject() != null) getSubject().setState(state);
    }

    @Override
    public void onDoubleClickItem(TModel item) {
        // bind item bình thường
        super.bindItem(item);
        setSelectedItem(item);
        if (mDetailView != null)
            mDetailView.bindItem(item);

        // báo cho các observ khác về việc double click item
        GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_DOUBLE_CLICK_ITEM);
        if (getSubject() != null) getSubject().setState(state);
    }

    @Override
    public void setSearchString(String search) {
        mSearchString = search;
    }

    @Override
    public String getSearchString() {
        return mSearchString;
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
     *
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
    public void doRetrieve() {
        // chuẩn bị task load data
        doRetrieve(1, getView().getPageSize());
    }

    /**
     * Thực hiện load dữ liệu lúc đầu mở view
     */
    @Override
    public void doRetrieve(int page, int pageSize) {
        // hiển thị progress
        if (page == 1) getView().showProgress(true);
        else getView().showProgressLoadingMore(true);

        // chuẩn bị task load data
        RetrieveListTask<TModel> task = new RetrieveListTask<TModel>(this);
        task.doExecute(page, pageSize);

        // cho phép layzy loading
        getView().lockLazyLoading(true);
    }

    /**
     * Load data từ background ngầm, các lớp con sẽ nạp chồng hàm này
     * @return
     * @throws Exception
     */
    @Override
    public List<TModel> onRetrieveBackground(int page, int pageSize) throws Exception {
        List<TModel> returnList;
        if (getListService() != null) {
            if (pageSize > 0) {
                if (mSearchString == null || StringUtil.STRING_EMPTY.equals(mSearchString))
                    returnList = getListService().retrieve(page, pageSize);
                else
                    returnList = getListService().retrieve(mSearchString, page, pageSize);
            }
            else {
                if (mSearchString == null || StringUtil.STRING_EMPTY.equals(mSearchString))
                    returnList = getListService().retrieve();
                else
                    returnList = getListService().retrieve(mSearchString, 1, 500);
            }
        } else
            returnList = loadDataBackground();
        return returnList;
    }

    /**
     * Sự kiện sau khi load dữ liệu
     *
     * @param list
     */
    @Override
    public synchronized void onRetrievePostExecute(List<TModel> list) {
        // tắt progress
        getView().hideAllProgressBar();

        // xem chế độ là lazyloading hay k0
        if (getView().haveLazyLoading()) {
            if (mList == null || mList.size() == 0) {
                bindList(list);

                // disable lazyloading nếu đã là cuối danh sách
                getView().enableLazyLoading(!(list == null || list.size() < getView().getPageSize()));
                getView().lockLazyLoading(false);

                // Chọn item đầu tiên
                if (mblnAutoChooseFirstItem && mList != null && mDetailView != null && mList.size() > 0)
                    bindItem(mList.get(0));
            } else {
                // disable lazyloading nếu đã là cuối danh sách
                if (list == null || list.size() < getView().getPageSize())
                    getView().enableLazyLoading(false);

                // bổ sung thêm vào list theo lazyloading
                mList.addAll(list);
                getView().insertListAtLast(list);
                getView().lockLazyLoading(false);
            }
        } else {
            bindList(list);

            // Chọn item đầu tiên
            if (mblnAutoChooseFirstItem && mList != null && mDetailView != null && mList.size() > 0)
                bindItem(mList.get(0));
        }

        // báo cho các observ khác về việc bind item
        GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_RETRIEVE);
        if (getSubject() != null) getSubject().setState(state);
    }

    /**
     * Load data từ background ngầm, các lớp con sẽ nạp chồng hàm này
     * Bản cũ không dùng nữa
     *
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
     *
     * @throws Exception
     */
    @Override
    public boolean onUpdateBackGround(TModel oldModel, TModel newModels) throws Exception {
        if (getListService() != null) {
            return getListService().update(oldModel, newModels);
        }
        return false;
    }

    /**
     * Cập nhật thành công
     *
     * @param success
     */
    @Override
    public void onUpdatePostExecute(Boolean success, TModel oldModel, TModel newModels) {
        if (success) {
            getView().replaceModel(oldModel, newModels);
//            getView().notifyDataSetChanged();

            // báo cho các observ khác về việc bind item
            GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_UPDATE);
            if (getSubject() != null) getSubject().setState(state);
        }
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
     *
     * @throws Exception
     */
    @Override
    public boolean onDeleteBackGround(TModel... models) throws Exception {
        if (getListService() != null)
            return getListService().delete(models);
        return false;
    }

    /**
     * Xóa thành công
     *
     * @param success
     */
    @Override
    public void onDeletePostExecute(Boolean success, TModel... models) {
        if (success) {
            getView().deleteList(models);

            // báo cho các observ khác về việc bind item
            GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_DELETE);
            if (getSubject() != null) getSubject().setState(state);
        }
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

    @Override
    public void onInsertPreExecute(TModel... models) {
        if (!isInsertAfterSuccess()) {
            getView().updateModel(models);
            if (isInsertAtLast()) getView().insertListAtLast(models);
            else getView().insertListAtFirst(models);
        }
    }

    /**
     * Overider hàm này để xử lý nghiệp vụ insert
     * @param params
     * @throws Exception
     */
    @Override
    public boolean onInsertBackground(TModel... params)
            throws Exception {
        if (getListService() != null)
            return getListService().insert(params);
        return false;
    }

    /**
     * Insert thành công
     * @param success
     */
    @Override
    public void onInsertPostExecute(Boolean success, TModel... models) {
        if (success) {
            if (isInsertAfterSuccess()) {
                getView().updateModel(models);
                if (isInsertAtLast()) getView().insertListAtLast(models);
                else getView().insertListAtFirst(models);
            }

            // báo cho các observ khác về việc bind item
            GenericState<ListController<TModel>> state = new GenericState<ListController<TModel>>(this, GenericState.DEFAULT_STATE_CODE_ON_INSERT);
            if (getSubject() != null) getSubject().setState(state);
        }
    }


    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledLoadData(Exception exp) {
        getView().hideAllProgressBar();
        getView().showErrorMsgWithReload(exp);
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
        return mList;
    }

    /**
     * Trả về toàn bộ list
     * @return
     */
    @Override
    public List<TModel> getListItems() {
        return mList;
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
        setService(service);
    }

    /**
     * Trả về list service xử lý thao tác
     * @return
     */
    @Override
    public ListService<TModel> getListService() {
        return getService();
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
     * Hiển thị confirm delete item
     * @param item
     */
    @Override
    public void doShowDeleteItemInput(TModel item) {
        getView().showDeleteItemInput(item);
    }

    /**
     * Hiển thị để update 1 item
     * @param item
     */
    @Override
    public void doShowUpdateItemInput(TModel item) {
        getView().showUpdateItemInput(item);
    }

    /**
     * Hiển thị để insert 1 item
     */
    @Override
    public void doShowInsertItemInput() {
        getView().showInsertItemInput();
    }

    @Override
    public void doShowInsertItemInput(State state) {
        doShowInsertItemInput();
    }

    /**
     * Load thêm dữ liệu vào list theo lazy loading
     * @param page
     */
    @Override
    public void doRetrieveMore(int page) {
        doRetrieve(page, getView().getPageSize());
    }

    /**
     * Bật tắt, hiển thị detail
     * @param show
     */
    @Override
    public void doShowDetailPanel(boolean show) {
        if (mDetailView != null) mDetailView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public AbstractDetailPanel<TModel> getDetailView() {
        return mDetailView;
    }

    /**
     * Hiển thị panel list
     * @param show
     */
    @Override
    public void doShowListPanel(boolean show) {
        if (getView() != null) getView().setVisibility(show ? View.VISIBLE : View.GONE);

    }

    /**
     * Cho phép lazy loading
     * @param enable
     */
    @Override
    public void enableLazyLoading(boolean enable) {
        if (getView() != null) getView().enableLazyLoading(enable);
    }

    /**
     * Hiển thị kết quả tìm kiếm trên list
     * @param model
     */
    @Override
    public void displaySearch(TModel model) {
        List<TModel> listModel = new ArrayList<TModel>();
        listModel.add(model);
        getView().publishSearchList(listModel);
    }

    /**
     * Hiển thị kết quả tìm kiếm trên list
     * @param listModel
     */
    @Override
    public void displaySearch(List<TModel> listModel) {
        getView().publishSearchList(listModel);
    }

    /**
     * Giấu hết quả tìm kiếm trên list
     */
    @Override
    public void hideSearch() {
        getView().closeSearchList();
    }

    /**
     * Reload data lại từ đầu, từ trang 1
     */
    @Override
    public void reload() {
        clearList();
        doRetrieve();
    }

    @Override
    public boolean isInsertAtLast() {
        return isInsertAtLast;
    }

    @Override
    public void setInsertAtLast(boolean insertAtLast) {
        isInsertAtLast = insertAtLast;
    }

    @Override
    public void setInsertAtLast() {
        setInsertAtLast(true);
    }

    @Override
    public void setInsertAtFirst() {
        setInsertAtLast(false);
    }

    @Override
    public boolean isInsertAfterSuccess() {
        return isInsertAfterSuccess;
    }

    @Override
    public void setInsertAfterSuccess(boolean insertAfterSuccess) {
        isInsertAfterSuccess = insertAfterSuccess;
    }

    @Override
    public void setInsertAfterSuccess() {
        setInsertAfterSuccess(true);
    }

    @Override
    public void setInsertBeforeSuccess() {
        setInsertAfterSuccess(false);
    }
}